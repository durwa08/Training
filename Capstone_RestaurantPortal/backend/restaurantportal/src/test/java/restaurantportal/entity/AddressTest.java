package restaurantportal.entity;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Test class for Address entity.
 */
class AddressTest {

    /**
     * Tests builder and getter methods of Address.
     */
    @Test
    void testBuilderAndGetters() {
        User user = new User();
        user.setId(1L);

        Address address = Address.builder()
                .id(10L)
                .street("Street 1")
                .city("Bhopal")
                .state("MP")
                .pincode("462001")
                .user(user)
                .build();

        assertEquals(10L, address.getId());
        assertEquals("Bhopal", address.getCity());
        assertEquals(1L, address.getUser().getId());
    }
}