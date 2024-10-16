package com.csse3200.game.screens;

import com.badlogic.gdx.scenes.scene2d.Stage;
import com.csse3200.game.GdxGame;
import com.csse3200.game.components.animal.BackgroundImage;
import com.csse3200.game.components.loading.LoadingDisplay;
import com.csse3200.game.entities.Entity;
import com.csse3200.game.input.InputDecorator;
import com.csse3200.game.input.InputService;
import com.csse3200.game.services.GameTime;
import com.csse3200.game.services.ResourceService;
import com.csse3200.game.services.ServiceLocator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/** The game screen containing the settings. */
public class LoadingScreen extends ResizableScreen {
  private static final Logger logger = LoggerFactory.getLogger(LoadingScreen.class);

  private final GdxGame game;
  private LoadingDisplay loadingDisplay;

  public LoadingScreen(GdxGame game) {
    super();
    
    this.game = game;

    logger.debug("Initialising loading screen services");
    ServiceLocator.registerInputService(new InputService());
    ServiceLocator.registerResourceService(new ResourceService());
    ServiceLocator.registerTimeSource(new GameTime());

    renderer.getCamera().getEntity().setPosition(5f, 5f);

    createUI();
  }
  @Override
  public void render(float delta) {
    super.render(delta);
    if (loadingDisplay.isLoadingFinished()) {
      game.setScreen(GdxGame.ScreenType.MAIN_GAME);
    }
  }
  
  /**
   * Creates the setting screen's ui including components for rendering ui elements to the screen
   * and capturing and handling ui input.
   */
  private void createUI() {
    logger.debug("Creating ui");
    Stage stage = ServiceLocator.getRenderService().getStage();
    Entity ui = new Entity();
    BackgroundImage loadingScreenImage = new BackgroundImage("images/animal/shootingstar3.jpeg");
    stage.addActor(loadingScreenImage);
    loadingDisplay = new LoadingDisplay();
    ui.addComponent(loadingDisplay).addComponent(new InputDecorator(stage, 10));
    ServiceLocator.getEntityService().register(ui);
  }
}
