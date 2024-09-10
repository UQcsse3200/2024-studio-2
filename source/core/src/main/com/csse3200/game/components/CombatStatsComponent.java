package com.csse3200.game.components;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Component used to store information related to combat such as health, attack, etc. Any entities
 * which engage it combat should have an instance of this class registered. This class can be
 * extended for more specific combat needs.
 */
public class CombatStatsComponent extends Component {

  private static final Logger logger = LoggerFactory.getLogger(CombatStatsComponent.class);
  private int maxHealth;
  private int health;
  private int hunger;
  private int strength;
  private int defense;
  private int speed;
  private int experience;
  private boolean isPlayer;
  private final int maxHunger;
  private int maxExperience;

  public CombatStatsComponent(int health, int hunger, int strength, int defense, int speed, int experience, boolean isPlayer) {
      this.maxHealth = health;
      this.maxHunger = hunger;
      this.maxExperience=100;
      this.isPlayer = isPlayer;
      setHealth(health);
      setHunger(hunger);
      setStrength(strength);
      setDefense(defense);
      setSpeed(speed);
      setExperience(experience);

  }

  /**
   * Returns true if the entity's has 0 health, otherwise false.
   *
   * @return is player dead
   */
  public Boolean isDead() {
    return health == 0;
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
    if (health >= 0) {
      if (health >= this.maxHealth) {
        this.health = this.maxHealth;
      }
      else {
        this.health = health;
      }
    } else {
      this.health = 0;
    }
    if (entity != null) {
      entity.getEvents().trigger("updateHealth", this.health);
    }
  }

  /**
   * Adds to the player's health. The amount added can be negative.
   *
   * @param change the health to add
   */
  public void addHealth(int change) {
    int newHealth = Math.min(maxHealth, this.health + change);
    newHealth = Math.max(0, newHealth);
    setHealth(newHealth);
  }

  /**
   * Returns the entity's hunger.
   *
   * @return entity's hunger
   */
  public int getHunger() {
    return hunger;
  }

  /**
   * Sets the entity's hunger. hunger has a minimum bound of 0.
   *
   * @param hunger hunger
   */
  public void setHunger(int hunger) {
    if (hunger >= 0) {
      if (hunger >= this.maxHunger) {
        this.hunger = this.maxHunger;
      }
      else {
        this.hunger = hunger;
      }
    } else {
      this.hunger = 0;
    }
    if (entity != null) {
      entity.getEvents().trigger("updateHunger", this.hunger);
    }
  }

  /**
   * Adds to the player's hunger. The amount added can be negative.
   *
   * @param change hunger to add
   */
  public void addHunger(int change) {
    int newHunger = Math.min(maxHunger, this.hunger + change);
    newHunger = Math.max(0, newHunger);
    setHunger(newHunger);
  }


  /**

   * Returns the entity's strength.
   *
   * @return entity's strength
   */

  public int getStrength() {
    return strength;
  }

  /**
   * Sets the entity's strength. Strength has a minimum bound of 0.
   *
   * @param strength Strength
   */

  public void setStrength(int strength) {
    if (strength >= 0) {
      this.strength = strength;
    } else {
      this.strength = 0;
    }
  }

  /**
   * Adds to the player's strength. The amount added can be negative.
   *
   * @param strength strength to add
   */
  public void addStrength(int strength) {
    setStrength(this.strength + strength);
  }

  /**
   * Returns the entity's defense.
   *
   * @return entity's defense
   */
  public int getDefense() {
    return defense;
  }

  /**
   * Sets the entity's defense. Defense has a minimum bound of 0.
   *
   * @param defense Defense
   */
  public void setDefense(int defense) {
    if (defense >= 0) {
      this.defense = defense;
    } else {
      this.defense = 0;
    }
  }

  /**
   * Adds to the player's defense. The amount added can be negative.
   *
   * @param defense defense to add
   */
  public void addDefense(int defense) {
    setDefense(this.defense + defense);
  }

  /**
   * Returns the entity's speed.
   *
   * @return entity's speed
   */
  public int getSpeed() {
    return speed;
  }


  /**
   * Sets the entity's speed. speed has a minimum bound of 0.
   *
   * @param speed speed
   */
  public void setSpeed(int speed) {
    if (speed >= 0) {
      this.speed = speed;
    } else {
      this.speed = 0;
    }
  }

  /**
   * Adds to the player's defense. The amount added can be negative.
   *
   * //@param defense defense to add
   */
  public void addSpeed(int speed) {
    setSpeed(this.speed + speed);
  }


  public int getExperience() {
    return experience;
  }


  /**
   * Sets the entity's experience. experience has a minimum bound of 0.
   *
   * @param experience experience
   */
  public void setExperience(int experience) {
    if (experience >= 0) {
      this.experience = experience;
    } else {
      this.experience = 0;
    }

    if (experience >= this.maxExperience && isPlayer) {

      int experienceDiff = this.experience - this.maxExperience;

      this.maxExperience = (int) Math.ceil(maxExperience * 1.25);
      setExperience(experienceDiff);

      int healthDiff = (int) Math.ceil(this.maxHealth * 0.02);
      this.maxHealth = this.maxHealth + healthDiff;
      addHealth(healthDiff);
      addStrength((int) Math.ceil(this.strength * 0.02));
      addDefense((int) Math.ceil(this.defense * 0.02));
      addSpeed((int) Math.ceil(this.speed * 0.02));

    }

    if (experience >= this.maxExperience && !isPlayer) {
      this.experience = this.maxExperience;
    }
  }

  /**
   * Adds to the player's experience. The amount added can be negative.
   *
   * @param experience experience to add
   */
  public void addExperience(int experience) {

    setExperience(this.experience + experience);

  }


  public void hit(CombatStatsComponent attacker) {
    int newHealth = getHealth() - attacker.getStrength();
    logger.info("Attacker Attack:");
    logger.info("Attacker's Strength: {}", attacker.getStrength());
    logger.info("New Health: {}", newHealth);
    setHealth(newHealth);
  }

  public int getMaxHealth() {
    return maxHealth;
  }
  public int getMaxHunger() {
    return maxHunger;
  }
  public int getMaxExperience(){return maxExperience;}
}
