package com.csse3200.game.components.player;

import com.badlogic.gdx.scenes.scene2d.utils.DragAndDrop;

public class DragAndDropService {
    private static final DragAndDrop dnd = new DragAndDrop();

    public static DragAndDrop getDragAndDrop() {
        return dnd;
    }
}