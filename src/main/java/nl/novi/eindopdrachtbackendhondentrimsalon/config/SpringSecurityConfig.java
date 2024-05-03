package nl.novi.eindopdrachtbackendhondentrimsalon.config;

import nl.novi.eindopdrachtbackendhondentrimsalon.filters.JwtRequestFilter;
import nl.novi.eindopdrachtbackendhondentrimsalon.services.CustomUserDetailsService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class SpringSecurityConfig {

    public final CustomUserDetailsService customUserDetailsService;

    private final JwtRequestFilter jwtRequestFilter;

    public SpringSecurityConfig(CustomUserDetailsService customUserDetailsService, JwtRequestFilter jwtRequestFilter) {
        this.customUserDetailsService = customUserDetailsService;
        this.jwtRequestFilter = jwtRequestFilter;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager(PasswordEncoder passwordEncoder) {
        var auth = new DaoAuthenticationProvider();
        auth.setPasswordEncoder(passwordEncoder);
        auth.setUserDetailsService(customUserDetailsService);
        return new ProviderManager(auth);
    }

    @Bean
    protected SecurityFilterChain filter(HttpSecurity http) throws Exception {

        http
                .csrf(AbstractHttpConfigurer::disable)
                .httpBasic(AbstractHttpConfigurer::disable)
                .cors(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/api/customers/**", "/api/appointments/**", "api/dogs/**", "api/treatments/**", "/api/products/**").hasRole("ROLE_ADMIN")
                        .requestMatchers(HttpMethod.POST, "/api/appointments/{appointmentId}/generate-receipt").hasRole("ROLE_CASHIER")
                        .requestMatchers(HttpMethod.GET, "api/appointments/{appointmentId}").hasRole("ROLE_CASHIER")
                        .requestMatchers(HttpMethod.PUT, "api/appointments/{appointmentId}").hasRole("ROLE_CASHIER")
                        .requestMatchers(HttpMethod.POST, "api/appointments").hasRole("ROLE_DOGGROOMER")
                        .requestMatchers(HttpMethod.GET, "api/customers/{customerId}").hasRole("ROLE_DOGGROOMER")
                        .requestMatchers(HttpMethod.POST, "api/appointments/{appointmentId}/products").hasRole("ROLE_DOGGROOMER")
                        .requestMatchers(HttpMethod.POST, "api/appointments/{appointmentId}/treatments").hasRole("ROLE_DOGGROOMER")
                        .requestMatchers(HttpMethod.POST, "api/appointments/{appointmentId}/custom-treatment").hasRole("ROLE_DOGGROOMER")
                        .requestMatchers("/authenticated").hasRole("ADMIN")
                        .requestMatchers("/authenticated").hasRole("ADMIN")
                        .anyRequest().denyAll()
                )
                .sessionManagement(sess -> sess.sessionCreationPolicy(SessionCreationPolicy.STATELESS));
        http.addFilterBefore(jwtRequestFilter, UsernamePasswordAuthenticationFilter.class);
        return http.build();
    }

}
