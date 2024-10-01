package com.csse3200.game.overlays;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.Align;
import com.csse3200.game.components.combat.CombatManager;
import com.csse3200.game.services.ServiceLocator;
import com.csse3200.game.ui.UIComponent;
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
     * Triggers fireball to be thrown at enemy NPC's position
     */
    public void attackAnimation() {
        Texture combatTexture = ServiceLocator.getResourceService()
                .getAsset("images/flipped_fireball.png", Texture.class); // Replaced with single_fireball

        combatImage = new Image(combatTexture);
        combatImage.setAlign(Align.center);
        combatImage.setScale(1f);

        // Set the initial position of the image (e.g., off-screen or at a specific point)
        combatImage.setPosition(600, 550);
        combatImage.setVisible(true);

        // Add the table to the stage
        stage.addActor(combatImage);

        // Clear any existing actions to avoid conflicts
        combatImage.clearActions();

        // Move the image over 2 seconds
        combatImage.addAction(moveTo(1200, 550, 2f, Interpolation.linear));

        // Schedule to hide the image after it reaches its destination
        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                combatImage.setVisible(false);
                dispose();
            }
        }, 2000);  // 2 seconds
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
