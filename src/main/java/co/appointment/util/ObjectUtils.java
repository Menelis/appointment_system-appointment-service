package co.appointment.util;

import co.appointment.entity.Appointment;
import co.appointment.entity.Slot;
import co.appointment.grpc.GetUserResponse;
import co.appointment.shared.constant.SharedConstants;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class ObjectUtils {

    /**
     * Get confirmed appointment.
     * @param appointment {@link Appointment} instance
     * @param userResponse {@link GetUserResponse} instance
     * @return Email body
     */
    public static String getAppointmentPendingConfirmedEmailBody(final Appointment appointment,
                                                                 final GetUserResponse userResponse,
                                                                 final String emailTemplate) {
        return String.format(emailTemplate,
                userResponse.getFullName(),
                appointment.getBranchId(),
                appointment.getAppointmentDate(),
                getAppointmentSlot(appointment),
                appointment.getReferenceNo(),
                appointment.getStatus(),
                SharedConstants.APPOINTMENT_SYSTEM_EMAIL_FOOTER);
    }
    public static String getAppointmentConfirmedEmailBody(final Appointment appointment,
                                                          final GetUserResponse userResponse,
                                                          final String emailTemplate) {
        return String.format(emailTemplate,
                userResponse.getFullName(),
                appointment.getBranchId(),
                appointment.getAppointmentDate(),
                getAppointmentSlot(appointment),
                appointment.getReferenceNo(),
                appointment.getStatus(),
                SharedConstants.APPOINTMENT_SYSTEM_EMAIL_FOOTER);
    }
    public static String getAppointmentCancelledEmailBody(final Appointment appointment,
                                                          final GetUserResponse userResponse,
                                                          final String emailTemplate) {
        return String.format(emailTemplate,
                userResponse.getFullName(),
                appointment.getBranchId(),
                appointment.getAppointmentDate(),
                getAppointmentSlot(appointment),
                appointment.getDescription(),
                SharedConstants.APPOINTMENT_SYSTEM_EMAIL_FOOTER);
    }
    private static String getAppointmentSlot(final Appointment appointment) {
        Slot appointmentSlot = appointment.getSlot();
        if(appointmentSlot == null) {
            return "Any time";
        }
        return String.format("%s - %s", appointmentSlot.getSlotStart(), appointmentSlot.getSlotEnd());
    }
}
