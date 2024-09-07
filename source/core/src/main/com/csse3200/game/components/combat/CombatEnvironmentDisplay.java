package com.csse3200.game.components.combat;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.csse3200.game.areas.CombatArea;
import com.csse3200.game.areas.terrain.CombatTerrainFactory;
import com.csse3200.game.components.CameraComponent;
import com.csse3200.game.components.animal.AnimalSelectionActions;
import com.csse3200.game.entities.Entity;
import com.csse3200.game.entities.factories.EnemyFactory;
import com.csse3200.game.entities.factories.NPCFactory;
import com.csse3200.game.entities.factories.PlayerFactory;
import com.csse3200.game.screens.CombatScreen;
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
  private CombatTerrainFactory combatTerrainFactory;
  // private Stage stage;

//  public CombatEnvironmentDisplay(Stage stage) {
//    this.stage = stage;
//    super.create();
//    addActors();
//  }

  @Override
  public void create() {
    // this.stage = stage;
    super.create();
    addActors();
  }

  /**
   * Adds a background image which takes up the entirety of the stage.
   */
  private void addActors() {
    table = new Table();
    table.setFillParent(true);
//
//    String imagePath = AnimalSelectionActions.getSelectedAnimalImagePath();
//    Entity newPlayer = PlayerFactory.createCombatPlayer(imagePath);
//    // newPlayer.setZIndex(1);
//    newPlayer.setPosition(300,300);
//    // newPlayer.addComponent(combatTerrainFactory.getCameraComponent());

    Texture texture = ServiceLocator.getResourceService().getAsset("images/combat_background_one.png", Texture.class);
    Image bg = new Image(texture);
    bg.setZIndex(0);

    Texture entity1 = ServiceLocator.getResourceService().getAsset("images/dog.png", Texture.class);
    Image player = new Image(entity1);
    player.setZIndex(1);
    player.setPosition(500,500);

    bg.setSize(stage.getWidth(), stage.getHeight());

    table.add(bg).expand();
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
