package com.csse3200.game.components.player;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.csse3200.game.components.CombatStatsComponent;
import com.csse3200.game.services.ServiceLocator;
import com.csse3200.game.ui.UIComponent;

/**
 * A ui component for displaying player stats, e.g. health.
 */
public class PlayerStatsDisplay extends UIComponent {
  Table table;
  private Image heartImage;
  private Label healthLabel;
  private Label strengthLabel;
  private Label defenseLabel;

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


    // Heart image
    float heartSideLength = 30f;
    heartImage = new Image(ServiceLocator.getResourceService().getAsset("images/heart.png", Texture.class));

    // Health text
    CombatStatsComponent stats = entity.getComponent(CombatStatsComponent.class);
    int health = stats.getHealth();
    int strength = stats.getStrength();
    int defense = stats.getDefense();


    CharSequence healthText = String.format("Health: %d", health);
    healthLabel = new Label(healthText, skin, "large");


    CharSequence strengthText = String.format("Strength: %d", strength);
    strengthLabel = new Label(strengthText, skin, "large");

    CharSequence defenseText = String.format("Defense: %d", defense);
    defenseLabel = new Label(defenseText, skin, "large");


    table.add(heartImage).size(heartSideLength).pad(5);
    table.add(healthLabel).pad(5);
    table.row();
    table.add(strengthLabel).pad(5);
    table.row();
    table.add(defenseLabel).pad(5);

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

  @Override
  public void dispose() {
    super.dispose();
    heartImage.remove();
    healthLabel.remove();
    strengthLabel.remove();
    defenseLabel.remove();

  }
}
