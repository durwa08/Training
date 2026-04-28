package restaurantportal.security;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;

/**
 * Utility class for fetching details of the currently authenticated user.
 */
public class SecurityUtil {

    /**
     * Retrieves the email of the currently logged-in user.
     */
    public static String getCurrentUserEmail() {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication != null && authentication.getPrincipal() instanceof org.springframework.security.core.userdetails.UserDetails userDetails) {
            return userDetails.getUsername();
        }

        return null;
    }

    /**
     * Retrieves the role of the currently logged-in user.
     */
    public static String getCurrentUserRole() {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication != null && !authentication.getAuthorities().isEmpty()) {
            return authentication.getAuthorities().iterator().next().getAuthority();
        }

        return null;
    }

    /**
     * Retrieves the user ID of the currently logged-in user.
     *
     * NOTE: Spring Security default User does NOT provide ID.
     * This method returns null unless CustomUserDetails is implemented.
     */
    public static Long getCurrentUserId() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication != null && authentication.getPrincipal() instanceof User) {

            /** Default Spring Security User has NO ID field
            // You must use CustomUserDetails for real ID access
                CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
                return userDetails.getId();
                */

            return null;
        }

        return null;
    }
}