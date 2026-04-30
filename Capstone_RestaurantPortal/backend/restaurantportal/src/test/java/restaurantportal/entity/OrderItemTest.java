package restaurantportal.entity;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class OrderItemTest {

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