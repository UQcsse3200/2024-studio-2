package com.csse3200.game.entities.configs;

import java.util.Map;

/**
 * Defines the Snake's statistics stored in snake config files to be loaded by the NPC factory.
 */
public class SnakeConfig extends BaseEntityConfig {
	

	public int health = 30;
	public int baseAttack = 0;
	public String favouriteFood = "Apple";
	public String animalName = "Snake";
	public String soundPath = "sounds/snake-hiss.mp3";
	
	public static final Map<Integer, String[]> hints = Map.of(
			0, new String[]{"Welcome to Animal Kingdom!", "I am Sam the Snake."},
			1, new String[]{"This is snake specific hint 2.", "We hope you're having fun"}
	);
	
	public static int hintLevel = 0;
	public static int currentHint = 0;
	
	public String[] getStringHintLevel() {
		return hints.get(hintLevel);
	}
	
	public void incrementHintLevel() {
		if (hints != null && hintLevel < (hints.size() - 1)) {
			hintLevel = hintLevel + 1;
			restartCurrentHint();
		}
	}
	
	public int getHintLevel() {
		if (hints != null) {
			return hintLevel;
		}
		return -1;
	}
	
	public void restartCurrentHint() {
		if (hints != null) {
			this.currentHint = 0;
		}
	}
	
	public String getCurrentHint() {
		if (hints != null) {
			String[] hint = hints.get(hintLevel);
			return hint[currentHint];
		}
		return "";
	}
	
	public void incrementCurrentHint() {
		if (hints != null) {
			currentHint = (currentHint + 1) % (hints.get(hintLevel)).length;
		}
	}
	
	public void decrementCurrentHint() {
		if (hints != null) {
			currentHint = (currentHint - 1) % (hints.get(hintLevel)).length;
		}
	}
	
	public String getAnimalName() {
		return "Snake";
	}
}