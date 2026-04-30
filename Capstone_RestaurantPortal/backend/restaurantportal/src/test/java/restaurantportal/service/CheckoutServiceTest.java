package restaurantportal.service;

import org.junit.jupiter.api.*;
import org.mockito.*;
import restaurantportal.dto.CheckoutResponse;
import restaurantportal.entity.*;
import restaurantportal.repository.CartRepository;
import restaurantportal.repository.UserRepository;
import restaurantportal.security.SecurityUtil;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class CheckoutServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private CartRepository cartRepository;

    @InjectMocks
    private CheckoutService checkoutService;

    private AutoCloseable closeable;

    @BeforeEach
    void setup() {
        closeable = MockitoAnnotations.openMocks(this);
    }

    @AfterEach
    void tearDown() throws Exception {
        closeable.close();
    }

    @Test
    void testCheckout_Success_CanPlaceOrder() {
        try (MockedStatic<SecurityUtil> mocked = mockStatic(SecurityUtil.class)) {

            mocked.when(SecurityUtil::getCurrentUserEmail).thenReturn("test@mail.com");

            User user = new User();
            user.setEmail("test@mail.com");
            user.setWalletBalance(500.0);

            MenuItem menuItem = new MenuItem();
            menuItem.setName("Pizza");

            CartItem item = new CartItem();
            item.setId(1L);
            item.setMenuItem(menuItem);
            item.setPrice(100.0);
            item.setQuantity(2);

            Cart cart = new Cart();
            cart.setUser(user);
            cart.setItems(new ArrayList<>(List.of(item)));
            cart.setTotalAmount(200.0);

            when(userRepository.findByEmail("test@mail.com")).thenReturn(Optional.of(user));
            when(cartRepository.findByUser(user)).thenReturn(Optional.of(cart));

            CheckoutResponse response = checkoutService.checkout();

            assertEquals(200.0, response.getTotalAmount());
            assertEquals(500.0, response.getWalletBalance());
            assertTrue(response.isCanPlaceOrder());
            assertEquals(1, response.getItems().size());
        }
    }

    @Test
    void testCheckout_Success_CannotPlaceOrder() {
        try (MockedStatic<SecurityUtil> mocked = mockStatic(SecurityUtil.class)) {

            mocked.when(SecurityUtil::getCurrentUserEmail).thenReturn("test@mail.com");

            User user = new User();
            user.setEmail("test@mail.com");
            user.setWalletBalance(100.0);

            MenuItem menuItem = new MenuItem();
            menuItem.setName("Burger");

            CartItem item = new CartItem();
            item.setId(1L);
            item.setMenuItem(menuItem);
            item.setPrice(200.0);
            item.setQuantity(1);

            Cart cart = new Cart();
            cart.setUser(user);
            cart.setItems(new ArrayList<>(List.of(item)));
            cart.setTotalAmount(200.0);

            when(userRepository.findByEmail("test@mail.com")).thenReturn(Optional.of(user));
            when(cartRepository.findByUser(user)).thenReturn(Optional.of(cart));

            CheckoutResponse response = checkoutService.checkout();

            assertFalse(response.isCanPlaceOrder());
        }
    }

    @Test
    void testCheckout_UserNotFound() {
        try (MockedStatic<SecurityUtil> mocked = mockStatic(SecurityUtil.class)) {

            mocked.when(SecurityUtil::getCurrentUserEmail).thenReturn("test@mail.com");

            when(userRepository.findByEmail("test@mail.com")).thenReturn(Optional.empty());

            RuntimeException ex = assertThrows(RuntimeException.class,
                    () -> checkoutService.checkout());

            assertEquals("User not found", ex.getMessage());
        }
    }

    @Test
    void testCheckout_CartNotFound() {
        try (MockedStatic<SecurityUtil> mocked = mockStatic(SecurityUtil.class)) {

            mocked.when(SecurityUtil::getCurrentUserEmail).thenReturn("test@mail.com");

            User user = new User();
            user.setEmail("test@mail.com");

            when(userRepository.findByEmail("test@mail.com")).thenReturn(Optional.of(user));
            when(cartRepository.findByUser(user)).thenReturn(Optional.empty());

            RuntimeException ex = assertThrows(RuntimeException.class,
                    () -> checkoutService.checkout());

            assertEquals("Cart not found", ex.getMessage());
        }
    }

    @Test
    void testCheckout_EmptyCart() {
        try (MockedStatic<SecurityUtil> mocked = mockStatic(SecurityUtil.class)) {

            mocked.when(SecurityUtil::getCurrentUserEmail).thenReturn("test@mail.com");

            User user = new User();
            user.setEmail("test@mail.com");
            user.setWalletBalance(500.0);

            Cart cart = new Cart();
            cart.setUser(user);
            cart.setItems(new ArrayList<>());
            cart.setTotalAmount(0.0);

            when(userRepository.findByEmail("test@mail.com")).thenReturn(Optional.of(user));
            when(cartRepository.findByUser(user)).thenReturn(Optional.of(cart));

            RuntimeException ex = assertThrows(RuntimeException.class,
                    () -> checkoutService.checkout());

            assertEquals("Cart is empty", ex.getMessage());
        }
    }
}