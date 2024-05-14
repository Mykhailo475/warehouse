package ua.wms.warehouse.configuration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.LogoutConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import ua.wms.warehouse.service.UserPrincipalDetailsService;

@Configuration
@EnableWebSecurity
public class SecurityConfiguration {

    @Value("${domen}")
    private String domen;

    @Autowired
    private UserPrincipalDetailsService userPrincipalDetailsService;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .authorizeHttpRequests((requests) -> requests
                                .requestMatchers("/home", "/sign-up", "/logout", "/login", "/activate/*").permitAll()
                        .requestMatchers("/employees","/employees/**").hasRole("CLIENT")
                                .anyRequest().authenticated()
                )
                .formLogin((form) -> form
                        .loginPage("/home")
                        .defaultSuccessUrl(domen)
                        .permitAll()
                        .usernameParameter("email")
                )
                .logout(LogoutConfigurer::permitAll);

        return http.build();
    }

    @Bean
    DaoAuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider daoAuthProvider = new DaoAuthenticationProvider();

        daoAuthProvider.setPasswordEncoder(passwordEncoder());
        daoAuthProvider.setUserDetailsService(this.userPrincipalDetailsService);

        return daoAuthProvider;
    }

    @Bean
    public static PasswordEncoder passwordEncoder() {

        return new BCryptPasswordEncoder();
    }

}
