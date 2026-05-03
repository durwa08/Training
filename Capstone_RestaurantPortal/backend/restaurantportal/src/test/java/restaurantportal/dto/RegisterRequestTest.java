package restaurantportal.dto;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Test class for RegisterRequest DTO.
 */
class RegisterRequestTest {

    /**
     * Tests getter and setter methods of RegisterRequest.
     */
    @Test
    void testGetterSetter() {
        RegisterRequest request = new RegisterRequest();

        request.setFirstName("John");
        request.setLastName("Doe");
        request.setEmail("john@gmail.com");
        request.setPassword("12345");
        request.setPhoneNumber("9999999999");
        request.setRole("USER");

        assertEquals("John", request.getFirstName());
        assertEquals("Doe", request.getLastName());
        assertEquals("john@gmail.com", request.getEmail());
        assertEquals("12345", request.getPassword());
        assertEquals("9999999999", request.getPhoneNumber());
        assertEquals("USER", request.getRole());
    }

    /**
     * Tests all-arguments constructor of RegisterRequest.
     */
    @Test
    void testAllArgsConstructor() {
        RegisterRequest request = new RegisterRequest(
                "John", "Doe", "john@gmail.com",
                "12345", "9999999999", "USER"
        );

        assertEquals("John", request.getFirstName());
    }
}