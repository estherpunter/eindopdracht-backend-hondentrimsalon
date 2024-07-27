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
                        .requestMatchers(HttpMethod.GET, "/api/appointments").hasAnyAuthority("ADMIN", "DOGGROOMER", "CASHIER")
                        .requestMatchers(HttpMethod.GET, "/api/appointments/{appointmentId}").hasAnyAuthority("ADMIN", "DOGGROOMER", "CASHIER")
                        .requestMatchers(HttpMethod.POST, "/api/appointments").hasAuthority("ADMIN")
                        .requestMatchers(HttpMethod.PUT, "/api/appointments/{appointmentId}").hasAnyAuthority("ADMIN", "CASHIER")
                        .requestMatchers(HttpMethod.DELETE, "/api/appointments/{appointmentId}").hasAnyAuthority("ADMIN", "CASHIER")
                        .requestMatchers(HttpMethod.POST, "/api/appointments/**").hasAuthority("ADMIN")
                        .requestMatchers(HttpMethod.POST, "/api/appointments/{appointmentId}/products").hasAuthority("DOGGROOMER")
                        .requestMatchers(HttpMethod.POST, "/api/appointments/{appointmentId}/treatments").hasAuthority("DOGGROOMER")
                        .requestMatchers(HttpMethod.POST, "/api/appointments/{appointmentId}/custom-treatment").hasAuthority("DOGGROOMER")
                        .requestMatchers(HttpMethod.POST, "/api/appointments/{appointmentId}/generate-receipt").hasAuthority("CASHIER")

                        // Authentication endpoints
                        .requestMatchers("/authenticated").authenticated()
                        .requestMatchers("/authenticate").permitAll()

                        // Customers endpoints
                        .requestMatchers(HttpMethod.GET, "/api/customers").hasAuthority("ADMIN")
                        .requestMatchers(HttpMethod.GET, "/api/customers/{customerId}").hasAuthority("ADMIN")
                        .requestMatchers(HttpMethod.POST, "/api/customers").hasAuthority("ADMIN")
                        .requestMatchers(HttpMethod.PUT, "/api/customers/{customerId}").hasAuthority("ADMIN")
                        .requestMatchers(HttpMethod.DELETE, "/api/customers/{customerId}").hasAuthority("ADMIN")
                        .requestMatchers(HttpMethod.POST, "/api/customers/{customerId}/dogs").hasAuthority("ADMIN")
                        .requestMatchers(HttpMethod.DELETE, "api/customers/{customerId}/dogs/{dogId}").hasAuthority("ADMIN")

                        // Products endpoints
                        .requestMatchers(HttpMethod.GET, "/api/products").hasAnyAuthority("ADMIN", "DOGGROOMER")
                        .requestMatchers(HttpMethod.GET, "api/products/**").hasAnyAuthority("ADMIN", "DOGGROOMER")
                        .requestMatchers(HttpMethod.POST, "/api/products").hasAuthority("ADMIN")
                        .requestMatchers(HttpMethod.PUT, "/api/products/**").hasAuthority("ADMIN")
                        .requestMatchers(HttpMethod.DELETE, "api/products/**").hasAuthority("ADMIN")

                        // Treatments endpoints
                        .requestMatchers(HttpMethod.GET, "/api/treatments").hasAnyAuthority("ADMIN", "DOGGROOMER")
                        .requestMatchers(HttpMethod.GET, "api/treatments/**").hasAnyAuthority("ADMIN", "DOGGROOMER")
                        .requestMatchers(HttpMethod.POST, "/api/treatments").hasAuthority("ADMIN")
                        .requestMatchers(HttpMethod.PUT, "api/treatments/{treatmentId}").hasAuthority("ADMIN")
                        .requestMatchers(HttpMethod.DELETE, "/api/treatments/{treatmentId}").hasAuthority("ADMIN")

                        // Users endpoints
                        .requestMatchers("/api/users").permitAll()
                        .requestMatchers("api/users/**").permitAll()

                        .anyRequest().denyAll()
                )
                .sessionManagement(sess -> sess.sessionCreationPolicy(SessionCreationPolicy.STATELESS));

        http.addFilterBefore(jwtRequestFilter, UsernamePasswordAuthenticationFilter.class);
        return http.build();
    }

}
