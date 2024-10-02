package com.csse3200.game.components;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.util.HashSet;
import java.util.Set;

/**
 * Component used to store information related to combat such as health, attack, etc. Any entities
 * which engage in combat should have an instance of this class registered.
 */
public class CombatStatsComponent extends Component {

  private static final Logger logger = LoggerFactory.getLogger(CombatStatsComponent.class);

  // Enum for status effects
  public enum StatusEffect {
    CONFUSION, BLEEDING, POISONED, SHOCKED
  }

  // Set to hold active status effects
  private Set<StatusEffect> statusEffects = new HashSet<>();

  private int maxHealth;
  private int health;
  private int hunger;
  private int strength;
  private int defense;
  private int speed;
  private int experience;
  private int stamina;
  private int level;
  private final int maxLevel;
  private final int maxStamina;
  private final int maxHunger;
  private int maxExperience;
  private final boolean isPlayer;
  private final boolean isBoss;


  /**
   * Constructor for CombatStatsComponent, initializing all combat-related attributes for an entity.
   *
   * @param health Initial health value
   * @param hunger Initial hunger value
   * @param strength Initial strength value
   * @param defense Initial defense value
   * @param speed Initial speed value
   * @param experience Initial experience value
   * @param stamina Initial stamina value
   * @param isPlayer Boolean indicating if this entity is the player
   */
  public CombatStatsComponent(int health, int hunger, int strength, int defense, int speed, int experience, int stamina, boolean isPlayer, boolean isBoss, int level) {
    this.maxHealth = health;
    this.maxHunger = hunger;
    this.maxExperience = (int) Math.ceil(71.7125 * Math.pow(Math.E, 0.191529 * this.level) + 13.1489);
    this.maxStamina = stamina;
    this.isPlayer = isPlayer;
    this.isBoss = isBoss;
    this.maxLevel = 10;
    setHealth(health);
    setHunger(hunger);
    setStrength(strength);
    setDefense(defense);
    setSpeed(speed);
    setExperience(experience);
    setStamina(stamina);
  }

  /**
   * Returns true if the entity is a player, otherwise false.
   *
   * @return is entity player
   */
  public Boolean isPlayer() {
    return isPlayer;
  }

  /**
   * Returns true if the entity has 0 health, otherwise false.
   *
   * @return is entity dead
   */
  public Boolean isDead() {
    return health == 0;
  }

  /**
   * Returns true if the entity's is a boss, otherwise false.
   *
   * @return is player dead
   */
  public Boolean isBoss() {
    return this.isBoss;
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
   * Sets the entity's health. Health has a minimum bound of 0 and cannot exceed maxHealth.
   *
   * @param health health value to set
   */
  public void setHealth(int health) {
    if (health >= 0) {
      if (health >= this.maxHealth) {
        this.health = this.maxHealth;
      } else {
        this.health = health;
      }
    } else {
      this.health = 0;
    }
    if (entity != null) {
      entity.getEvents().trigger("updateHealth", this.health, this.maxHealth, this.isPlayer);
    }
  }

  /**
   * Adds a specified amount to the entity's health. The amount can be negative to reduce health.
   *
   * @param change the health to add (positive or negative)
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
   * Sets the entity's hunger. Hunger has a minimum bound of 0 and cannot exceed maxHunger.
   *
   * @param hunger hunger value to set
   */
  public void setHunger(int hunger) {
    if (hunger >= 0) {
      if (hunger >= this.maxHunger) {
        this.hunger = this.maxHunger;
      } else {
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
   * Adds a specified amount to the entity's hunger. The amount can be negative to reduce hunger.
   *
   * @param change hunger to add (positive or negative)
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
   * @param strength Strength value to set
   */
  public void setStrength(int strength) {
    this.strength = Math.max(0, strength);
  }

  /**
   * Adds a specified amount to the entity's strength. The amount can be negative to reduce strength.
   *
   * @param strength strength to add (positive or negative)
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
   * @param defense defense value to set
   */
  public void setDefense(int defense) {
    this.defense = Math.max(0, defense);
  }

  /**
   * Adds a specified amount to the entity's defense. The amount can be negative to reduce defense.
   *
   * @param defense defense to add (positive or negative)
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
   * Sets the entity's speed. Speed has a minimum bound of 0.
   *
   * @param speed speed value to set
   */
  public void setSpeed(int speed) {
    this.speed = Math.max(0, speed);
  }

  /**
   * Adds a specified amount to the entity's speed. The amount can be negative to reduce speed.
   *
   * @param speed speed to add (positive or negative)
   */
  public void addSpeed(int speed) {
    setSpeed(this.speed + speed);
  }

  /**
   * Returns the entity's experience.
   *
   * @return entity's experience
   */
  public int getExperience() {
    return experience;
  }

  /**
   * Sets the entity's experience. Experience has a minimum bound of 0.
   * If the entity is a player, level up logic is applied once experience exceeds the maximum threshold.
   *
   * @param experience experience value to set
   */
  public void setExperience(int experience) {
    this.experience = Math.max(0, experience);

    if (this.experience >= this.maxExperience && isPlayer && (this.level < this.maxLevel)) {
      int experienceDiff = this.experience - this.maxExperience;

      setExperience(experienceDiff);


      this.maxHealth += 1;
      addHealth(1);
      addStrength(1);
      addDefense(1);
      addSpeed(1);
      addLevel(1);
      this.maxExperience = (int) Math.ceil(71.7125 * Math.pow(Math.E, 0.191529 * this.level) + 13.1489);
    }

    if (this.experience >= this.maxExperience && !isPlayer) {
      this.experience = this.maxExperience;
    }
  }

  /**
   * Adds a specified amount to the entity's experience. The amount can be negative to reduce experience.
   *
   * @param experience experience to add (positive or negative)
   */
  public void addExperience(int experience) {
    setExperience(this.experience + experience);
  }

  /**
   * Simulates the entity being hit by an attacker. The target's health is reduced based on the attacker's strength.
   *
   * @param attacker CombatStatsComponent of the attacking entity
   */
  public void hit(CombatStatsComponent attacker) {
    int newHealth = getHealth() - attacker.getStrength();
    logger.info("Attacker's Strength: {}", attacker.getStrength());
    logger.info("New Health: {}", newHealth);
    setHealth(newHealth);
  }

  /**
   * Returns the entity's maximum health.
   *
   * @return entity's max health
   */
  public int getMaxHealth() {
    return maxHealth;
  }

  /**
   * Returns the entity's maximum hunger.
   *
   * @return entity's max hunger
   */
  public int getMaxHunger() {
    return maxHunger;
  }

  /**
   * Returns the entity's maximum experience.
   *
   * @return entity's max experience
   */
  public int getMaxExperience() {
    return this.maxExperience;
  }

  /**
   * Returns the entity's stamina.
   *
   * @return entity's stamina
   */
  public int getStamina() {
    return stamina;
  }

  /**
   * Sets the entity's stamina. Stamina has a minimum bound of 0 and cannot exceed maxStamina.
   *
   * @param stamina stamina value to set
   */
  public void setStamina(int stamina) {
    this.stamina = Math.min(maxStamina, Math.max(0, stamina));
  }

  /**
   * Adds a specified amount to the entity's stamina. The amount can be negative to reduce stamina.
   *
   * @param stamina stamina to add (positive or negative)
   */
  public void addStamina(int stamina) {
    setStamina(this.stamina + stamina);
  }

  /**
   * Returns the entity's maximum stamina.
   *
   * @return entity's max stamina
   */
  public int getMaxStamina() {
    return maxStamina;
  }

  /**
   * Returns the entity's level.
   *
   * @return entity's level
   */
  public int getLevel(){ return level; }

  /**
   * Sets the entity's level.
   *
   * @param level sets entity's level
   */
  public void setLevel(int level){

    this.level = Math.max(0, level);
    if (this.level > 10) {
      this.level = 10;
    }
  }

  /**
   * Add int to entity's level
   *
   * @param level adds int to the current level
   */
  public void addLevel(int level){
    setLevel(this.level + level);
  }

  /**
   * Adds a status effect to the entity. Status effects can impact combat in different ways.
   * For example, 'CONFUSION' may cause an entity to randomly attack, while 'BLEEDING' may
   * cause gradual health loss over time.
   * @param effect The status effect to add
   */
  public void addStatusEffect(StatusEffect effect) {
    statusEffects.add(effect);
    logger.info("Added status effect: {}", effect);
  }

  /**
   * Removes a status effect from the entity. This can be used to stop the negative
   * impact of certain effects, such as stopping health loss from 'BLEEDING'.
   *
   * @param effect The status effect to remove
   */
  public void removeStatusEffect(StatusEffect effect) {
    if (statusEffects.contains(effect)) {
      statusEffects.remove(effect);
      logger.info("Removed status effect: {}", effect);
    }
  }

  /**
   * Checks if the entity has a specific status effect.
   *
   * @param effect The status effect to check
   * @return true if the entity has the status effect, false otherwise
   */
  public boolean hasStatusEffect(StatusEffect effect) {
    return statusEffects.contains(effect);
  }
}
