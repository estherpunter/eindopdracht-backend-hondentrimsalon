package nl.novi.eindopdrachtbackendhondentrimsalon.config;

import nl.novi.eindopdrachtbackendhondentrimsalon.filters.JwtRequestFilter;
import nl.novi.eindopdrachtbackendhondentrimsalon.services.CustomUserDetailsService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.Customizer;
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
    private final CustomUserDetailsService customUserDetailsService;

    public SpringSecurityConfig(CustomUserDetailsService customUserDetailsService) {
        this.customUserDetailsService = customUserDetailsService;
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
    protected SecurityFilterChain filter(HttpSecurity http, JwtRequestFilter jwtRequestFilter) throws Exception {
        http
                .csrf(AbstractHttpConfigurer::disable)
                .httpBasic(AbstractHttpConfigurer::disable)
                .cors(Customizer.withDefaults())
                .authorizeHttpRequests(auth -> auth
                        // Appointments endpoints
                        .requestMatchers(HttpMethod.GET, "/api/appointments/allappointments").hasAnyAuthority("ADMIN", "DOGGROOMER", "CASHIER")
                        .requestMatchers(HttpMethod.GET, "/api/appointments/{appointmentId}").hasAnyAuthority("ADMIN", "DOGGROOMER", "CASHIER")
                        .requestMatchers(HttpMethod.POST, "/api/appointments/allappointments").hasAuthority("ADMIN") // Scheduling appointments
                        .requestMatchers(HttpMethod.PUT, "/api/appointments/{appointmentId}").hasAnyAuthority("ADMIN", "CASHIER") // Updating appointments
                        .requestMatchers(HttpMethod.DELETE, "/api/appointments/{appointmentId}").hasAnyAuthority("ADMIN", "CASHIER") // Canceling an appointment
                        .requestMatchers(HttpMethod.POST, "/api/appointments/**").hasAuthority("ADMIN") // Adding products and treatments to appointments
                        .requestMatchers(HttpMethod.POST, "/api/appointments/{appointmentId}/products").hasAuthority("DOGGROOMER") // Adding products to appointments
                        .requestMatchers(HttpMethod.POST, "/api/appointments/{appointmentId}/treatments").hasAuthority("DOGGROOMER") // Adding treatments to appointments
                        .requestMatchers(HttpMethod.POST, "/api/appointments/{appointmentId}/custom-treatment").hasAuthority("DOGGROOMER") // Adding a custom treatment to an appointment
                        .requestMatchers(HttpMethod.POST, "/api/appointments/{appointmentId}/generate-receipt").hasAuthority("CASHIER")

                        // Authentication endpoints
                        .requestMatchers("/authenticated").authenticated()
                        .requestMatchers("/authenticate").permitAll()

                        // Customers endpoints
                        .requestMatchers(HttpMethod.POST, "/api/customers").hasAuthority("ADMIN")
                        .requestMatchers(HttpMethod.PUT, "/api/customers/**").hasAuthority("ADMIN")

                        // Dogs endpoints
                        .requestMatchers(HttpMethod.PUT, "/api/dogs/**").hasAuthority("ADMIN")

                        // Products endpoints
                        .requestMatchers(HttpMethod.POST, "/api/products").hasAuthority("ADMIN")
                        .requestMatchers(HttpMethod.PUT, "/api/products/**").hasAuthority("ADMIN")
                        .requestMatchers(HttpMethod.GET, "/api/products/**").hasAuthority("ADMIN")

                        // Treatments endpoints
                        .requestMatchers(HttpMethod.POST, "/api/treatments").hasAuthority("ADMIN")
                        .requestMatchers(HttpMethod.GET, "/api/treatments/**").hasAuthority("DOGGROOMER")
                        .requestMatchers(HttpMethod.PUT, "/api/treatments/**").hasAuthority("DOGGROOMER")

                        // Users endpoints
                        .requestMatchers("/api/users").permitAll()
                        .anyRequest().denyAll()
                )
                .sessionManagement(sess -> sess.sessionCreationPolicy(SessionCreationPolicy.STATELESS));

        http.addFilterBefore(jwtRequestFilter, UsernamePasswordAuthenticationFilter.class);
        return http.build();
    }

}
