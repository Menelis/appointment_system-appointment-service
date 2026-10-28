package co.appointment.controller.v1;

import static co.appointment.shared.constant.SharedConstants.X_CORRELATION_ID;

import co.appointment.dto.AppointmentDTO;
import co.appointment.payload.requests.NewAppointmentRequest;
import co.appointment.payload.requests.UpdateAppointmentRequest;
import co.appointment.payload.requests.UpdateAppointmentStatusRequest;
import co.appointment.service.AppointmentService;
import co.appointment.shared.constant.SharedConstants;
import co.appointment.shared.dto.ServiceResponseDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import java.util.Optional;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/appointment")
@RequiredArgsConstructor
@Tag(name = "Appointments", description = "Endpoints for managing and viewing appointments")
public class AppointmentControllerV1 {

    private final AppointmentService appointmentService;

    @Operation(summary = "Get appointments", description = "Retrieves all appointments for admin")
    @ApiResponses(
            value = {
                @ApiResponse(
                        responseCode = "200",
                        description = "Successfully retrieved list of appointments",
                        content =
                                @Content(
                                        array = @ArraySchema(schema = @Schema(implementation = AppointmentDTO.class)))),
                @ApiResponse(responseCode = "204", description = "No appointments found", content = @Content),
                @ApiResponse(responseCode = "401", description = "Unauthorized", content = @Content),
                @ApiResponse(responseCode = "403", description = "Insufficient permissions", content = @Content),
                @ApiResponse(responseCode = "500", description = "Internal Server Error", content = @Content)
            })
    @GetMapping("/admin/get-all-appointments")
    public ResponseEntity<Page<AppointmentDTO>> getAllAppointments(
            @RequestParam(
                            name = SharedConstants.PAGE_NUMBER_PARAMETER_NAME,
                            defaultValue = SharedConstants.PAGE_NUMBER_DEFAULT_VALUE)
                    final int pageNumber,
            @RequestParam(
                            name = SharedConstants.PAGE_SIZE_PARAMETER_NAME,
                            defaultValue = SharedConstants.PAGE_SIZE_DEFAULT_VALUE)
                    final int pageSize,
            @RequestParam(name = SharedConstants.SEARCH_TERM_PARAMETER_NAME, required = false) final String searchTerm,
            @RequestHeader(X_CORRELATION_ID) final UUID xCorrelationId) {
        Page<AppointmentDTO> pagedAppointments =
                appointmentService.getAllAppointments(pageNumber, pageSize, searchTerm, xCorrelationId);
        return ResponseEntity.ok(pagedAppointments);
    }

    @Operation(summary = "Get appointments", description = "Retrieves all appointments for logged in customer")
    @ApiResponses(
            value = {
                @ApiResponse(
                        responseCode = "200",
                        description = "Successfully retrieved list of appointments for customer",
                        content =
                                @Content(
                                        array = @ArraySchema(schema = @Schema(implementation = AppointmentDTO.class)))),
                @ApiResponse(responseCode = "204", description = "No appointments found", content = @Content),
                @ApiResponse(responseCode = "401", description = "Unauthorized", content = @Content),
                @ApiResponse(responseCode = "403", description = "Insufficient permissions", content = @Content),
                @ApiResponse(responseCode = "500", description = "Internal Server Error", content = @Content)
            })
    @GetMapping("/customer/appointments")
    public ResponseEntity<Page<AppointmentDTO>> getCustomerAppointments(
            @RequestParam(
                            name = SharedConstants.PAGE_NUMBER_PARAMETER_NAME,
                            defaultValue = SharedConstants.PAGE_NUMBER_DEFAULT_VALUE)
                    final int pageNumber,
            @RequestParam(
                            name = SharedConstants.PAGE_SIZE_PARAMETER_NAME,
                            defaultValue = SharedConstants.PAGE_SIZE_DEFAULT_VALUE)
                    final int pageSize,
            @RequestParam(name = SharedConstants.SEARCH_TERM_PARAMETER_NAME, required = false) final String searchTerm,
            @RequestHeader(X_CORRELATION_ID) final UUID xCorrelationId) {

        Page<AppointmentDTO> pagedAppointments =
                appointmentService.getAppointmentsByCustomer(pageNumber, pageSize, searchTerm, xCorrelationId);

        return ResponseEntity.ok(pagedAppointments);
    }

    @GetMapping("/findByReferenceNo")
    public ResponseEntity<Page<AppointmentDTO>> findAppointmentByReferenceNo(
            @RequestParam(name = "referenceNo") final String referenceNo,
            @RequestParam(
                            name = SharedConstants.PAGE_NUMBER_PARAMETER_NAME,
                            defaultValue = SharedConstants.PAGE_NUMBER_DEFAULT_VALUE,
                            required = false)
                    final int pageNumber,
            @RequestParam(
                            name = SharedConstants.PAGE_SIZE_PARAMETER_NAME,
                            defaultValue = SharedConstants.PAGE_SIZE_DEFAULT_VALUE,
                            required = false)
                    final int pageSize,
            @RequestHeader(X_CORRELATION_ID) final UUID xCorrelationId) {

        Page<AppointmentDTO> pagedAppointments =
                appointmentService.getAppointmentByReferenceNo(referenceNo, pageNumber, pageSize, xCorrelationId);

        return ResponseEntity.ok(pagedAppointments);
    }

    @Operation(
            summary = "Get Appointment by ID",
            description = "Retrieves Appointment details that matches the provided ID(UUID).")
    @ApiResponses(
            value = {
                @ApiResponse(
                        responseCode = "200",
                        description = "Successfully retrieved appointment details",
                        content =
                                @Content(
                                        array = @ArraySchema(schema = @Schema(implementation = AppointmentDTO.class)))),
                @ApiResponse(responseCode = "204", description = "No appointment found", content = @Content),
                @ApiResponse(responseCode = "401", description = "Unauthorized", content = @Content),
                @ApiResponse(responseCode = "403", description = "Insufficient permissions", content = @Content),
                @ApiResponse(responseCode = "500", description = "Internal Server Error", content = @Content)
            })
    @GetMapping("/{id}")
    public ResponseEntity<AppointmentDTO> getAppointmentById(
            @PathVariable final UUID id, @RequestHeader(X_CORRELATION_ID) final UUID xCorrelationId) {

        Optional<AppointmentDTO> appointmentDTO = appointmentService.getAppointmentById(id, xCorrelationId);

        return ResponseEntity.ok(appointmentDTO.get());
    }

    @Operation(summary = "Create an appointment", description = "Make an appointment on the branch")
    @ApiResponses(
            value = {
                @ApiResponse(
                        responseCode = "200",
                        description = "Successfully created the appointment",
                        content = @Content(contentSchema = @Schema(implementation = NewAppointmentRequest.class))),
                @ApiResponse(responseCode = "401", description = "Unauthorized", content = @Content),
                @ApiResponse(responseCode = "403", description = "Insufficient permissions", content = @Content),
                @ApiResponse(responseCode = "500", description = "Internal Server Error", content = @Content)
            })
    @PostMapping
    public ResponseEntity<ServiceResponseDto> createAppointment(
            @RequestBody @Valid final NewAppointmentRequest request,
            @RequestHeader(X_CORRELATION_ID) final UUID xCorrelationId) {

        Optional<ServiceResponseDto> responseDto = appointmentService.createAppointment(request, xCorrelationId);

        return ResponseEntity.ok(responseDto.get());
    }

    @Operation(summary = "Modifies the existing appointment", description = "Modifies the existing appointment.")
    @ApiResponses(
            value = {
                @ApiResponse(
                        responseCode = "200",
                        description = "Successfully updated the appointment",
                        content = @Content(contentSchema = @Schema(implementation = UpdateAppointmentRequest.class))),
                @ApiResponse(responseCode = "204", description = "No appointment found", content = @Content),
                @ApiResponse(responseCode = "401", description = "Unauthorized", content = @Content),
                @ApiResponse(responseCode = "403", description = "Insufficient permissions", content = @Content),
                @ApiResponse(responseCode = "500", description = "Internal Server Error", content = @Content)
            })
    @PutMapping("/{id}")
    public ResponseEntity<ServiceResponseDto> updateAppointment(
            @PathVariable("id") final UUID id,
            @RequestBody @Valid final UpdateAppointmentRequest request,
            @RequestHeader(X_CORRELATION_ID) final UUID xCorrelationId) {

        Optional<ServiceResponseDto> responseDto = appointmentService.updateAppointment(request, xCorrelationId);

        return ResponseEntity.ok(responseDto.get());
    }

    @Operation(
            summary = "Modifies the existing appointment status",
            description = "Modifies the existing appointment status.")
    @ApiResponses(
            value = {
                @ApiResponse(
                        responseCode = "200",
                        description = "Successfully updated the appointment",
                        content = @Content(contentSchema = @Schema(implementation = UpdateAppointmentRequest.class))),
                @ApiResponse(responseCode = "204", description = "No appointment found", content = @Content),
                @ApiResponse(responseCode = "401", description = "Unauthorized", content = @Content),
                @ApiResponse(responseCode = "403", description = "Insufficient permissions", content = @Content),
                @ApiResponse(responseCode = "500", description = "Internal Server Error", content = @Content)
            })
    @PostMapping("/update-status")
    public ResponseEntity<ServiceResponseDto> updateStatus(
            @RequestBody @Valid final UpdateAppointmentStatusRequest request,
            @RequestHeader(X_CORRELATION_ID) final UUID xCorrelationId) {

        Optional<ServiceResponseDto> responseDto = appointmentService.updateAppointmentStatus(request, xCorrelationId);

        return ResponseEntity.ok(responseDto.get());
    }
}
