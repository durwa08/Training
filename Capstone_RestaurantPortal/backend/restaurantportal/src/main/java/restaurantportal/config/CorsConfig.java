package restaurantportal.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
 * CORS (Cross-Origin Resource Sharing) is a security feature that helps in setting up the communication
 * between different ports .
 */
public class CorsConfig {

    private static final Logger logger = LoggerFactory.getLogger(CorsConfig.class);

    /**
     * This method defines a CorsFilter bean that configures CORS settings for the application.

     */
    @Bean
    public CorsFilter corsFilter() {

        logger.info("Initializing CORS configuration");

        CorsConfiguration config = new CorsConfiguration();

        /**
         *Allow ALL origins (any localhost port, any frontend)
         */

        config.addAllowedOriginPattern("*");
        logger.debug("Allowed origin pattern set to *");

        /** Allow all HTTP methods
         *
         */
        config.addAllowedMethod("GET");
        config.addAllowedMethod("POST");
        config.addAllowedMethod("PUT");
        config.addAllowedMethod("DELETE");
        config.addAllowedMethod("PATCH");
        config.addAllowedMethod("OPTIONS");
        logger.debug("Allowed HTTP methods configured");

        /** Allow all headers including Authorization
         *
         */
        config.addAllowedHeader("*");
        logger.debug("Allowed headers set to *");

        /** Allow Authorization header to be sent
        * This is needed for JWT token to pass through

         */
        config.setAllowCredentials(false);
        logger.debug("Allow credentials set to false");

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();


        source.registerCorsConfiguration("/**", config);
        logger.info("CORS configuration applied to all routes");

        return new CorsFilter(source);
    }
}