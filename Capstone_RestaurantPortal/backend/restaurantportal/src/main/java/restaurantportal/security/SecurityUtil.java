package restaurantportal.security;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;

/**
 * Utility class for fetching details of the currently authenticated user.
 */
public class SecurityUtil {

    private static final Logger logger = LoggerFactory.getLogger(SecurityUtil.class);

    /**
     * Retrieves the email of the currently logged-in user.
     */
    public static String getCurrentUserEmail() {

        logger.debug("Fetching current user email");

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication != null && authentication.getPrincipal() instanceof org.springframework.security.core.userdetails.UserDetails userDetails) {
            String email = userDetails.getUsername();
            logger.debug("Current user email: {}", email);
            return email;
        }

        logger.warn("No authenticated user found while fetching email");
        return null;
    }

    /**
     * Retrieves the role of the currently logged-in user.
     */
    public static String getCurrentUserRole() {

        logger.debug("Fetching current user role");

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication != null && !authentication.getAuthorities().isEmpty()) {
            String role = authentication.getAuthorities().iterator().next().getAuthority();
            logger.debug("Current user role: {}", role);
            return role;
        }

        logger.warn("No authenticated user found while fetching role");
        return null;
    }

    /**
     * Retrieves the user ID of the currently logged-in user.
     * This method returns null unless CustomUserDetails is implemented.
     */
    public static Long getCurrentUserId() {

        logger.debug("Fetching current user ID");

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication != null && authentication.getPrincipal() instanceof User) {

            return null;
        }

        logger.warn("No authenticated user found while fetching user ID");
        return null;
    }
}