package com.csse3200.game;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.csse3200.game.files.UserSettings;
import com.csse3200.game.screens.MainGameScreen;
import com.csse3200.game.screens.MainGameScreenDup;
import com.csse3200.game.screens.MainMenuScreen;
import com.csse3200.game.screens.SettingsScreen;
import com.csse3200.game.services.ServiceLocator;
import com.csse3200.game.services.eventservice.EventService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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
    Gdx.gl.glClearColor(248f/255f, 249/255f, 178/255f, 1);

    setScreen(ScreenType.MAIN_MENU);

    ServiceLocator.registerEventService(new EventService());
    System.out.println("Gets here Number 4!");

    ServiceLocator.getEventService().globalEventHandler.addListener("overlay",this::overlayMainGameDup);
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
    logger.info("Setting game screen to {}", screenType);
    Screen currentScreen = getScreen();
    if (currentScreen != null) {
      currentScreen.dispose();
    }
    setScreen(newScreen(screenType, null));
  }

  /**
   * Changes to a screen that already exists, disposing of the current screen
   * @param screen to be switched to
   */
   public void setOldScreen(Screen screen) {
    logger.info("Setting game screen to {}", screen);
    Screen currentScreen = getScreen();
    if (currentScreen != null) {
      currentScreen.dispose();
    }
    setScreen(screen);
  }

  public void overlayMainGameDup() {
    System.out.println("Gets here Number 3!");

    overlayScreen(ScreenType.MAIN_GAME_DUP, getScreen());
   }

  /**
   * Changes to a new screen, does NOT dispose of old screen
   *
   * @param screenType screen type
   * @param screen Old screen if we want to remember/ return to it.
   */
  public void overlayScreen (ScreenType screenType, Screen screen) {
    logger.info("Setting game screen to {}", screenType);
    setScreen(newScreen(screenType, screen));
  }

  @Override
  public void dispose() {
    logger.debug("Disposing of current screen");
    getScreen().dispose();
  }

  /**
   * Create a new screen of the provided type.
   * @param screenType screen type
   * @param screen for returning to an old screen, may be null.
   * @return new screen
   */
  private Screen newScreen(ScreenType screenType, Screen screen) {
    switch (screenType) {
      case MAIN_MENU:
        return new MainMenuScreen(this);
      case MAIN_GAME:
        return new MainGameScreen(this);
      case MAIN_GAME_DUP:
        return new MainGameScreenDup(this, screen);
      case SETTINGS:
        return new SettingsScreen(this);
      default:
        return null;
    }
  }

  public enum ScreenType {
    MAIN_MENU, MAIN_GAME, MAIN_GAME_DUP, SETTINGS
  }

  /**
   * Exit the game.
   */
  public void exit() {
    app.exit();
  }
}
