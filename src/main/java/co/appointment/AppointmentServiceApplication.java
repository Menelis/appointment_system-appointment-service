package co.appointment;

import co.appointment.config.AppConfigProperties;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.data.jdbc.repository.config.EnableJdbcAuditing;

@SpringBootApplication
@EnableConfigurationProperties({AppConfigProperties.class})
@EnableJdbcAuditing
@SuppressWarnings({"HideUtilityClassConstructor"})
public class AppointmentServiceApplication {

    static void main(final String[] args) {
        SpringApplication.run(AppointmentServiceApplication.class, args);
    }
}
