package restaurantportal.dto;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Test class for CategoryRequest DTO.
 */
class CategoryRequestTest {

    /**
     * Tests getter and setter methods of CategoryRequest.
     */
    @Test
    void testGetterSetter() {
        CategoryRequest request = new CategoryRequest();

        request.setName("Food");

        assertEquals("Food", request.getName());
    }
}