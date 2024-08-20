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

    // TODO: THIS FUNCTIONALITY IS NOT YET IMPLEMENTED!
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

    // TODO: THIS FUNCTION IS NOT YET IMPLEMENTED!!!
    private void handleHover(int screenX, int screenY) {
        // Handle hover logic her
        // // TODO: ADD ITEM DESCRIPTION TO ABSTRACT ITEM!
        //                    // TODO: FIGURE OUT HOW TO DO ITEM DESCRIPTION WITH HOVERING
        //                    // Tooltip for item description
        ////                    String description = "Hi my name is";
        ////                    Label tooltipLabel = new Label(description, new Label.LabelStyle(skin.getFont("default-font"), Color.WHITE));
        ////                    Table x = new Table();
        ////                    x.add(tooltipLabel);
        ////                    Tooltip<Table> tooltip = new Tooltip<>(x);
        ////                    entity.getEvents().addListener(tooltip);
        // TODO: NEED TO ADD CORRESPONDING EVENT HANDLER TO PlayerInventoryDisplay ONCE THE
        //  TRIGGER IS WRITTEN!!!
    }
}