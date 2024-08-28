package com.csse3200.game.components.player;

import com.csse3200.game.entities.Entity;

public class PlayerStatsDisplayTester {
 public static void testCreate(boolean addActorsTester, Entity entity) {
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
}





