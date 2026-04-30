package restaurantportal.controller;

import org.junit.jupiter.api.*;
import org.mockito.*;
import restaurantportal.dto.RestaurantRequest;
import restaurantportal.dto.RestaurantResponse;
import restaurantportal.service.RestaurantService;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class RestaurantControllerTest {

    @InjectMocks
    private RestaurantController controller;

    @Mock
    private RestaurantService service;

    private AutoCloseable closeable;

    @BeforeEach
    void setup() {
        closeable = MockitoAnnotations.openMocks(this);
    }

    @AfterEach
    void tearDown() throws Exception {
        closeable.close();
    }

    private RestaurantResponse response() {
        return new RestaurantResponse(
                1L,
                "Dominos",
                "Pizza",
                "Bhopal"

        );
    }

    @Test
    void create_success() {
        RestaurantRequest req = new RestaurantRequest();
        req.setName("Dominos");

        when(service.create(req)).thenReturn(response());

        RestaurantResponse res = controller.create(req);

        assertNotNull(res);
        assertEquals("Dominos", res.getName());
        verify(service).create(req);
    }

    @Test
    void create_exception() {
        RestaurantRequest req = new RestaurantRequest();

        when(service.create(req)).thenThrow(new RuntimeException());

        assertThrows(RuntimeException.class, () -> controller.create(req));
    }

    @Test
    void get_success() {
        when(service.getAll()).thenReturn(List.of(response()));

        List<RestaurantResponse> list = controller.get();

        assertEquals(1, list.size());
        verify(service).getAll();
    }

    @Test
    void get_empty() {
        when(service.getAll()).thenReturn(List.of());

        List<RestaurantResponse> list = controller.get();

        assertTrue(list.isEmpty());
    }

    @Test
    void getById_success() {
        when(service.getById(1L)).thenReturn(response());

        RestaurantResponse res = controller.getById(1L);

        assertNotNull(res);
        verify(service).getById(1L);
    }

    @Test
    void getById_exception() {
        when(service.getById(1L)).thenThrow(new RuntimeException());

        assertThrows(RuntimeException.class, () -> controller.getById(1L));
    }

    @Test
    void update_success() {
        RestaurantRequest req = new RestaurantRequest();

        when(service.update(1L, req)).thenReturn(response());

        RestaurantResponse res = controller.update(1L, req);

        assertNotNull(res);
        verify(service).update(1L, req);
    }

    @Test
    void update_exception() {
        RestaurantRequest req = new RestaurantRequest();

        when(service.update(1L, req)).thenThrow(new RuntimeException());

        assertThrows(RuntimeException.class, () -> controller.update(1L, req));
    }

    @Test
    void delete_success() {
        doNothing().when(service).delete(1L);

        assertDoesNotThrow(() -> controller.delete(1L));

        verify(service).delete(1L);
    }

    @Test
    void delete_exception() {
        doThrow(new RuntimeException()).when(service).delete(1L);

        assertThrows(RuntimeException.class, () -> controller.delete(1L));
    }
}