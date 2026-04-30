package restaurantportal.entity;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class CartItemTest {

    @Test
    void testConstructor() {
        MenuItem menuItem = new MenuItem();

        CartItem item = new CartItem(menuItem, 2, 200.0);

        assertEquals(menuItem, item.getMenuItem());
        assertEquals(2, item.getQuantity());
        assertEquals(200.0, item.getPrice());
    }
}