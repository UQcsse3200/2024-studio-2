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
import com.csse3200.game.services.AudioManager;
import com.csse3200.game.services.ServiceLocator;
import com.csse3200.game.ui.CustomButton;
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
    private Label audioScaleValue;
    private Label soundScaleValue;
    private SelectBox<String> musicSelectBox; // Declare as a class-level field

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
        Label audioScaleLabel = new Label("Background Music:", skin);
        audioScaleSlider = new Slider(0, 100, 1, false, skin);
        audioScaleSlider.setValue(AudioManager.getDesiredMusicVolume() * 100);  // Set slider from AudioManager
        audioScaleValue = new Label(String.format("%d", (int) (AudioManager.getDesiredMusicVolume() * 100)), skin);

        Label musicSelectLabel = new Label("Select Music:", skin);
        musicSelectBox = new SelectBox<>(skin);  // Initialize the class-level field

        // Add available music tracks (these should match the keys used in AudioManager)
        musicSelectBox.setItems("Track 1", "Track 2"); // Example music track names
        musicSelectBox.setSelected(settings.selectedMusicTrack);  // Set default selected track


        Label soundScaleLabel = new Label("Sound:", skin);
        soundScaleSlider = new Slider(0, 100, 1, false, skin);
        soundScaleSlider.setValue(AudioManager.getDesiredSoundVolume() * 100);  // Set slider from AudioManager
        soundScaleValue = new Label(String.format("%d", (int) (AudioManager.getDesiredSoundVolume() * 100)), skin);

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

        table.row().padTop(10f);
        table.add(musicSelectLabel).left().padRight(15f);
        table.add(musicSelectBox).left().expandX();
        table.row();

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

        // Listeners for sliders
        audioScaleSlider.addListener(
                (Event event) -> {
                    float value = audioScaleSlider.getValue();
                    audioScaleValue.setText(String.format("%d", (int) value));
                    AudioManager.setMusicVolume(value / 100f);  // Update AudioManager directly
                    return true;
                });

        soundScaleSlider.addListener(
                (Event event) -> {
                    float value = soundScaleSlider.getValue();
                    soundScaleValue.setText(String.format("%d", (int) value));
                    AudioManager.setSoundVolume(value / 100f);  // Update AudioManager directly
                    return true;
                });

        musicSelectBox.addListener(
                new ChangeListener() {
                    @Override
                    public void changed(ChangeEvent event, Actor actor) {
                        settings.selectedMusicTrack = musicSelectBox.getSelected();
                    }
                }
        );



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
        CustomButton exitBtn = new CustomButton("Exit", skin);
        CustomButton applyBtn = new CustomButton("Apply", skin);

        exitBtn.addClickListener(() -> {
                        logger.info("Exit button clicked");
                });

        applyBtn.addClickListener(() -> {
                        logger.info("Apply button clicked");
                        applyChanges();
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
        settings.selectedMusicTrack = musicSelectBox.getSelected();  // Save selected music track
        UserSettings.set(settings, true);

        AudioManager.setMusicVolume(settings.audioScale / 100f);
        AudioManager.setSoundVolume(settings.soundScale / 100f);

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