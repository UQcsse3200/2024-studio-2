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
        // neither player nor enemy attacks
        if (playerMove != CombatManager.Action.ATTACK && enemyMove != CombatManager.Action.ATTACK) {
            // play sound for player followed by a short delay than enemy sound
            switch (playerMove) {
                case CombatManager.Action.SLEEP -> sleep();
                case CombatManager.Action.GUARD -> raiseGuard();
            }
            Timer.schedule(new Timer.Task() {
                @Override
                public void run() {
                    switch (enemyMove) {
                        case CombatManager.Action.SLEEP -> sleep();
                        case CombatManager.Action.GUARD -> raiseGuard();
                    }
                }
            }, CombatAnimationDisplay.getRockTravelTime());
            return;
        }
        // code below here means one or more of the entities attacked

        // one entity is attacking and one entity is guarding
        if (playerMove == CombatManager.Action.GUARD || enemyMove == CombatManager.Action.GUARD) {
            attackBlock();
            return;
        }

        // play attack sound as one the entities attack an unguarded player
        attackHit();
        // both attacking
        if (playerMove == enemyMove) {
            Timer.schedule(new Timer.Task() {
                @Override
                public void run() {
                    attackHit();
                }
            }, CombatAnimationDisplay.getBothAttackAnimationDelay());
            return;
        }
        // one of the entities attacked and the other slept
        sleep();
    }

    /**
     * Plays the start of guard sound
     */
    private void raiseGuard() {
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
     * Plays the start of the attack sound and the guarding sound and then attack hitting the guard
     */
    private void attackBlock() {
        AudioManager.playSound("sounds/combat/attack start.wav");
        raiseGuard();
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
