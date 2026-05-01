package restaurantportal.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

/**
 * ============================================
 *   CORS Configuration
 * ============================================
 */
@Configuration
/**
 * CORS (Cross-Origin Resource Sharing) is a security feature that helps in setting up the communication between different ports .
 * example our backend runs on a port 8080 while frontend on 6000,so cors helps in allowing the frontend to access the backend resources without any issues.
 */
public class CorsConfig {

    /**
     * This method defines a CorsFilter bean that configures CORS settings for the application.

     */
    @Bean
    public CorsFilter corsFilter() {

        CorsConfiguration config = new CorsConfiguration();

        /*
         Allow ALL origins (any localhost port, any frontend)
         */
        // In production you would restrict this to your domain
        config.addAllowedOriginPattern("*");

        // Allow all HTTP methods
        config.addAllowedMethod("GET");
        config.addAllowedMethod("POST");
        config.addAllowedMethod("PUT");
        config.addAllowedMethod("DELETE");
        config.addAllowedMethod("PATCH");
        config.addAllowedMethod("OPTIONS");

        // Allow all headers including Authorization (for JWT)
        config.addAllowedHeader("*");

        /* Allow Authorization header to be sent
         This is needed for JWT token to pass through

         */
        config.setAllowCredentials(false);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();

        // Apply to ALL routes
        source.registerCorsConfiguration("/**", config);

        return new CorsFilter(source);
    }
}