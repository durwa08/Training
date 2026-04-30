package restaurantportal.entity;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class MenuItemTest {

    @Test
    void testGetterSetter() {
        MenuItem item = new MenuItem();

        item.setName("Pizza");
        item.setPrice(300.0);
        item.setIsAvailable(true);

        assertEquals("Pizza", item.getName());
        assertEquals(300.0, item.getPrice());
        assertTrue(item.getIsAvailable());
    }
}