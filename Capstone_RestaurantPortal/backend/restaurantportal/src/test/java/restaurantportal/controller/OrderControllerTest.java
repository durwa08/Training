package restaurantportal.controller;

import org.junit.jupiter.api.*;
import org.mockito.*;
import restaurantportal.dto.OrderResponse;
import restaurantportal.dto.PlaceOrderRequest;
import restaurantportal.service.OrderService;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * Unit tests for OrderController.
 */
class OrderControllerTest {

    @InjectMocks
    private OrderController controller;

    @Mock
    private OrderService service;

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
     * Creates a sample OrderResponse.
     */
    private OrderResponse response() {
        return new OrderResponse(
                1L,
                250.0,
                "PLACED",
                List.of(),
                null,
                null,
                null,
                null,
                null
        );
    }

    /**
     * Tests successful order preview.
     */
    @Test
    void preview_success() {
        when(service.previewOrder()).thenReturn(response());

        OrderResponse res = controller.preview();

        assertNotNull(res);
        verify(service).previewOrder();
    }

    /**
     * Tests order preview exception.
     */
    @Test
    void preview_exception() {
        when(service.previewOrder()).thenThrow(new RuntimeException());

        assertThrows(RuntimeException.class, () -> controller.preview());
    }

    /**
     * Tests successful order placement.
     */
    @Test
    void placeOrder_success() {
        PlaceOrderRequest req = new PlaceOrderRequest();

        when(service.placeOrder(req)).thenReturn(response());

        OrderResponse res = controller.placeOrder(req);

        assertNotNull(res);
        verify(service).placeOrder(req);
    }

    /**
     * Tests order placement exception.
     */
    @Test
    void placeOrder_exception() {
        PlaceOrderRequest req = new PlaceOrderRequest();

        when(service.placeOrder(req)).thenThrow(new RuntimeException());

        assertThrows(RuntimeException.class, () -> controller.placeOrder(req));
    }

    /**
     * Tests fetching user orders.
     */
    @Test
    void getMyOrders_success() {
        when(service.getMyOrders()).thenReturn(List.of(response()));

        List<OrderResponse> list = controller.getMyOrders();

        assertEquals(1, list.size());
        verify(service).getMyOrders();
    }

    /**
     * Tests empty user orders.
     */
    @Test
    void getMyOrders_empty() {
        when(service.getMyOrders()).thenReturn(List.of());

        List<OrderResponse> list = controller.getMyOrders();

        assertTrue(list.isEmpty());
    }

    /**
     * Tests fetching owner orders.
     */
    @Test
    void getOwnerOrders_success() {
        when(service.getOwnerOrders()).thenReturn(List.of(response()));

        List<OrderResponse> list = controller.getOwnerOrders();

        assertEquals(1, list.size());
        verify(service).getOwnerOrders();
    }

    /**
     * Tests successful status update.
     */
    @Test
    void updateStatus_success() {
        when(service.updateStatus(1L, "DELIVERED")).thenReturn(response());

        OrderResponse res = controller.updateStatus(1L, "DELIVERED");

        assertNotNull(res);
        verify(service).updateStatus(1L, "DELIVERED");
    }

    /**
     * Tests status update exception.
     */
    @Test
    void updateStatus_exception() {
        when(service.updateStatus(1L, "DELIVERED"))
                .thenThrow(new RuntimeException());

        assertThrows(RuntimeException.class,
                () -> controller.updateStatus(1L, "DELIVERED"));
    }

    /**
     * Tests successful order cancellation.
     */
    @Test
    void cancelOrder_success() {
        when(service.cancelOrder(1L)).thenReturn("Cancelled");

        String res = controller.cancelOrder(1L);

        assertEquals("Cancelled", res);
        verify(service).cancelOrder(1L);
    }

    /**
     * Tests order cancellation exception.
     */
    @Test
    void cancelOrder_exception() {
        when(service.cancelOrder(1L)).thenThrow(new RuntimeException());

        assertThrows(RuntimeException.class,
                () -> controller.cancelOrder(1L));
    }
}