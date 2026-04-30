package restaurantportal.dto;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class CartItemResponseTest {

    @Test
    void testConstructorAndGetters() {
        CartItemResponse item = new CartItemResponse(1L, "Pizza", 200.0, 2);

        assertEquals(1L, item.getId());
        assertEquals("Pizza", item.getName());
        assertEquals(200.0, item.getPrice());
        assertEquals(2, item.getQuantity());
    }
}