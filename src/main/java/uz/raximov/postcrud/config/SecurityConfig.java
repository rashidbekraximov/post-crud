package uz.raximov.postcrud.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.LoginUrlAuthenticationEntryPoint;
import uz.raximov.postcrud.service.HttpUserDetailsService;


@Configuration
@EnableMethodSecurity(prePostEnabled = true)
public class SecurityConfig {

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }

    @Bean
    public SecurityFilterChain httpFilterChain(HttpSecurity http) throws Exception {
        http
                .authorizeHttpRequests(authorize -> authorize
                        .requestMatchers("/", "/static/css/**", "/css/**").permitAll() // Allow CSS and root
                        .requestMatchers("/**").authenticated() // Secure certain paths
                        .anyRequest().permitAll() // Allow other requests
                )
                .formLogin(form -> form
                        .loginPage("/login") // Custom login page
                        .defaultSuccessUrl("/home", true) // Redirect after successful login
                        .failureUrl("/") // Redirect after failure
                        .usernameParameter("username") // Form parameter name
                        .passwordParameter("password")
                        .successHandler(new CustomAuthenticationSuccessHandler())
                        .failureHandler(new CustomAuthenticationFailureHandler())// Form parameter name
                )
                .logout(logout -> logout
                        .logoutSuccessUrl("/") // Redirect after logout
                )
                .exceptionHandling(exception -> exception
                        .authenticationEntryPoint(new LoginUrlAuthenticationEntryPoint("/"))
                );

        return http.build();
    }
}

