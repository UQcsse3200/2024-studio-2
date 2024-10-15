package com.csse3200.game.components.combat.quicktimeevent;

import com.badlogic.gdx.utils.Timer;
import com.csse3200.game.components.combat.CombatManager;
import com.csse3200.game.overlays.CombatAnimationDisplay;
import com.csse3200.game.services.AudioManager;

public class CombatMoveAudio {

    /**
     * Plays the sound for combat based on the moves of the enemy and player
     */
    public void playCombatSound(CombatManager.Action playerMove, CombatManager.Action enemyMove) {
        // one of the entities is attacking
        if (playerMove == CombatManager.Action.ATTACK || enemyMove == CombatManager.Action.ATTACK) {
            // both entities are attacking
            if (playerMove == enemyMove) {
                attackHit();
                Timer.schedule(new Timer.Task() {
                    @Override
                    public void run() {
                        attackHit();
                    }
                }, CombatAnimationDisplay.getBothAttackAnimationDelay());
                return;
            }
            // one entity is attacking and one entity is guarding
            if (playerMove == CombatManager.Action.GUARD || enemyMove == CombatManager.Action.GUARD) {
                guard();
                attackBlock();
                return;
            }
            attackHit();
            if (playerMove == CombatManager.Action.SLEEP || enemyMove == CombatManager.Action.SLEEP) {
                sleep();
                return;
            }

        }
        switch (playerMove) {
            case CombatManager.Action.SLEEP -> sleep();
            case CombatManager.Action.GUARD -> guard();
            case CombatManager.Action.ATTACK -> attackHit();
        }
        Timer.schedule(new Timer.Task() {
            @Override
            public void run() {
                switch (enemyMove) {
                    case CombatManager.Action.SLEEP -> sleep();
                    case CombatManager.Action.GUARD -> guard();
                    case CombatManager.Action.ATTACK -> attackHit();
                }
            }
        }, CombatAnimationDisplay.getRockTravelTime());
    }

    /**
     * Plays the start of guard sound
     */
    private void guard() {
        AudioManager.playSound("sounds/combat/guard.wav");
    }

    /**
     * Plays the start of the attack sound and the hut sound if the enemy uses any moves except guard
     */
    private void attackHit() {
        AudioManager.playSound("sounds/combat/attack start.wav");
        Timer.schedule(new Timer.Task() {
            @Override
            public void run() {
                AudioManager.playSound("sounds/combat/attack hit.wav");
            }
        }, CombatAnimationDisplay.getRockTravelTime());
    }

    /**
     * Plays the start of the attack sound and the blocking of the attack if an entity uses guard
     */
    private void attackBlock() {
        AudioManager.playSound("sounds/combat/attack start.wav");
        Timer.schedule(new Timer.Task() {
            @Override
            public void run() {
                AudioManager.playSound("sounds/combat/attack blocked.wav");
            }
        }, CombatAnimationDisplay.getRockTravelTime());
    }

    private void sleep() {
        AudioManager.playSound("sounds/combat/sleep.wav");
    }
}
