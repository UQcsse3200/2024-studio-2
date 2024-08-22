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
  private Animation<Sprite> healthBarAnimation;
  private Texture healthBarImages;
  private TextureRegion[] animationFrames;
  private SpriteBatch batch;
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
    //int index = 0;
    //float stateTime = 0;
    //batch = new SpriteBatch();
    //textureAtlas = new TextureAtlas("healthBars.txt");
    // healthBarAnimation = new Animation<>(0.066f, textureAtlas.createSprites("capguy"))

    // healthBarImages = new Texture("spriteSheets/")
    //TextureRegion[][] tmpFrames = TextureRegion.split(healthBarImages, 110, 573);
    //animationFrames = new TextureRegion[11];
    table.top().left();
    table.setFillParent(true);
    table.padTop(45f).padLeft(5f);

    // Heart image
    float heartSideLength = 150f;
    //iconImage = new Image(ServiceLocator.getResourceService().getAsset("images/player_icon_forest.png", Texture.class));
    heartImage = new Image(ServiceLocator.getResourceService().getAsset("images/health_bar_x1.png", Texture.class));

    // Get the original width and height of the image
    float heartImageWidth = (float) (heartImage.getWidth() * 0.8);
    float heartImageHeight = (float) (heartImage.getHeight() * 0.5);

    // Loop to decide health bar status
    //for (int i = 0; i < 3; i++) {
      //for (int j = 0; j < 3; j++) {
        //if (index < 11) {
          //animationFrames[index++] = tmpFrames[j][i];
        //}
      //}
    //}

    // Health text
    int health = entity.getComponent(CombatStatsComponent.class).getHealth();
    CharSequence healthText = String.format("Health: %d", health);
    healthLabel = new Label(healthText, skin, "large");

    table.add(heartImage).size(heartImageWidth, heartImageHeight).pad(5);
    table.add(healthLabel);
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
