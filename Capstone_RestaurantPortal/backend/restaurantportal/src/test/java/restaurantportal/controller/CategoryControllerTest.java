package restaurantportal.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import restaurantportal.dto.CategoryRequest;
import restaurantportal.dto.CategoryResponse;
import restaurantportal.service.CategoryService;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * Unit tests for CategoryController.
 */
class CategoryControllerTest {

    @InjectMocks
    private CategoryController categoryController;

    @Mock
    private CategoryService categoryService;

    /**
     * Initializes mocks before each test.
     */
    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
    }

    /**
     * Creates a sample CategoryResponse.
     */
    private CategoryResponse response() {
        return new CategoryResponse(
                1L,
                "Pizza",
                1L
        );
    }

    /**
     * Tests successful category creation.
     */
    @Test
    void create_success() {

        CategoryRequest req = new CategoryRequest();
        req.setName("Pizza");

        when(categoryService.create(1L, req)).thenReturn(response());

        CategoryResponse res = categoryController.create(1L, req);

        assertNotNull(res);
        assertEquals("Pizza", res.getName());
        verify(categoryService).create(1L, req);
    }

    /**
     * Tests category creation exception.
     */
    @Test
    void create_exception() {

        CategoryRequest req = new CategoryRequest();

        when(categoryService.create(1L, req))
                .thenThrow(new RuntimeException());

        assertThrows(RuntimeException.class, () -> {
            categoryController.create(1L, req);
        });
    }

    /**
     * Tests fetching categories by restaurant.
     */
    @Test
    void getByRestaurant_success() {

        when(categoryService.getByRestaurant(1L))
                .thenReturn(List.of(response()));

        List<CategoryResponse> list = categoryController.getByRestaurant(1L);

        assertEquals(1, list.size());
        verify(categoryService).getByRestaurant(1L);
    }

    /**
     * Tests empty category list.
     */
    @Test
    void getByRestaurant_empty() {

        when(categoryService.getByRestaurant(1L))
                .thenReturn(List.of());

        List<CategoryResponse> list = categoryController.getByRestaurant(1L);

        assertTrue(list.isEmpty());
    }

    /**
     * Tests successful category deletion.
     */
    @Test
    void delete_success() {

        doNothing().when(categoryService).delete(1L);

        assertDoesNotThrow(() -> categoryController.delete(1L));

        verify(categoryService).delete(1L);
    }

    /**
     * Tests category deletion exception.
     */
    @Test
    void delete_exception() {

        doThrow(new RuntimeException())
                .when(categoryService).delete(1L);

        assertThrows(RuntimeException.class, () -> {
            categoryController.delete(1L);
        });
    }
}