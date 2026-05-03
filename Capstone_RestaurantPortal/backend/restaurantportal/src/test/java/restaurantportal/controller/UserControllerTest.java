package restaurantportal.controller;

import org.junit.jupiter.api.*;
import org.mockito.*;
import org.springframework.http.ResponseEntity;
import restaurantportal.dto.*;
import restaurantportal.entity.User;
import restaurantportal.repository.UserRepository;
import restaurantportal.security.SecurityUtil;
import restaurantportal.service.UserService;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * Unit tests for UserController.
 */
class UserControllerTest {

    @InjectMocks
    private UserController controller;

    @Mock
    private UserService userService;

    @Mock
    private UserRepository userRepository;

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
     * Creates a sample UserResponse.
     */
    private UserResponse userResponse() {
        return new UserResponse(
                1L,
                "John",
                "Doe",
                "john@gmail.com",
                "9999999999",
                "USER",
                1000.0
        );
    }

    /**
     * Creates a sample User entity.
     */
    private User user() {
        User u = new User();
        u.setId(1L);
        u.setEmail("john@gmail.com");
        return u;
    }

    /**
     * Tests successful user registration.
     */
    @Test
    void register_success() {
        RegisterRequest req = new RegisterRequest();
        req.setEmail("john@gmail.com");

        when(userService.register(req)).thenReturn(userResponse());

        ResponseEntity<UserResponse> res = controller.register(req);

        assertNotNull(res);
        assertEquals(200, res.getStatusCodeValue());
        verify(userService).register(req);
    }

    /**
     * Tests registration exception.
     */
    @Test
    void register_exception() {
        RegisterRequest req = new RegisterRequest();

        when(userService.register(req)).thenThrow(new RuntimeException());

        assertThrows(RuntimeException.class, () -> controller.register(req));
    }

    /**
     * Tests successful login.
     */
    @Test
    void login_success() {
        LoginRequest req = new LoginRequest();
        req.setEmail("john@gmail.com");
        req.setPassword("pass");

        when(userService.login("john@gmail.com", "pass"))
                .thenReturn("token123");

        ResponseEntity<LoginResponse> res = controller.login(req);

        assertNotNull(res);
        assertEquals(200, res.getStatusCodeValue());
        assertEquals("token123", res.getBody().getToken());

        verify(userService).login("john@gmail.com", "pass");
    }

    /**
     * Tests login exception.
     */
    @Test
    void login_exception() {
        LoginRequest req = new LoginRequest();

        when(userService.login(null, null))
                .thenThrow(new RuntimeException());

        assertThrows(RuntimeException.class, () -> controller.login(req));
    }

    /**
     * Tests fetching current user.
     */
    @Test
    void currentUser_success() {
        try (MockedStatic<SecurityUtil> mocked = mockStatic(SecurityUtil.class)) {

            mocked.when(SecurityUtil::getCurrentUserEmail)
                    .thenReturn("john@gmail.com");

            when(userRepository.findByEmail("john@gmail.com"))
                    .thenReturn(Optional.of(user()));

            User res = controller.currentUser();

            assertNotNull(res);
            assertEquals("john@gmail.com", res.getEmail());
        }
    }

    /**
     * Tests current user not found scenario.
     */
    @Test
    void currentUser_notFound() {
        try (MockedStatic<SecurityUtil> mocked = mockStatic(SecurityUtil.class)) {

            mocked.when(SecurityUtil::getCurrentUserEmail)
                    .thenReturn("john@gmail.com");

            when(userRepository.findByEmail("john@gmail.com"))
                    .thenReturn(Optional.empty());

            assertThrows(RuntimeException.class, () -> controller.currentUser());
        }
    }
}