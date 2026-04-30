package restaurantportal.dto;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class LoginRequestTest {

    @Test
    void testGetterSetter() {
        LoginRequest request = new LoginRequest();

        request.setEmail("test@gmail.com");
        request.setPassword("12345");

        assertEquals("test@gmail.com", request.getEmail());
        assertEquals("12345", request.getPassword());
    }

    @Test
    void testAllArgsConstructor() {
        LoginRequest request = new LoginRequest("a@gmail.com", "pass");

        assertEquals("a@gmail.com", request.getEmail());
        assertEquals("pass", request.getPassword());
    }
}