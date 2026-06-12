package restaurantportal.dto;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Test class for CategoryResponse DTO.
 */
class CategoryResponseTest {

    /**
     * Tests constructor of CategoryResponse.
     */
    @Test
    void testConstructor() {
        CategoryResponse response = new CategoryResponse(1L, "Snacks", 2L);

        assertEquals(1L, response.getId());
        assertEquals("Snacks", response.getName());
        assertEquals(2L, response.getRestaurantId());
    }
}