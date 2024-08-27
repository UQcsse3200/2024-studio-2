package com.csse3200.game;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.csse3200.game.files.UserSettings;
import com.csse3200.game.screens.*;
import com.csse3200.game.services.ServiceContainer;
import com.csse3200.game.services.ServiceLocator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.csse3200.game.services.ServiceLocator;
import static com.badlogic.gdx.Gdx.app;

/**
 * Entry point of the non-platform-specific game logic. Controls which screen is currently running.
 * The current screen triggers transitions to other screens. This works similarly to a finite state
 * machine (See the State Pattern).
 */
public class GdxGame extends Game {
  private static final Logger logger = LoggerFactory.getLogger(GdxGame.class);

  @Override
  public void create() {
    logger.info("Creating game");
    loadSettings();

    // Sets background to light yellow
    Gdx.gl.glClearColor(248f / 255f, 249 / 255f, 178 / 255f, 1);

    setScreen(ScreenType.MAIN_MENU);
  }

  /**
   * Loads the game's settings.
   */
  private void loadSettings() {
    logger.debug("Loading game settings");
    UserSettings.Settings settings = UserSettings.get();
    UserSettings.applySettings(settings);
  }

  /**
   * Sets the game's screen to a new screen of the provided type.
   * @param screenType screen type
   */
  public void setScreen(ScreenType screenType) {
    logger.info("Setting screen to {}", screenType);
    Screen currentScreen = getScreen();
    if (currentScreen != null) {
      currentScreen.dispose();
    }
    setScreen(newScreen(screenType));
  }

  /**
   * Changes to a screen that already exists, disposing of the current screen
   * @param screen to be switched to
   */
   public void setOldScreen(Screen screen, ServiceContainer container) {
    logger.info("Setting old screen: {}", screen);
    Screen currentScreen = getScreen();
    if (currentScreen != null) {
      currentScreen.dispose();
    }
    setScreen(screen);
    ServiceLocator.registerTimeSource(container.getTimeSource());
    ServiceLocator.registerPhysicsService(container.getPhysicsService());
    ServiceLocator.registerInputService(container.getInputService());
    ServiceLocator.registerResourceService(container.getResourceService());
    ServiceLocator.registerEntityService(container.getEntityService());
    ServiceLocator.registerRenderService(container.getRenderService());
    ServiceLocator.registerEventService(container.getEventService());
    screen.resume();
  }

  public void addMainGameDup() {
    addScreen(ScreenType.MAIN_GAME_DUP, getScreen());
   }

  /**
   * Changes to a new screen, does NOT dispose of old screen
   *
   * @param screenType screen type
   * @param screen Old screen if we want to remember/ return to it.
   */
  public void addScreen (ScreenType screenType, Screen screen) {
    logger.info("Adding screen: {}", screenType);
    screen.pause();
    ServiceContainer container = new ServiceContainer(ServiceLocator.getEntityService(),
            ServiceLocator.getRenderService(), ServiceLocator.getPhysicsService(),
            ServiceLocator.getTimeSource(), ServiceLocator.getInputService(),
            ServiceLocator.getResourceService(), ServiceLocator.getEventService());

    ServiceLocator.clear();
    setScreen(newScreen(screenType));
  }

  @Override
  public void dispose() {
    logger.debug("Disposing of current screen");
    getScreen().dispose();
  }

  /**
   * Create a new screen of the provided type.
   * @param screenType screen type
   * @return new screen
   */
  private Screen newScreen(ScreenType screenType) {
    switch (screenType) {
      case MAIN_MENU:
        return new MainMenuScreen(this);
      case MAIN_GAME:
        return new MainGameScreen(this);
      case MAIN_GAME_DUP:
        return new MainGameScreenDup(this, screen, null);
      case SETTINGS:
        return new SettingsScreen(this);
      case MINI_GAME_MENU_SCREEN:
          return new MiniGameMenuScreen(this);
      case LOADING_SCREEN:
        return new LoadingScreen(this);
      case ANIMAL_SELECTION:
        return new AnimalSelectionScreen(this);

      default:
        return null;
    }
  }

  public enum ScreenType {
      MAIN_MENU, MAIN_GAME, MAIN_GAME_DUP, SETTINGS, MINI_GAME_MENU_SCREEN, LOADING_SCREEN, ANIMAL_SELECTION
  }

  /**
   * Exit the game.
   */
  public void exit() {
    app.exit();
  }
}
