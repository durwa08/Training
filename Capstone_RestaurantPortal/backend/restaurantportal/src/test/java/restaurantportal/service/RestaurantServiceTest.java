package restaurantportal.service;

import org.junit.jupiter.api.*;
import org.mockito.*;
import restaurantportal.dto.*;
import restaurantportal.entity.*;
import restaurantportal.repository.*;
import restaurantportal.security.SecurityUtil;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class RestaurantServiceTest {

    @Mock private RestaurantRepository restaurantRepository;
    @Mock private UserRepository userRepository;

    @InjectMocks private RestaurantService restaurantService;

    private AutoCloseable closeable;

    @BeforeEach
    void setup() {
        closeable = MockitoAnnotations.openMocks(this);
    }

    @AfterEach
    void tearDown() throws Exception {
        closeable.close();
    }

    private User user(String email) {
        User u = new User();
        u.setId(1L);
        u.setEmail(email);
        return u;
    }

    private Restaurant restaurant(User owner) {
        Restaurant r = new Restaurant();
        r.setId(1L);
        r.setName("Test");
        r.setAddress("Addr");
        r.setStatus(RestaurantStatus.OPEN);
        r.setOwner(owner);
        return r;
    }

    private RestaurantRequest request(String status) {
        RestaurantRequest req = new RestaurantRequest();
        req.setName("Test");
        req.setAddress("Addr");
        req.setStatus(status);
        return req;
    }

    @Test
    void create_success() {
        try (MockedStatic<SecurityUtil> s = mockStatic(SecurityUtil.class)) {

            s.when(SecurityUtil::getCurrentUserEmail).thenReturn("x");

            User u = user("x");

            when(userRepository.findByEmail("x")).thenReturn(Optional.of(u));
            when(restaurantRepository.save(any())).thenAnswer(i -> i.getArgument(0));

            RestaurantResponse res = restaurantService.create(request("OPEN"));

            assertEquals("Test", res.getName());
            assertEquals("OPEN", res.getStatus());
        }
    }

    @Test
    void create_userNotFound() {
        try (MockedStatic<SecurityUtil> s = mockStatic(SecurityUtil.class)) {

            s.when(SecurityUtil::getCurrentUserEmail).thenReturn("x");

            when(userRepository.findByEmail("x")).thenReturn(Optional.empty());

            assertThrows(RuntimeException.class,
                    () -> restaurantService.create(request("OPEN")));
        }
    }

    @Test
    void create_nullStatus() {
        try (MockedStatic<SecurityUtil> s = mockStatic(SecurityUtil.class)) {

            s.when(SecurityUtil::getCurrentUserEmail).thenReturn("x");

            User u = user("x");

            when(userRepository.findByEmail("x")).thenReturn(Optional.of(u));

            assertThrows(IllegalArgumentException.class,
                    () -> restaurantService.create(request(null)));
        }
    }

    @Test
    void getAll_success() {
        Restaurant r1 = restaurant(user("a"));
        Restaurant r2 = restaurant(user("b"));

        when(restaurantRepository.findAll()).thenReturn(List.of(r1, r2));

        List<RestaurantResponse> res = restaurantService.getAll();

        assertEquals(2, res.size());
    }

    @Test
    void getAll_empty() {
        when(restaurantRepository.findAll()).thenReturn(new ArrayList<>());

        List<RestaurantResponse> res = restaurantService.getAll();

        assertEquals(0, res.size());
    }

    @Test
    void getById_success() {
        Restaurant r = restaurant(user("x"));

        when(restaurantRepository.findById(1L)).thenReturn(Optional.of(r));

        RestaurantResponse res = restaurantService.getById(1L);

        assertEquals("Test", res.getName());
    }

    @Test
    void getById_notFound() {
        when(restaurantRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class,
                () -> restaurantService.getById(1L));
    }

    @Test
    void update_success() {
        try (MockedStatic<SecurityUtil> s = mockStatic(SecurityUtil.class)) {

            s.when(SecurityUtil::getCurrentUserEmail).thenReturn("x");

            User owner = user("x");
            Restaurant r = restaurant(owner);

            when(restaurantRepository.findById(1L)).thenReturn(Optional.of(r));
            when(restaurantRepository.save(any())).thenAnswer(i -> i.getArgument(0));

            RestaurantResponse res = restaurantService.update(1L, request("CLOSED"));

            assertEquals("CLOSED", res.getStatus());
        }
    }

    @Test
    void update_notFound() {
        try (MockedStatic<SecurityUtil> s = mockStatic(SecurityUtil.class)) {

            s.when(SecurityUtil::getCurrentUserEmail).thenReturn("x");

            when(restaurantRepository.findById(1L)).thenReturn(Optional.empty());

            assertThrows(RuntimeException.class,
                    () -> restaurantService.update(1L, request("OPEN")));
        }
    }

    @Test
    void update_unauthorized() {
        try (MockedStatic<SecurityUtil> s = mockStatic(SecurityUtil.class)) {

            s.when(SecurityUtil::getCurrentUserEmail).thenReturn("x");

            User owner = user("y");
            Restaurant r = restaurant(owner);

            when(restaurantRepository.findById(1L)).thenReturn(Optional.of(r));

            assertThrows(RuntimeException.class,
                    () -> restaurantService.update(1L, request("OPEN")));
        }
    }

    @Test
    void update_nullOwner() {
        try (MockedStatic<SecurityUtil> s = mockStatic(SecurityUtil.class)) {

            s.when(SecurityUtil::getCurrentUserEmail).thenReturn("x");

            Restaurant r = restaurant(null);

            when(restaurantRepository.findById(1L)).thenReturn(Optional.of(r));

            assertThrows(RuntimeException.class,
                    () -> restaurantService.update(1L, request("OPEN")));
        }
    }

    @Test
    void update_nullStatus() {
        try (MockedStatic<SecurityUtil> s = mockStatic(SecurityUtil.class)) {

            s.when(SecurityUtil::getCurrentUserEmail).thenReturn("x");

            User owner = user("x");
            Restaurant r = restaurant(owner);

            when(restaurantRepository.findById(1L)).thenReturn(Optional.of(r));

            assertThrows(IllegalArgumentException.class,
                    () -> restaurantService.update(1L, request(null)));
        }
    }

    @Test
    void delete_success() {
        try (MockedStatic<SecurityUtil> s = mockStatic(SecurityUtil.class)) {

            s.when(SecurityUtil::getCurrentUserEmail).thenReturn("x");

            User owner = user("x");
            Restaurant r = restaurant(owner);

            when(restaurantRepository.findById(1L)).thenReturn(Optional.of(r));

            restaurantService.delete(1L);

            verify(restaurantRepository).deleteById(1L);
        }
    }

    @Test
    void delete_notFound() {
        try (MockedStatic<SecurityUtil> s = mockStatic(SecurityUtil.class)) {

            s.when(SecurityUtil::getCurrentUserEmail).thenReturn("x");

            when(restaurantRepository.findById(1L)).thenReturn(Optional.empty());

            assertThrows(RuntimeException.class,
                    () -> restaurantService.delete(1L));
        }
    }

    @Test
    void delete_unauthorized() {
        try (MockedStatic<SecurityUtil> s = mockStatic(SecurityUtil.class)) {

            s.when(SecurityUtil::getCurrentUserEmail).thenReturn("x");

            User owner = user("y");
            Restaurant r = restaurant(owner);

            when(restaurantRepository.findById(1L)).thenReturn(Optional.of(r));

            assertThrows(RuntimeException.class,
                    () -> restaurantService.delete(1L));
        }
    }

    @Test
    void delete_nullOwner() {
        try (MockedStatic<SecurityUtil> s = mockStatic(SecurityUtil.class)) {

            s.when(SecurityUtil::getCurrentUserEmail).thenReturn("x");

            Restaurant r = restaurant(null);

            when(restaurantRepository.findById(1L)).thenReturn(Optional.of(r));

            assertThrows(RuntimeException.class,
                    () -> restaurantService.delete(1L));
        }
    }
}