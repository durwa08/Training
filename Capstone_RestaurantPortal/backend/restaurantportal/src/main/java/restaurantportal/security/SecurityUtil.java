package restaurantportal.security;

import org.springframework.security.core.context.SecurityContextHolder;

//SecurityUtil is a utility class to get the current authenticated user's email from the security context.
// This is useful in service layer to get the current user's email and perform operations based on that.
public class SecurityUtil {
// This method retrieves the email of the currently authenticated user from the security context.
// It uses SecurityContextHolder to access the authentication object and then gets the name (email) of the user.
    public static String getCurrentUserEmail() {
        return SecurityContextHolder.getContext()
                .getAuthentication()
                .getName();
    }
}