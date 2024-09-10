package com.csse3200.game.components;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.util.HashSet;
import java.util.Set;

/**
 * Component used to store information related to combat such as health, attack, etc. Any entities
 * which engage it combat should have an instance of this class registered. This class can be
 * extended for more specific combat needs.
 */
public class CombatStatsComponent extends Component {

  private static final Logger logger = LoggerFactory.getLogger(CombatStatsComponent.class);

  // Enum for status effects
  public enum StatusEffect {
    CONFUSION, BLEEDING
  }

  // Set to hold active status effects
  private Set<StatusEffect> statusEffects = new HashSet<>();

  private final int maxHealth;
  private int health;
  private int hunger;
  private int strength;
  private int defense;
  private int speed;
  private int experience;
  private int stamina;
  private final int maxStamina;
  private final int maxHunger;
  private final int maxExperience;
  private final boolean isPlayer;

  public CombatStatsComponent(int health, int hunger, int strength, int defense, int speed, int experience, int stamina, boolean isPlayer) {
      this.maxHealth = health;
      this.maxHunger = hunger;
      this.maxExperience=experience;
      this.maxStamina = stamina;
      this.isPlayer = isPlayer;
      setHealth(health);
      setHunger(hunger);
      setStrength(strength);
      setDefense(defense);
      setSpeed(speed);
      setExperience(experience);
      setStamina(stamina);
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
      entity.getEvents().trigger("updateHealth", this.health, this.maxHealth, this.isPlayer);
    }
  }

  /**
   * Adds to the player's health. The amount added can be negative.
   *
   * @param health health to add
   */
  public void addHealth(int health) {
    int newHealth = Math.min(maxHealth, this.health + health);
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
      this.hunger = hunger;
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
   * @param hunger hunger to add
   */
  public void addHunger(int hunger) {
    setHunger(this.hunger + hunger);
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
      logger.error("Cannot set defense to a negative value");
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
      logger.error("Cannot set speed to a negative value");
    }
  }

  /**
   * Adds to the player's speed. The amount added can be negative.
   *
   * @param speed speed to add
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
      logger.error("Cannot set experience to a negative value");
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

  public int getMaxExperience(){
    return maxExperience;
  }

  public int getMaxStamina() {
      return maxStamina;
  }

  /**
   * @return current stamina of the entity.
   */
  public int getStamina() {
    return stamina;
  }

  /**
   * Sets new stamina value.
   * @param stamina to be changed to.
   */
  public void setStamina(int stamina) {
    if (stamina >= 0) {
      this.stamina = stamina;
    } else {
      logger.error("Cannot set stamina to a negative value");
    }
  }

  /**
   * Adds to the player's stamina. The amount added can be negative.
   * @param change to the current stamina (positive or negative).
   */
  public void addStamina(int change) {
    int newStamina = Math.min(maxStamina, this.stamina + change);
    newStamina = Math.max(0, newStamina);
    setStamina(newStamina);
  }

  // Status Effects
  public void addStatusEffect(StatusEffect effect) {
    statusEffects.add(effect);
    logger.info("Added status effect: {}", effect);
  }

  public void removeStatusEffect(StatusEffect effect) {
    if (statusEffects.contains(effect)) {
      statusEffects.remove(effect);
      logger.info("Removed status effect: {}", effect);
    }
  }

  public boolean hasStatusEffect(StatusEffect effect) {
    return statusEffects.contains(effect);
  }

  /**
   * Processes active status effects (to be called at the start of each turn).
   */
  public void processStatusEffects() {
    if (hasStatusEffect(StatusEffect.CONFUSION)) {
      logger.info("Entity is confused and may act unpredictably.");
      // Implement confusion logic here
    }

    if (hasStatusEffect(StatusEffect.BLEEDING)) {
      logger.info("Entity is bleeding and takes damage.");
      // Implement bleeding logic here
    }
  }
}
