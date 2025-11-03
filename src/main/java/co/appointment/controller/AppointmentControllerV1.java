package co.appointment.controller;

import co.appointment.dto.AppointmentDTO;
import co.appointment.payload.requests.NewAppointmentRequest;
import co.appointment.payload.requests.UpdateAppointmentRequest;
import co.appointment.payload.requests.UpdateAppointmentStatusRequest;
import co.appointment.service.AppointmentService;
import co.appointment.shared.constant.SharedConstants;
import co.appointment.shared.payload.response.ApiResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/appointment")
@RequiredArgsConstructor
public class AppointmentControllerV1 {

    private final AppointmentService appointmentService;

    @GetMapping("/admin/appointments")
    public ResponseEntity<ApiResponse<Page<AppointmentDTO>>> getAllAppointments(
                                                                                @RequestParam(name = SharedConstants.PAGE_NUMBER_PARAMETER_NAME, defaultValue = SharedConstants.PAGE_NUMBER_DEFAULT_VALUE) final int pageNumber,
                                                                                @RequestParam(name = SharedConstants.PAGE_SIZE_PARAMETER_NAME, defaultValue = SharedConstants.PAGE_SIZE_DEFAULT_VALUE) final int pageSize) {
        Page<AppointmentDTO> pagedAppointments = appointmentService.getAllAppointments(pageNumber, pageSize);
        return ResponseEntity.ok(new ApiResponse<>(pagedAppointments));
    }
    @GetMapping("/customer/appointments")
    public ResponseEntity<ApiResponse<Page<AppointmentDTO>>> getCustomerAppointments(
            @RequestParam(name = SharedConstants.PAGE_NUMBER_PARAMETER_NAME, defaultValue = SharedConstants.PAGE_NUMBER_DEFAULT_VALUE) final int pageNumber,
            @RequestParam(name = SharedConstants.PAGE_SIZE_PARAMETER_NAME, defaultValue = SharedConstants.PAGE_SIZE_DEFAULT_VALUE) final int pageSize) {

        Page<AppointmentDTO> pagedAppointments = appointmentService.getAppointmentsByCustomer(pageNumber, pageSize);

        return ResponseEntity.ok(new ApiResponse<>(pagedAppointments));
    }
    @PostMapping("/customer/create")
    public ResponseEntity<ApiResponse<AppointmentDTO>> createAppointment(@RequestBody @Valid final NewAppointmentRequest request) {
        AppointmentDTO appointment = appointmentService.createAppointment(request);
        if(appointment == null) {
            return ResponseEntity.badRequest().build();
        }
        return ResponseEntity.ok(new ApiResponse<>(true, String.format("Appointment has been created successfully.Your reference number is: %s", appointment.getReferenceNo())));
    }
    @PutMapping("/update/{id}")
    public ResponseEntity<ApiResponse<AppointmentDTO>> updateAppointment(@PathVariable("id") final long id,
                                                                         @RequestBody @Valid UpdateAppointmentRequest request) {
        return appointmentService.updateAppointment(id, request);
    }
    @PostMapping("/update-status")
    public ResponseEntity<ApiResponse<AppointmentDTO>> updateStatus(@RequestBody @Valid final UpdateAppointmentStatusRequest request) {
        return appointmentService.updateAppointmentStatus(request);
    }
}
