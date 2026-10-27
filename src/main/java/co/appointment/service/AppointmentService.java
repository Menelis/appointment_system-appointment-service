package co.appointment.service;

import static co.appointment.constant.AppointmentStatus.BOOKING_CONFIRMED;
import static co.appointment.constant.AppointmentStatus.BOOKING_PENDING_CONFIRMATION;
import static co.appointment.shared.constant.EventTypeConstants.BOOKING_CANCELLED_EVENT;
import static co.appointment.shared.constant.EventTypeConstants.BOOKING_CONFIRMED_EVENT;
import static co.appointment.shared.constant.EventTypeConstants.BOOKING_PENDING_CONFIRMATION_EVENT;

import co.appointment.config.AppConfigProperties;
import co.appointment.constant.AppointmentStatus;
import co.appointment.dto.AppointmentDTO;
import co.appointment.kafka.KafkaProducerService;
import co.appointment.kafka.event.AppointmentEvent;
import co.appointment.mapper.AppointmentMapper;
import co.appointment.payload.requests.NewAppointmentRequest;
import co.appointment.payload.requests.UpdateAppointmentRequest;
import co.appointment.payload.requests.UpdateAppointmentStatusRequest;
import co.appointment.persistence.AppointmentEntity;
import co.appointment.persistence.AppointmentRepository;
import co.appointment.shared.dto.ServiceResponseDto;
import co.appointment.shared.event.DomainEventMessage;
import co.appointment.shared.exception.ServiceException;
import co.appointment.shared.security.service.AuthenticationFacade;
import co.appointment.shared.util.SharedObjectUtils;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

@Service
@RequiredArgsConstructor
@Slf4j
public class AppointmentService {

    private final AppointmentRepository appointmentRepository;
    private final AppointmentMapper appointmentMapper;
    private final AuthenticationFacade authenticationFacade;
    private final AppConfigProperties configProperties;
    private final KafkaProducerService kafkaProducerService;

    private Page<AppointmentEntity> getSearchedOrUnSearchedAppointments(
            final int pageNo, final int pageSize, final String searchTerm) {
        Pageable pageable = SharedObjectUtils.getPageable(pageNo, pageSize, Sort.by(Sort.Direction.DESC, "createdAt"));
        if (!StringUtils.hasText(searchTerm)) {
            return appointmentRepository.findAll(pageable);
        }
        return appointmentRepository.findAll(pageable);
    }

    public Page<AppointmentDTO> getAllAppointments(
            final int pageNo, final int pageSize, final String searchTerm, final UUID xCorrelationId) {
        final Page<AppointmentEntity> appointments = getSearchedOrUnSearchedAppointments(pageNo, pageSize, searchTerm);

        final List<AppointmentDTO> content =
                appointments.stream().map(appointmentMapper::toDTO).toList();

        return new PageImpl<>(content, appointments.getPageable(), appointments.getTotalElements());
    }

    private Page<AppointmentEntity> getSearchedOrUnSearchedCustomerAppointments(
            final int pageNo, final int pageSize, final String searchTerm) {
        Pageable pageable = SharedObjectUtils.getPageable(pageNo, pageSize, Sort.by(Sort.Direction.DESC, "createdAt"));
        if (!StringUtils.hasText(searchTerm)) {
            return appointmentRepository.findAll(pageable);
        }
        return appointmentRepository.findAll(pageable);
    }

    public Page<AppointmentDTO> getAppointmentsByCustomer(
            final int pageNo, final int pageSize, final String searchTerm, final UUID xCorrelationId) {
        final Page<AppointmentEntity> pagedAppointments =
                getSearchedOrUnSearchedCustomerAppointments(pageNo, pageSize, searchTerm);

        final List<AppointmentDTO> content =
                pagedAppointments.stream().map(appointmentMapper::toDTO).toList();
        return new PageImpl<>(content, pagedAppointments.getPageable(), pagedAppointments.getTotalElements());
    }

    public Optional<AppointmentDTO> getAppointmentById(final UUID id, final UUID xCorrelationId) {
        Optional<AppointmentEntity> optionalAppointment = appointmentRepository.findById(id);
        if (optionalAppointment.isEmpty()) {
            throw new ServiceException(HttpStatus.NO_CONTENT);
        }
        return Optional.of(appointmentMapper.toDTO(optionalAppointment.get()));
    }

    public Page<AppointmentDTO> getAppointmentByReferenceNo(
            final String referenceNo, final int pageNo, final int pageSize, final UUID xCorrelationId) {
        Page<AppointmentEntity> pagedAppointments = appointmentRepository.findAllByReferenceNo(
                referenceNo,
                SharedObjectUtils.getPageable(
                        pageNo, pageSize, Sort.by("createdAt").descending()));
        List<AppointmentDTO> content =
                pagedAppointments.stream().map(appointmentMapper::toDTO).toList();
        return new PageImpl<>(content, pagedAppointments.getPageable(), pagedAppointments.getTotalElements());
    }

    public boolean activeCustomerAppointmentExists(final NewAppointmentRequest newAppointmentRequest) {
        return appointmentRepository.activeCustomerAppointmentExists(
                authenticationFacade.getUserId(),
                newAppointmentRequest.getAppointmentDate(),
                AppointmentStatus.BOOKING_CANCELLED);
    }

    public Optional<ServiceResponseDto> createAppointment(
            final NewAppointmentRequest request, final UUID xCorrelationId) {
        boolean activeCustomerAppointmentExists = activeCustomerAppointmentExists(request);
        if (activeCustomerAppointmentExists) {
            throw new ServiceException(
                    HttpStatus.CONFLICT,
                    ServiceResponseDto.builder()
                            .success(false)
                            .correlationId(xCorrelationId)
                            .errorDescription(
                                    "There is an already active appointment for date=[%s]. Only one booking can be made on the date."
                                            .formatted(request.getAppointmentDate()))
                            .build());
        }
        final String referenceNo = UUID.randomUUID().toString();
        AppointmentEntity appointment = appointmentMapper.toEntity(request);
        appointment.setStatus(BOOKING_PENDING_CONFIRMATION);
        appointment.setCustomerId(authenticationFacade.getUserId());
        appointment.setReferenceNo(referenceNo);

        appointmentRepository.save(appointment);

        // Publish event for confirmed email
        publishEvent(
                getDomainEventMessage(
                        AppointmentEvent.builder()
                                .referenceNo(referenceNo)
                                .eventId(UUID.randomUUID())
                                .build(),
                        xCorrelationId,
                        BOOKING_PENDING_CONFIRMATION),
                Map.of());
        return Optional.of(ServiceResponseDto.builder()
                .success(true)
                .correlationId(xCorrelationId)
                .referenceNo(referenceNo)
                .build());
    }

    void publishEvent(
            final DomainEventMessage<AppointmentEvent> domainEventMessage, final Map<String, Object> headers) {
        kafkaProducerService.publish(
                configProperties.getKafka().getTopic().getOutgoing().getNotification(), domainEventMessage, headers);
    }

    private DomainEventMessage<AppointmentEvent> getDomainEventMessage(
            final AppointmentEvent appointmentEvent, final UUID xCorrelationId, final String status) {
        final String eventType = switch (status) {
            case BOOKING_PENDING_CONFIRMATION -> BOOKING_PENDING_CONFIRMATION_EVENT;
            case BOOKING_CONFIRMED -> BOOKING_CONFIRMED_EVENT;
            default -> BOOKING_CANCELLED_EVENT;
        };
        return DomainEventMessage.<AppointmentEvent>builder()
                .event(appointmentEvent)
                .correlationId(xCorrelationId.toString())
                .eventType(eventType)
                .build();
    }

    public Optional<ServiceResponseDto> updateAppointment(
            final UpdateAppointmentRequest request, final UUID xCorrelationId) {
        AppointmentEntity appointment = appointmentRepository
                .findById(request.getId())
                .orElseThrow(() -> new ServiceException(HttpStatus.NO_CONTENT));

        boolean statusChanged = appointmentStatusChanged(appointment.getStatus(), request.getStatus());

        String status = getStatus(request.getStatus(), appointment.getStatus()).toUpperCase();
        appointmentMapper.toEntity(request, appointment);

        appointment.setUpdatedBy(authenticationFacade.getUserId());
        appointment.setStatus(status);
        appointmentRepository.save(appointment);

        if (statusChanged) {
            publishEvent(
                    getDomainEventMessage(
                            AppointmentEvent.builder()
                                    .referenceNo(appointment.getReferenceNo())
                                    .eventId(UUID.randomUUID())
                                    .build(),
                            xCorrelationId,
                            status),
                    Map.of());
        }
        return Optional.of(ServiceResponseDto.builder()
                .success(true)
                .correlationId(xCorrelationId)
                .build());
    }

    private String getStatus(final String requestStatus, final String dbObjectStatus) {
        if (StringUtils.hasText(requestStatus)) {
            return requestStatus;
        }
        return dbObjectStatus;
    }

    private boolean appointmentStatusChanged(final String oldStatus, final String newStatus) {
        return !oldStatus.equalsIgnoreCase(newStatus);
    }

    public Optional<ServiceResponseDto> updateAppointmentStatus(
            final UpdateAppointmentStatusRequest request, final UUID xCorrelationId) {
        AppointmentEntity appointment = appointmentRepository
                .findById(request.getId())
                .orElseThrow(() -> new ServiceException(HttpStatus.NO_CONTENT));

        boolean statusChanged = appointmentStatusChanged(appointment.getStatus(), request.getStatus());
        String status = request.getStatus().toUpperCase();
        appointment.setUpdatedBy(authenticationFacade.getUserId());
        appointment.setStatus(status);
        appointment.setCancellationReason(request.getReason());
        appointmentRepository.save(appointment);

        if (statusChanged) {
            publishEvent(
                    getDomainEventMessage(
                            AppointmentEvent.builder()
                                    .referenceNo(appointment.getReferenceNo())
                                    .eventId(UUID.randomUUID())
                                    .build(),
                            xCorrelationId,
                            status),
                    Map.of());
        }
        return Optional.of(ServiceResponseDto.builder()
                .success(true)
                .correlationId(xCorrelationId)
                .build());
    }
}
