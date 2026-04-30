package restaurantportal.entity;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class UserTest {

    @Test
    void testBuilder() {
        User user = User.builder()
                .id(1L)
                .firstName("John")
                .email("john@gmail.com")
                .walletBalance(500.0)
                .build();

        assertEquals(1L, user.getId());
        assertEquals("John", user.getFirstName());
        assertEquals(500.0, user.getWalletBalance());
    }

    @Test
    void testCartRelation() {
        User user = new User();
        Cart cart = new Cart();

        user.setCart(cart);

        assertEquals(cart, user.getCart());
    }
}