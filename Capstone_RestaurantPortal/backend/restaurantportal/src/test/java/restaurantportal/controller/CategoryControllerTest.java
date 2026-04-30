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

class CategoryControllerTest {

    @InjectMocks
    private CategoryController categoryController;

    @Mock
    private CategoryService categoryService;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
    }

    private CategoryResponse response() {
        return new CategoryResponse(
                1L,
                "Pizza",
                1L   // restaurantId (adjust if your constructor differs)
        );
    }

    //CREATE
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

    @Test
    void create_exception() {

        CategoryRequest req = new CategoryRequest();

        when(categoryService.create(1L, req))
                .thenThrow(new RuntimeException());

        assertThrows(RuntimeException.class, () -> {
            categoryController.create(1L, req);
        });
    }

    // GET BY RESTAURANT
    @Test
    void getByRestaurant_success() {

        when(categoryService.getByRestaurant(1L))
                .thenReturn(List.of(response()));

        List<CategoryResponse> list = categoryController.getByRestaurant(1L);

        assertEquals(1, list.size());
        verify(categoryService).getByRestaurant(1L);
    }

    @Test
    void getByRestaurant_empty() {

        when(categoryService.getByRestaurant(1L))
                .thenReturn(List.of());

        List<CategoryResponse> list = categoryController.getByRestaurant(1L);

        assertTrue(list.isEmpty());
    }

    // DELETE
    @Test
    void delete_success() {

        doNothing().when(categoryService).delete(1L);

        assertDoesNotThrow(() -> categoryController.delete(1L));

        verify(categoryService).delete(1L);
    }

    @Test
    void delete_exception() {

        doThrow(new RuntimeException())
                .when(categoryService).delete(1L);

        assertThrows(RuntimeException.class, () -> {
            categoryController.delete(1L);
        });
    }
}