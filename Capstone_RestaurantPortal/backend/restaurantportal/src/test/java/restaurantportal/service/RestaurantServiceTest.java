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

/**
 * Test class for RestaurantService.
 */
class RestaurantServiceTest {

    @Mock private RestaurantRepository restaurantRepository;
    @Mock private UserRepository userRepository;

    @InjectMocks private RestaurantService restaurantService;

    private AutoCloseable closeable;

    /**
     * Initializes mocks before each test.
     */
    @BeforeEach
    void setup() {
        closeable = MockitoAnnotations.openMocks(this);
    }

    /**
     * Closes resources after each test.
     */
    @AfterEach
    void tearDown() throws Exception {
        closeable.close();
    }

    /**
     * Creates a mock user.
     */
    private User user(String email) {
        User u = new User();
        u.setId(1L);
        u.setEmail(email);
        return u;
    }

    /**
     * Creates a mock restaurant.
     */
    private Restaurant restaurant(User owner) {
        Restaurant r = new Restaurant();
        r.setId(1L);
        r.setName("Test");
        r.setAddress("Addr");
        r.setStatus(RestaurantStatus.OPEN);
        r.setOwner(owner);
        return r;
    }

    /**
     * Creates a restaurant request.
     */
    private RestaurantRequest request(String status) {
        RestaurantRequest req = new RestaurantRequest();
        req.setName("Test");
        req.setAddress("Addr");
        req.setStatus(status);
        return req;
    }

    /**
     * Tests successful restaurant creation.
     */
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

    /**
     * Tests creation when user not found.
     */
    @Test
    void create_userNotFound() {
        try (MockedStatic<SecurityUtil> s = mockStatic(SecurityUtil.class)) {

            s.when(SecurityUtil::getCurrentUserEmail).thenReturn("x");

            when(userRepository.findByEmail("x")).thenReturn(Optional.empty());

            assertThrows(RuntimeException.class,
                    () -> restaurantService.create(request("OPEN")));
        }
    }

    /**
     * Tests creation with null status.
     */
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

    /**
     * Tests fetching all restaurants.
     */
    @Test
    void getAll_success() {
        Restaurant r1 = restaurant(user("a"));
        Restaurant r2 = restaurant(user("b"));

        when(restaurantRepository.findAll()).thenReturn(List.of(r1, r2));

        List<RestaurantResponse> res = restaurantService.getAll();

        assertEquals(2, res.size());
    }

    /**
     * Tests fetching when no restaurants exist.
     */
    @Test
    void getAll_empty() {
        when(restaurantRepository.findAll()).thenReturn(new ArrayList<>());

        List<RestaurantResponse> res = restaurantService.getAll();

        assertEquals(0, res.size());
    }

    /**
     * Tests fetching restaurant by ID.
     */
    @Test
    void getById_success() {
        Restaurant r = restaurant(user("x"));

        when(restaurantRepository.findById(1L)).thenReturn(Optional.of(r));

        RestaurantResponse res = restaurantService.getById(1L);

        assertEquals("Test", res.getName());
    }

    /**
     * Tests fetching restaurant when not found.
     */
    @Test
    void getById_notFound() {
        when(restaurantRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class,
                () -> restaurantService.getById(1L));
    }

    /**
     * Tests successful update.
     */
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

    /**
     * Tests update when restaurant not found.
     */
    @Test
    void update_notFound() {
        try (MockedStatic<SecurityUtil> s = mockStatic(SecurityUtil.class)) {

            s.when(SecurityUtil::getCurrentUserEmail).thenReturn("x");

            when(restaurantRepository.findById(1L)).thenReturn(Optional.empty());

            assertThrows(RuntimeException.class,
                    () -> restaurantService.update(1L, request("OPEN")));
        }
    }

    /**
     * Tests update when user is unauthorized.
     */
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

    /**
     * Tests update when owner is null.
     */
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

    /**
     * Tests update with null status.
     */
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

    /**
     * Tests successful delete.
     */
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

    /**
     * Tests delete when restaurant not found.
     */
    @Test
    void delete_notFound() {
        try (MockedStatic<SecurityUtil> s = mockStatic(SecurityUtil.class)) {

            s.when(SecurityUtil::getCurrentUserEmail).thenReturn("x");

            when(restaurantRepository.findById(1L)).thenReturn(Optional.empty());

            assertThrows(RuntimeException.class,
                    () -> restaurantService.delete(1L));
        }
    }

    /**
     * Tests delete when user is unauthorized.
     */
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

    /**
     * Tests delete when owner is null.
     */
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