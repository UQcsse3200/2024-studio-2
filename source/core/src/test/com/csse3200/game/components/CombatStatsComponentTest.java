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
    CombatStatsComponent combat = new CombatStatsComponent(100, 100, 20, 0, 0, 0, false);
    assertEquals(100, combat.getHealth());

    combat.setHealth(150);
    assertEquals(150, combat.getHealth());

    combat.setHealth(-50);
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
    combat.addHealth(-20);
    assertEquals(80, combat.getHealth());
  }

  @Test
  void shouldSetGetstrength() {
    CombatStatsComponent combat = new CombatStatsComponent(100, 100, 20, 0, 0, 0, false);
    assertEquals(20, combat.getStrength());

    combat.setStrength(150);
    assertEquals(150, combat.getStrength());

    combat.setStrength(-50);
    assertEquals(150, combat.getStrength());

    combat.addStrength(50);
    assertEquals(200, combat.getStrength());

    combat.addStrength(-100);
    assertEquals(100, combat.getStrength());
  }

  @Test
  void shouldSetGetdefense() {
    CombatStatsComponent combat = new CombatStatsComponent(100, 100, 0, 20, 0, 0, false);
    assertEquals(20, combat.getDefense());

    combat.setDefense(150);
    assertEquals(150, combat.getDefense());

    combat.setDefense(-50);
    assertEquals(150, combat.getDefense());

    combat.addDefense(50);
    assertEquals(200, combat.getDefense());

    combat.addDefense(-100);
    assertEquals(100, combat.getDefense());
  }

  @Test
  void shouldSetGetspeed() {
    CombatStatsComponent combat = new CombatStatsComponent(100, 100, 0, 0, 20, 0, false);
    assertEquals(20, combat.getSpeed());

    combat.setSpeed(150);
    assertEquals(150, combat.getSpeed());

    combat.setSpeed(-50);
    assertEquals(150, combat.getSpeed());

    combat.addSpeed(50);
    assertEquals(200, combat.getSpeed());

    combat.addSpeed(-100);
    assertEquals(100, combat.getSpeed());
  }

  @Test
  void shouldSetGetexperience() {
    CombatStatsComponent combat = new CombatStatsComponent(100, 100, 0, 0, 0, 20, false);
    assertEquals(20, combat.getExperience());

    combat.setExperience(150);
    assertEquals(150, combat.getExperience());

    combat.setExperience(-50);
    assertEquals(150, combat.getExperience());

    combat.addExperience(50);
    assertEquals(200, combat.getExperience());

    combat.addExperience(-100);
    assertEquals(100, combat.getExperience());
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

}
