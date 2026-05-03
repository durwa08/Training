package restaurantportal.entity;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Test class for Cart entity.
 */
class CartTest {

    /**
     * Tests adding item to cart.
     */
    @Test
    void testAddItem() {
        Cart cart = new Cart();
        CartItem item = new CartItem();

        cart.addItem(item);

        assertEquals(1, cart.getItems().size());
        assertEquals(cart, item.getCart());
    }

    /**
     * Tests removing item from cart.
     */
    @Test
    void testRemoveItem() {
        Cart cart = new Cart();
        CartItem item = new CartItem();

        cart.addItem(item);
        cart.removeItem(item);

        assertTrue(cart.getItems().isEmpty());
        assertNull(item.getCart());
    }

    /**
     * Tests constructor of Cart.
     */
    @Test
    void testConstructor() {
        User user = new User();
        Cart cart = new Cart(user);

        assertEquals(user, cart.getUser());
        assertNotNull(cart.getItems());
    }
}