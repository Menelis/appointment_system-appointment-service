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
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;

@RestController
@RequestMapping("/api/v1/appointment")
@RequiredArgsConstructor
public class AppointmentControllerV1 {

    private final AppointmentService appointmentService;

    @GetMapping("/admin/get-all-appointments")
    public ResponseEntity<ApiResponse<Page<AppointmentDTO>>> getAllAppointments(
                                                                                @RequestParam(name = SharedConstants.PAGE_NUMBER_PARAMETER_NAME, defaultValue = SharedConstants.PAGE_NUMBER_DEFAULT_VALUE) final int pageNumber,
                                                                                @RequestParam(name = SharedConstants.PAGE_SIZE_PARAMETER_NAME, defaultValue = SharedConstants.PAGE_SIZE_DEFAULT_VALUE) final int pageSize,
                                                                                @RequestParam(name = SharedConstants.SEARCH_TERM_PARAMETER_NAME, required = false) final String searchTerm) {
        Page<AppointmentDTO> pagedAppointments = appointmentService.getAllAppointments(pageNumber, pageSize, searchTerm);
        return ResponseEntity.ok(new ApiResponse<>(pagedAppointments));
    }
    @GetMapping("/customer/get-all-appointments")
    public ResponseEntity<ApiResponse<Page<AppointmentDTO>>> getCustomerAppointments(
            @RequestParam(name = SharedConstants.PAGE_NUMBER_PARAMETER_NAME, defaultValue = SharedConstants.PAGE_NUMBER_DEFAULT_VALUE) final int pageNumber,
            @RequestParam(name = SharedConstants.PAGE_SIZE_PARAMETER_NAME, defaultValue = SharedConstants.PAGE_SIZE_DEFAULT_VALUE) final int pageSize,
            @RequestParam(name = SharedConstants.SEARCH_TERM_PARAMETER_NAME, required = false) final String searchTerm) {

        Page<AppointmentDTO> pagedAppointments = appointmentService.getAppointmentsByCustomer(pageNumber, pageSize, searchTerm);

        return ResponseEntity.ok(new ApiResponse<>(pagedAppointments));
    }
    @GetMapping("/findByReferenceNo")
    public ResponseEntity<ApiResponse<Page<AppointmentDTO>>> findAppointmentByReferenceNo(
            @RequestParam(name = "referenceNo") final String referenceNo,
            @RequestParam(name = SharedConstants.PAGE_NUMBER_PARAMETER_NAME, defaultValue = SharedConstants.PAGE_NUMBER_DEFAULT_VALUE, required = false) final int pageNumber,
            @RequestParam(name = SharedConstants.PAGE_SIZE_PARAMETER_NAME, defaultValue = SharedConstants.PAGE_SIZE_DEFAULT_VALUE, required = false) final int pageSize) {

        Page<AppointmentDTO> pagedAppointments = appointmentService.getAppointmentByReferenceNo(referenceNo, pageNumber, pageSize);

        return ResponseEntity.ok(new ApiResponse<>(pagedAppointments));
    }
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<AppointmentDTO>> getAppointmentById(@PathVariable final long id,
                                                                          @RequestBody @Valid UpdateAppointmentRequest request) {
        return appointmentService.getAppointmentById(id);
    }
    @PostMapping("/create")
    public ResponseEntity<ApiResponse<AppointmentDTO>> createAppointment(@RequestBody @Valid final NewAppointmentRequest request) {
        boolean activeCustomerBookingExists = appointmentService.activeCustomerAppointmentExists(request);
        if(activeCustomerBookingExists) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(new ApiResponse<>(false, String.format("There is an already active appointment for date: %s. Only one booking can be made on the date.", request.getAppointmentDate())));
        }
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
