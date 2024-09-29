package com.csse3200.game.components.inventory;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Disposable;
import com.badlogic.gdx.utils.SnapshotArray;
import com.csse3200.game.extensions.GameExtension;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(GameExtension.class)
@ExtendWith(MockitoExtension.class)
public class InventoryUtilsTest {

    private Group parentGroup;
    private Group childGroup;
    private Disposable disposableActor;

    @BeforeEach
    public void setUp() {
        // Mock the Group and Disposable actors
        parentGroup = mock(Group.class);
        childGroup = mock(Group.class);
        disposableActor = mock(Disposable.class);

        // Create mock snapshot Array<Actor> to return from getChildren()
        Array<Actor> parentChildren = new Array<>();
        parentChildren.addAll((Actor) disposableActor, childGroup);

        Array<Actor> childChildren = new Array<>();

        // Setup mock behavior for getting children (returning snapshot)
        when(parentGroup.getChildren()).thenReturn(new SnapshotArray<>(parentChildren));
        when(childGroup.getChildren()).thenReturn(new SnapshotArray<>(childChildren));
    }

    @Test
    public void testDisposeGroupRecursively() {
        // Call the method to test
        InventoryUtils.disposeGroupRecursively(parentGroup);

        // Verify disposable actor is disposed
        verify(disposableActor).dispose();

        // Verify recursive call to child group
        verify(childGroup).getChildren();
        verify(childGroup).clearChildren();
        verify(childGroup).remove();

        // Verify parent group actions
        verify(parentGroup).clearChildren();
        verify(parentGroup).remove();
    }

    @Test
    void testInitialisation() {
        assertThrows(InstantiationException.class, InventoryUtils::new);
    }
}
