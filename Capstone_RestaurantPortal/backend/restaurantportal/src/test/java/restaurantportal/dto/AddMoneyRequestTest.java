package restaurantportal.dto;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Test class for AddMoneyRequest DTO.
 */
class AddMoneyRequestTest {

    /**
     * Tests getter and setter of AddMoneyRequest.
     */
    @Test
    void testGetterSetter() {
        AddMoneyRequest request = new AddMoneyRequest();

        request.setAmount(100.0);

        assertEquals(100.0, request.getAmount());
    }
}