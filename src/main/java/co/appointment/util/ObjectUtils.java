package co.appointment.util;

import co.appointment.entity.Appointment;
import co.appointment.entity.Slot;
import co.appointment.grpc.GetBranchResponse;
import co.appointment.grpc.GetUserResponse;
import co.appointment.shared.constant.SharedConstants;
import co.appointment.shared.util.SharedObjectUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.StringUtils;

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
                                                                 final GetBranchResponse branchResponse,
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
                                                          final GetBranchResponse branchResponse,
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
                                                          final GetBranchResponse branchResponse,
                                                          final String emailTemplate) {
        return String.format(emailTemplate,
                userResponse.getFullName(),
                branchResponse.getName(),
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
    private static String appendBranchDetails(final GetBranchResponse response) {
        String fullAddress = String.format("%s %s %s  %s  %s %s",
                SharedObjectUtils.returnEmptyIfNullOrBlank(response.getStreetNo()),
                SharedObjectUtils.returnEmptyIfNullOrBlank(response.getAddressLine1()),
                SharedObjectUtils.returnEmptyIfNullOrBlank(response.getAddressLine2()),
                SharedObjectUtils.returnEmptyIfNullOrBlank(response.getCity()),
                SharedObjectUtils.returnEmptyIfNullOrBlank(response.getProvince()),
                SharedObjectUtils.returnEmptyIfNullOrBlank(response.getPostalCode()));
        return String.format("<b>Address:</b>%s<br/><b>Email:</b>%s<br/><b>Land Line:</b>%s<br/><b>Fax:</b>%s",
                fullAddress,
                response.getEmailAddress(),
                response.getLandLine(),
                response.getFaxNumber());
    }
    private static String returnEmptyStringIfNull(final String stringValue) {
        if(StringUtils.hasText(stringValue)) {
            return stringValue;
        }
        return "";
    }
}
