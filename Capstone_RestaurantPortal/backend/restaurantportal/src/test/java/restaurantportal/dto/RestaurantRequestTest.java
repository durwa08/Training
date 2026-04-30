package restaurantportal.dto;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class RestaurantRequestTest {

    @Test
    void testGetterSetter() {
        RestaurantRequest request = new RestaurantRequest();

        request.setName("ABC Restaurant");
        request.setAddress("Bhopal");
        request.setStatus("OPEN");

        assertEquals("ABC Restaurant", request.getName());
        assertEquals("Bhopal", request.getAddress());
        assertEquals("OPEN", request.getStatus());
    }
}