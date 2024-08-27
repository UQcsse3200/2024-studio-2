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
    CombatStatsComponent combat = new CombatStatsComponent(100, 100, 20, 0, 0, 0);
    assertEquals(100, combat.getHealth());

    combat.setHealth(150);
    assertEquals(150, combat.getHealth());

    combat.setHealth(-50);
    assertEquals(0, combat.getHealth());
  }

  @Test
  void shouldCheckIsDead() {
    CombatStatsComponent combat = new CombatStatsComponent(100, 100, 20, 0, 0, 0);
    assertFalse(combat.isDead());

    combat.setHealth(0);
    assertTrue(combat.isDead());
  }

  @Test
  void shouldAddHealth() {
    CombatStatsComponent combat = new CombatStatsComponent(100, 100, 20, 0, 0, 0);
    combat.addHealth(-500);
    assertEquals(0, combat.getHealth());

    combat.addHealth(100);
    combat.addHealth(-20);
    assertEquals(80, combat.getHealth());
  }

  @Test
  void shouldSetGetHunger() {
    CombatStatsComponent combat = new CombatStatsComponent(100, 100, 20, 0, 0, 0);
    assertEquals(100, combat.getHunger());

    combat.setHunger(150);
    assertEquals(150, combat.getHunger());

    combat.setHunger(-50);
    assertEquals(0, combat.getHunger());
  }

@Test
void shouldSetGetExperience() {
  CombatStatsComponent combat = new CombatStatsComponent(100, 100, 20, 0, 0, 100);
  assertEquals(100, combat.getExperience());

  combat.setExperience(150);
  assertEquals(150, combat.getExperience());


}
}
