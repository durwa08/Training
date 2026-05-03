package restaurantportal.dto;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Test class for LoginResponse DTO.
 */
class LoginResponseTest {

    /**
     * Tests constructor of LoginResponse.
     */
    @Test
    void testConstructor() {
        LoginResponse response = new LoginResponse("token123");

        assertEquals("token123", response.getToken());
    }
}