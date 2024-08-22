package com.csse3200.game.components.player;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
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
  private Image iconImage;
  private Image hungerImage;
  private Image xpImage;
  private Label healthLabel;
  private Animation<TextureRegion> healthBarAnimation;
  private TextureAtlas textureAtlas;

  /**
   * Creates reusable ui styles and adds actors to the stage.
   */
  @Override
  public void create() {
    super.create();
    addActors();

    entity.getEvents().addListener("updateHealth", this::updatePlayerHealthUI);
  }

  /**
   * Creates actors and positions them on the stage using a table.
   * @see Table for positioning options
   */
  private void addActors() {
    table = new Table();

    // Debugged and Developed Animation with ChatGPT
    TextureRegion[] healthBarFrames = new TextureRegion[11]; // Adjust size according to your frames
    for (int i = 0; i < healthBarFrames.length; i++) {
      // Create the frame names based on your naming convention
      String frameName = (100 - i * 10) + "%_health"; // This will create "100%_health", "90%_health", ..., "00%_health"
      healthBarFrames[i] = textureAtlas.findRegion(frameName); // Retrieve the frame
    }
    healthBarAnimation = new Animation<>(0.066f, healthBarFrames);
    textureAtlas = new TextureAtlas("healthBars.txt");

    table.top().left();
    table.setFillParent(true);
    table.padTop(45f).padLeft(5f);

    // Health Dimensions
    float heartSideLength = 150f;
    heartImage = new Image(ServiceLocator.getResourceService().getAsset("images/health_bar_x1.png", Texture.class));
    xpImage = new Image(ServiceLocator.getResourceService().getAsset("images/xp_bar.png", Texture.class));

    // Get the original width and height of the image
    float barImageWidth = (float) (heartImage.getWidth() * 0.8);
    float barImageHeight = (float) (heartImage.getHeight() * 0.5);

    // Health text
    int health = entity.getComponent(CombatStatsComponent.class).getHealth();
    CharSequence healthText = String.format("HP: %d", health);
    healthLabel = new Label(healthText, skin, "large");

    // Health animation update
    int frameIndex = Math.max(0, Math.min(healthBarFrames.length - 1, (health * (healthBarFrames.length - 1)) / health));

    // Experience text maybe
    CharSequence xpText = String.format("EXP: %d", 99);
    Label xpLabel = new Label(xpText, skin, "large");


    table.add(heartImage).size(barImageWidth, barImageHeight).pad(5);
    table.add(healthLabel);
    table.row();
    table.add(xpImage).size(barImageWidth, barImageHeight);
    table.add(xpLabel);
    stage.addActor(table);
  }

  @Override
  public void draw(SpriteBatch batch)  {
    // draw is handled by the stage
  }

  /**
   * Updates the player's health on the ui.
   * @param health player health
   */
  public void updatePlayerHealthUI(int health) {
    CharSequence text = String.format("Health: %d", health);
    healthLabel.setText(text);
  }

  @Override
  public void dispose() {
    super.dispose();
    heartImage.remove();
    healthLabel.remove();
  }
}
