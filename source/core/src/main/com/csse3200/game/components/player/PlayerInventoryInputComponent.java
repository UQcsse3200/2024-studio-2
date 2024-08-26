package com.csse3200.game.components.player;

import com.csse3200.game.input.InputComponent;

public class PlayerInventoryInputComponent extends InputComponent {
    private boolean inventoryOpen = false;

    @Override
    public void create() {
        super.create();
        entity.getEvents().addListener("toggleInventory", this::toggleInventory);
    }

    private void toggleInventory() {inventoryOpen = !inventoryOpen;}

    @Override
    public boolean mouseMoved(int screenX, int screenY) {
        if (inventoryOpen) {
            handleHover(screenX, screenY);
        }
        return true;
    }

    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        if (inventoryOpen) {
            entity.getEvents().trigger("slotClicked", screenX, screenY);
        }
        return false;
    }

    private void handleHover(int screenX, int screenY) {
    }
}