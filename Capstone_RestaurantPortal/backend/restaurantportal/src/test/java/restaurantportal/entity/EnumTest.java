package restaurantportal.entity;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class EnumTest {

    @Test
    void testOrderStatusEnum() {
        for (OrderStatus status : OrderStatus.values()) {
            assertNotNull(status);
            assertEquals(status, OrderStatus.valueOf(status.name()));
        }
    }

    @Test
    void testRoleEnum() {
        for (Role role : Role.values()) {
            assertNotNull(role);
            assertEquals(role, Role.valueOf(role.name()));
        }
    }

    @Test
    void testRestaurantStatusEnum() {
        for (RestaurantStatus status : RestaurantStatus.values()) {
            assertNotNull(status);
            assertEquals(status, RestaurantStatus.valueOf(status.name()));
        }
    }
}