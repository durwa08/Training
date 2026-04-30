package restaurantportal.entity;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class CategoryTest {

    @Test
    void testBuilder() {
        Restaurant restaurant = new Restaurant();

        Category category = Category.builder()
                .id(1L)
                .name("Starters")
                .restaurant(restaurant)
                .build();

        assertEquals("Starters", category.getName());
        assertEquals(restaurant, category.getRestaurant());
    }
}