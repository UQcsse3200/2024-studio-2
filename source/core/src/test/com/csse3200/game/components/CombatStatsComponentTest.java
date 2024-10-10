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
    CombatStatsComponent combat = new CombatStatsComponent(100, 100, 20, 0, 0, 0, 0, true, false, 1);

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
    CombatStatsComponent combat = new CombatStatsComponent(100, 100, 20, 0, 0, 0, 0, true, false, 1);
    assertFalse(combat.isDead());

    combat.setHealth(0);
    assertTrue(combat.isDead());
  }

  @Test
  void shouldAddHealth() {
    CombatStatsComponent combat = new CombatStatsComponent(100, 100, 20, 0, 0, 0, 0, true, false, 1);
    combat.addHealth(-500);
    assertEquals(0, combat.getHealth());

    combat.addHealth(100);
    assertEquals(100, combat.getHealth());
    combat.addHealth(-20);
    assertEquals(80, combat.getHealth());
  }

  @Test
  void shouldSetHungerWithinBounds() {
    CombatStatsComponent combat = new CombatStatsComponent(100, 100, 0, 0, 0, 0, 0, false, false, 1);
    combat.setHunger(150);
    assertEquals(100, combat.getHunger());

    combat.setHunger(-50);
    assertEquals(0, combat.getHunger());

    combat.setHunger(50);
    assertEquals(50, combat.getHunger());
  }

  @Test
  void shouldAddHungerWithCap() {
    CombatStatsComponent combat = new CombatStatsComponent(100, 100, 0, 0, 0, 0, 0, false, false, 1);
    combat.addHunger(60);
    assertEquals(100, combat.getHunger());

    combat.addHunger(-120);
    assertEquals(0, combat.getHunger());
  }

  // Strength Tests

  @Test
  void shouldSetStrength() {
    CombatStatsComponent combat = new CombatStatsComponent(100, 100, 20, 0, 0, 0, 0, true, false, 1);
    combat.setStrength(0);
    assertEquals(0, combat.getStrength());

    combat.setStrength(150);
    assertEquals(150, combat.getStrength());

    combat.setStrength(-50);
    assertEquals(0, combat.getStrength());
  }

  @Test
  void shouldAddStrength() {
    CombatStatsComponent combat = new CombatStatsComponent(100, 100, 20, 0, 0, 0, 0, false, false, 1);
    combat.addStrength(50);
    assertEquals(70, combat.getStrength());

    combat.addStrength(-100);
    assertEquals(0, combat.getStrength());
  }

  // Defense Tests

  @Test
  void shouldSetDefense() {
    CombatStatsComponent combat = new CombatStatsComponent(100, 100, 0, 20, 0, 0, 0, false, false, 1);
    combat.setDefense(0);
    assertEquals(0, combat.getDefense());

    combat.setDefense(150);
    assertEquals(150, combat.getDefense());

    combat.setDefense(-50);
    assertEquals(0, combat.getDefense());
  }

  @Test
  void shouldAddDefense() {
    CombatStatsComponent combat = new CombatStatsComponent(100, 100, 0, 20, 0, 0, 0, false, false, 1);
    combat.addDefense(50);
    assertEquals(70, combat.getDefense());

    combat.addDefense(-100);
    assertEquals(0, combat.getDefense());
  }

  // Speed Tests

  @Test
  void shouldSetSpeed() {
    CombatStatsComponent combat = new CombatStatsComponent(100, 100, 0, 0, 20, 0, 0, false, false, 1);
    combat.setSpeed(0);
    assertEquals(0, combat.getSpeed());

    combat.setSpeed(150);
    assertEquals(150, combat.getSpeed());

    combat.setSpeed(-50);
    assertEquals(0, combat.getSpeed());
  }

  @Test
  void shouldAddSpeed() {
    CombatStatsComponent combat = new CombatStatsComponent(100, 100, 0, 0, 20, 0, 0, false, false, 1);
    combat.addSpeed(50);
    assertEquals(70, combat.getSpeed());

    combat.addSpeed(-100);
    assertEquals(0, combat.getSpeed());
  }


  @Test
  void shouldSetExperienceAndHandleOverflow() {
    CombatStatsComponent combat = new CombatStatsComponent(100, 100, 0, 0, 0, 0, 0, true, false, 1);

    combat.setExperience(150);
    assertEquals(65, combat.getExperience());

    combat.setExperience(0);
    assertEquals(0, combat.getExperience());

    combat.setExperience(125);
    assertEquals(25, combat.getExperience());

    combat.setExperience(-50);
    assertEquals(0, combat.getExperience());
  }

  @Test
  void shouldAddExperienceWithOverflow() {
    CombatStatsComponent combat = new CombatStatsComponent(100, 100, 0, 0, 0, 0, 0, true, false, 1);
    combat.addExperience(100);
    assertEquals(15, combat.getExperience());

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
    int level = 1;
    int maxExperience = (int) Math.ceil(71.7125 * Math.pow(Math.E, 0.191529 * level) + 13.1489);


    CombatStatsComponent combat = new CombatStatsComponent(initialHealth, 100, initialStrength, initialDefense, initialSpeed, initialExperience, 100, true, false, 1);

    combat.addExperience(10);
    assertEquals(20, combat.getExperience());

    assertEquals(maxExperience, combat.getMaxExperience());
    assertEquals(initialHealth + 1, combat.getMaxHealth());
    assertEquals(initialStrength + 1, combat.getStrength());
    assertEquals(initialDefense + 1, combat.getDefense());
    assertEquals(initialSpeed + 1, combat.getSpeed());
  }

  @Test
  void shouldLevelUpAndIncreaseStats2() {
    int initialHealth = 100;
    int initialStrength = 50;
    int initialDefense = 30;
    int initialSpeed = 20;
    int initialExperience = 65;
    int level = 1;
    int maxExperience = (int) Math.ceil(71.7125 * Math.pow(Math.E, 0.191529 * level) + 13.1489);

    CombatStatsComponent combat = new CombatStatsComponent(initialHealth, 100, initialStrength, initialDefense, initialSpeed, initialExperience, 100, true, false, 1);

    combat.setExperience(105);
    assertEquals(20, combat.getExperience());

    assertEquals(maxExperience, combat.getMaxExperience());
    assertEquals(initialHealth + 1, combat.getMaxHealth());
    assertEquals(initialStrength + 1, combat.getStrength());
    assertEquals(initialDefense + 1, combat.getDefense());
    assertEquals(initialSpeed + 1, combat.getSpeed());
  }

  @Test
  void shouldNotLevelUpAndIncreaseStats() {
    int initialHealth = 100;
    int initialStrength = 50;
    int initialDefense = 30;
    int initialSpeed = 20;
    int initialExperience = 65;
    int maxHealth = 100;
    int level = 1;

    CombatStatsComponent combat = new CombatStatsComponent(initialHealth, 100, initialStrength,
            initialDefense, initialSpeed, initialExperience, 100, false, false, level);

    combat.addExperience(10);
    assertEquals(75, combat.getExperience());

    assertEquals(85, combat.getMaxExperience());
    assertEquals(maxHealth, combat.getMaxHealth());
    assertEquals(50, combat.getStrength());
    assertEquals(30, combat.getDefense());
    assertEquals(20, combat.getSpeed());
  }

  @Test
  void testSimpleLevelUp() {

    CombatStatsComponent combatStats = new CombatStatsComponent(100, 100, 0, 0, 0, 0, 100, true, false, 1);
    combatStats.setLevel(0);
    assertEquals(0, combatStats.getLevel());

    combatStats.addLevel(5);
    assertEquals(5, combatStats.getLevel());

    combatStats.setLevel(1);
    assertEquals(1, combatStats.getLevel());

    combatStats.addLevel(5);
    assertEquals(6, combatStats.getLevel());
  }

  @Test
  void testMaxLevel() {
    int maxLevel = 10;

    CombatStatsComponent combatStats = new CombatStatsComponent(100, 100, 0, 0, 0, 0, 100, true, false, 1);

    combatStats.setLevel(maxLevel);
    assertEquals(maxLevel, combatStats.getLevel());

    combatStats.addLevel(1);
    assertEquals(maxLevel, combatStats.getLevel());  // Cannot exceed max level
  }

  @Test
  void testAddXpLevelProgression() {
    CombatStatsComponent combatStats = new CombatStatsComponent(100, 100, 0, 0, 0, 0, 100, true, false, 1);
    combatStats.setLevel(0);
    assertEquals(0, combatStats.getLevel());

    combatStats.addExperience(100);
    assertEquals(1, combatStats.getLevel());

    combatStats.addLevel(2);
    assertEquals(3, combatStats.getLevel());

    combatStats.addExperience(190);
    assertEquals(5, combatStats.getLevel());
  }

  @Test
  void testMaxLevelWithXp() {
    int maxLevel = 10;
    CombatStatsComponent combatStats = new CombatStatsComponent(100, 100, 0, 0, 0, 0, 100, true, false, 1);

    combatStats.setLevel(0);
    assertEquals(0, combatStats.getLevel());

    combatStats.addLevel(maxLevel);
    assertEquals(maxLevel, combatStats.getLevel());

    combatStats.addExperience(1000);
    assertEquals(maxLevel, combatStats.getLevel());
  }

  @Test
  void testNegativeXpAndLevel() {
    CombatStatsComponent combatStats = new CombatStatsComponent(100, 100, 0, 0, 0, 0, 100, true, false, 0);
    combatStats.setLevel(1);
    assertEquals(1, combatStats.getLevel());

    combatStats.addExperience(-100);
    assertEquals(1, combatStats.getLevel());

    combatStats.addLevel(-5);
    assertEquals(0, combatStats.getLevel());
  }

  @Test
  void testSetLevelLowerThanCurrent() {
    CombatStatsComponent combatStats = new CombatStatsComponent(100, 100, 0, 0, 0, 0, 100, true, false, 0);
    combatStats.setLevel(1);
    assertEquals(1, combatStats.getLevel());

    combatStats.setLevel(5);
    assertEquals(5, combatStats.getLevel());

    combatStats.setLevel(0);
    assertEquals(0, combatStats.getLevel());
  }

  @Test
  void testMaxXPAndLevel() {
    CombatStatsComponent combatStats = new CombatStatsComponent(100, 100, 0, 0, 0, 0, 100, true, false, 0);

    assertEquals(0, combatStats.getLevel());

    combatStats.addExperience(9999);
    assertEquals(10, combatStats.getLevel());
  }
}
