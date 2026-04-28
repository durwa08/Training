package restaurantportal.security;

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

    private final User user;

    /**
     * Constructs CustomUserDetails from User entity.
     *
     * @param user application user entity
     */
    public CustomUserDetails(User user) {
        this.user = user;
    }

    /**
     * Returns granted authorities for the user.
     * Prefix ROLE_ is required for Spring Security role-based access.
     *
     * @return collection of authorities
     */
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return Collections.singleton(
                new SimpleGrantedAuthority("ROLE_" + user.getRole().name())
        );
    }

    /**
     * Returns encrypted password of user.
     *
     * @return password
     */
    @Override
    public String getPassword() {
        return user.getPassword();
    }

    /**
     * Returns username used for authentication (email).
     *
     * @return email
     */
    @Override
    public String getUsername() {
        return user.getEmail();
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}