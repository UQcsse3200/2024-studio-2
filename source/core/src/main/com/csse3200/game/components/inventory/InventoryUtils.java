package com.csse3200.game.components.inventory;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.utils.Disposable;

public class InventoryUtils {
    private InventoryUtils() throws InstantiationException {
        throw new InstantiationException("Do not instantiate static util class!");
    }

    public static void disposeGroupRecursively(Group group) {
        for (Actor child : group.getChildren()) {
            // Dispose if child implements Disposable
            if (child instanceof Disposable c) {
                c.dispose();
            }
            // If the child is a Group (including Table), dispose of its children as well
            if (child instanceof Group g) {
                disposeGroupRecursively(g);
            }
        }
        group.clearChildren(); // Remove all children from the group
        group.remove();
    }
}
