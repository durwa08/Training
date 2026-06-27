package restaurantportal.entity;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Test class for OrderItem entity.
 */
class OrderItemTest {

    /**
     * Tests getter and setter methods of OrderItem.
     */
    @Test
    void testGetterSetter() {
        OrderItem item = new OrderItem();

        item.setName("Burger");
        item.setPrice(150.0);
        item.setQuantity(2);

        assertEquals("Burger", item.getName());
        assertEquals(150.0, item.getPrice());
        assertEquals(2, item.getQuantity());
    }
}