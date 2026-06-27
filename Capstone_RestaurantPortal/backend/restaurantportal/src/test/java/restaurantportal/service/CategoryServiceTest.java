package restaurantportal.service;

import org.junit.jupiter.api.*;
import org.mockito.*;
import restaurantportal.dto.CategoryRequest;
import restaurantportal.dto.CategoryResponse;
import restaurantportal.entity.Category;
import restaurantportal.entity.Restaurant;
import restaurantportal.repository.CategoryRepository;
import restaurantportal.repository.RestaurantRepository;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * Unit tests for CategoryService.
 */
class CategoryServiceTest {

    @Mock
    private CategoryRepository categoryRepository;

    @Mock
    private RestaurantRepository restaurantRepository;

    @InjectMocks
    private CategoryService categoryService;

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
     * Tests successful category creation.
     */
    @Test
    void testCreate_Success() {
        Long restaurantId = 1L;

        CategoryRequest request = new CategoryRequest();
        request.setName("Starters");

        Restaurant restaurant = new Restaurant();
        restaurant.setId(restaurantId);

        Category savedCategory = new Category();
        savedCategory.setId(10L);
        savedCategory.setName("Starters");
        savedCategory.setRestaurant(restaurant);

        when(restaurantRepository.findById(restaurantId))
                .thenReturn(Optional.of(restaurant));

        when(categoryRepository.save(any(Category.class)))
                .thenReturn(savedCategory);

        CategoryResponse response = categoryService.create(restaurantId, request);

        assertNotNull(response);
        assertEquals(10L, response.getId());
        assertEquals("Starters", response.getName());
        assertEquals(restaurantId, response.getRestaurantId());
    }

    /**
     * Tests category creation when restaurant not found.
     */
    @Test
    void testCreate_RestaurantNotFound() {
        Long restaurantId = 1L;

        CategoryRequest request = new CategoryRequest();
        request.setName("Starters");

        when(restaurantRepository.findById(restaurantId))
                .thenReturn(Optional.empty());

        RuntimeException ex = assertThrows(RuntimeException.class,
                () -> categoryService.create(restaurantId, request));

        assertEquals("Restaurant not found", ex.getMessage());
    }

    /**
     * Tests fetching categories by restaurant.
     */
    @Test
    void testGetByRestaurant() {
        Long restaurantId = 1L;

        Restaurant restaurant = new Restaurant();
        restaurant.setId(restaurantId);

        Category c1 = new Category();
        c1.setId(1L);
        c1.setName("Starters");
        c1.setRestaurant(restaurant);
        c1.setDeleted(false);

        Category c2 = new Category();
        c2.setId(2L);
        c2.setName("Desserts");
        c2.setRestaurant(restaurant);
        c2.setDeleted(false);

        when(categoryRepository.findByRestaurantId(restaurantId))
                .thenReturn(Arrays.asList(c1, c2));

        List<CategoryResponse> result = categoryService.getByRestaurant(restaurantId);

        assertEquals(2, result.size());
        assertEquals("Starters", result.get(0).getName());
        assertEquals("Desserts", result.get(1).getName());
    }

    /**
     * Tests filtering deleted categories.
     */
    @Test
    void testGetByRestaurant_FilterDeleted() {
        Long restaurantId = 1L;

        Restaurant restaurant = new Restaurant();
        restaurant.setId(restaurantId);

        Category active = new Category();
        active.setId(1L);
        active.setName("Starters");
        active.setRestaurant(restaurant);
        active.setDeleted(false);

        Category deleted = new Category();
        deleted.setId(2L);
        deleted.setName("Old");
        deleted.setRestaurant(restaurant);
        deleted.setDeleted(true);

        when(categoryRepository.findByRestaurantId(restaurantId))
                .thenReturn(Arrays.asList(active, deleted));

        List<CategoryResponse> result = categoryService.getByRestaurant(restaurantId);

        assertEquals(1, result.size());
        assertEquals("Starters", result.get(0).getName());
    }

    /**
     * Tests successful soft delete.
     */
    @Test
    void testDelete_Success() {
        Long id = 1L;

        Category category = new Category();
        category.setId(id);
        category.setDeleted(false);

        when(categoryRepository.findById(id))
                .thenReturn(Optional.of(category));

        categoryService.delete(id);

        assertTrue(category.getDeleted());
        verify(categoryRepository).save(category);
    }

    /**
     * Tests delete when category not found.
     */
    @Test
    void testDelete_NotFound() {
        when(categoryRepository.findById(1L))
                .thenReturn(Optional.empty());

        RuntimeException ex = assertThrows(RuntimeException.class,
                () -> categoryService.delete(1L));

        assertEquals("Category not found", ex.getMessage());
    }
}