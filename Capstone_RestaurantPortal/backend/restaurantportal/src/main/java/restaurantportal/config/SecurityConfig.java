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
//This class configures Spring Security for the application. It defines the security filter chain, password encoder, and authentication manager. The security filter chain specifies which endpoints are public and which require authentication, as well as adding the JWT filter to validate tokens on incoming requests.
// The password encoder uses BCrypt for hashing passwords securely.
public class SecurityConfig {

    private final JwtFilter jwtFilter;

    public SecurityConfig(JwtFilter jwtFilter) {
        this.jwtFilter = jwtFilter;
    }

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

    // password encoder (bcrypt)
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    // authentication manager
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }
}