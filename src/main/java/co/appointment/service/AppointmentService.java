package co.appointment.service;

import co.appointment.constant.AppointmentStatus;
import co.appointment.dto.AppointmentDTO;
import co.appointment.entity.Appointment;
import co.appointment.mapper.AppointmentToDTOMapper;
import co.appointment.payload.requests.NewAppointmentRequest;
import co.appointment.payload.requests.UpdateAppointmentRequest;
import co.appointment.payload.requests.UpdateAppointmentStatusRequest;
import co.appointment.repository.AppointmentRepository;
import co.appointment.shared.payload.response.ApiResponse;
import co.appointment.shared.security.service.AuthenticationFacade;
import co.appointment.shared.util.SharedObjectUtils;
import co.appointment.util.ObjectUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class AppointmentService {

    private final AppointmentRepository appointmentRepository;
    private final AppointmentToDTOMapper appointmentToDTOMapper;
    private final AuthenticationFacade authenticationFacade;

    public Page<AppointmentDTO> getAllAppointments(final int pageNo, final int pageSize) {
        Page<Appointment> appointments = appointmentRepository.findAll(ObjectUtils.getPageable(pageNo, pageSize));

        List<AppointmentDTO> content = appointments.stream()
                .map(appointmentToDTOMapper::toDTO)
                .toList();

        return new PageImpl<>(content, appointments.getPageable(), appointments.getTotalElements());
    }
    public Page<AppointmentDTO> getAppointmentsByCustomer(final int pageNo, final int pageSize) {
        Page<Appointment> pagedAppointments = appointmentRepository.findAllByCustomerId(
                authenticationFacade.getUserId(),
                ObjectUtils.getPageable(pageNo, pageSize, Sort.by("createdAt").descending())
        );
        List<AppointmentDTO> content = pagedAppointments.stream()
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
    public AppointmentDTO createAppointment(final NewAppointmentRequest request) {
        Appointment appointment = appointmentToDTOMapper.toEntity(request);
        appointment.setStatus(AppointmentStatus.BOOKING_CONFIRMED);
        appointment.setCustomerId(authenticationFacade.getUserId());
        return appointmentToDTOMapper.toDTO(appointmentRepository.save(appointment));
    }
    public ResponseEntity<ApiResponse<AppointmentDTO>> updateAppointment(final long id, final UpdateAppointmentRequest request) {
        if(id != request.getId()) {
            return ResponseEntity.badRequest().body(new ApiResponse<>("id mismatch"));
        }
        Optional<Appointment> optionalAppointment = appointmentRepository.findById(id);
        if(optionalAppointment.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        String status = getStatus(request.getStatus(), optionalAppointment.get().getStatus());
        Appointment appointment = appointmentToDTOMapper.toEntity(request);
        SharedObjectUtils.mapAuditFields(appointment, optionalAppointment.get());
        appointment.setUpdatedAt(LocalDateTime.now());
        appointment.setUpdatedBy(authenticationFacade.getUserId());
        appointment.setUpdatedAt(LocalDateTime.now());
        appointment.setStatus(status);
        appointmentRepository.save(appointment);
        sendKafkaEvent(status, request.getId());
        return ResponseEntity.ok(new ApiResponse<>(true, "Appointment has been updated successfully."));
    }
    private String getStatus(final String requestStatus, final String dbObjectStatus) {
        if(StringUtils.hasText(requestStatus)) {
            return requestStatus;
        }
        return dbObjectStatus;
    }
    private void sendKafkaEvent(final String status, final Long appointmentId) {
        log.info("Sending event to kafka topic for appointment id: {} and status: {}", appointmentId, status);
    }
    public ResponseEntity<ApiResponse<AppointmentDTO>> updateAppointmentStatus(final UpdateAppointmentStatusRequest request) {
        Appointment appointment = appointmentRepository.findById(request.getId()).orElse(null);
        if(appointment == null) {
            return ResponseEntity.badRequest().body(new ApiResponse<>(String.format("Invalid appointment with appointment id: %s", request.getId())));
        }
        appointment.setUpdatedAt(LocalDateTime.now());
        appointment.setUpdatedBy(authenticationFacade.getUserId());
        appointment.setStatus(request.getStatus());
        appointmentRepository.save(appointment);

        sendKafkaEvent(request.getStatus(), request.getId());
        return ResponseEntity.ok(new ApiResponse<>(true, "Appointment has been updated successfully"));
    }
}
