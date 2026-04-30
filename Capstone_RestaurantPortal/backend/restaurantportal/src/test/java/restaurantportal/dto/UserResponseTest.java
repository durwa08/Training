package restaurantportal.dto;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class UserResponseTest {

    @Test
    void testGetterSetter() {
        UserResponse user = new UserResponse();

        user.setId(1L);
        user.setFirstName("John");
        user.setLastName("Doe");
        user.setEmail("john@gmail.com");
        user.setPhoneNumber("9999999999");
        user.setRole("USER");
        user.setWalletBalance(500.0);

        assertEquals(1L, user.getId());
        assertEquals("John", user.getFirstName());
        assertEquals(500.0, user.getWalletBalance());
    }
}