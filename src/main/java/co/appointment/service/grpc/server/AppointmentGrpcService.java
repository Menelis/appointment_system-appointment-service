package co.appointment.service.grpc.server;

import co.appointment.grpc.AppointmentServiceGrpc;
import co.appointment.grpc.GetAppointmentByReferenceNoRequest;
import co.appointment.grpc.GetAppointmentResponse;
import co.appointment.persistence.AppointmentRepository;
import co.appointment.shared.record.AppointmentSummary;
import io.grpc.stub.StreamObserver;
import lombok.RequiredArgsConstructor;
import org.springframework.grpc.server.service.GrpcService;

@GrpcService
@RequiredArgsConstructor
public class AppointmentGrpcService extends AppointmentServiceGrpc.AppointmentServiceImplBase {

    private final AppointmentRepository appointmentRepository;

    @Override
    public void processAppointmentByReferenceNo(
            final GetAppointmentByReferenceNoRequest request,
            final StreamObserver<GetAppointmentResponse> responseObserver) {
        AppointmentSummary appointmentSummary = appointmentRepository
                .getAppointmentByReferenceNo(request.getReferenceNo())
                .orElseThrow(() -> new IllegalArgumentException(
                        "No Appointment exists with referenceNo=[%s]".formatted(request.getReferenceNo())));

        GetAppointmentResponse.Builder builder = GetAppointmentResponse.newBuilder()
                .setId(appointmentSummary.id().toString())
                .setCustomerId(appointmentSummary.customerId().toString())
                .setBranchId(appointmentSummary.branchId().toString())
                .setAppointmentDate(appointmentSummary.appointmentDate().toString())
                .setSlotStart(appointmentSummary.slotStart())
                .setSlotEnd(appointmentSummary.slotEnd())
                .setDescription(appointmentSummary.description())
                .setReferenceNo(appointmentSummary.referenceNo())
                .setStatus(appointmentSummary.status())
                .setCancellationReason(appointmentSummary.cancellationReason());

        responseObserver.onNext(builder.build());
        responseObserver.onCompleted();
    }
}
