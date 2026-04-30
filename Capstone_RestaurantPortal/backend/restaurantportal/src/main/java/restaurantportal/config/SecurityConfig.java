package restaurantportal.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.cors.CorsConfiguration;
import restaurantportal.security.JwtFilter;

import java.util.List;

/**
 * Security configuration for JWT-based authentication.
 */
@Configuration
public class SecurityConfig {

    private static final Logger log = LoggerFactory.getLogger(SecurityConfig.class);

    /** JWT filter for request validation */
    private final JwtFilter jwtFilter;

    /**
     * Constructor injection for JwtFilter.
     */
    public SecurityConfig(JwtFilter jwtFilter) {
        this.jwtFilter = jwtFilter;
    }

    /**
     * Defines security rules and filter chain.
     */
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

        log.info("Initializing SecurityFilterChain...");

        http
                .csrf(csrf -> csrf.disable())
                .cors(cors -> {
                    log.debug("CORS enabled in SecurityConfig");
                })
                .sessionManagement(session ->
                        session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(auth -> auth

                        .requestMatchers("/api/users/register", "/api/users/login").permitAll()

                        .requestMatchers("/api/restaurants/**")
                        .hasAnyAuthority("ROLE_USER", "ROLE_RESTAURANT_OWNER")

                        .requestMatchers("/api/cart/**")
                        .hasAuthority("ROLE_USER")

                        .requestMatchers("/api/wallet/**")
                        .hasAuthority("ROLE_USER")

                        .requestMatchers("/api/orders/owner/**")
                        .hasAuthority("ROLE_RESTAURANT_OWNER")

                        .anyRequest().authenticated()
                )
                .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class);

        log.info("SecurityFilterChain initialized successfully.");

        return http.build();
    }

    /**
     * Password encoder bean using BCrypt.
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        log.debug("Creating PasswordEncoder bean (BCrypt)");
        return new BCryptPasswordEncoder();
    }

    /**
     * Authentication manager bean.
     */
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        log.debug("Creating AuthenticationManager bean");
        return config.getAuthenticationManager();
    }

    /**
     * Global CORS configuration source.
     */
    @Bean
    public CorsConfigurationSource corsConfigurationSource() {

        log.info("Initializing CORS configuration source...");

        CorsConfiguration configuration = new CorsConfiguration();

        configuration.setAllowedOriginPatterns(List.of("*"));
        configuration.setAllowedMethods(List.of("*"));
        configuration.setAllowedHeaders(List.of("*"));
        configuration.setAllowCredentials(true);

        UrlBasedCorsConfigurationSource source =
                new UrlBasedCorsConfigurationSource();

        source.registerCorsConfiguration("/**", configuration);

        log.info("CORS configuration source initialized.");

        return source;
    }
}