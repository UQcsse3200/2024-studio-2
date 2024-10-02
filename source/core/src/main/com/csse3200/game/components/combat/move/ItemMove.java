package com.csse3200.game.components.combat.move;

import com.csse3200.game.components.CombatStatsComponent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * The ItemMove class represents a increase stat move where the entity takes a moment to increase or restore one of
 * their stats (health, stamina, defense, strength)
 */
public class ItemMove extends CombatMove {
    private static final Logger logger = LoggerFactory.getLogger(ItemMove.class);

    /**
     * Constructor to initialize the ItemMove with its name and stamina cost.
     *
     * @param moveName the name of the item move.
     * @param staminaCost the stamina cost required to execute the item move.
     */
    public ItemMove(String moveName, int staminaCost) {
        super(moveName, staminaCost);
    }

    /**
     * Executes the items move, increasing stamina.
     *
     * @param attackerStats the combat stats of the entity performing the item move.
     */
    @Override
    public void execute(CombatStatsComponent attackerStats) {
        if (attackerStats != null) {
            logger.info("{} item increasing {} stamina.",
                    attackerStats.isPlayer() ? "PLAYER" : "ENEMY",
                    this.getStaminaCost());

            attackerStats.addStamina(-(this.getStaminaCost()));
        } else {
            logger.error("Entity does not have CombatStatsComponent.");
        }
    }

    /**
     * Executes the item move, which simply consumes stamina. The target is ignored in this case.
     *
     * @param attackerStats the combat stats of the entity performing the move.
     * @param targetStats   the combat stats of the target (ignored for item moves).
     */
    @Override
    public void execute(CombatStatsComponent attackerStats, CombatStatsComponent targetStats) {
        execute(attackerStats);
    }

    /**
     * Executes the item move, either consuming or increasing stamina. The target's guarding status is ignored in this
     * context.
     *
     * @param attackerStats   the combat stats of the entity performing the move.
     * @param targetStats     the combat stats of the target (ignored for item moves).
     * @param targetIsGuarded whether the target is guarding (ignored for item moves).
     */
    @Override
    public void execute(CombatStatsComponent attackerStats, CombatStatsComponent targetStats, boolean targetIsGuarded) {
        execute(attackerStats);
    }

    /**
     * Executes the item move, consuming stamina. The number of hits landed is ignored for item moves.
     *
     * @param attackerStats   the combat stats of the entity performing the move.
     * @param targetStats     the combat stats of the target (ignored for item moves).
     * @param targetIsGuarded whether the target is guarding (ignored for item moves).
     * @param numHitsLanded   the number of hits landed (ignored for guard moves).
     */
    @Override
    public void execute(CombatStatsComponent attackerStats, CombatStatsComponent targetStats, boolean targetIsGuarded,
                        int numHitsLanded) {
        execute(attackerStats);
    }
}
