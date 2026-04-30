package restaurantportal.dto;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class MenuItemResponseTest {

    @Test
    void testConstructor() {
        MenuItemResponse response = new MenuItemResponse(1L, "Burger", 120.0, 5L);

        assertEquals(1L, response.getId());
        assertEquals("Burger", response.getName());
        assertEquals(120.0, response.getPrice());
        assertEquals(5L, response.getCategoryId());
    }
}