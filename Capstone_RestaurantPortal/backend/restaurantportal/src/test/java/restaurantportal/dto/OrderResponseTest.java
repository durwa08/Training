package restaurantportal.dto;

import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class OrderResponseTest {

    @Test
    void testConstructorAndSetters() {
        OrderItemResponse item = new OrderItemResponse(1L, "Pizza", 200.0, 2);

        OrderResponse response = new OrderResponse(
                1L,
                200.0,
                "PLACED",
                List.of(item),
                "Order placed successfully",
                "2026-01-01",
                "Bhopal",
                "123 Main Street",
                "9999999999"
        );

        assertEquals(1L, response.getOrderId());
        assertEquals(200.0, response.getTotalAmount());
        assertEquals("PLACED", response.getStatus());
        assertEquals(1, response.getItems().size());

        // setters
        response.setStatus("DELIVERED");
        assertEquals("DELIVERED", response.getStatus());
    }
}