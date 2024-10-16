package com.csse3200.game.components.combat.move;

import com.csse3200.game.components.CombatStatsComponent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * The ItemMove class represents a increase stat move where the entity takes a moment to increase or restore one of
 * their stats (health, hunger, defense, strength)
 */
public class ItemMove extends CombatMove {
    private static final Logger logger = LoggerFactory.getLogger(ItemMove.class);

    /**
     * Constructor to initialize the ItemMove with its name and hunger cost.
     *
     * @param moveName the name of the item move.
     * @param hungerCost the hunger cost required to execute the item move.
     */
    public ItemMove(String moveName, int hungerCost) {
        super(moveName, hungerCost);
    }

    /**
     * Executes the items move, increasing hunger.
     *
     * @param attackerStats the combat stats of the entity performing the item move.
     */
    @Override
    public void execute(CombatStatsComponent attackerStats) {
        if (attackerStats != null) {
            logger.info("{} item increasing {} hunger.",
                    attackerStats.isPlayer() ? "PLAYER" : "ENEMY",
                    this.getHungerCost());

            attackerStats.addHunger(-(this.getHungerCost()));
        } else {
            logger.error("Entity does not have CombatStatsComponent.");
        }
    }

    /**
     * Executes the item move, which simply consumes hunger. The target is ignored in this case.
     *
     * @param attackerStats the combat stats of the entity performing the move.
     * @param targetStats   the combat stats of the target (ignored for item moves).
     */
    @Override
    public void execute(CombatStatsComponent attackerStats, CombatStatsComponent targetStats) {
        execute(attackerStats);
    }

    /**
     * Executes the item move, either consuming or increasing hunger. The target's guarding status is ignored in this
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
     * Executes the item move, consuming hunger. The number of hits landed is ignored for item moves.
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
