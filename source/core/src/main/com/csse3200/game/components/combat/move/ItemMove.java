package com.csse3200.game.components.combat.move;

import com.csse3200.game.components.CombatStatsComponent;
import com.csse3200.game.entities.Entity;
import com.csse3200.game.inventory.items.AbstractItem;
import com.csse3200.game.inventory.items.ItemUsageContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * The ItemMove class represents an increase stat move where the entity takes a moment to increase or restore one of
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
    public String execute(CombatStatsComponent attackerStats) {
        if (attackerStats != null) {
            logger.info("{} item increasing {} hunger.",
                    attackerStats.isPlayer() ? "PLAYER" : "ENEMY",
                    this.getHungerCost());

            attackerStats.addHunger(-(this.getHungerCost()));
        } else {
            logger.error("Entity does not have CombatStatsComponent.");
        }
        return "Item";
    }

    @Override
    public String execute(CombatStatsComponent attackerStats, Entity eventCaller,
                          AbstractItem item, ItemUsageContext context, int index) {
        if (item != null) {
            int hungerBefore = attackerStats.getHunger();
            int healthBefore = attackerStats.getHealth();
            int defenseBefore = attackerStats.getDefense();
            int strengthBefore = attackerStats.getStrength();

            // Use item and inflict status changes.
            eventCaller.getEvents().trigger("itemUsedInCombat", item, context, index);

            // Calculate stat changes.
            int hungerGain = attackerStats.getHunger() - hungerBefore;
            int healthGain = attackerStats.getHealth() - healthBefore;
            int defenseGain = attackerStats.getDefense() - defenseBefore;
            int strengthGain = attackerStats.getStrength() - strengthBefore;

            // Log and return stat changes.
            if (hungerGain > 0) {
                return String.format("gained %d hunger.", hungerGain);
            } else if (healthGain > 0) {
                return String.format("gained %d health.", healthGain);
            } else if (defenseGain > 0) {
                return String.format("gained %d defense.", defenseGain);
            } else if (strengthGain > 0) {
                return String.format("gained %d strength.", strengthGain);
            } else {
                return "did not inflict any stat changes.";
            }
        }
        return "did not inflict any stat changes.";
    }

    /**
     * Executes the item move, which simply consumes hunger. The target is ignored in this case.
     *
     * @param attackerStats the combat stats of the entity performing the move.
     * @param targetStats   the combat stats of the target (ignored for item moves).
     */
    @Override
    public String execute(CombatStatsComponent attackerStats, CombatStatsComponent targetStats) {
        return execute(attackerStats);
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
    public String execute(CombatStatsComponent attackerStats, CombatStatsComponent targetStats, boolean targetIsGuarded) {
        return execute(attackerStats);
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
    public String execute(CombatStatsComponent attackerStats, CombatStatsComponent targetStats, boolean targetIsGuarded,
                        int numHitsLanded) {
        return execute(attackerStats);
    }
}
