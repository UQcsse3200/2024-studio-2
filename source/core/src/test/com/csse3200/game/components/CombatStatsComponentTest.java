package com.csse3200.game.components;

import com.csse3200.game.extensions.GameExtension;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertFalse;

@ExtendWith(GameExtension.class)
class CombatStatsComponentTest {
  @Test
  void shouldSetGetHealth() {
    int maxHealth = 100;
    CombatStatsComponent combat = new CombatStatsComponent(100, 100, 20, 0, 0, 0, false);
    assertEquals(100, combat.getHealth());

    combat.setHealth(150);
    assertEquals(100, combat.getHealth());

    combat.setHealth(-50);
    assertEquals(0, combat.getHealth());

    combat.setHealth(90);
    assertEquals(90, combat.getHealth());

    combat.setHealth(100);
    assertEquals(100, combat.getHealth());

    combat.setHealth(0);
    assertEquals(0, combat.getHealth());
  }

  @Test
  void shouldCheckIsDead() {
    CombatStatsComponent combat = new CombatStatsComponent(100, 100, 20, 0, 0, 0, false);
    assertFalse(combat.isDead());

    combat.setHealth(0);
    assertTrue(combat.isDead());
  }

  @Test
  void shouldAddHealth() {
    CombatStatsComponent combat = new CombatStatsComponent(100, 100, 20, 0, 0, 0, false);
    combat.addHealth(-500);
    assertEquals(0, combat.getHealth());

    combat.addHealth(100);
    assertEquals(100, combat.getHealth());
    combat.addHealth(-20);
    assertEquals(80, combat.getHealth());
  }

  @Test
  void shouldSetHungerWithinBounds() {
    CombatStatsComponent combat = new CombatStatsComponent(100, 100, 0, 0, 0, 0, false);
    combat.setHunger(150);
    assertEquals(100, combat.getHunger());

    combat.setHunger(-50);
    assertEquals(0, combat.getHunger());

    combat.setHunger(50);
    assertEquals(50, combat.getHunger());
  }

  @Test
  void shouldAddHungerWithCap() {
    CombatStatsComponent combat = new CombatStatsComponent(100, 100, 0, 0, 0, 0, false);
    combat.addHunger(60);
    assertEquals(100, combat.getHunger());

    combat.addHunger(-120);
    assertEquals(0, combat.getHunger());
  }

  // Strength Tests

  @Test
  void shouldSetStrength() {
    CombatStatsComponent combat = new CombatStatsComponent(100, 100, 20, 0, 0, 0, false);
    combat.setStrength(0);
    assertEquals(0, combat.getStrength());

    combat.setStrength(150);
    assertEquals(150, combat.getStrength());

    combat.setStrength(-50);
    assertEquals(0, combat.getStrength());
  }

  @Test
  void shouldAddStrength() {
    CombatStatsComponent combat = new CombatStatsComponent(100, 100, 20, 0, 0, 0, false);
    combat.addStrength(50);
    assertEquals(70, combat.getStrength());

    combat.addStrength(-100);
    assertEquals(0, combat.getStrength());
  }

  // Defense Tests

  @Test
  void shouldSetDefense() {
    CombatStatsComponent combat = new CombatStatsComponent(100, 100, 0, 20, 0, 0, false);
    combat.setDefense(0);
    assertEquals(0, combat.getDefense());

    combat.setDefense(150);
    assertEquals(150, combat.getDefense());

    combat.setDefense(-50);
    assertEquals(0, combat.getDefense());
  }

  @Test
  void shouldAddDefense() {
    CombatStatsComponent combat = new CombatStatsComponent(100, 100, 0, 20, 0, 0, false);
    combat.addDefense(50);
    assertEquals(70, combat.getDefense());

    combat.addDefense(-100);
    assertEquals(0, combat.getDefense());
  }

  // Speed Tests

  @Test
  void shouldSetSpeed() {
    CombatStatsComponent combat = new CombatStatsComponent(100, 100, 0, 0, 20, 0, false);
    combat.setSpeed(0);
    assertEquals(0, combat.getSpeed());

    combat.setSpeed(150);
    assertEquals(150, combat.getSpeed());

    combat.setSpeed(-50);
    assertEquals(0, combat.getSpeed());
  }

  @Test
  void shouldAddSpeed() {
    CombatStatsComponent combat = new CombatStatsComponent(100, 100, 0, 0, 20, 0, false);
    combat.addSpeed(50);
    assertEquals(70, combat.getSpeed());

    combat.addSpeed(-100);
    assertEquals(0, combat.getSpeed());
  }


  @Test
  void shouldSetExperienceAndHandleOverflow() {
    int maxExperience = 100;
    CombatStatsComponent combat = new CombatStatsComponent(100, 100, 0, 0, 0, 0, true);

    combat.setExperience(150);
    assertEquals(50, combat.getExperience());

    combat.setExperience(0);
    assertEquals(0, combat.getExperience());

    combat.setExperience(125);
    assertEquals(0, combat.getExperience());

    combat.setExperience(-50);
    assertEquals(0, combat.getExperience());
  }

  @Test
  void shouldAddExperienceWithOverflow() {
    CombatStatsComponent combat = new CombatStatsComponent(100, 100, 0, 0, 0, 20, true);
    combat.addExperience(100);
    assertEquals(20, combat.getExperience());

    combat.addExperience(-100);
    assertEquals(0, combat.getExperience());
  }

  @Test
  void shouldLevelUpAndIncreaseStats() {
    int initialHealth = 100;
    int initialStrength = 50;
    int initialDefense = 30;
    int initialSpeed = 20;
    int initialExperience = 95;
    int maxExperience = 100;

    CombatStatsComponent combat = new CombatStatsComponent(initialHealth, 100, initialStrength, initialDefense, initialSpeed, initialExperience, true);

    combat.addExperience(10);
    assertEquals(5, combat.getExperience());

    assertEquals((int) Math.ceil(maxExperience * 1.25), combat.getMaxExperience());
    assertEquals((int) Math.ceil(initialHealth * 1.02), combat.getMaxHealth());
    assertEquals((int) Math.ceil(initialStrength * 1.02), combat.getStrength());
    assertEquals((int) Math.ceil(initialDefense * 1.02), combat.getDefense());
    assertEquals((int) Math.ceil(initialSpeed * 1.02), combat.getSpeed());
  }

  @Test
  void shouldLevelUpAndIncreaseStats2() {
    int initialHealth = 100;
    int initialStrength = 50;
    int initialDefense = 30;
    int initialSpeed = 20;
    int initialExperience = 95;
    int maxExperience = 100;

    CombatStatsComponent combat = new CombatStatsComponent(initialHealth, 100, initialStrength, initialDefense, initialSpeed, initialExperience, true);

    combat.setExperience(105);
    assertEquals(5, combat.getExperience());

    assertEquals((int) Math.ceil(maxExperience * 1.25), combat.getMaxExperience());
    assertEquals((int) Math.ceil(initialHealth * 1.02), combat.getMaxHealth());
    assertEquals((int) Math.ceil(initialStrength * 1.02), combat.getStrength());
    assertEquals((int) Math.ceil(initialDefense * 1.02), combat.getDefense());
    assertEquals((int) Math.ceil(initialSpeed * 1.02), combat.getSpeed());
  }

  @Test
  void shouldNotLevelUpAndIncreaseStats() {
    int initialHealth = 100;
    int initialStrength = 50;
    int initialDefense = 30;
    int initialSpeed = 20;
    int initialExperience = 95;
    int maxHealth = 100;
    int maxExperience = 100;

    CombatStatsComponent combat = new CombatStatsComponent(initialHealth, 100, initialStrength, initialDefense, initialSpeed, initialExperience, false);

    combat.addExperience(10);
    assertEquals(maxExperience, combat.getExperience());

    assertEquals(100, combat.getMaxExperience());
    assertEquals(100, combat.getMaxHealth());
    assertEquals(50, combat.getStrength());
    assertEquals(30, combat.getDefense());
    assertEquals(20, combat.getSpeed());
  }


}
