package com.csse3200.game.entities.configs;

/**
 * Defines the Snake's statistics stored in snake config files to be loaded by the NPC factory.
 */
public class SnakeConfig extends BaseEntityConfig {
	protected SnakeConfig() {
		this.baseHint = new String[]{"Welcome to Animal Kingdom!", "I am Sam the Snake."};
		this.spritePath = "images/snake.atlas";
		this.animationSpeed = 0.1f;
		this.soundPath = new String[] {"sounds/snake-hiss.mp3"};
		this.health = 30;
		this.baseAttack = 0;
		this.animalName = "Snake";
	}
}