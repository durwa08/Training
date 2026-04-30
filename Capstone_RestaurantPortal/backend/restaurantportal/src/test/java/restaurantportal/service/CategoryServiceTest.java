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

class CategoryServiceTest {

    @Mock
    private CategoryRepository categoryRepository;

    @Mock
    private RestaurantRepository restaurantRepository;

    @InjectMocks
    private CategoryService categoryService;

    private AutoCloseable closeable;

    @BeforeEach
    void setup() {
        closeable = MockitoAnnotations.openMocks(this);
    }

    @AfterEach
    void tearDown() throws Exception {
        closeable.close();
    }

    //CREATE - SUCCESS
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

    // CREATE - RESTAURANT NOT FOUND
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

    //GET BY RESTAURANT
    @Test
    void testGetByRestaurant() {
        Long restaurantId = 1L;

        Restaurant restaurant = new Restaurant();
        restaurant.setId(restaurantId);

        Category c1 = new Category();
        c1.setId(1L);
        c1.setName("Starters");
        c1.setRestaurant(restaurant);

        Category c2 = new Category();
        c2.setId(2L);
        c2.setName("Desserts");
        c2.setRestaurant(restaurant);

        when(categoryRepository.findByRestaurantId(restaurantId))
                .thenReturn(Arrays.asList(c1, c2));

        List<CategoryResponse> result = categoryService.getByRestaurant(restaurantId);

        assertEquals(2, result.size());
        assertEquals("Starters", result.get(0).getName());
        assertEquals("Desserts", result.get(1).getName());
    }

    // GET BY RESTAURANT - EMPTY LIST
    @Test
    void testGetByRestaurant_Empty() {
        when(categoryRepository.findByRestaurantId(1L))
                .thenReturn(Collections.emptyList());

        List<CategoryResponse> result = categoryService.getByRestaurant(1L);

        assertTrue(result.isEmpty());
    }

    // DELETE
    @Test
    void testDelete() {
        Long id = 1L;

        categoryService.delete(id);

        verify(categoryRepository).deleteById(id);
    }
}