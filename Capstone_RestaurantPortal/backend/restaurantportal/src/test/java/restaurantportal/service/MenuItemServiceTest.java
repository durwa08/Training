package restaurantportal.service;

import org.junit.jupiter.api.*;
import org.mockito.*;
import restaurantportal.dto.MenuItemRequest;
import restaurantportal.dto.MenuItemResponse;
import restaurantportal.entity.*;
import restaurantportal.repository.CategoryRepository;
import restaurantportal.repository.MenuItemRepository;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * Unit tests for MenuItemService.
 */
class MenuItemServiceTest {

    @Mock
    private MenuItemRepository menuItemRepository;

    @Mock
    private CategoryRepository categoryRepository;

    @InjectMocks
    private MenuItemService menuItemService;

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
     * Tests successful menu item creation.
     */
    @Test
    void testCreate_Success() {
        Long categoryId = 1L;

        MenuItemRequest request = new MenuItemRequest();
        request.setName("Pizza");
        request.setPrice(200.0);

        Restaurant restaurant = new Restaurant();
        restaurant.setId(10L);

        Category category = new Category();
        category.setId(categoryId);
        category.setRestaurant(restaurant);

        MenuItem saved = new MenuItem();
        saved.setId(5L);
        saved.setName("Pizza");
        saved.setPrice(200.0);
        saved.setCategory(category);
        saved.setAvailable(true);

        when(categoryRepository.findById(categoryId)).thenReturn(Optional.of(category));
        when(menuItemRepository.save(any(MenuItem.class))).thenReturn(saved);

        MenuItemResponse response = menuItemService.create(categoryId, request);

        assertEquals(5L, response.getId());
        assertEquals("Pizza", response.getName());
        assertEquals(200.0, response.getPrice());
        assertEquals(categoryId, response.getCategoryId());
    }

    /**
     * Tests creation when category not found.
     */
    @Test
    void testCreate_CategoryNotFound() {
        when(categoryRepository.findById(1L)).thenReturn(Optional.empty());

        MenuItemRequest request = new MenuItemRequest();
        request.setName("Pizza");

        RuntimeException ex = assertThrows(RuntimeException.class,
                () -> menuItemService.create(1L, request));

        assertEquals("Category not found", ex.getMessage());
    }

    /**
     * Tests fetching menu items by restaurant.
     */
    @Test
    void testGetByRestaurant() {
        Long restaurantId = 1L;

        Category category = new Category();
        category.setId(100L);

        MenuItem m1 = new MenuItem();
        m1.setId(1L);
        m1.setName("Burger");
        m1.setPrice(100.0);
        m1.setCategory(category);
        m1.setDeleted(false);

        MenuItem m2 = new MenuItem();
        m2.setId(2L);
        m2.setName("Pizza");
        m2.setPrice(200.0);
        m2.setCategory(category);
        m2.setDeleted(false);

        when(menuItemRepository.findByRestaurantId(restaurantId))
                .thenReturn(Arrays.asList(m1, m2));

        List<MenuItemResponse> result = menuItemService.getByRestaurant(restaurantId);

        assertEquals(2, result.size());
        assertEquals("Burger", result.get(0).getName());
        assertEquals("Pizza", result.get(1).getName());
    }

    /**
     * Tests successful soft delete.
     */
    @Test
    void testDelete_Success() {
        Long id = 1L;

        MenuItem item = new MenuItem();
        item.setId(id);
        item.setDeleted(false);

        when(menuItemRepository.findById(id)).thenReturn(Optional.of(item));

        menuItemService.delete(id);

        assertTrue(item.getDeleted());
        verify(menuItemRepository).save(item);
    }

    /**
     * Tests delete when item not found.
     */
    @Test
    void testDelete_NotFound() {
        when(menuItemRepository.findById(1L)).thenReturn(Optional.empty());

        RuntimeException ex = assertThrows(RuntimeException.class,
                () -> menuItemService.delete(1L));

        assertEquals("Menu item not found", ex.getMessage());
    }

    /**
     * Tests successful update of menu item.
     */
    @Test
    void testUpdate_Success() {
        Long id = 1L;

        MenuItemRequest request = new MenuItemRequest();
        request.setName("Updated Pizza");
        request.setPrice(250.0);
        request.setAvailable(true);

        Category category = new Category();
        category.setId(10L);

        MenuItem item = new MenuItem();
        item.setId(id);
        item.setCategory(category);

        when(menuItemRepository.findById(id)).thenReturn(Optional.of(item));
        when(menuItemRepository.save(any(MenuItem.class))).thenReturn(item);

        MenuItemResponse response = menuItemService.update(id, request);

        assertEquals("Updated Pizza", item.getName());
        assertEquals(250.0, item.getPrice());
    }
}