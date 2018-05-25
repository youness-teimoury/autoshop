package youness.automotive;

/**
 * Youness Teimouri
 * www.youness-teimouri.com
 * <p>
 * A sample App that uses templating to automatically create simple CRUD views for models
 * For more info about SpringBootApplication visit:
 * https://spring.io/guides/gs/rest-service/
 * <p>
 * For more info about Spring JMS messaging visit:
 * https://spring.io/guides/gs/messaging-jms/
 */

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication // tells Spring to look for other components and activates key behaviours of Spring MVC
@EnableJpaRepositories("youness.automotive.repository") // To enable JPA repositories
public class Application {

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }
}
