package com.csse3200.game.components.combat.quicktimeevent;

import com.badlogic.gdx.utils.Timer;
import com.csse3200.game.components.combat.CombatManager;
import com.csse3200.game.services.AudioManager;

public class CombatMoveAudio {

    public void playSound(CombatManager.Action playerMove, CombatManager.Action enemyMove) {
        switch (playerMove) {
            case CombatManager.Action.ATTACK -> {
                switch (enemyMove) {
                    case CombatManager.Action.GUARD -> attackBlock();
                    default -> attackHit();
                }
            }
            case CombatManager.Action.SLEEP -> sleep();
            case CombatManager.Action.GUARD -> guard();
        }
    }

    private void guard() {
        AudioManager.playSound("sounds/combat/guard.wav");
    }
    private void attackHit() {
        AudioManager.playSound("sounds/combat/attack start.wav");
        Timer.schedule(new Timer.Task() {
            @Override
            public void run() {
                AudioManager.playSound("sounds/combat/attack hit.wav");
            }
        }, 0.8f);
    }

    private void attackBlock() {
        AudioManager.playSound("sounds/combat/attack start.wav");
        Timer.schedule(new Timer.Task() {
            @Override
            public void run() {
                AudioManager.playSound("sounds/combat/attack blocked.wav");
            }
        }, 0.8f);
    }

    private void sleep() {
        AudioManager.playSound("sounds/combat/sleep.wav");
    }
}
