package com.csse3200.game.components.inventory;

import static org.mockito.Mockito.*;

import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.utils.SnapshotArray;
import com.csse3200.game.extensions.GameExtension;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;


@ExtendWith(GameExtension.class)
@ExtendWith(MockitoExtension.class)
class InventoryUtilsTest {
    @Mock
    private Group table;
    @Mock
    private Group child;

    @Test
    void testDisposeGroupRecursively() {
        when(table.getChildren()).thenReturn(new SnapshotArray<>(new Group[]{child}));
        when(child.getChildren()).thenReturn(new SnapshotArray<>());

        // Call the method to test
        InventoryUtils.disposeGroupRecursively(table);

        // Verify parent group actions
        verify(table).getChildren();
        verify(table).clearChildren();
        verify(table).remove();

        // Verify child actions
        verify(child).getChildren();
        verify(child).clearChildren();
        verify(child).remove();
    }
}
