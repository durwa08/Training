package restaurantportal.dto;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Test class for MenuItemRequest DTO.
 */
class MenuItemRequestTest {

    /**
     * Tests getter and setter methods of MenuItemRequest.
     */
    @Test
    void testGetterSetter() {
        MenuItemRequest request = new MenuItemRequest();

        request.setName("Pizza");
        request.setPrice(250.0);

        assertEquals("Pizza", request.getName());
        assertEquals(250.0, request.getPrice());
    }
}