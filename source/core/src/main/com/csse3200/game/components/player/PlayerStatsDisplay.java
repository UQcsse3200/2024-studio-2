package com.csse3200.game.components.player;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.csse3200.game.components.CombatStatsComponent;
import com.csse3200.game.services.ServiceLocator;
import com.csse3200.game.ui.UIComponent;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

import java.security.Provider;


/**
 * A ui component for displaying player stats, e.g. health.
 */
public class PlayerStatsDisplay extends UIComponent {
  Table table;
  private Image heartImage;

  private Image xpImage;
  private Image hungerImage;
  private Label healthLabel;
  private Label xpLabel;
  private Label hungerLabel;
  private Animation<TextureRegion> healthBarAnimation;
  private TextureAtlas textureAtlas;
  private Label strengthLabel;
  private Label defenseLabel;
  private Label speedLabel;
  private Label experienceLabel;


  /**
   * Creates reusable ui styles and adds actors to the stage.
   */
  @Override
  public void create() {
    super.create();
    addActors();

    entity.getEvents().addListener("updateHealth", this::updatePlayerHealthUI);

    entity.getEvents().addListener("updateStrength", this::updatePlayerStrengthUI);
    entity.getEvents().addListener("updateDefense", this::updatePlayerDefenseUI);
    entity.getEvents().addListener("updateSpeed", this::updatePlayerSpeedUI);
    entity.getEvents().addListener("updateExperience", this::updatePlayerExperienceUI);

  }

  /**
   * Creates actors and positions them on the stage using a table.
   * @see Table for positioning options
   */
  private void addActors() {
    table = new Table();
    table.top().left();
    table.setFillParent(true);
    table.padTop(45f).padLeft(5f);

    // Animation Setup
    // Load health bar frames into a TextureAtlas
    textureAtlas = new TextureAtlas("spriteSheets/healthBars.txt"); // Load the atlas with health bar frames
    TextureRegion[] healthBarFrames = new TextureRegion[11]; // Array to hold the frames

    // Populate the array with frames based on your naming convention
    for (int i = 0; i < healthBarFrames.length; i++) {
      String frameName = (100 - i * 10) + "%_health"; // Create frame names like "100%_health"
      healthBarFrames[i] = textureAtlas.findRegion(frameName); // Retrieve the frame
    }

    healthBarAnimation = new Animation<>(0.066f, healthBarFrames); // Create the animation with frame duration

    // Health Dimensions
    float heartSideLength = 150f;
    heartImage = new Image(ServiceLocator.getResourceService().getAsset("images/health_bar_x1.png", Texture.class));
    xpImage = new Image(ServiceLocator.getResourceService().getAsset("images/xp_bar.png", Texture.class));
    hungerImage = new Image(ServiceLocator.getResourceService().getAsset("images/hunger_bar.png", Texture.class));

    // Get the original width and height of the image
    float barImageWidth = (float) (heartImage.getWidth() * 0.8);
    float barImageHeight = (float) (heartImage.getHeight() * 0.5);

    // Health text




    int health = entity.getComponent(CombatStatsComponent.class).getHealth();
    int maxHealth = entity.getComponent(CombatStatsComponent.class).getMaxHealth();
    CharSequence healthText = String.format("HP: %d", health);

    // Experience text maybe
    CharSequence xpText = String.format("EXP: %d", 100);
    xpLabel = new Label(xpText, skin, "large");

    // Hunger text maybe
    CharSequence hungerText = String.format("HGR: %d", 100);
    hungerLabel = new Label(hungerText, skin, "large");

    // Add all components to table
    table.add(heartImage).size(barImageWidth, barImageHeight).pad(5);
    table.add(healthLabel);
    table.row();
    table.add(xpImage).size(barImageWidth, (float) (barImageHeight * 0.95));
    table.add(xpLabel);
    table.row();
    table.add(hungerImage).size(barImageWidth, barImageHeight * 2);
    table.add(hungerLabel);
    stage.addActor(table);
  }

  @Override
  public void draw(SpriteBatch batch)  {
    // handled by stage
  }

  /**
   * Updates the player's health on the ui.
   * @param health player health
   */
  public void updatePlayerHealthUI(int health) {
    CharSequence text = String.format("Health: %d", health);
    healthLabel.setText(text);
    int totalFrames = 11;
    // Debugged and Developed with ChatGPT
    // Calculate the frame index based on the current health
    int maxHealth = entity.getComponent(CombatStatsComponent.class).getMaxHealth();
    int frameIndex = totalFrames - 1 - (int) ((float) health / maxHealth * (totalFrames - 1));
    frameIndex = Math.max(0, Math.min(frameIndex, totalFrames - 1));

    // Set the current frame of the health bar animation
    TextureRegion currentFrame = healthBarAnimation.getKeyFrame(frameIndex * 0.066f);
    heartImage.setDrawable(new TextureRegionDrawable(currentFrame));  // Update the heartImage with the new frame
  }


  /**
   * Updates the player's strength on the UI.
   * @param strength Player strength
   */

  public void updatePlayerStrengthUI(int strength) {
    CharSequence text = String.format("Strength: %d", strength);
    strengthLabel.setText(text);
  }

  /**
   * Updates the player's defense on the UI.
   * @param defense Player defense
   */
  public void updatePlayerDefenseUI(int defense) {
    CharSequence text = String.format("Defense: %d", defense);
    defenseLabel.setText(text);
  }

  /**
   * Updates the player's speed on the UI.
   * @param speed Player speed
   */
  public void updatePlayerSpeedUI(int speed) {
    CharSequence text = String.format("Speed: %d", speed);
    speedLabel.setText(text);
  }

  public void updatePlayerExperienceUI(int experience) {
    CharSequence text = String.format("Experience: %d", experience);
    experienceLabel.setText(text);
  }

  @Override
  public void dispose() {
    super.dispose();
    heartImage.remove();
    healthLabel.remove();
    xpImage.remove();
    xpLabel.remove();
    hungerImage.remove();
    hungerLabel.remove();
    textureAtlas.dispose();
  }
}
