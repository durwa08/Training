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
/**
 * Configures Spring Security for the application.
 * Defines the security filter chain, password encoder, and authentication manager.
 * Specifies public and authenticated endpoints, and adds the JWT filter for token validation.
 * Uses BCrypt for secure password hashing.
 */
public class SecurityConfig {

    private final JwtFilter jwtFilter;
    /**
     * Constructor to initialize the JWT filter.
     *
     *  JWT filter for token validation
     */

    public SecurityConfig(JwtFilter jwtFilter) {
        this.jwtFilter = jwtFilter;
    }

    /**
     * Configures the security filter chain.
     * Disables CSRF, sets session management to stateless, and defines API authorization rules.
     *
     * @throws Exception if an error occurs during configuration
     */
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

        http
                // disable csrf because we are using JWT (stateless)
                .csrf(csrf -> csrf.disable())

                // no session, every request must have token
                .sessionManagement(session ->
                        session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))

                // API authorization rules
                .authorizeHttpRequests(auth -> auth

                        // public APIs
                        .requestMatchers("/api/users/register", "/api/users/login")
                        .permitAll()

                        // cart APIs → only USER (customer)
                        .requestMatchers("/api/cart/**")
                        .hasRole("USER")

                        // everything else requires login
                        .anyRequest()
                        .authenticated()
                )

                // add JWT filter before default auth filter
                .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    /**
     * Provides a password encoder using BCrypt.
     *
     * @return the BCryptPasswordEncoder instance
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }


    /**
     * Provides the authentication manager.
     *
     * config the authentication configuration
     * @return the AuthenticationManager instance
     * @throws Exception if an error occurs during configuration
     */
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }
}