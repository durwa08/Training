package restaurantportal.entity;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Test class for Restaurant entity.
 */
class RestaurantTest {

    /**
     * Tests builder of Restaurant.
     */
    @Test
    void testBuilder() {
        User owner = new User();

        Restaurant restaurant = Restaurant.builder()
                .id(1L)
                .name("ABC")
                .address("Bhopal")
                .owner(owner)
                .build();

        assertEquals("ABC", restaurant.getName());
        assertEquals(owner, restaurant.getOwner());
    }
}