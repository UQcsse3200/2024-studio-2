package com.csse3200.game.minigames.maze.components;

import com.badlogic.gdx.math.Vector2;
import com.csse3200.game.components.Component;
import com.csse3200.game.minigames.maze.entities.MazePlayer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Component used to store information related to combat such as health, attack, etc. Any entities
 * which engage in combat should have an instance of this class registered. This class can be
 * extended for more specific combat needs.
 */
public class MazeCombatStatsComponent extends Component {

    private static final Logger logger = LoggerFactory.getLogger(MazeCombatStatsComponent.class);
    private int health;
    private int baseAttack;
    private Vector2 baseSpeed;

    public MazeCombatStatsComponent(int health, int baseAttack, float baseSpeed) {
        setHealth(health);
        setBaseAttack(baseAttack);
        setBaseSpeed(baseSpeed);
    }

    /**
     * Returns true if the entity's has 0 health, otherwise false.
     * This is true for all entities not just the player
     */
    private void isDead() {
        if (entity instanceof MazePlayer) {
            entity.getEvents().trigger("endGame", entity.getComponent(MazeGameManagerComponent.class).getScore());
        }
    }

    /**
     * Returns the entity's health.
     *
     * @return entity's health
     */
    public int getHealth() {
        return health;
    }

    /**
     * Sets the entity's health. Health has a minimum bound of 0.
     *
     * @param health health
     */
    public void setHealth(int health) {
        if (health > 0) {
            this.health = health;
        } else {
            this.health = 0;
            isDead();
        }
        if (entity != null) {
            entity.getEvents().trigger("updateHealth", this.health);
        }
    }

    /**
     * Adds to the player's health. The amount added can be negative.
     *
     * @param health health to add
     */
    public void addHealth(int health) {
        setHealth(this.health + health);
    }

    /**
     * Returns the entity's base attack damage.
     *
     * @return base attack damage
     */
    public int getBaseAttack() {
        return baseAttack;
    }

    /**
     * Sets the entity's attack damage. Attack damage has a minimum bound of 0.
     *
     * @param attack Attack damage
     */
    public void setBaseAttack(int attack) {
        if (attack >= 0) {
            this.baseAttack = attack;
        } else {
            logger.error("Can not set base attack to a negative attack value");
        }
    }

    /**
     * Get the base speed for this NPC
     * @return the speed
     */
    public Vector2 getBaseSpeed() {
        return baseSpeed.cpy();
    }

    /**
     * Sets the base speed for this entity
     * @param speed the speed to set to
     */
    public void setBaseSpeed(float speed) {
        this.baseSpeed = new Vector2(speed, speed);
    }

    /**
     * Used when the player is hit by an entity and reduced health based on attackers base attack
     * @param attacker the entity attacking the player
     */
    public void hit(MazeCombatStatsComponent attacker) {
        int newHealth = getHealth() - attacker.getBaseAttack();
        setHealth(newHealth);
    }
}
