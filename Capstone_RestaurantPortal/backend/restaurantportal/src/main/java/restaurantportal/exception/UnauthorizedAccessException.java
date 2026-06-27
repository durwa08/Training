package restaurantportal.exception;

/**
 * Exception thrown when a user tries to access a resource without proper authorization.
 */
public class UnauthorizedAccessException extends RuntimeException {

    /**
     * Creates a new UnauthorizedAccessException with a custom message.
     */
    public UnauthorizedAccessException(String message) {
        super(message);
    }
}