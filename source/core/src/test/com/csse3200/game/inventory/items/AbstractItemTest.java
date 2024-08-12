package test.com.csse3200.game.inventory.items;

import static org.junit.jupiter.api.Assertions.assertEquals;

import com.csse3200.game.extensions.GameExtension;
import main.com.csse3200.game.inventory.items.AbstractItem;
import com.csse3200.game.inventory.items.ItemUsageContext;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;

@ExtendWith(GameExtension.class)
public class AbstractItemTest {
    private TestableItem item;
    private ItemUsageContext context;

    /**
     * Concrete subclass of AbstractItem for testing purposes.
     */
    private static class TestableItem extends AbstractItem {
        public int numUsed = 0;

        public TestableItem(String name, int limit) {
            super(name, limit);
        }

        @Override
        public void useItem(ItemUsageContext context) {
            // For testing purposes only
            numUsed++;
        }
    }

    @BeforeEach
    public void setUp() { // Initialize TestableItem and ItemUsageContext
        item = new TestableItem("TestItem", 7);
        context = new ItemUsageContext();
    }

    @Test
    public void testGetName() {
        assertEquals("TestItem", item.getName(), "The name of the item should be 'TestItem'.");
    }

    @Test
    public void testGetLimit() {
        assertEquals(7, item.getLimit(), "The limit of the item should be 7.");
    }

    @Test
    public void testUseItem() {
        item.useItem(context);
        assertEquals(1, item.numUsed, "NumUsed should return 1.");
    }
}