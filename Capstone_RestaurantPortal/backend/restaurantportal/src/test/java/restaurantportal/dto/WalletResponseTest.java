package restaurantportal.dto;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class WalletResponseTest {

    @Test
    void testConstructor() {
        WalletResponse response = new WalletResponse(1000.0);

        assertEquals(1000.0, response.getBalance());
    }
}