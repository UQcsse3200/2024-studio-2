package com.csse3200.game.components.combat.move;

import com.csse3200.game.components.CombatStatsComponent;
import com.csse3200.game.extensions.GameExtension;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(GameExtension.class)
public class AttackMoveTest {

    private AttackMove attackMove;
    private CombatStatsComponent attackerStats;
    private CombatStatsComponent targetStats;

    @BeforeEach
    void setUp() {
        attackMove = new AttackMove("Basic Attack", 10);

        // Mock attacker and target stats for testing
        attackerStats = Mockito.mock(CombatStatsComponent.class);
        targetStats = Mockito.mock(CombatStatsComponent.class);
    }

    @Test
    void testExecuteWithoutTarget() {
        // Test the execute() method without target stats, expecting an error log.
        attackMove.execute(attackerStats);
        verify(attackerStats, never()).getStrength();  // Strength should not be accessed as no target is passed.
    }

    @Test
    void testExecuteWithTarget() {
        // Mock stats
        when(attackerStats.getStrength()).thenReturn(20);
        when(targetStats.getDefense()).thenReturn(10);
        when(attackerStats.getStamina()).thenReturn(100);
        when(targetStats.getHealth()).thenReturn(50);

        // Execute attack and verify health reduction
        attackMove.execute(attackerStats, targetStats);
        verify(targetStats).setHealth(anyInt());
    }

    @Test
    void testExecuteWithGuardedTarget() {
        // Mock stats for guarded target
        when(attackerStats.getStrength()).thenReturn(20);
        when(targetStats.getDefense()).thenReturn(10);
        when(attackerStats.getStamina()).thenReturn(100);
        when(targetStats.getHealth()).thenReturn(50);

        // Execute attack with guarded target
        attackMove.execute(attackerStats, targetStats, true);

        // Verify health reduction is less due to guarding
        verify(targetStats).setHealth(anyInt());
    }

    @Test
    void testExecuteWithNullAttackerOrTarget() {
        // Test null attacker or target
        attackMove.execute(null, targetStats);
        verify(targetStats, never()).setHealth(anyInt());

        attackMove.execute(attackerStats, null);
        verify(attackerStats, never()).addStamina(anyInt());
    }

    @Test
    void testStaminaReductionAfterAttack() {
        // Mock attacker stats
        when(attackerStats.getStrength()).thenReturn(20);
        when(targetStats.getDefense()).thenReturn(10);
        when(attackerStats.getStamina()).thenReturn(100);

        // Execute attack
        attackMove.execute(attackerStats, targetStats);

        // Verify stamina is reduced by the stamina cost
        verify(attackerStats).addStamina(-10);  // -10 is the stamina cost
    }
}
