package restaurantportal.entity;

import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

class OrderTest {

    @Test
    void testSettersAndGetters() {
        Order order = new Order();

        order.setDeliveryAddress("Bhopal");
        order.setPhoneNumber("9999999999");
        order.setTotalAmount(500.0);
        order.setCreatedAt(LocalDateTime.now());

        assertEquals("Bhopal", order.getDeliveryAddress());
        assertEquals(500.0, order.getTotalAmount());
        assertNotNull(order.getCreatedAt());
    }
}