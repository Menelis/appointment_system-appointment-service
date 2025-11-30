package co.appointment.service;

import co.appointment.constant.AppointmentStatus;
import co.appointment.dto.AppointmentDTO;
import co.appointment.entity.Appointment;
import co.appointment.mapper.AppointmentToDTOMapper;
import co.appointment.payload.requests.NewAppointmentRequest;
import co.appointment.payload.requests.UpdateAppointmentRequest;
import co.appointment.payload.requests.UpdateAppointmentStatusRequest;
import co.appointment.repository.AppointmentRepository;
import co.appointment.repository.specification.AppointmentSpecifications;
import co.appointment.shared.payload.response.ApiResponse;
import co.appointment.shared.security.service.AuthenticationFacade;
import co.appointment.shared.util.SharedObjectUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class AppointmentService {

    private final AppointmentRepository appointmentRepository;
    private final AppointmentToDTOMapper appointmentToDTOMapper;
    private final NotificationEventService notificationEventService;
    private final AuthenticationFacade authenticationFacade;

    private Page<Appointment> getSearchedOrUnSearchedAppointments(final int pageNo, final int pageSize, final String searchTerm) {
        Pageable pageable = SharedObjectUtils.getPageable(pageNo, pageSize, Sort.by(Sort.Direction.DESC, "createdAt"));
        final Specification<Appointment> specification = AppointmentSpecifications.notEqualToStatus(AppointmentStatus.BOOKING_CANCELLED);
        if(!StringUtils.hasText(searchTerm)) {
            return appointmentRepository.findAll(specification ,pageable);
        }
        specification.and(AppointmentSpecifications.referenceNoContains(searchTerm));
        return appointmentRepository.findAll(specification ,pageable);
    }
    public Page<AppointmentDTO> getAllAppointments(final int pageNo, final int pageSize, final String searchTerm) {
        final Page<Appointment> appointments = getSearchedOrUnSearchedAppointments(pageNo, pageSize, searchTerm);

        final List<AppointmentDTO> content = appointments.stream()
                .map(appointmentToDTOMapper::toDTO)
                .toList();

        return new PageImpl<>(content, appointments.getPageable(), appointments.getTotalElements());
    }
    private Page<Appointment> getSearchedOrUnSearchedCustomerAppointments(final int pageNo, final int pageSize, final String searchTerm) {
        Pageable pageable = SharedObjectUtils.getPageable(pageNo, pageSize, Sort.by(Sort.Direction.DESC,"createdAt"));
        if(!StringUtils.hasText(searchTerm)) {
            return appointmentRepository.findAll(
                    AppointmentSpecifications.equalsCustomerId(authenticationFacade.getUserId()) ,pageable);
        }
        final Specification<Appointment> appointmentSpecification = AppointmentSpecifications.equalsCustomerId(authenticationFacade.getUserId())
                .and(AppointmentSpecifications.referenceNoContains(searchTerm));
        return appointmentRepository.findAll(appointmentSpecification ,pageable);
    }
    public Page<AppointmentDTO> getAppointmentsByCustomer(final int pageNo, final int pageSize,
                                                          final String searchTerm) {
        final Page<Appointment> pagedAppointments = getSearchedOrUnSearchedCustomerAppointments(pageNo, pageSize, searchTerm);

        final List<AppointmentDTO> content = pagedAppointments.stream()
                .map(appointmentToDTOMapper::toDTO)
                .toList();
        return new PageImpl<>(content, pagedAppointments.getPageable(), pagedAppointments.getTotalElements());
    }

    public ResponseEntity<ApiResponse<AppointmentDTO>> getAppointmentById(final long id) {
        Optional<Appointment> optionalAppointment = appointmentRepository.findById(id);

        return optionalAppointment
                .map(appointment -> ResponseEntity.ok(new ApiResponse<>(appointmentToDTOMapper.toDTO(appointment))))
                .orElseGet(() -> ResponseEntity.notFound().build());
    }
    public Page<AppointmentDTO> getAppointmentByReferenceNo(final String referenceNo, final int pageNo, final int pageSize) {
        Page<Appointment> pagedAppointments = appointmentRepository.findAllByReferenceNo(
                referenceNo, SharedObjectUtils.getPageable(pageNo, pageSize, Sort.by("createdAt").descending())
        );
        log.info("Content is: {}", pagedAppointments.getContent());
        List<AppointmentDTO> content = pagedAppointments.stream()
                .map(appointmentToDTOMapper::toDTO)
                .toList();
        return new PageImpl<>(content, pagedAppointments.getPageable(), pagedAppointments.getTotalElements());
    }
    public boolean activeCustomerAppointmentExists(final NewAppointmentRequest newAppointmentRequest) {
        return appointmentRepository.activeCustomerAppointmentExists(
                authenticationFacade.getUserId(), newAppointmentRequest.getAppointmentDate(), AppointmentStatus.BOOKING_CANCELLED);
    }
    public AppointmentDTO createAppointment(final NewAppointmentRequest request) {
        final String referenceNo = UUID.randomUUID().toString();
        Appointment appointment = appointmentToDTOMapper.toEntity(request);
        appointment.setStatus(AppointmentStatus.BOOKING_PENDING_CONFIRMATION);
        appointment.setCustomerId(authenticationFacade.getUserId());
        appointment.setReferenceNo(referenceNo);
        Appointment savedAppointment = appointmentRepository.save(appointment);
        // Publish event for confirmed email
        notificationEventService.publishAppointmentEvent(savedAppointment);
        return appointmentToDTOMapper.toDTO(savedAppointment);
    }
    public ResponseEntity<ApiResponse<AppointmentDTO>> updateAppointment(final long id, final UpdateAppointmentRequest request) {
        if(id != request.getId()) {
            return ResponseEntity.badRequest().body(new ApiResponse<>("id mismatch"));
        }
        Optional<Appointment> optionalAppointment = appointmentRepository.findById(id);
        if(optionalAppointment.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        boolean statusChanged = appointmentStatusChanged(optionalAppointment.get().getStatus(), request.getStatus());

        String status = getStatus(request.getStatus(), optionalAppointment.get().getStatus()).toUpperCase();
        Appointment appointment = appointmentToDTOMapper.toEntity(request);
        Appointment dbAppointment = optionalAppointment.get();

        SharedObjectUtils.mapAuditFields(appointment, dbAppointment);
        appointment.setUpdatedAt(LocalDateTime.now());
        appointment.setUpdatedBy(authenticationFacade.getUserId());
        appointment.setUpdatedAt(LocalDateTime.now());
        appointment.setStatus(status);
        appointmentRepository.save(appointment);

        if(statusChanged) {
            notificationEventService.publishAppointmentEvent(appointment);
        }
        return ResponseEntity.ok(new ApiResponse<>(true, "Appointment has been updated successfully."));
    }
    private String getStatus(final String requestStatus, final String dbObjectStatus) {
        if(StringUtils.hasText(requestStatus)) {
            return requestStatus;
        }
        return dbObjectStatus;
    }
    private boolean appointmentStatusChanged(final String oldStatus, final String newStatus) {
        return !oldStatus.equalsIgnoreCase(newStatus);
    }
    public ResponseEntity<ApiResponse<AppointmentDTO>> updateAppointmentStatus(final UpdateAppointmentStatusRequest request) {
        final Optional<Appointment> optionalAppointment = appointmentRepository.findById(request.getId());
        if(optionalAppointment.isEmpty()) {
            return ResponseEntity.badRequest().body(new ApiResponse<>(String.format("Invalid appointment with appointment id: %s", request.getId())));
        }
        boolean statusChanged = appointmentStatusChanged(optionalAppointment.get().getStatus(), request.getStatus());
        final Appointment appointment = optionalAppointment.get();
        appointment.setUpdatedAt(LocalDateTime.now());
        appointment.setUpdatedBy(authenticationFacade.getUserId());
        appointment.setStatus(request.getStatus().toUpperCase());
        appointment.setCancellationReason(request.getReason());
        appointmentRepository.save(appointment);

        // Send notification about appointment status
        if(statusChanged) {
            notificationEventService.publishAppointmentEvent(appointment);
        }
        return ResponseEntity.ok(new ApiResponse<>(true, "Appointment has been updated successfully"));
    }
}
