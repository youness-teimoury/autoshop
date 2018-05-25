package youness.automotive.conf.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

import javax.sql.DataSource;

/**
 * Youness Teimouri
 * www.youness-teimouri.com
 * <p>
 * Configuration to define spring security settings
 * CSRF is configured according to http://www.baeldung.com/csrf-thymeleaf-with-spring-security
 */
@Configuration
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    private DataSource dataSource;

    /**
     * Define security level by URL pattern
     *
     * @param http
     * @throws Exception
     */
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.authorizeRequests().antMatchers("*").permitAll();
//        http
//                .authorizeRequests()
//                .antMatchers("/").permitAll()
//                .anyRequest().authenticated()
//                .and()
//                .formLogin()
//                .loginPage("/login")
//                .permitAll()
//                .and()
//                .logout()
//                .permitAll();
    }

    @Override
    public void configure(WebSecurity web) throws Exception {
        web.ignoring()
                .antMatchers(
                        "/js/**",
                        "/css/**");
    }

    @Autowired
    public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
        auth
                .inMemoryAuthentication()
                .withUser("admin").password("admin").roles("ADMIN");
        //        auth
        //                .jdbcAuthentication()
        //                .dataSource(dataSource)
        //                .usersByUsernameQuery(getUsernameQuery())
        //                .authoritiesByUsernameQuery(getAuthoritiesQuery());
    }
}
