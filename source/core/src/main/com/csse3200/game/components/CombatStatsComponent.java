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
  private int maxHunger;
  private int health;
  private int strength;
  private int defense;
  private int speed;
  private int experience;

  public CombatStatsComponent(int health, int strength, int defense, int speed, int experience,int hunger) {
      this.maxHealth = health;
      this.maxHunger=hunger;
      setHealth(health);
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
      this.health = health;
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
   * @param health health to add
   */
  public void addHealth(int health) {
    setHealth(this.health + health);
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
      logger.error("Cannot set strength to a negative value");
    }
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
      logger.error("Cannot set defense to a negative value");
    }
  }

  /**
   * Returns the entity's strength.
   *
   * @return entity's strength
   */
  public int getSpeed() {
    return speed;
  }


  /**
   * Sets the entity's strength. Strength has a minimum bound of 0.
   *
   * @param speed speed
   */
  public void setSpeed(int speed) {
    if (speed >= 0) {
      this.speed = speed;
    } else {
      logger.error("Cannot set speed to a negative value");
    }
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
      logger.error("Cannot set experience to a negative value");
    }
  }

  public void hit(CombatStatsComponent attacker) {
    int newHealth = getHealth() - attacker.getStrength();
    System.out.println("Attacker Attack:");
    System.out.println(attacker.getStrength());
    System.out.println(newHealth);
    setHealth(newHealth);
  }

  public int getMaxHunger()
  {
    return maxHunger;
  }



  public int getMaxHealth() {
    return maxHealth;
  }
}
