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
    public AuthenticationManager authenticationManager(HttpSecurity http, PasswordEncoder passwordEncoder) throws Exception {
        var auth = new DaoAuthenticationProvider();
        auth.setPasswordEncoder(passwordEncoder);
        auth.setUserDetailsService(customUserDetailsService);
        return new ProviderManager(auth);
    }

    @Bean
    protected SecurityFilterChain filter(HttpSecurity http) throws Exception {

        http
                .csrf(csrf -> csrf.disable())
                .httpBasic(basic -> basic.disable())
                .cors(Customizer.withDefaults())
                .authorizeHttpRequests(auth -> auth

                        .requestMatchers("/api/customers/**", "/api/appointments/**", "api/dogs/**", "api/treatments/**", "/api/products/**").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.POST, "/api/appointments/{appointmentId}/generate-receipt").hasRole("CASHIER")
                        .requestMatchers(HttpMethod.GET, "api/appointments/{appointmentId}").hasRole("CASHIER")
                        .requestMatchers(HttpMethod.POST, "api/appointments").hasRole("DOGGROOMER")
                        .requestMatchers(HttpMethod.GET, "api/customers/{customerId}").hasRole("DOGGROOMER")
                        .requestMatchers(HttpMethod.POST, "api/appointments/{appointmentId}/products").hasRole("DOGGROOMER")
                        .requestMatchers(HttpMethod.POST, "api/appointments/{appointmentId}/treatments").hasRole("DOGGROOMER")
                        .requestMatchers(HttpMethod.POST, "api/appointments/{appointmentId}/custom-treatment").hasRole("DOGGROOMER")
                        .requestMatchers("/authenticated").hasRole("ADMIN")
                        .requestMatchers("/authenticated").hasRole("ADMIN")
                        .anyRequest().denyAll()
                )
                .sessionManagement(sess -> sess.sessionCreationPolicy(SessionCreationPolicy.STATELESS));
        http.addFilterBefore(jwtRequestFilter, UsernamePasswordAuthenticationFilter.class);
        return http.build();
    }

}
