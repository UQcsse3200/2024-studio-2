package com.csse3200.game.components.inventory;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.utils.Disposable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class InventoryUtils {
    private static final Logger logger = LoggerFactory.getLogger(InventoryUtils.class);

    public InventoryUtils() throws InstantiationException {
        throw new InstantiationException("Do not instantiate static util class (InventoryUtils)");
    }

    public static void disposeGroupRecursively(Group group) {
        for (Actor child : group.getChildren()) {
            // Dispose if child implements Disposable
            if (child instanceof Disposable) {
                ((Disposable) child).dispose();
            }
            // If the child is a Group (including Table), dispose of its children as well
            if (child instanceof Group) {
                disposeGroupRecursively((Group) child);
            }
        }
        group.clearChildren(); // Remove all children from the group
        group.remove();
    }
}
