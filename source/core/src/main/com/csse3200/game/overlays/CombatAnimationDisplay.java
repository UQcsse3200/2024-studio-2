package com.csse3200.game.overlays;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.Align;
import com.csse3200.game.services.ServiceLocator;
import com.csse3200.game.ui.UIComponent;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Timer;
import java.util.TimerTask;

/**
 * CombatAnimationDisplay represents the UI displayed during combat, such as attack animations.
 */
public class CombatAnimationDisplay extends UIComponent {
    private Table rootTable;
    private Image combatImage;
    private float displayTime = 2.0f; // Duration to display the image
    private float timer = 0.0f; // Timer to track the display duration
    private boolean isVisible = false; // Track visibility of the image

    /**
     * Constructor for the CombatAnimationDisplay.
     */
    public CombatAnimationDisplay() {
        super();
    }

    @Override
    public void create() {
        super.create();
        addActors();
    }

    /**
     * Adds the image and other components to the stage.
     */
    private void addActors() {
        // Load the image texture from assets
        Texture combatTexture = ServiceLocator.getResourceService()
                .getAsset("images/cat_scratch.png", Texture.class);

        combatImage = new Image(combatTexture);
        combatImage.setAlign(Align.center);
        combatImage.setScale(1.2f);
        // combatImage.setPosition(250, 280);

        // Create a table and add the image to the center
        rootTable = new Table();
        rootTable.setFillParent(true);  // Makes the table fill the screen
        rootTable.add(combatImage).center();

        // Add the table to the stage
        stage.addActor(rootTable);

        Timer timer = new Timer();
        // Schedule a task to hide the cat scratch image after 2 seconds
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                combatImage.setVisible(false);
            }
        }, 1000); // 1 second


    }

    @Override
    public void draw(SpriteBatch batch) {
        // UI is drawn via the stage, so no need for custom draw calls here
    }

    @Override
    public void update() {
        // Update the stage (handles input, animations, etc.)
        stage.act(ServiceLocator.getTimeSource().getDeltaTime());
    }

    // Remove combat animations from screen
    @Override
    public void dispose() {
        // Clean up the root table and image
        rootTable.clear();
        combatImage.remove();
        super.dispose();
    }
}
