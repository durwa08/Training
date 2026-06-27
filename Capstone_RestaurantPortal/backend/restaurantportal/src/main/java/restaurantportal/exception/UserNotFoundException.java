package restaurantportal.exception;

/**
 * Exception thrown when a user is not found in the system.
 */
public class UserNotFoundException extends RuntimeException {

    /**
     * Creates a new UserNotFoundException with a custom message.
     */
    public UserNotFoundException(String message) {
        super(message);
    }
}