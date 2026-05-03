package restaurantportal.entity;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Test class for enum validations.
 */
class EnumTest {

    /**
     * Tests OrderStatus enum values.
     */
    @Test
    void testOrderStatusEnum() {
        for (OrderStatus status : OrderStatus.values()) {
            assertNotNull(status);
            assertEquals(status, OrderStatus.valueOf(status.name()));
        }
    }

    /**
     * Tests Role enum values.
     */
    @Test
    void testRoleEnum() {
        for (Role role : Role.values()) {
            assertNotNull(role);
            assertEquals(role, Role.valueOf(role.name()));
        }
    }

    /**
     * Tests RestaurantStatus enum values.
     */
    @Test
    void testRestaurantStatusEnum() {
        for (RestaurantStatus status : RestaurantStatus.values()) {
            assertNotNull(status);
            assertEquals(status, RestaurantStatus.valueOf(status.name()));
        }
    }
}