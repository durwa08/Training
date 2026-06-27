package restaurantportal.security;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.*;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import restaurantportal.entity.User;

import java.util.Collection;
import java.util.Collections;

/**
 * Custom implementation of UserDetails for Spring Security.
 * Maps application User entity into Spring Security authentication model.
 * Ensures proper role formatting for authorization checks.
 */
public class CustomUserDetails implements UserDetails {

    private static final Logger logger = LoggerFactory.getLogger(CustomUserDetails.class);

    private final User user;

    /**
     * Constructs CustomUserDetails from User entity.
     */
    public CustomUserDetails(User user) {
        this.user = user;
        logger.debug("CustomUserDetails created for user: {}", user.getEmail());
    }

    /**
     * Returns granted authorities for the user.
     * Prefix ROLE_ is required for Spring Security role-based access.
     *
     * @return collection of authorities
     */
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        logger.debug("Fetching authorities for user: {}", user.getEmail());
        return Collections.singleton(
                new SimpleGrantedAuthority("ROLE_" + user.getRole().name())
        );
    }

    /**
     * Returns encrypted password of user.

     */
    @Override
    public String getPassword() {
        logger.debug("Fetching password for user: {}", user.getEmail());
        return user.getPassword();
    }

    /**
     * Returns username used for authentication (email).
     *
     * @return email
     */
    @Override
    public String getUsername() {
        logger.debug("Fetching username for user");
        return user.getEmail();
    }

    @Override
    public boolean isAccountNonExpired() {
        logger.debug("Checking if account is non-expired for user: {}", user.getEmail());
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        logger.debug("Checking if account is non-locked for user: {}", user.getEmail());
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        logger.debug("Checking if credentials are non-expired for user: {}", user.getEmail());
        return true;
    }

    @Override
    public boolean isEnabled() {
        logger.debug("Checking if account is enabled for user: {}", user.getEmail());
        return true;
    }
}