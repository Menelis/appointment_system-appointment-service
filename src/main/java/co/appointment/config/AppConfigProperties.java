package co.appointment.config;

import co.appointment.shared.model.CorsSettings;
import co.appointment.shared.model.OpenApiSettings;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "app")
@Data
public class AppConfigProperties {

    private OpenApiSettings openApi = new OpenApiSettings();
    private String[] whiteList;
    private CorsSettings cors = new CorsSettings();
}
