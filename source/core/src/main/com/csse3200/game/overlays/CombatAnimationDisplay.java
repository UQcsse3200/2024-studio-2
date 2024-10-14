package com.csse3200.game.overlays;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.utils.Align;
import com.csse3200.game.areas.combat.CombatArea;
import com.csse3200.game.components.combat.CombatManager;
import com.csse3200.game.services.ServiceLocator;
import com.csse3200.game.ui.UIComponent;
import com.badlogic.gdx.utils.Timer;
import static com.badlogic.gdx.scenes.scene2d.actions.Actions.*;

/**
 * CombatAnimationDisplay represents the UI displayed during combat, such as attack animations.
 */
public class CombatAnimationDisplay extends UIComponent {
    private Image combatImage;
    private Image guardImage;
    private Image sleepImage;
    private Image enemyCombatImage; // enemy combat image
    private Image enemySleepImage; // enemy sleep image
    private Image enemyGuardImage; // enemy guard image
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

    public void animateCombat(CombatManager.Action playerAction, CombatManager.Action enemyAction, Boolean playerFaster) {
        if (playerAction == enemyAction && playerAction == CombatManager.Action.ATTACK) {
            if (playerFaster) {
                initiatePlayerAnimation(playerAction);
                Timer.schedule(new Timer.Task() {
                    @Override
                    public void run() {
                        initiateEnemyAnimation(enemyAction);
                    }
                }, 1f);
            } else {
                initiateEnemyAnimation(enemyAction);
                Timer.schedule(new Timer.Task() {
                    @Override
                    public void run() {
                        initiatePlayerAnimation(playerAction);
                    }
                }, 1f);
            }
        }
    }

    /**
     * Initialise the intended animation passed as an animationType from the
     * combat manager class based on chosen / randomised player or enemy attack
     */
    public void initiatePlayerAnimation(CombatManager.Action animationType){
        create();

        if (animationType == CombatManager.Action.ATTACK){
            playerAttackAnimation();
        } else if (animationType == CombatManager.Action.GUARD) {
            guardAnimation();
        } else if (animationType == CombatManager.Action.SLEEP) {
            sleepAnimation();
        }
    }

    /**
     * Initialise the intended animation passed as an animationType from the
     * combat manager class based on chosen / randomised player or enemy attack
     */
    public void initiateEnemyAnimation(CombatManager.Action animationType){
        create();

        if (animationType == CombatManager.Action.ATTACK){
            enemyAttackAnimation();
        } else if (animationType == CombatManager.Action.GUARD) {
            enemyGuardAnimation();
        } else if (animationType == CombatManager.Action.SLEEP) {
            enemySleepAnimation();
        }
    }

    /**
     * Triggers general sleep animation when sleeping action chosen by
     * player
     */
    public void sleepAnimation(){
        Texture sleepTexture = ServiceLocator.getResourceService()
                .getAsset("images/zzz.png", Texture.class);
        sleepImage = new Image(sleepTexture);

        if (CombatArea.kingdomType == CombatArea.KINGDOM.WATER){
            float xZ = stage.getWidth() * 0.2f;
            float yZ = stage.getHeight() * 0.6f;
            sleepImage.setPosition(xZ, yZ);
        } else {
            float xZ = stage.getWidth() * 0.23f;
            float yZ = stage.getHeight() * 0.39f;
            sleepImage.setPosition(xZ, yZ);
        }

        float scaleFactor = stage.getWidth() * 0.06f / sleepImage.getWidth();
        sleepImage.setScale(scaleFactor);
        // sleepImage.setScale(0.3f);
        sleepImage.setVisible(true);
        stage.addActor(sleepImage);
        sleepImage.clearActions();
        sleepImage.addAction(fadeIn(2f, Interpolation.fade));

        Timer.schedule(new Timer.Task() {
            @Override
            public void run() {
                sleepImage.setVisible(false);
                if (sleepImage != null) {
                    sleepImage.remove();
                }
            }
        }, 1f);

        Timer.schedule(new Timer.Task() {
            @Override
            public void run() {
                sleepImage.setVisible(true);
                stage.addActor(sleepImage);
                sleepImage.addAction(fadeIn(2f, Interpolation.fade));
            }
        }, 1.1f);

        Timer.schedule(new Timer.Task() {
            @Override
            public void run() {
                sleepImage.setVisible(false);
                if (sleepImage != null) {
                    sleepImage.remove();
                }
            }
        }, 2f);
    }

    /**
     * Triggers identical sleep animation for enemy
     */
    public void enemySleepAnimation(){
        Texture sleepTexture2 = ServiceLocator.getResourceService()
                .getAsset("images/zzz.png", Texture.class);
        enemySleepImage = new Image(sleepTexture2);

        float xZ = 1150;
        float yZ = 640;

        enemySleepImage.setPosition(xZ, yZ);
        enemySleepImage.setScale(0.3f);
        enemySleepImage.setVisible(true);
        stage.addActor(enemySleepImage);
        enemySleepImage.clearActions();
        enemySleepImage.addAction(fadeIn(2f, Interpolation.fade));

        // Schedule a task to hide the cat scratch image after 2 seconds
        Timer.schedule(new Timer.Task() {
            @Override
            public void run() {
                enemySleepImage.setVisible(false);
                if (enemySleepImage != null) {
                    enemySleepImage.remove();
                }
            }
        }, 1f);

        Timer.schedule(new Timer.Task() {
            @Override
            public void run() {
                enemySleepImage.setVisible(true);
                stage.addActor(enemySleepImage);
                enemySleepImage.addAction(fadeIn(2f, Interpolation.fade));
            }
        }, 1.1f);

        Timer.schedule(new Timer.Task() {
            @Override
            public void run() {
                enemySleepImage.setVisible(false);
                if (enemySleepImage != null) {
                    enemySleepImage.remove();
                }
            }
        }, 2.1f);
    }

    /**
     * Triggers generalised guard animation when guard button pressed for all
     * players/
     */

    public void guardAnimation(){

        Texture guardTexture = ServiceLocator.getResourceService()
                .getAsset("images/shield_flipped.png", Texture.class);
        guardImage = new Image(guardTexture);


        float xZ;
        float yZ;
        if (CombatArea.kingdomType == CombatArea.KINGDOM.WATER) {
            xZ = stage.getWidth() * 0.39f;
            yZ = stage.getHeight() * 0.5f;
        } else {
            xZ = stage.getWidth() * 0.370f;
            yZ = stage.getHeight() * 0.285f;
        }

        guardImage.setPosition(xZ, yZ);

        float scaleFactor = stage.getWidth() * 0.1f / guardImage.getWidth();
        guardImage.setScale(scaleFactor);

        // guardImage.setScale(0.35f);
        guardImage.setColor(1f, 1f, 1f, 0.7f);
        guardImage.setVisible(true);
        stage.addActor(guardImage);
        guardImage.clearActions();
        guardImage.addAction(fadeIn(1f, Interpolation.fade));
        Timer.schedule(new Timer.Task() {
            @Override
            public void run() {
                guardImage.setVisible(false);
                if (guardImage != null) {
                    guardImage.remove();
                }
            }
        }, 1.2f); // 1.2 second

    }

    /**
     * Triggers generalised guard animation when guard button pressed for all
     * players/
     */

    public void enemyGuardAnimation(){

        Texture guardTexture = ServiceLocator.getResourceService()
                .getAsset("images/shield.png", Texture.class);
        enemyGuardImage = new Image(guardTexture);

        float xZ = stage.getWidth() * 0.6f;
        float yZ = stage.getHeight() * 0.25f;

        enemyGuardImage.setPosition(xZ, yZ);
        enemyGuardImage.setScale(0.3f);
        enemyGuardImage.setColor(1f, 1f, 1f, 0.7f);
        enemyGuardImage.setVisible(true);
        stage.addActor(enemyGuardImage);
        enemyGuardImage.clearActions();
        enemyGuardImage.addAction(fadeIn(1f, Interpolation.fade));
        Timer.schedule(new Timer.Task() {
            @Override
            public void run() {
                enemyGuardImage.setVisible(false);
                if (enemyGuardImage != null) {
                    enemyGuardImage.remove();
                }
            }
        }, 1.2f); // 1.2 second
    }

    /**
     * Triggers fireball to be thrown at enemy entity's position
     */
    public void playerAttackAnimation() {
        Texture combatTexture = ServiceLocator.getResourceService()
                .getAsset("images/rock.png", Texture.class); // Replaced with single_fireball

        combatImage = new Image(combatTexture);
        combatImage.setAlign(Align.center);
        combatImage.setScale(0.3f);

        float xZ;
        float yZ;

        if (CombatArea.kingdomType == CombatArea.KINGDOM.WATER) {
            xZ = stage.getWidth() * 0.3f;
            yZ = stage.getHeight() * 0.5f;
        } else {
            xZ = stage.getWidth() * 0.3f;
            yZ = stage.getHeight() * 0.3f;
        }

        // Set the initial position of the image (e.g., off-screen or at a specific point)
        combatImage.setPosition(xZ, yZ);
        combatImage.setVisible(true);

        // Add the table to the stage
        stage.addActor(combatImage);

        // Clear any existing actions to avoid conflicts
        combatImage.clearActions();

        float xMove = stage.getWidth() * 0.69f;

        // Move the image over 2 seconds
        // combatImage.addAction(moveTo(1200, 550, 0.8f, Interpolation.linear));
        combatImage.addAction(moveTo(xMove, yZ, 0.8f, Interpolation.linear));
        Timer.schedule(new Timer.Task() {
            @Override
            public void run() {
                combatImage.setVisible(false);
                if (combatImage != null) {
                    combatImage.remove();
                }
            }
        }, 0.8f);  // 2 seconds
    }

    /**
     * Generates fireball animation from enemy entity position to
     * player position
     */
    public void enemyAttackAnimation() {
        Texture combatTexture = ServiceLocator.getResourceService()
                .getAsset("images/rock.png", Texture.class); // Replaced with single_fireball

        combatImage = new Image(combatTexture);
        combatImage.setAlign(Align.center);
        combatImage.setScale(0.3f);

        float xZ;
        float yZ;

        if (CombatArea.kingdomType == CombatArea.KINGDOM.WATER) {
            xZ = stage.getWidth() * 0.69f;
            yZ = stage.getHeight() * 0.5f;
        } else {
            xZ = stage.getWidth() * 0.69f;
            yZ = stage.getHeight() * 0.3f;
        }

        // Set the initial position of the image (e.g., off-screen or at a specific point)
        combatImage.setPosition(xZ, yZ);
        combatImage.setVisible(true);

        // Add the table to the stage
        stage.addActor(combatImage);

        // Clear any existing actions to avoid conflicts
        combatImage.clearActions();

        float xMove = stage.getWidth() * 0.3f;

        // Move the image over 2 seconds
        // combatImage.addAction(moveTo(1200, 550, 0.8f, Interpolation.linear));
        combatImage.addAction(moveTo(xMove, yZ, 0.8f, Interpolation.linear));

        // Schedule to hide the image after it reaches its destination
        Timer.schedule(new Timer.Task() {
            @Override
            public void run() {
                combatImage.setVisible(false);
                if (combatImage != null) {
                    combatImage.remove();
                }
            }
        }, 0.8f);  // 2 seconds
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
        // Clean up the animations image
        if (combatImage != null) {
            combatImage.remove();
        }
        if (guardImage != null) {
            guardImage.remove();
        }
        if (sleepImage != null) {
            sleepImage.remove();
        }
        if (enemyCombatImage != null) {
            enemyCombatImage.setVisible(false);
            enemyCombatImage.remove();
        }
        if (enemySleepImage != null) {
            enemySleepImage.setVisible(false);
            enemySleepImage.remove();
        }
        if (enemyGuardImage != null) {
            enemyGuardImage.setVisible(false);
            enemyGuardImage.remove();
        }
        super.dispose();
    }
}
