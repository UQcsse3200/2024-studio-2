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
    CombatStatsComponent combat = new CombatStatsComponent(100, 100, 20, 0, 0, 0, 0, true);
    assertEquals(100, combat.getHealth());

    combat.setHealth(150);
    assertEquals(150, combat.getHealth());

    combat.setHealth(-50);
    assertEquals(0, combat.getHealth());
  }

  @Test
  void shouldCheckIsDead() {
    CombatStatsComponent combat = new CombatStatsComponent(100, 100, 20, 0, 0, 0, 0, true);
    assertFalse(combat.isDead());

    combat.setHealth(0);
    assertTrue(combat.isDead());
  }

  @Test
  void shouldAddHealth() {
    CombatStatsComponent combat = new CombatStatsComponent(100, 100, 20, 0, 0, 0, 0, true);
    combat.addHealth(-500);
    assertEquals(0, combat.getHealth());

    combat.addHealth(100);
    combat.addHealth(-20);
    assertEquals(80, combat.getHealth());
  }

  @Test
  void shouldSetGetstrength() {
    CombatStatsComponent combat = new CombatStatsComponent(100, 100, 20, 0, 0, 0, 0, true);
    assertEquals(20, combat.getStrength());

    combat.setStrength(150);
    assertEquals(150, combat.getStrength());

    combat.setStrength(-50);
    assertEquals(150, combat.getStrength());
  }

  @Test
  void shouldAddHunger() {
    CombatStatsComponent combat = new CombatStatsComponent(100, 50, 20, 0, 0, 0);
    combat.addHunger(-500);
    assertEquals(0, combat.getHunger());
    combat.addHunger(10);
    assertEquals(10, combat.getHunger());
  }
}
