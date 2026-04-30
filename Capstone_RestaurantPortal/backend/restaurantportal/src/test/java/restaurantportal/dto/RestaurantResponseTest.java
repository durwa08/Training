package restaurantportal.dto;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class RestaurantResponseTest {

    @Test
    void testConstructor() {
        RestaurantResponse response = new RestaurantResponse(1L, "ABC", "Bhopal", "OPEN");

        assertEquals(1L, response.getId());
        assertEquals("ABC", response.getName());
        assertEquals("Bhopal", response.getAddress());
        assertEquals("OPEN", response.getStatus());
    }
}