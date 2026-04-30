package restaurantportal.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

/**
 * CORS Configuration for the application.
 */
@Configuration
public class CorsConfig {

    private static final Logger log = LoggerFactory.getLogger(CorsConfig.class);

    /**
     * Creates and configures global CORS filter.
     */
    @Bean
    public CorsFilter corsFilter() {

        log.info("Initializing CORS configuration...");

        CorsConfiguration config = new CorsConfiguration();

        /**
         * Allow all origins (useful for local development).
         * In production, restrict to specific domains.
         */
        config.addAllowedOriginPattern("*");

        /**
         * Allowed HTTP methods for cross-origin requests.
         */
        config.addAllowedMethod("GET");
        config.addAllowedMethod("POST");
        config.addAllowedMethod("PUT");
        config.addAllowedMethod("DELETE");
        config.addAllowedMethod("PATCH");
        config.addAllowedMethod("OPTIONS");

        /**
         * Allow all headers including Authorization (JWT support).
         */
        config.addAllowedHeader("*");

        /**
         * Credential sharing disabled (JWT sent via headers).
         */
        config.setAllowCredentials(false);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();

        /**
         * Apply CORS configuration to all endpoints.
         */
        source.registerCorsConfiguration("/**", config);

        log.info("CORS configuration successfully initialized.");

        return new CorsFilter(source);
    }
}