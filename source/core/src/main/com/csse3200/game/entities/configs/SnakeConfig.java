package com.csse3200.game.entities.configs;

import java.util.Map;

/**
 * Defines the Snake's statistics stored in snake config files to be loaded by the NPC factory.
 */
public class SnakeConfig extends BaseEntityConfig {
	public String favouriteFood = "Apple";
	public String animalName = "Snake";
	public String soundPath = "sounds/snake-hiss.mp3";

	protected SnakeConfig() {
		this.health = 30;
		this.baseAttack = 0;
		this.hints = Map.of(
				0, new String[]{"Welcome to Animal Kingdom!", "I am Sam the Snake."},
				1, new String[]{"This is snake specific hint 2.", "We hope you're having fun"}
		);
		this.hintLevel = 0;
		this.currentHint = 0;

	}
}