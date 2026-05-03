package restaurantportal.dto;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Test class for MenuItemResponse DTO.
 */
class MenuItemResponseTest {

    /**
     * Tests constructor of MenuItemResponse.
     */
    @Test
    void testConstructor() {
        MenuItemResponse response = new MenuItemResponse(
                1L,
                "Burger",
                120.0,
                "Delicious burger",
                true,
                5L
        );

        assertEquals(1L, response.getId());
        assertEquals("Burger", response.getName());
        assertEquals(120.0, response.getPrice());
        assertEquals("Delicious burger", response.getDescription());
        assertTrue(response.getAvailable());
        assertEquals(5L, response.getCategoryId());
    }
}