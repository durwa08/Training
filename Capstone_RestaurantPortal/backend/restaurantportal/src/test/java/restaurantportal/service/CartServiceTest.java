package restaurantportal.service;

import org.junit.jupiter.api.*;
import org.mockito.*;
import restaurantportal.dto.AddToCartRequest;
import restaurantportal.dto.CartResponse;
import restaurantportal.entity.*;
import restaurantportal.repository.*;
import restaurantportal.security.SecurityUtil;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * Unit tests for CartService.
 */
class CartServiceTest {

    @Mock
    private CartRepository cartRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private MenuItemRepository menuItemRepository;

    @InjectMocks
    private CartService cartService;

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
     * Tests adding a new item to cart.
     */
    @Test
    void testAddToCart_NewItem() {
        try (MockedStatic<SecurityUtil> mocked = mockStatic(SecurityUtil.class)) {

            mocked.when(SecurityUtil::getCurrentUserEmail).thenReturn("dp@mail.com");

            User user = new User();
            user.setEmail("dp@mail.com");

            Cart cart = new Cart();
            cart.setUser(user);

            MenuItem menuItem = new MenuItem();
            menuItem.setId(1L);
            menuItem.setName("Pizza");
            menuItem.setPrice(100.0);

            AddToCartRequest request = new AddToCartRequest();
            request.setMenuItemId(1L);
            request.setQuantity(2);

            when(userRepository.findByEmail("dp@mail.com")).thenReturn(Optional.of(user));
            when(cartRepository.findByUser(user)).thenReturn(Optional.of(cart));
            when(menuItemRepository.findById(1L)).thenReturn(Optional.of(menuItem));
            when(cartRepository.save(any())).thenAnswer(i -> i.getArgument(0));

            CartResponse response = cartService.addToCart(request);

            assertEquals(200.0, response.getTotalAmount());
            assertEquals(1, response.getItems().size());
        }
    }

    /**
     * Tests add to cart when user not found.
     */
    @Test
    void testAddToCart_UserNotFound() {
        try (MockedStatic<SecurityUtil> mocked = mockStatic(SecurityUtil.class)) {

            mocked.when(SecurityUtil::getCurrentUserEmail).thenReturn("dp@mail.com");

            AddToCartRequest request = new AddToCartRequest();
            request.setMenuItemId(1L);

            when(userRepository.findByEmail("dp@mail.com")).thenReturn(Optional.empty());

            RuntimeException ex = assertThrows(RuntimeException.class,
                    () -> cartService.addToCart(request));

            assertEquals("User not found", ex.getMessage());
        }
    }

    /**
     * Tests successful removal of cart item.
     */
    @Test
    void testRemoveItem_Success() {
        try (MockedStatic<SecurityUtil> mocked = mockStatic(SecurityUtil.class)) {

            mocked.when(SecurityUtil::getCurrentUserEmail).thenReturn("dp@mail.com");

            User user = new User();
            user.setEmail("dp@mail.com");

            Cart cart = new Cart();
            cart.setUser(user);

            CartItem item = new CartItem();
            item.setId(1L);
            item.setPrice(100.0);
            item.setQuantity(1);
            item.setCart(cart);

            cart.getItems().add(item);

            when(userRepository.findByEmail("dp@mail.com")).thenReturn(Optional.of(user));
            when(cartRepository.findByUser(user)).thenReturn(Optional.of(cart));

            cartService.removeItem(1L);

            assertTrue(cart.getItems().isEmpty());
        }
    }

    /**
     * Tests clearing the cart.
     */
    @Test
    void testClearCart() {
        try (MockedStatic<SecurityUtil> mocked = mockStatic(SecurityUtil.class)) {

            mocked.when(SecurityUtil::getCurrentUserEmail).thenReturn("dp@mail.com");

            User user = new User();
            user.setEmail("dp@mail.com");

            Cart cart = new Cart();
            cart.setUser(user);
            cart.getItems().add(new CartItem());
            cart.setTotalAmount(100.0);

            when(userRepository.findByEmail("dp@mail.com")).thenReturn(Optional.of(user));
            when(cartRepository.findByUser(user)).thenReturn(Optional.of(cart));

            cartService.clearCart();

            assertEquals(0.0, cart.getTotalAmount());
            assertTrue(cart.getItems().isEmpty());
        }
    }
}