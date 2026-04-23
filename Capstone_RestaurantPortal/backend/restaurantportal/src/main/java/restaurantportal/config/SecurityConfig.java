package restaurantportal.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import restaurantportal.security.JwtFilter;

@Configuration
public class SecurityConfig {

    private final JwtFilter jwtFilter;

    public SecurityConfig(JwtFilter jwtFilter) {
        this.jwtFilter = jwtFilter;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

        http
                // disable csrf (not needed for REST APIs)
                .csrf(csrf -> csrf.disable())

                // no session (JWT based)
                .sessionManagement(session ->
                        session.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                )

                // authorization rules
                .authorizeHttpRequests(auth -> auth

                        // public APIs (no login required)
                        .requestMatchers("/api/users/register", "/api/users/login").permitAll()

                        // USER role APIs
                        .requestMatchers("/api/cart/**", "/api/orders/**")
                        .hasAuthority("USER")

                        // RESTAURANT OWNER APIs
                        .requestMatchers("/api/restaurants/**")
                        .hasAuthority("RESTAURANT_OWNER")

                        // any other request must be authenticated
                        .anyRequest().authenticated()
                )

                // add JWT filter before Spring Security filter
                .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    // password encoder (for hashing passwords)
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    // authentication manager (used internally by Spring Security)
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }
}