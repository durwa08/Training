package restaurantportal.dto;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
/**
 * Test class for CartItemResponse DTO.
 */
class CartItemResponseTest {

    /**
     * Tests constructor and getter methods of CartItemResponse.
     */
    @Test
    void testConstructorAndGetters() {
        CartItemResponse item = new CartItemResponse(1L, "Pizza", 200.0, 2);

        assertEquals(1L, item.getId());
        assertEquals("Pizza", item.getName());
        assertEquals(200.0, item.getPrice());
        assertEquals(2, item.getQuantity());
    }
}