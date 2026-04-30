package restaurantportal.dto;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class AddToCartRequestTest {

    @Test
    void testGettersAndSetters() {
        AddToCartRequest request = new AddToCartRequest();

        request.setMenuItemId(1L);
        request.setQuantity(2);

        assertEquals(1L, request.getMenuItemId());
        assertEquals(2, request.getQuantity());
    }
}