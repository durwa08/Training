package restaurantportal.dto;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class OrderItemResponseTest {

    @Test
    void testConstructor() {
        OrderItemResponse item = new OrderItemResponse(1L, "Pizza", 200.0, 2);

        assertEquals(1L, item.getId());
        assertEquals("Pizza", item.getMenuItemName());
        assertEquals(200.0, item.getPrice());
        assertEquals(2, item.getQuantity());
    }
}