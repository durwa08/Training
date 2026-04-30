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

class MenuItemServiceTest {

    @Mock
    private MenuItemRepository menuItemRepository;

    @Mock
    private CategoryRepository categoryRepository;

    @InjectMocks
    private MenuItemService menuItemService;

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

        when(categoryRepository.findById(categoryId)).thenReturn(Optional.of(category));
        when(menuItemRepository.save(any(MenuItem.class))).thenReturn(saved);

        MenuItemResponse response = menuItemService.create(categoryId, request);

        assertEquals(5L, response.getId());
        assertEquals("Pizza", response.getName());
        assertEquals(200.0, response.getPrice());
        assertEquals(categoryId, response.getCategoryId());
    }

    @Test
    void testCreate_CategoryNotFound() {
        when(categoryRepository.findById(1L)).thenReturn(Optional.empty());

        MenuItemRequest request = new MenuItemRequest();
        request.setName("Pizza");

        RuntimeException ex = assertThrows(RuntimeException.class,
                () -> menuItemService.create(1L, request));

        assertEquals("Category not found", ex.getMessage());
    }

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

        MenuItem m2 = new MenuItem();
        m2.setId(2L);
        m2.setName("Pizza");
        m2.setPrice(200.0);
        m2.setCategory(category);

        when(menuItemRepository.findByRestaurantId(restaurantId))
                .thenReturn(Arrays.asList(m1, m2));

        List<MenuItemResponse> result = menuItemService.getByRestaurant(restaurantId);

        assertEquals(2, result.size());
        assertEquals("Burger", result.get(0).getName());
        assertEquals("Pizza", result.get(1).getName());
    }

    @Test
    void testGetByRestaurant_Empty() {
        when(menuItemRepository.findByRestaurantId(1L))
                .thenReturn(Collections.emptyList());

        List<MenuItemResponse> result = menuItemService.getByRestaurant(1L);

        assertTrue(result.isEmpty());
    }

    @Test
    void testDelete() {
        Long id = 1L;

        menuItemService.delete(id);

        verify(menuItemRepository).deleteById(id);
    }
}