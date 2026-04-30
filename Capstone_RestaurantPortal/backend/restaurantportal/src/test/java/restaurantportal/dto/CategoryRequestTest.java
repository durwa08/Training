package restaurantportal.dto;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class CategoryRequestTest {

    @Test
    void testGetterSetter() {
        CategoryRequest request = new CategoryRequest();

        request.setName("Food");

        assertEquals("Food", request.getName());
    }
}