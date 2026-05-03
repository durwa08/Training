package restaurantportal.controller;

import org.junit.jupiter.api.*;
import org.mockito.*;
import restaurantportal.dto.AddMoneyRequest;
import restaurantportal.dto.WalletResponse;
import restaurantportal.service.WalletService;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * Unit tests for WalletController.
 */
class WalletControllerTest {

    @InjectMocks
    private WalletController controller;

    @Mock
    private WalletService service;

    private AutoCloseable closeable;

    /**
     * Initializes mocks.
     */
    @BeforeEach
    void setup() {
        closeable = MockitoAnnotations.openMocks(this);
    }

    /**
     * Releases resources.
     */
    @AfterEach
    void tearDown() throws Exception {
        closeable.close();
    }

    /**
     * Creates a sample WalletResponse.
     */
    private WalletResponse response() {
        return new WalletResponse(1500.0);
    }

    /**
     * Tests successful add money.
     */
    @Test
    void addMoney_success() {
        AddMoneyRequest req = new AddMoneyRequest();
        req.setAmount(500.0);

        when(service.addMoney(req)).thenReturn(response());

        WalletResponse res = controller.addMoney(req);

        assertNotNull(res);
        assertEquals(1500.0, res.getBalance());
        verify(service).addMoney(req);
    }

    /**
     * Tests add money exception.
     */
    @Test
    void addMoney_exception() {
        AddMoneyRequest req = new AddMoneyRequest();

        when(service.addMoney(req)).thenThrow(new RuntimeException());

        assertThrows(RuntimeException.class, () -> controller.addMoney(req));
    }

    /**
     * Tests successful balance fetch.
     */
    @Test
    void getBalance_success() {
        when(service.getBalance()).thenReturn(response());

        WalletResponse res = controller.getBalance();

        assertNotNull(res);
        assertEquals(1500.0, res.getBalance());
        verify(service).getBalance();
    }

    /**
     * Tests balance fetch exception.
     */
    @Test
    void getBalance_exception() {
        when(service.getBalance()).thenThrow(new RuntimeException());

        assertThrows(RuntimeException.class, () -> controller.getBalance());
    }
}