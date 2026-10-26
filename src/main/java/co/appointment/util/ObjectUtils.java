package co.appointment.util;

import co.appointment.persistence.AppointmentEntity;
import co.appointment.shared.constant.SharedConstants;
import co.appointment.shared.record.BranchRecord;
import co.appointment.shared.record.UserRecord;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@SuppressWarnings({"HideUtilityClassConstructor"})
public class ObjectUtils {
    /**
     * Get confirmed appointment.
     * @param appointment {@link AppointmentEntity} instance
     * @param userRecord {@link UserRecord} instance
     * @param branchRecord {@link BranchRecord} instance
     * @return Email body
     */
    //    public static String getAppointmentPendingConfirmedEmailBody(final Appointment appointment,
    //                                                                 final UserRecord userRecord,
    //                                                                 final BranchRecord branchRecord,
    //                                                                 final String emailTemplate) {
    //        return String.format(emailTemplate,
    //                userRecord.fullName(),
    //                branchRecord.name(),
    //                appointment.getAppointmentDate(),
    //                null,//getAppointmentSlot(appointment),
    //                appointment.getReferenceNo(),
    //                appointment.getStatus(),
    //                SharedConstants.APPOINTMENT_SYSTEM_EMAIL_FOOTER,
    //                appendBranchDetails(branchRecord));
    //    }
    //    public static String getAppointmentConfirmedEmailBody(final Appointment appointment,
    //                                                          final UserRecord userRecord,
    //                                                          final BranchRecord branchRecord,
    //                                                          final String emailTemplate) {
    //        return String.format(emailTemplate,
    //                userRecord.fullName(),
    //                branchRecord.name(),
    //                appointment.getAppointmentDate(),
    //                getAppointmentSlot(appointment),
    //                appointment.getReferenceNo(),
    //                appointment.getStatus(),
    //                SharedConstants.APPOINTMENT_SYSTEM_EMAIL_FOOTER,
    //                appendBranchDetails(branchRecord));
    //    }
    public static String getAppointmentCancelledEmailBody(
            final AppointmentEntity appointment,
            final UserRecord userRecord,
            final BranchRecord branchRecord,
            final String emailTemplate) {
        return String.format(
                emailTemplate,
                userRecord.fullName(),
                branchRecord.name(),
                appointment.getAppointmentDate(),
                null /*getAppointmentSlot(appointment)*/,
                appointment.getCancellationReason(),
                SharedConstants.APPOINTMENT_SYSTEM_EMAIL_FOOTER,
                appendBranchDetails(branchRecord));
    }
    //    private static String getAppointmentSlot(final AppointmentEntity appointment) {
    //        Slot appointmentSlot = appointment.getSlot();
    //        if (appointmentSlot == null) {
    //            return "Any time";
    //        }
    //        return String.format("%s - %s", appointmentSlot.getSlotStart(), appointmentSlot.getSlotEnd());
    //    }
    private static String appendBranchDetails(final BranchRecord branchRecord) {
        return "";
        /*String fullAddress = String.format("%s %s %s  %s  %s %s",
                SharedObjectUtils.returnEmptyIfNullOrBlank(branchRecord.streetNo()),
                SharedObjectUtils.returnEmptyIfNullOrBlank(branchRecord.addressLine1()),
                SharedObjectUtils.returnEmptyIfNullOrBlank(branchRecord.addressLine2()),
                SharedObjectUtils.returnEmptyIfNullOrBlank(branchRecord.city()),
                SharedObjectUtils.returnEmptyIfNullOrBlank(branchRecord.province()),
                SharedObjectUtils.returnEmptyIfNullOrBlank(branchRecord.postalCode()));
        return String.format("<b>Address:</b>%s<br/><b>Email:</b>%s<br/><b>Land Line:</b>%s<br/><b>Fax:</b>%s",
                fullAddress,
                branchRecord.email(),
                branchRecord.landLine(),
                branchRecord.faxNo());*/
    }
}
