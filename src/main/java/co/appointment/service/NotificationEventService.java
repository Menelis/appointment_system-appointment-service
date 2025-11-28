package co.appointment.service;

import co.appointment.config.AppConfigProperties;
import co.appointment.constant.AppointmentStatus;
import co.appointment.entity.Appointment;
import co.appointment.grpc.GetBranchResponse;
import co.appointment.grpc.GetUserResponse;
import co.appointment.shared.constant.EventTypeConstants;
import co.appointment.shared.kafka.event.EmailEvent;
import co.appointment.shared.record.BranchRecord;
import co.appointment.shared.record.UserRecord;
import co.appointment.shared.service.GrcpAuthService;
import co.appointment.shared.service.GrcpBranchService;
import co.appointment.shared.util.KafkaUtils;
import co.appointment.util.ObjectUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.util.AbstractMap;
import java.util.Map;

@Slf4j
@Service
public class NotificationEventService {

    private final KafkaTemplate<String, EmailEvent> kafkaTemplate;
    private final String notificationTopic;
    private final AppConfigProperties.EmailTemplate emailTemplate;
    //Communicate with user service
    private final GrcpAuthService grcpAuthService;
    //Communicate with branch service
    private final GrcpBranchService grcpBranchService;


    private static final Map<String, Object> BOOKING_CONFIRMED_HEADER = Map.of(
            EventTypeConstants.EVENT_TYPE,
            EventTypeConstants.BOOKING_CONFIRMED_EVENT);
    private static final Map<String, Object> BOOKING_PENDING_CONFIRM_HEADER = Map.of(
            EventTypeConstants.EVENT_TYPE,
            EventTypeConstants.BOOKING_PENDING_CONFIRMED_EVENT);
    private static final Map<String, Object> BOOKING_CANCELLED_HEADER = Map.of(
            EventTypeConstants.EVENT_TYPE,
            EventTypeConstants.BOOKING_CANCELLED_EVENT);

    public NotificationEventService(
            final KafkaTemplate<String, EmailEvent> kafkaTemplate,
            final AppConfigProperties appConfigProperties,
            final GrcpAuthService grcpAuthService,
            final GrcpBranchService grcpBranchService) {
        this.kafkaTemplate = kafkaTemplate;
        this.notificationTopic = appConfigProperties.getKafka().getNotificationTopic();
        this.grcpAuthService = grcpAuthService;
        this.grcpBranchService = grcpBranchService;
        this.emailTemplate = appConfigProperties.getEmailTemplate();
    }
    public void publishAppointmentEvent(final Appointment appointment) {
        UserRecord userResponse = grcpAuthService.getUserById(appointment.getCustomerId());
        BranchRecord branchResponse = grcpBranchService.getBranchById(appointment.getBranchId());

        if(userResponse == null) {
            throw new IllegalArgumentException(String.format("Could not sent notification for appointment Id: %s because the user response is null", appointment.getId()));
        }
        if(branchResponse == null) {
            throw new NullPointerException(String.format("Could not sent notification for appointment Id: %s because the branch response is null", appointment.getId()));
        }
        AbstractMap.SimpleImmutableEntry<String, Map<String, Object>> emailBodyWithEventHeaders = getEmailBodyWithEventHeaders(appointment, userResponse, branchResponse);
        EmailEvent emailEvent = new EmailEvent(
                userResponse.email(), String.format("Booking - %s", appointment.getReferenceNo()), emailBodyWithEventHeaders.getKey(), false);
        KafkaUtils.sendKafkaEvent(kafkaTemplate, notificationTopic, null, emailEvent, emailBodyWithEventHeaders.getValue());
    }
    private AbstractMap.SimpleImmutableEntry<String, Map<String, Object>> getEmailBodyWithEventHeaders(
            final Appointment appointment, final UserRecord userRecord, final BranchRecord branchRecord) {

        return switch (appointment.getStatus().toUpperCase()) {
            case AppointmentStatus.BOOKING_PENDING_CONFIRMATION ->
                new AbstractMap.SimpleImmutableEntry<>(ObjectUtils.getAppointmentPendingConfirmedEmailBody(
                        appointment, userRecord, branchRecord, emailTemplate.getPendingConfirmationEmailTemplate()), BOOKING_CONFIRMED_HEADER);
            case AppointmentStatus.BOOKING_CONFIRMED ->
                new AbstractMap.SimpleImmutableEntry<>(ObjectUtils.getAppointmentConfirmedEmailBody(
                        appointment, userRecord, branchRecord, emailTemplate.getConfirmationEmailTemplate()), BOOKING_CONFIRMED_HEADER);
            case AppointmentStatus.BOOKING_CANCELLED ->
                new AbstractMap.SimpleImmutableEntry<>(ObjectUtils.getAppointmentCancelledEmailBody(
                        appointment, userRecord, branchRecord, emailTemplate.getCancellationEmailTemplate()), BOOKING_CANCELLED_HEADER);
            default -> throw new IllegalArgumentException("Invalid status " + appointment.getStatus());
        };
    }
}
