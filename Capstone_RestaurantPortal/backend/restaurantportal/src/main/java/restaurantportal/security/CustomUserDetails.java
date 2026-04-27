package restaurantportal.security;

import org.springframework.security.core.*;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import restaurantportal.entity.User;

import java.util.Collection;
import java.util.Collections;

/**
 * Custom implementation of UserDetails for Spring Security.
 * It adapts the User entity to Spring Security's authentication model.
 */
public class CustomUserDetails implements UserDetails {

    private final User user;

    /**
     * Creates CustomUserDetails from User entity.
     */
    public CustomUserDetails(User user) {
        this.user = user;
    }

    /**
     * Returns user authorities (roles).
     */
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return Collections.singleton(
                new SimpleGrantedAuthority(user.getRole().name())
        );
    }

    /**
     * Returns encrypted password of the user.
     */
    @Override
    public String getPassword() {
        return user.getPassword();
    }

    /**
     * Returns username (email in this case).
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