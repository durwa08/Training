package restaurantportal.entity;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Test class for MenuItem entity.
 */
class MenuItemTest {

    /**
     * Tests getter and setter methods of MenuItem.
     */
    @Test
    void testGetterSetter() {
        MenuItem item = new MenuItem();

        item.setName("Pizza");
        item.setPrice(300.0);
        item.setAvailable(true);

        assertEquals("Pizza", item.getName());
        assertEquals(300.0, item.getPrice());
        assertTrue(item.getAvailable());
    }
}