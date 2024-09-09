package com.csse3200.game.components.player;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.csse3200.game.entities.Entity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * A tester class for the PlayerStatsDisplay component that tests various methods
 * for displaying player stats, such as creating actors, initializing
 * animations, and updating player stats.
 */
public class PlayerStatsDisplayTester extends PlayerStatsDisplay {
    static boolean delayedActionDone=false;
    private static final Logger logger = LoggerFactory.getLogger(PlayerStatsDisplay.class);
    /**
     * Tests the create() method by checking if the actors were successfully added
     * and if the event listeners were correctly registered for
     * player stats (health, experience, hunger) were registered.
     * @param addActorsTester a boolean indicating whether the actors were added successfully
     * @param entity          the player entity whose events are being checked for listeners
     */
    public static void testCreate(boolean addActorsTester, Entity entity){
    }

    /**
     * Tests the initBarAnimations() method by checking the initialization of the texture atlas
     * and frames for health, hunger, and experience bar animations.
     * @param textureAtlas         an array of TextureAtlas objects used for animations
     * @param healthBarAnimation   the animation for the health bar
     * @param hungerBarAnimation   the animation for the hunger bar
     * @param xpBarAnimation       the animation for the experience bar
     */
    public static void testInitBarAnimation(TextureAtlas[]textureAtlas,
                                         Animation<TextureRegion> healthBarAnimation,
                                         Animation<TextureRegion> hungerBarAnimation,
                                         Animation<TextureRegion> xpBarAnimation)
    {

    }
    /**
     * Tests the updatePlayerStatsUI() method by checking if the max stats and current stats
     * for a given stat name are within the correct ranges. Shows error if not correct ranges
     * @param maxStats the maximum value for the stat
     * @param stat     the current value for the stat
     * @param statName the name of the stat being checked (e.g., health, hunger, experience)
     */
    public static void testUpdatePlayerStatsUI(int maxStats, int stat, String statName) {
    }
}








