package restaurantportal.dto;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class AddMoneyRequestTest {

    @Test
    void testGetterSetter() {
        AddMoneyRequest request = new AddMoneyRequest();

        request.setAmount(100.0);

        assertEquals(100.0, request.getAmount());
    }
}