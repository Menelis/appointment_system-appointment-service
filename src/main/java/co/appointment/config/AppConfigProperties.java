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
    private String authoritiesClaimName = "roles";
    private String authoritiesClaimPrefix = "ROLE_";

    @Data
    public static class KafkaSettings {
        private TopicConfig topic;
    }

    @Data
    public static class TopicConfig {
        private Outgoing outgoing;
    }

    @Data
    public static class Outgoing {
        private String notification;
    }
}
