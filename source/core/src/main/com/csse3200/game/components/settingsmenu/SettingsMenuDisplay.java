package com.csse3200.game.components.settingsmenu;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Graphics.DisplayMode;
import com.badlogic.gdx.Graphics.Monitor;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Event;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.Array;
import com.csse3200.game.services.ServiceLocator;
import com.csse3200.game.ui.UIComponent;
import com.csse3200.game.utils.StringDecorator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Settings menu display and logic. If you bork the settings, they can be changed manually in
 * CSSE3200Game/settings.json under your home directory (This is C:/users/[username] on Windows).
 */
public class SettingsMenuDisplay extends UIComponent {
  private static final Logger logger = LoggerFactory.getLogger(com.csse3200.game.components.settingsmenu.SettingsMenuDisplay.class);
  //private final Optional<GdxGame> game;
  private Table rootTable;
  private TextField fpsText;
  private CheckBox fullScreenCheck;
  private Slider audioScaleSlider;
  private Slider soundScaleSlider;
  private SelectBox<StringDecorator<DisplayMode>> displayModeSelect;

  public SettingsMenuDisplay() {
    super();
    //this.game = game != null ? game : Optional.empty();
  }

  @Override
  public void create() {
    super.create();
    addActors();
  }

  private void addActors() {
    Label title = new Label("Settings", skin, "title");
    Table settingsTable = makeSettingsTable();
    Table menuBtns = makeMenuBtns();

    rootTable = new Table();
    rootTable.setFillParent(true);

    rootTable.add(title).expandX().top().padTop(20f);

    rootTable.row().padTop(30f);
    rootTable.add(settingsTable).left().padLeft(20f).expandX().expandY();

    rootTable.row();
    rootTable.add(menuBtns).fillX();

    stage.addActor(rootTable);
  }

  public Table makeSettingsTable() {
    // Get current values
    UserSettings.Settings settings = UserSettings.get();

    // Create components
    Label audioScaleLabel = new Label("Audio:", skin);
    audioScaleSlider = new Slider(0, 100, 1, false, skin);
    audioScaleSlider.setValue(settings.audioScale);
    Label audioScaleValue = new Label(String.format("%d", (int) settings.audioScale), skin);

    Label soundScaleLabel = new Label("Sound:", skin);
    soundScaleSlider = new Slider(0, 100, 1, false, skin);
    soundScaleSlider.setValue(settings.soundScale);
    Label soundScaleValue = new Label(String.format("%d", (int) settings.soundScale), skin);

    Label fpsLabel = new Label("FPS Cap:", skin);
    fpsText = new TextField(Integer.toString(settings.fps), skin);

    Label fullScreenLabel = new Label("Fullscreen:", skin);
    fullScreenCheck = new CheckBox("", skin);
    fullScreenCheck.setChecked(settings.fullscreen);

    Label displayModeLabel = new Label("Resolution:", skin);
    displayModeSelect = new SelectBox<>(skin);
    Monitor selectedMonitor = Gdx.graphics.getMonitor();
    displayModeSelect.setItems(getDisplayModes(selectedMonitor));
    displayModeSelect.setSelected(getActiveMode(displayModeSelect.getItems()));

    // Position Components on table
    Table table = new Table();
    Table audioScaleTable = new Table();
    audioScaleTable.add(audioScaleSlider).width(100).left();
    audioScaleTable.add(audioScaleValue).left().padLeft(5f).expandX();

    table.add(audioScaleLabel).left().padRight(15f);
    table.add(audioScaleTable).left();

    table.row().padTop(10f);
    Table soundScaleTable = new Table();
    soundScaleTable.add(soundScaleSlider).width(100).left();
    soundScaleTable.add(soundScaleValue).left().padLeft(5f).expandX();

    table.add(soundScaleLabel).left().padRight(15f);
    table.add(soundScaleTable).left();

    table.row().padTop(10f);
    table.add(fpsLabel).left().padRight(15f);
    table.add(fpsText).width(100).left();

    table.row().padTop(10f);
    table.add(fullScreenLabel).left().padRight(15f);
    table.add(fullScreenCheck).left();

    table.row().padTop(10f);
    table.add(displayModeLabel).left().padRight(15f);
    table.add(displayModeSelect).left();

    // Events on inputs
    audioScaleSlider.addListener(
            (Event event) -> {
              float value = audioScaleSlider.getValue();
              audioScaleValue.setText(String.format("%d", (int) value));
              return true;
            });

    soundScaleSlider.addListener(
            (Event event) -> {
              float value = soundScaleSlider.getValue();
              soundScaleValue.setText(String.format("%d", (int) value));
              return true;
            });

    return table;
  }

  private StringDecorator<DisplayMode> getActiveMode(Array<StringDecorator<DisplayMode>> modes) {
    DisplayMode active = Gdx.graphics.getDisplayMode();

    for (StringDecorator<DisplayMode> stringMode : modes) {
      DisplayMode mode = stringMode.object;
      if (active.width == mode.width
              && active.height == mode.height
              && active.refreshRate == mode.refreshRate) {
        return stringMode;
      }
    }
    return null;
  }

  private Array<StringDecorator<DisplayMode>> getDisplayModes(Monitor monitor) {
    DisplayMode[] displayModes = Gdx.graphics.getDisplayModes(monitor);
    Array<StringDecorator<DisplayMode>> arr = new Array<>();

    for (DisplayMode displayMode : displayModes) {
      arr.add(new StringDecorator<>(displayMode, this::prettyPrint));
    }

    return arr;
  }

  private String prettyPrint(DisplayMode displayMode) {
    return displayMode.width + "x" + displayMode.height + ", " + displayMode.refreshRate + "hz";
  }

  private Table makeMenuBtns() {
    TextButton exitBtn = new TextButton("Exit", skin);
    TextButton applyBtn = new TextButton("Apply", skin);

    exitBtn.addListener(
            new ChangeListener() {
              @Override
              public void changed(ChangeEvent changeEvent, Actor actor) {
                logger.debug("Exit button clicked");
              }
            });

    applyBtn.addListener(
            new ChangeListener() {
              @Override
              public void changed(ChangeEvent changeEvent, Actor actor) {
                logger.debug("Apply button clicked");
                applyChanges();
              }
            });


    Table table = new Table();
    table.add(exitBtn).expandX().left().pad(0f, 15f, 15f, 0f);
    table.add(applyBtn).expandX().right().pad(0f, 0f, 15f, 15f);
    return table;
  }

  public void applyChanges() {
    UserSettings.Settings settings = UserSettings.get();

    Integer fpsVal = parseOrNull(fpsText.getText());
    if (fpsVal != null) {
      settings.fps = fpsVal;
    }
    settings.fullscreen = fullScreenCheck.isChecked();
    settings.audioScale = audioScaleSlider.getValue();
    settings.soundScale = soundScaleSlider.getValue();
    settings.displayMode = new UserSettings.DisplaySettings(displayModeSelect.getSelected().object);

    UserSettings.set(settings, true);
  }


  private Integer parseOrNull(String num) {
    try {
      return Integer.parseInt(num, 10);
    } catch (NumberFormatException e) {
      return null;
    }
  }

  @Override
  protected void draw(SpriteBatch batch) {
    // draw is handled by the stage
  }

  @Override
  public void update() {
    stage.act(ServiceLocator.getTimeSource().getDeltaTime());
  }

  @Override
  public void dispose() {
    rootTable.clear();
    super.dispose();
  }
}



//package com.csse3200.game.components.settingsmenu;
//
//import com.badlogic.gdx.Gdx;
//import com.badlogic.gdx.Graphics.DisplayMode;
//import com.badlogic.gdx.Graphics.Monitor;
//import com.badlogic.gdx.graphics.g2d.SpriteBatch;
//import com.badlogic.gdx.scenes.scene2d.Actor;
//import com.badlogic.gdx.scenes.scene2d.Event;
//import com.badlogic.gdx.scenes.scene2d.ui.*;
//import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
//import com.badlogic.gdx.utils.Array;
//import com.csse3200.game.GdxGame;
//import com.csse3200.game.GdxGame.ScreenType;
//import com.csse3200.game.files.UserSettings;
//import com.csse3200.game.files.UserSettings.DisplaySettings;
//import com.csse3200.game.services.ServiceLocator;
//import com.csse3200.game.ui.UIComponent;
//import com.csse3200.game.utils.StringDecorator;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//
///**
// * Settings menu display and logic. If you bork the settings, they can be changed manually in
// * CSSE3200Game/settings.json under your home directory (This is C:/users/[username] on Windows).
// */
//public class SettingsMenuDisplay extends UIComponent {
//  private static final Logger logger = LoggerFactory.getLogger(SettingsMenuDisplay.class);
//  private final GdxGame game;
//
//  private Table rootTable;
//  private TextField fpsText;
//  private CheckBox fullScreenCheck;
//  private CheckBox vsyncCheck;
//  private Slider uiScaleSlider;
//  private SelectBox<StringDecorator<DisplayMode>> displayModeSelect;
//
//  public SettingsMenuDisplay(GdxGame game) {
//    super();
//    this.game = game;
//  }
//
//  @Override
//  public void create() {
//    super.create();
//    addActors();
//  }
//
//  private void addActors() {
//    Label title = new Label("Settings", skin, "title");
//    Table settingsTable = makeSettingsTable();
//    Table menuBtns = makeMenuBtns();
//
//    rootTable = new Table();
//    rootTable.setFillParent(true);
//
//    rootTable.add(title).expandX().top().padTop(20f);
//
//    rootTable.row().padTop(30f);
//    rootTable.add(settingsTable).expandX().expandY();
//
//    rootTable.row();
//    rootTable.add(menuBtns).fillX();
//
//    stage.addActor(rootTable);
//  }
//
//  private Table makeSettingsTable() {
//    // Get current values
//    UserSettings.Settings settings = UserSettings.get();
//
//    // Create components
//    Label fpsLabel = new Label("FPS Cap:", skin);
//    fpsText = new TextField(Integer.toString(settings.fps), skin);
//
//    Label fullScreenLabel = new Label("Fullscreen:", skin);
//    fullScreenCheck = new CheckBox("", skin);
//    fullScreenCheck.setChecked(settings.fullscreen);
//
//    Label vsyncLabel = new Label("VSync:", skin);
//    vsyncCheck = new CheckBox("", skin);
//    vsyncCheck.setChecked(settings.vsync);
//
//    Label uiScaleLabel = new Label("ui Scale (Unused):", skin);
//    uiScaleSlider = new Slider(0.2f, 2f, 0.1f, false, skin);
//    uiScaleSlider.setValue(settings.uiScale);
//    Label uiScaleValue = new Label(String.format("%.2fx", settings.uiScale), skin);
//
//    Label displayModeLabel = new Label("Resolution:", skin);
//    displayModeSelect = new SelectBox<>(skin);
//    Monitor selectedMonitor = Gdx.graphics.getMonitor();
//    displayModeSelect.setItems(getDisplayModes(selectedMonitor));
//    displayModeSelect.setSelected(getActiveMode(displayModeSelect.getItems()));
//
//    // Position Components on table
//    Table table = new Table();
//
//    table.add(fpsLabel).right().padRight(15f);
//    table.add(fpsText).width(100).left();
//
//    table.row().padTop(10f);
//    table.add(fullScreenLabel).right().padRight(15f);
//    table.add(fullScreenCheck).left();
//
//    table.row().padTop(10f);
//    table.add(vsyncLabel).right().padRight(15f);
//    table.add(vsyncCheck).left();
//
//    table.row().padTop(10f);
//    Table uiScaleTable = new Table();
//    uiScaleTable.add(uiScaleSlider).width(100).left();
//    uiScaleTable.add(uiScaleValue).left().padLeft(5f).expandX();
//
//    table.add(uiScaleLabel).right().padRight(15f);
//    table.add(uiScaleTable).left();
//
//    table.row().padTop(10f);
//    table.add(displayModeLabel).right().padRight(15f);
//    table.add(displayModeSelect).left();
//
//    // Events on inputs
//    uiScaleSlider.addListener(
//        (Event event) -> {
//          float value = uiScaleSlider.getValue();
//          uiScaleValue.setText(String.format("%.2fx", value));
//          return true;
//        });
//
//    return table;
//  }
//
//  private StringDecorator<DisplayMode> getActiveMode(Array<StringDecorator<DisplayMode>> modes) {
//    DisplayMode active = Gdx.graphics.getDisplayMode();
//
//    for (StringDecorator<DisplayMode> stringMode : modes) {
//      DisplayMode mode = stringMode.object;
//      if (active.width == mode.width
//          && active.height == mode.height
//          && active.refreshRate == mode.refreshRate) {
//        return stringMode;
//      }
//    }
//    return null;
//  }
//
//  private Array<StringDecorator<DisplayMode>> getDisplayModes(Monitor monitor) {
//    DisplayMode[] displayModes = Gdx.graphics.getDisplayModes(monitor);
//    Array<StringDecorator<DisplayMode>> arr = new Array<>();
//
//    for (DisplayMode displayMode : displayModes) {
//      arr.add(new StringDecorator<>(displayMode, this::prettyPrint));
//    }
//
//    return arr;
//  }
//
//  private String prettyPrint(DisplayMode displayMode) {
//    return displayMode.width + "x" + displayMode.height + ", " + displayMode.refreshRate + "hz";
//  }
//
//  private Table makeMenuBtns() {
//    TextButton exitBtn = new TextButton("Exit", skin);
//    TextButton applyBtn = new TextButton("Apply", skin);
//
//    exitBtn.addListener(
//        new ChangeListener() {
//          @Override
//          public void changed(ChangeEvent changeEvent, Actor actor) {
//            logger.debug("Exit button clicked");
//            exitMenu();
//          }
//        });
//
//    applyBtn.addListener(
//        new ChangeListener() {
//          @Override
//          public void changed(ChangeEvent changeEvent, Actor actor) {
//            logger.debug("Apply button clicked");
//            applyChanges();
//          }
//        });
//
//    Table table = new Table();
//    table.add(exitBtn).expandX().left().pad(0f, 15f, 15f, 0f);
//    table.add(applyBtn).expandX().right().pad(0f, 0f, 15f, 15f);
//    return table;
//  }
//
//  private void applyChanges() {
//    UserSettings.Settings settings = UserSettings.get();
//
//    Integer fpsVal = parseOrNull(fpsText.getText());
//    if (fpsVal != null) {
//      settings.fps = fpsVal;
//    }
//    settings.fullscreen = fullScreenCheck.isChecked();
//    settings.uiScale = uiScaleSlider.getValue();
//    settings.displayMode = new DisplaySettings(displayModeSelect.getSelected().object);
//    settings.vsync = vsyncCheck.isChecked();
//
//    UserSettings.set(settings, true);
//  }
//
//  private void exitMenu() {
//    game.setScreen(ScreenType.MAIN_MENU);
//  }
//
//  private Integer parseOrNull(String num) {
//    try {
//      return Integer.parseInt(num, 10);
//    } catch (NumberFormatException e) {
//      return null;
//    }
//  }
//
//  @Override
//  protected void draw(SpriteBatch batch) {
//    // draw is handled by the stage
//  }
//
//  @Override
//  public void update() {
//    stage.act(ServiceLocator.getTimeSource().getDeltaTime());
//  }
//
//  @Override
//  public void dispose() {
//    rootTable.clear();
//    super.dispose();
//  }
//}
