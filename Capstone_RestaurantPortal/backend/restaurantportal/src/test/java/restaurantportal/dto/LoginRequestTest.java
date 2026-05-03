package restaurantportal.dto;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Test class for LoginRequest DTO.
 */
class LoginRequestTest {

    /**
     * Tests getter and setter methods of LoginRequest.
     */
    @Test
    void testGetterSetter() {
        LoginRequest request = new LoginRequest();

        request.setEmail("durwa@mail.com");
        request.setPassword("12345");

        assertEquals("durwa@mail.com", request.getEmail());
        assertEquals("12345", request.getPassword());
    }

    /**
     * Tests all-args constructor of LoginRequest.
     */
    @Test
    void testAllArgsConstructor() {
        LoginRequest request = new LoginRequest("durwa@mail.com", "pass");

        assertEquals("durwa@mail.com", request.getEmail());
        assertEquals("pass", request.getPassword());
    }
}