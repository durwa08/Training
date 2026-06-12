package restaurantportal.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import restaurantportal.dto.MenuItemRequest;
import restaurantportal.dto.MenuItemResponse;
import restaurantportal.service.MenuItemService;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * Unit tests for MenuItemController.
 */
class MenuItemControllerTest {

    @InjectMocks
    private MenuItemController menuItemController;

    @Mock
    private MenuItemService menuItemService;

    /**
     * Initializes mocks before each test.
     */
    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
    }

    /**
     * Creates a sample MenuItemResponse.
     */
    private MenuItemResponse response() {
        return new MenuItemResponse(
                1L,
                "Burger",
                150.0,
                "Tasty burger",
                true,
                2L
        );
    }

    /**
     * Tests successful menu item creation.
     */
    @Test
    void create_success() {

        MenuItemRequest req = new MenuItemRequest();
        req.setName("Burger");
        req.setPrice(150.0);

        when(menuItemService.create(1L, req)).thenReturn(response());

        MenuItemResponse res = menuItemController.create(1L, req);

        assertNotNull(res);
        assertEquals("Burger", res.getName());
        verify(menuItemService).create(1L, req);
    }

    /**
     * Tests menu item creation exception.
     */
    @Test
    void create_exception() {

        MenuItemRequest req = new MenuItemRequest();

        when(menuItemService.create(1L, req))
                .thenThrow(new RuntimeException());

        assertThrows(RuntimeException.class, () -> {
            menuItemController.create(1L, req);
        });
    }

    /**
     * Tests fetching menu items by restaurant.
     */
    @Test
    void getByRestaurant_success() {

        when(menuItemService.getByRestaurant(1L))
                .thenReturn(List.of(response()));

        List<MenuItemResponse> list = menuItemController.getByRestaurant(1L);

        assertEquals(1, list.size());
        verify(menuItemService).getByRestaurant(1L);
    }

    /**
     * Tests empty menu item list.
     */
    @Test
    void getByRestaurant_empty() {

        when(menuItemService.getByRestaurant(1L))
                .thenReturn(List.of());

        List<MenuItemResponse> list = menuItemController.getByRestaurant(1L);

        assertTrue(list.isEmpty());
    }

    /**
     * Tests successful menu item deletion.
     */
    @Test
    void delete_success() {

        doNothing().when(menuItemService).delete(1L);

        assertDoesNotThrow(() -> menuItemController.delete(1L));

        verify(menuItemService).delete(1L);
    }

    /**
     * Tests menu item deletion exception.
     */
    @Test
    void delete_exception() {

        doThrow(new RuntimeException())
                .when(menuItemService).delete(1L);

        assertThrows(RuntimeException.class, () -> {
            menuItemController.delete(1L);
        });
    }
}