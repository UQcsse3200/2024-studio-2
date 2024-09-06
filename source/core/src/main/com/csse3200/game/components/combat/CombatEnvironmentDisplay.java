package com.csse3200.game.components.combat;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.csse3200.game.services.ServiceLocator;
import com.csse3200.game.ui.UIComponent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Displays combat area images.
 */
public class CombatEnvironmentDisplay extends UIComponent {
  private static final Logger logger = LoggerFactory.getLogger(CombatEnvironmentDisplay.class);
  private static final float Z_INDEX = 2f;
  private Table table;

  @Override
  public void render(float delta) {

  }

  @Override
  public void resize(int width, int height) {

  }

  @Override
  public void create() {
    super.create();
    addActors();
  }

  /**
   * Adds a background image which takes up the entirety of the stage.
   */
  private void addActors() {
    table = new Table();
    table.setFillParent(true);

    // Import image.
    Texture texture = ServiceLocator.getResourceService().getAsset("images/grass_3.png", Texture.class);
    Image bg = new Image(texture);

    Stage stage = ServiceLocator.getRenderService().getStage();
    // Full stage.
    bg.setSize(stage.getWidth(), stage.getHeight());

    table.add(bg).expand().fill();
    stage.addActor(table);
  }

  @Override
  public void draw(SpriteBatch batch) {
    // draw is handled by the stage
  }

  @Override
  public float getZIndex() {
    return Z_INDEX;
  }

  @Override
  public void dispose() {
    table.clear();
    super.dispose();
  }
}
