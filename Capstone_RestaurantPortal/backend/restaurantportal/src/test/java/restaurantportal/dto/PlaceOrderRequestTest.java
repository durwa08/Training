package restaurantportal.dto;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class PlaceOrderRequestTest {

    @Test
    void testGetterSetter() {
        PlaceOrderRequest request = new PlaceOrderRequest();

        request.setDeliveryAddress("Bhopal");
        request.setPhoneNumber("9999999999");

        assertEquals("Bhopal", request.getDeliveryAddress());
        assertEquals("9999999999", request.getPhoneNumber());
    }
}