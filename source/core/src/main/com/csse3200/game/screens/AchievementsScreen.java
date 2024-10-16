package com.csse3200.game.screens;

import com.badlogic.gdx.scenes.scene2d.Stage;
import com.csse3200.game.GdxGame;
import com.csse3200.game.components.quests.AchievementDisplay;
import com.csse3200.game.entities.Entity;
import com.csse3200.game.input.InputDecorator;
import com.csse3200.game.input.InputService;
import com.csse3200.game.services.ResourceService;
import com.csse3200.game.services.ServiceLocator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AchievementsScreen extends ResizableScreen {
  private static final Logger logger = LoggerFactory.getLogger(AchievementsScreen.class);
  private final GdxGame game;
  

  public AchievementsScreen(GdxGame game) {
    super();
    this.game = game;
    logger.debug("Initialising achievement screen services");
    ServiceLocator.registerInputService(new InputService());
    ServiceLocator.registerResourceService(new ResourceService());

    renderer.getCamera().getEntity().setPosition(5f, 5f);
    createUI();
  }

  /**
   * Creates the achievement screen's ui including components for rendering ui elements to the screen and
   * capturing and handling ui input.
   */
  private void createUI() {
    logger.debug("Creating ui");
    Stage stage = ServiceLocator.getRenderService().getStage();
    Entity ui = new Entity();
    ui.addComponent(new AchievementDisplay(game)).addComponent(new InputDecorator(stage, 10));

    ServiceLocator.getEntityService().register(ui);
  }
}