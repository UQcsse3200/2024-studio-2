package com.csse3200.game.overlays;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.GridPoint2;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.Align;
import com.csse3200.game.components.animal.AnimalSelectionActions;
import com.csse3200.game.components.combat.CombatManager;
import com.csse3200.game.services.ServiceLocator;
import com.csse3200.game.ui.UIComponent;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Timer;
import java.util.TimerTask;

import static com.badlogic.gdx.scenes.scene2d.actions.Actions.*;

/**
 * CombatAnimationDisplay represents the UI displayed during combat, such as attack animations.
 */
public class CombatAnimationDisplay extends UIComponent {
    private Table rootTable;
    private Image combatImage;
    private Image guardImage;
    private Image sleepImage;
    private boolean increasing = true; // Whether to increase or decrease opacity
    private int pulseCount = 0; // Track number of pulses
    private Timer pulseTimer; // Timer for pulsing animation
    /**
     * Constructor for the CombatAnimationDisplay.
     */
    public CombatAnimationDisplay() {
        super();
    }

    @Override
    public void create() {
        super.create();
    }

    /**
     * Initialise the intended animation passed as an animationType from the
     * combat manager class based on chosen / randomised player or enemy attack
     */
    public void initiateAnimation(CombatManager.AnimationType animationType){
        create();

        if (animationType == CombatManager.AnimationType.ATTACK){
            attackAnimation();
        } else if (animationType == CombatManager.AnimationType.GUARD) {
            guardAnimation();
        } else if (animationType == CombatManager.AnimationType.SLEEP) {
            sleepAnimation();
        }
    }

    /**
     * Triggers general sleep animation when sleeping action chosen by either
     * player or enemy
     */
    public void sleepAnimation(){
        Texture sleepTexture = ServiceLocator.getResourceService()
                .getAsset("images/zzz.png", Texture.class);
        sleepImage = new Image(sleepTexture);

        float xZ = 310;
        float yZ = 640;

        sleepImage.setPosition(xZ, yZ);
        sleepImage.setScale(0.3f);
        sleepImage.setVisible(true);
        stage.addActor(sleepImage);
        sleepImage.clearActions();
        sleepImage.addAction(fadeIn(2f, Interpolation.fade));
        // sleepImage.addAction(fadeOut(1f, Interpolation.fade));

        Timer timer = new Timer();
        // Schedule a task to hide the cat scratch image after 2 seconds
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                sleepImage.setVisible(false);
                dispose();
            }
        }, 1000);

        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                sleepImage.setVisible(true);
                stage.addActor(sleepImage);
                sleepImage.addAction(fadeIn(2f, Interpolation.fade));
            }
        }, 1100);

        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                sleepImage.setVisible(false);
                dispose();
            }
        }, 2000);
    }

    /**
     * Triggers generalised guard animation when guard button pressed for all
     * players/
     */

    public void guardAnimation(){

        Texture guardTexture = ServiceLocator.getResourceService()
                .getAsset("images/shield_flipped.png", Texture.class);
        guardImage = new Image(guardTexture);


        float xZ = 750;
        float yZ = 400;

        guardImage.setPosition(xZ, yZ);
        guardImage.setScale(0.5f);
        guardImage.setColor(1f, 1f, 1f, 0.7f);
        guardImage.setVisible(true);
        stage.addActor(guardImage);
        guardImage.clearActions();
        guardImage.addAction(fadeIn(1f, Interpolation.fade));

        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                guardImage.setVisible(false);
                dispose();
            }
        }, 1200); // 1.2 second

    }

    /**
     * Triggers generalised guard animation when guard button pressed for all
     * players/
     */

    public void enemyGuardAnimation(){

        Texture guardTexture = ServiceLocator.getResourceService()
                .getAsset("images/shield.png", Texture.class);
        guardImage = new Image(guardTexture);


        float xZ = 950;
        float yZ = 410;

        guardImage.setPosition(xZ, yZ);
        guardImage.setScale(0.5f);
        guardImage.setColor(1f, 1f, 1f, 0.7f);
        guardImage.setVisible(true);
        stage.addActor(guardImage);
        guardImage.clearActions();
        guardImage.addAction(fadeIn(1f, Interpolation.fade));

        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                guardImage.setVisible(false);
                dispose();
            }
        }, 1200); // 1.2 second

    }

    /**
     * Triggers the cat scratch animation generalised for all players upon the attack
     * button being clicked
     */

    public void attackAnimation(){
        Texture combatTexture = ServiceLocator.getResourceService()
                .getAsset("images/cat_scratch.png", Texture.class);

        combatImage = new Image(combatTexture);
        combatImage.setAlign(Align.center);
        combatImage.setScale(1.2f);

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
                dispose();
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
        if (rootTable != null) {
            rootTable.clear();
        }
        if (combatImage != null) {
            combatImage.remove();
        }
        if (guardImage != null) {
            guardImage.remove();
        }
        if (sleepImage != null) {
            sleepImage.remove();
        }
        super.dispose();
    }
}
