package co.appointment.config;

import co.appointment.shared.model.OpenApiSettings;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "app")
@Data
public class AppConfigProperties {

    private OpenApiSettings openApi = new OpenApiSettings();
    private String[] whiteList = {};
    private String[] adminRoutes = {};
    private KafkaSettings kafka = new KafkaSettings();
    private EmailTemplate emailTemplate = new EmailTemplate();
    private String authoritiesClaimName = "roles";
    private String authoritiesClaimPrefix = "ROLE_";

    @Data
    public static class KafkaSettings {
        private String notificationTopic;
    }
    @Data
    public static class EmailTemplate {
        private String pendingConfirmationEmailTemplate;
        private String confirmationEmailTemplate;
        private String cancellationEmailTemplate;
    }
}
