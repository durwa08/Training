package restaurantportal.dto;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Test class for WalletResponse DTO.
 */
class WalletResponseTest {

    /**
     * Tests constructor of WalletResponse.
     */
    @Test
    void testConstructor() {
        WalletResponse response = new WalletResponse(1000.0);

        assertEquals(1000.0, response.getBalance());
    }
}