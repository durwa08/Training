package restaurantportal.dto;

import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Test class for CheckoutResponse DTO.
 */
class CheckoutResponseTest {

    /**
     * Tests constructor of CheckoutResponse.
     */
    @Test
    void testConstructor() {
        CartItemResponse item = new CartItemResponse(1L, "Pizza", 200.0, 2);

        CheckoutResponse response = new CheckoutResponse(
                400.0,
                500.0,
                true,
                List.of(item)
        );

        assertEquals(400.0, response.getTotalAmount());
        assertEquals(500.0, response.getWalletBalance());
        assertTrue(response.isCanPlaceOrder());
        assertEquals(1, response.getItems().size());
    }
}