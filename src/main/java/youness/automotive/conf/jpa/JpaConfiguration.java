package youness.automotive.conf.jpa;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.sql.DataSource;

/**
 * Youness Teimouri
 * www.youness-teimouri.com
 *
 * The configuration that looks up for persistence proerties and sets up the datasource
 * For more info visit http://www.baeldung.com/spring-jpa-test-in-memory-database
 */
@Configuration
@EnableJpaRepositories(basePackages = "youness.automotive.repository.model")
@PropertySource("persistence.properties")
@EnableTransactionManagement
public class JpaConfiguration {
    @Autowired
    private Environment env;

    @Bean
    public DataSource dataSource() {
        DriverManagerDataSource dataSource = new DriverManagerDataSource();
        dataSource.setDriverClassName(env.getProperty("jdbc.driverClassName"));
        dataSource.setUrl(env.getProperty("jdbc.url"));
        dataSource.setUsername(env.getProperty("jdbc.user"));
        dataSource.setPassword(env.getProperty("jdbc.pass"));

        return dataSource;
    }
}
