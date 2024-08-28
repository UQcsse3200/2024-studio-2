package com.csse3200.game.components.player;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.csse3200.game.entities.Entity;



public class PlayerStatsDisplayTester extends PlayerStatsDisplay {
    static boolean delayedActionDone=false;
 
    public static void testCreate(boolean addActorsTester, Entity entity)
    {
        System.out.println("Starting test for create() method...");
        if (addActorsTester) {
         System.out.println("Character stats have been added successfully");

         //check working of entity
         if (entity.getEvents().hasListener("updateHealth"))
          System.out.println("Listener for updateHealth successfully registered.");
         else System.out.println("Listener for updateHealth not registered.");

         if (entity.getEvents().hasListener("updateExperience"))
          System.out.println("Listener for updateExperience successfully registered.");
         else System.out.println("Listener for updateExperience not registered.");

         if (entity.getEvents().hasListener("updateHunger"))
          System.out.println("Listener for updateHunger successfully registered.");
         else System.out.println("Listener for updateHunger not registered.");
        }
        else System.out.println("Characters stats could not be added");
    }
 
    public static void testInitBarAnimation(TextureAtlas[]textureAtlas,
                                         Animation<TextureRegion> healthBarAnimation,
                                         Animation<TextureRegion> hungerBarAnimation,
                                         Animation<TextureRegion> xpBarAnimation)
    {
        System.out.println("Starting test for initBarAnimations() method...");
        // Check if textureAtlas array is initialized
        if (textureAtlas != null && textureAtlas.length == 3) {
         System.out.println("TextureAtlas array initialized correctly.");
        } else {
         System.out.println("Error: TextureAtlas array not initialized correctly.");
        }

        // Check if healthBarAnimation, hungerBarAnimation, and xpBarAnimation are not null
        if ( healthBarAnimation != null &&  hungerBarAnimation != null &&  xpBarAnimation != null) {
         System.out.println("Animations initialized correctly.");
        } else {
         System.out.println("Error: Animations not initialized correctly.");
        }

        // Check the number of frames in each animation
           assert healthBarAnimation != null;
           if ( healthBarAnimation.getKeyFrames().length == 11) {
         System.out.println("HealthBar animation frames initialized correctly.");
        } else {
         System.out.println("Error: HealthBar animation frames not initialized correctly.");
        }

           assert hungerBarAnimation != null;
           if ( hungerBarAnimation.getKeyFrames().length == 11) {
         System.out.println("HungerBar animation frames initialized correctly.");
        } else {
         System.out.println("Error: HungerBar animation frames not initialized correctly.");
        }

           assert xpBarAnimation != null;
           if ( xpBarAnimation.getKeyFrames().length == 11) {
         System.out.println("XpBar animation frames initialized correctly.");
        } else {
         System.out.println("Error: XpBar animation frames not initialized correctly.");
        }

    }

    public static void testUpdatePlayerStatsUI(int maxStats, int stat, String statName) {
     System.out.println("Starting test for updatePlayerHungerUI() method...");
     if (maxStats>0)
         System.out.println("Max "+ statName+ " is correct:" +maxStats);

     else
         System.out.println("Max "+ statName+ " is incorrect:"  +maxStats);


     if (stat >= 0 )
        System.out.println("current stats for "+ statName+ " is correct : "+stat);

     else
         System.out.println("current stats for "+ statName+ " is incorrect : "+stat);
    }






    }








