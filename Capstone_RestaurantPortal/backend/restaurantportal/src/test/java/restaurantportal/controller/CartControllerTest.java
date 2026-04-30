package restaurantportal.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import restaurantportal.dto.AddToCartRequest;
import restaurantportal.dto.CartResponse;
import restaurantportal.service.CartService;

import java.util.Collections;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class CartControllerTest {

    @InjectMocks
    private CartController cartController;

    @Mock
    private CartService cartService;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
    }

    private CartResponse response() {
        return new CartResponse(
                1L,
                200.0,
                Collections.emptyList()
        );
    }

    // ADD TO CART
    @Test
    void addToCart_success() {

        AddToCartRequest req = new AddToCartRequest();
        req.setMenuItemId(1L);
        req.setQuantity(2);

        when(cartService.addToCart(req)).thenReturn(response());

        CartResponse res = cartController.addToCart(req);

        assertNotNull(res);
        assertEquals(200.0, res.getTotalAmount());
        verify(cartService).addToCart(req);
    }

    @Test
    void addToCart_exception() {

        AddToCartRequest req = new AddToCartRequest();

        when(cartService.addToCart(req))
                .thenThrow(new RuntimeException());

        assertThrows(RuntimeException.class, () -> {
            cartController.addToCart(req);
        });
    }

    // GET CART
    @Test
    void getCart_success() {

        when(cartService.getMyCart()).thenReturn(response());

        CartResponse res = cartController.getCart();

        assertNotNull(res);
        verify(cartService).getMyCart();
    }

    // REMOVE ITEM
    @Test
    void remove_success() {

        doNothing().when(cartService).removeItem(1L);

        String result = cartController.remove(1L);

        assertEquals("Item removed", result);
        verify(cartService).removeItem(1L);
    }

    @Test
    void remove_exception() {

        doThrow(new RuntimeException())
                .when(cartService).removeItem(1L);

        assertThrows(RuntimeException.class, () -> {
            cartController.remove(1L);
        });
    }

    // CLEAR CART
    @Test
    void clear_success() {

        doNothing().when(cartService).clearCart();

        String result = cartController.clear();

        assertEquals("Cart cleared", result);
        verify(cartService).clearCart();
    }

    @Test
    void clear_exception() {

        doThrow(new RuntimeException())
                .when(cartService).clearCart();

        assertThrows(RuntimeException.class, () -> {
            cartController.clear();
        });
    }
}