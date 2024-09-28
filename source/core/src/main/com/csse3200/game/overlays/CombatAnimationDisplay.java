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
        // addActors();
    }

    public void initiateAnimation(CombatManager.AnimationType animationType){
        create();
        if (animationType == CombatManager.AnimationType.ATTACK){
            attackAnimation();
        } else if (animationType == CombatManager.AnimationType.GUARD) {
            // guardAnimation();
        } else if (animationType == CombatManager.AnimationType.SLEEP) {
            sleepAnimation();
        }
    }

    /**
     * Adds the image and other components to the stage.
     */
    private void addActors() {
    }

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
            }
        }, 2000);

        // 1 second
//        sleepImage.addAction(
//                sequence(
//                    fadeIn(1f, Interpolation.fade),
//                    fadeOut(1f, Interpolation.fade),
////                    fadeIn(1f, Interpolation.fade),
////                    fadeOut(1f, Interpolation.fade),
//                    run(new Runnable() {
//                        @Override
//                        public void run() {
//                            sleepImage.setVisible(false); // Make sure it's hidden at the end
//                        }
//                    })
//                )
//        );
    }

    public void guardAnimation(){
        Texture auraTexture = ServiceLocator.getResourceService()
                .getAsset("images/guard.png", Texture.class);

//        Texture auraTexture = ServiceLocator.getResourceService()
//                .getAsset("images/guard_outside_in.png", Texture.class);

        guardImage = new Image(auraTexture);
        guardImage.setColor(1f, 1f, 1f, 0.2f); // Set initial opacity with alpha (RGBA)
        guardImage.setScale(0.7f);
        String animalPath = AnimalSelectionActions.getSelectedAnimalImagePath();
        if (animalPath == "images/croc.png"){
            guardImage.setPosition(348, 345);
        } else if (animalPath == "images/dog.png"){
            guardImage.setPosition(380, 340);
        } else { //animal is bird
            guardImage.setPosition(368, 370);
        }

        // Add the table to the stage
        guardImage.setZIndex(10);
        stage.addActor(guardImage);
        guardImage.setVisible(true);
        guardImage.clearActions();

        // Set up actions: fade in, pulse effect, then fade out
        guardImage.addAction(
                sequence(
                        fadeIn(0.3f), // Fade in over 0.2 seconds
                        repeat(3, sequence(
                                alpha(0.5f, 0.5f), // Increase opacity to 0.8 over 0.5 seconds
                                alpha(0.2f, 0.5f)  // Decrease opacity to 0.2 over 0.5 seconds
                        )),
                        fadeOut(0.3f), // Fade out over 0.5 seconds
                        run(new Runnable() {
                            @Override
                            public void run() {
                                guardImage.setVisible(false); // Make sure it's hidden at the end
                            }
                        })
                )
        );
    }

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
