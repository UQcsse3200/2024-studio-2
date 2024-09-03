//package com.csse3200.game.components.player;
//
//import com.badlogic.gdx.graphics.g2d.Animation;
//import com.badlogic.gdx.graphics.g2d.TextureAtlas;
//import com.badlogic.gdx.graphics.g2d.TextureRegion;
//import com.csse3200.game.entities.Entity;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//
//
///**
// * A tester class for the PlayerStatsDisplay component that tests various methods
// * for displaying player stats, such as creating actors, initializing
// * animations, and updating player stats.
// */
//public class PlayerStatsDisplayTester extends PlayerStatsDisplay {
//    static boolean delayedActionDone=false;
//    private static final Logger logger = LoggerFactory.getLogger(PlayerStatsDisplay.class);
//    /**
//     * Tests the create() method by checking if the actors were successfully added
//     * and if the event listeners were correctly registered for
//     * player stats (health, experience, hunger) were registered.
//     * @param addActorsTester a boolean indicating whether the actors were added successfully
//     * @param entity          the player entity whose events are being checked for listeners
//     */
//    public static void testCreate(boolean addActorsTester, Entity entity)
//    {
//        logger.info("Starting test for create() method...");
//        if (addActorsTester) {
//         logger.info("Character stats have been added successfully");
//
//         //check working of entity
//         if (entity.getEvents().hasListener("updateHealth"))
//          logger.info("Listener for updateHealth successfully registered.");
//         else logger.info("Listener for updateHealth not registered.");
//
//         if (entity.getEvents().hasListener("updateExperience"))
//          logger.info("Listener for updateExperience successfully registered.");
//         else logger.info("Listener for updateExperience not registered.");
//
//         if (entity.getEvents().hasListener("updateHunger"))
//          logger.info("Listener for updateHunger successfully registered.");
//         else logger.info("Listener for updateHunger not registered.");
//        }
//        else logger.info("Characters stats could not be added");
//    }
//
//    /**
//     * Tests the initBarAnimations() method by checking the initialization of the texture atlas
//     * and frames for health, hunger, and experience bar animations.
//     * @param textureAtlas         an array of TextureAtlas objects used for animations
//     * @param healthBarAnimation   the animation for the health bar
//     * @param hungerBarAnimation   the animation for the hunger bar
//     * @param xpBarAnimation       the animation for the experience bar
//     */
//    public static void testInitBarAnimation(TextureAtlas[]textureAtlas,
//                                         Animation<TextureRegion> healthBarAnimation,
//                                         Animation<TextureRegion> hungerBarAnimation,
//                                         Animation<TextureRegion> xpBarAnimation)
//    {
//        logger.info("Starting test for initBarAnimations() method...");
//        // Check if textureAtlas array is initialized
//        if (textureAtlas != null && textureAtlas.length == 3) {
//         logger.info("TextureAtlas array initialized correctly.");
//        } else {
//         logger.info("Error: TextureAtlas array not initialized correctly.");
//        }
//
//        // Check if healthBarAnimation, hungerBarAnimation, and xpBarAnimation are not null
//        if ( healthBarAnimation != null &&  hungerBarAnimation != null &&  xpBarAnimation != null) {
//         logger.info("Animations initialized correctly.");
//        } else {
//         logger.info("Error: Animations not initialized correctly.");
//        }
//
//        // Check the number of frames in each animation
//           assert healthBarAnimation != null;
//           if ( healthBarAnimation.getKeyFrames().length == 11) {
//         logger.info("HealthBar animation frames initialized correctly.");
//        } else {
//         logger.info("Error: HealthBar animation frames not initialized correctly.");
//        }
//
//           assert hungerBarAnimation != null;
//           if ( hungerBarAnimation.getKeyFrames().length == 11) {
//         logger.info("HungerBar animation frames initialized correctly.");
//        } else {
//         logger.info("Error: HungerBar animation frames not initialized correctly.");
//        }
//
//           assert xpBarAnimation != null;
//           if ( xpBarAnimation.getKeyFrames().length == 11) {
//         logger.info("XpBar animation frames initialized correctly.");
//        } else {
//         logger.info("Error: XpBar animation frames not initialized correctly.");
//        }
//
//    }
//    /**
//     * Tests the updatePlayerStatsUI() method by checking if the max stats and current stats
//     * for a given stat name are within the correct ranges. Shows error if not correct ranges
//     * @param maxStats the maximum value for the stat
//     * @param stat     the current value for the stat
//     * @param statName the name of the stat being checked (e.g., health, hunger, experience)
//     */
//    public static void testUpdatePlayerStatsUI(int maxStats, int stat, String statName) {
//     logger.info("Starting test for updatePlayerHungerUI() method...");
//     if (maxStats>0)
//         logger.info("Max "+ statName+ " is correct:" +maxStats);
//
//     else
//         logger.info("Max "+ statName+ " is incorrect:"  +maxStats);
//
//
//     if (stat >= 0 )
//        logger.info("current stats for "+ statName+ " is correct : "+stat);
//
//     else
//         logger.info("current stats for "+ statName+ " is incorrect : "+stat);
//    }
//}
//
//
//
//
//
//
//
//
