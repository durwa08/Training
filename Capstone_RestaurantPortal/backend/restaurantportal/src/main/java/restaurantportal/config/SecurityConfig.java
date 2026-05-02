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

    private static final Logger logger = LoggerFactory.getLogger(SecurityConfig.class);

    /** JWT filter for request validation */
    private final JwtFilter jwtFilter;

    /**
     * Constructor injection for JwtFilter.
     */
    public SecurityConfig(JwtFilter jwtFilter) {
        this.jwtFilter = jwtFilter;
        logger.info("SecurityConfig initialized with JwtFilter");
    }

    /**
     * Defines security rules and filter chain.
     */
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

        logger.info("Configuring SecurityFilterChain");

        http
                .csrf(csrf -> {
                    logger.debug("Disabling CSRF");
                    csrf.disable();
                })
                .cors(cors -> {
                    logger.debug("Enabling CORS");
                })
                .sessionManagement(session -> {
                    logger.debug("Setting session management to STATELESS");
                    session.sessionCreationPolicy(SessionCreationPolicy.STATELESS);
                })
                .authorizeHttpRequests(auth -> {
                    logger.debug("Configuring authorization rules");
                    auth.requestMatchers("/api/users/register", "/api/users/login", "/pages/**", "/js/**", "/css/**")
                            .permitAll()
                            .requestMatchers("/api/restaurants/**").hasAnyAuthority("ROLE_USER", "ROLE_RESTAURANT_OWNER")

                            .requestMatchers("/api/cart/**")
                            .hasAuthority("ROLE_USER")

                            .requestMatchers("/api/wallet/**")
                            .hasAuthority("ROLE_USER")

                            .requestMatchers("/api/orders/owner/**")
                            .hasAuthority("ROLE_RESTAURANT_OWNER")

                            .anyRequest().authenticated();
                })
                .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class);

        logger.info("SecurityFilterChain configured successfully");

        return http.build();
    }

    /**
     * Provides password encoder.
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        logger.info("Creating BCryptPasswordEncoder bean");
        return new BCryptPasswordEncoder();
    }

    /**
     * Provides authentication manager.
     */
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        logger.info("Creating AuthenticationManager bean");
        return config.getAuthenticationManager();
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {

        logger.info("Configuring CorsConfigurationSource");

        CorsConfiguration configuration = new CorsConfiguration();

        configuration.setAllowedOriginPatterns(List.of("*"));
        logger.debug("Allowed origin patterns set");

        configuration.setAllowedMethods(List.of("*"));
        logger.debug("Allowed methods set");

        configuration.setAllowedHeaders(List.of("*"));
        logger.debug("Allowed headers set");

        configuration.setAllowCredentials(true);
        logger.debug("Allow credentials set to true");

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();

        source.registerCorsConfiguration("/**", configuration);
        logger.info("CORS configuration applied to all routes in SecurityConfig");

        return source;
    }
}