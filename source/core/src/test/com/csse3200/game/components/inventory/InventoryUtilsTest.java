package com.csse3200.game.components.inventory;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.SnapshotArray;
import com.csse3200.game.extensions.GameExtension;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(GameExtension.class)
@ExtendWith(MockitoExtension.class)
public class InventoryUtilsTest {

    private Table table;
    private Table child;
    private Texture other;

    @BeforeEach
    public void setUp() {
        // Mock the Group and Disposable actors
        table = mock(Table.class);
        child = mock(Table.class);

        // Setup mock behavior for getting children (returning snapshot)
        when(table.getChildren()).thenReturn(new SnapshotArray<>(new Table[]{child}));
    }

    @Test
    public void testDisposeGroupRecursively() {
        // Call the method to test
        InventoryUtils.disposeGroupRecursively(table);

        // Verify recursive call to child group
        verify(child).getChildren();
        verify(child).clearChildren();
        verify(child).remove();

        // Verify parent group actions
        verify(table).clearChildren();
        verify(table).remove();
    }

    @Test
    void testInitialisation() {
        assertThrows(InstantiationException.class, InventoryUtils::new);
    }
}
