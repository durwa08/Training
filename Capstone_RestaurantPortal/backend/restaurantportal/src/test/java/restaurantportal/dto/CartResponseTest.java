package restaurantportal.dto;

import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Test class for CartResponse DTO.
 */
class CartResponseTest {

    /**
     * Tests constructor and getter methods of CartResponse.
     */
    @Test
    void testConstructorAndGetters() {
        CartItemResponse item = new CartItemResponse(1L, "Burger", 150.0, 1);
        CartResponse response = new CartResponse(10L, 150.0, List.of(item));

        assertEquals(10L, response.getCartId());
        assertEquals(150.0, response.getTotalAmount());
        assertEquals(1, response.getItems().size());
    }
}