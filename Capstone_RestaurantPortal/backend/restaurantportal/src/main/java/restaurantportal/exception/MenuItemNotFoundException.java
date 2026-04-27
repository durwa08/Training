package restaurantportal.exception;

/**
 * Exception thrown when a menu item is not found in the system.
 */
public class MenuItemNotFoundException extends RuntimeException {

    /**
     * Creates a new MenuItemNotFoundException with a custom message.
     */
    public MenuItemNotFoundException(String message) {
        super(message);
    }
}