package restaurantportal.service;

import org.junit.jupiter.api.*;
import org.mockito.*;
import org.springframework.security.crypto.password.PasswordEncoder;
import restaurantportal.dto.RegisterRequest;
import restaurantportal.dto.UserResponse;
import restaurantportal.entity.Role;
import restaurantportal.entity.User;
import restaurantportal.repository.UserRepository;
import restaurantportal.security.JwtUtil;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class UserServiceTest {

    @Mock private UserRepository userRepository;
    @Mock private PasswordEncoder passwordEncoder;
    @Mock private JwtUtil jwtUtil;

    @InjectMocks private UserService userService;

    private AutoCloseable closeable;

    @BeforeEach
    void setup() {
        closeable = MockitoAnnotations.openMocks(this);
    }

    @AfterEach
    void tearDown() throws Exception {
        closeable.close();
    }

    private RegisterRequest request() {
        RegisterRequest r = new RegisterRequest();
        r.setFirstName("A");
        r.setLastName("B");
        r.setEmail("test@mail.com");
        r.setPassword("123");
        r.setPhoneNumber("999");
        r.setRole("USER");
        return r;
    }

    private User user() {
        return User.builder()
                .id(1L)
                .firstName("A")
                .lastName("B")
                .email("test@mail.com")
                .password("encoded")
                .phoneNumber("999")
                .role(Role.USER)
                .walletBalance(1000.0)
                .build();
    }

    @Test
    void register_success() {
        RegisterRequest req = request();

        when(userRepository.findByEmail(req.getEmail())).thenReturn(Optional.empty());
        when(passwordEncoder.encode("123")).thenReturn("encoded");
        when(userRepository.save(any())).thenAnswer(i -> i.getArgument(0));

        UserResponse res = userService.register(req);

        assertEquals("test@mail.com", res.getEmail());
        assertEquals("USER", res.getRole());
        assertEquals(1000.0, res.getWalletBalance());
    }

    @Test
    void register_emailAlreadyExists() {
        RegisterRequest req = request();

        when(userRepository.findByEmail(req.getEmail()))
                .thenReturn(Optional.of(user()));

        assertThrows(IllegalArgumentException.class,
                () -> userService.register(req));
    }


    @Test
    void login_success() {
        User u = user();

        when(userRepository.findByEmail("test@mail.com")).thenReturn(Optional.of(u));
        when(passwordEncoder.matches("123", "encoded")).thenReturn(true);
        when(jwtUtil.generateToken("test@mail.com", "USER")).thenReturn("token");

        String token = userService.login("test@mail.com", "123");

        assertEquals("token", token);
    }

    @Test
    void login_invalidEmail() {
        when(userRepository.findByEmail("x")).thenReturn(Optional.empty());

        assertThrows(IllegalArgumentException.class,
                () -> userService.login("x", "123"));
    }

    @Test
    void login_invalidPassword() {
        User u = user();

        when(userRepository.findByEmail("test@mail.com")).thenReturn(Optional.of(u));
        when(passwordEncoder.matches("wrong", "encoded")).thenReturn(false);

        assertThrows(IllegalArgumentException.class,
                () -> userService.login("test@mail.com", "wrong"));
    }
}