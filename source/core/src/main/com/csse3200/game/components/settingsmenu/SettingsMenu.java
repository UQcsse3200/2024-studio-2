package com.csse3200.game.components.settingsmenu;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Graphics.DisplayMode;
import com.badlogic.gdx.Graphics.Monitor;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Event;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Array;
import com.csse3200.game.components.mainmenu.MainMenuDisplay;
import com.csse3200.game.services.AudioManager;
import com.csse3200.game.services.ServiceLocator;
import com.csse3200.game.ui.CustomButton;
import com.csse3200.game.ui.UIComponent;
import com.csse3200.game.utils.StringDecorator;
import com.sun.tools.javac.Main;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SettingsMenu extends UIComponent {
    private Window settingsWindow;
    private TextField fpsText;
    private CheckBox fullScreenCheck;
    private Slider audioScaleSlider;
    private Slider soundScaleSlider;
    private SelectBox<StringDecorator<DisplayMode>> displayModeSelect;
    private Label audioScaleValue;
    private Label soundScaleValue;
    private SelectBox<String> musicSelectBox;
    private Skin skin;
    private Texture backgroundTexture;
    private MainMenuDisplay mainMenuDisplay;

    public SettingsMenu(MainMenuDisplay mainMenuDisplay) {
        super();
        this.mainMenuDisplay = mainMenuDisplay;
    }

    @Override
    public void create() {
        super.create();
        skin = new Skin(Gdx.files.internal("flat-earth/skin/flat-earth-ui.json"));
        backgroundTexture = new Texture("images/SettingBackground.png");
        setupSettingsMenu();
    }

    /**
     * Sets up the settings menu inside a Window.
     */
    private void setupSettingsMenu() {
        settingsWindow = new Window("", skin);  // Create a window without a title for more flexibility
        settingsWindow.setMovable(true);
        settingsWindow.setResizable(true);
        settingsWindow.setSize(400, 400);  // Adjusted window size
        settingsWindow.setModal(true);

        // Set background for the window
        settingsWindow.setBackground(new TextureRegionDrawable(backgroundTexture));

        // Add the "Settings" heading at the top
        Label titleLabel = new Label("Settings", skin, "title");
        Table titleTable = new Table();
        titleTable.add(titleLabel).center().padTop(30f).expandX();  // Center the title and add padding
        settingsWindow.add(titleTable).expandX().top().row();  // Add the title table
        settingsWindow.row();

        // Close button like in HelpWindow, aligned to top-right
        Button closeButton = createCloseButton();
        settingsWindow.getTitleTable().add(closeButton).size(100f, 100f).right().padTop(15f).padRight(10f);  // Close button aligned properly

        // Create the settings table that contains sliders, checkboxes, etc.
        Table settingsTable = makeSettingsTable();

        // Add the settings table to the window
        settingsWindow.add(settingsTable).expand().center().pad(20f).row();


        // Add the "Apply" button at the bottom right
        Table bottomTable = createBottomTable();
        settingsWindow.add(bottomTable).bottom().expandX().right().pad(20).row();

        settingsWindow.pack();
        settingsWindow.setVisible(false);
        stage.addActor(settingsWindow);
    }
    private Button createCloseButton() {
        Button closeButton = new Button(new Image(new Texture("images/CloseButton.png")).getDrawable());
        closeButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                applyChanges();
                mainMenuDisplay.setMenuTouchable();
                hideSettingsMenu();  // Hide the settings menu when the close button is clicked
            }
        });
        return closeButton;
    }

    /**
     * Constructs the table containing all the settings components (audio, video, performance).
     *
     * @return The settings table.
     */
    public Table makeSettingsTable() {
        UserSettings.Settings settings = UserSettings.get();

        // Create all components
        Label audioScaleLabel = new Label("Background Music:", skin);
        audioScaleSlider = new Slider(0, 100, 1, false, skin);
        audioScaleSlider.setValue(AudioManager.getDesiredMusicVolume() * 100);
        audioScaleValue = new Label(String.format("%d", (int) (AudioManager.getDesiredMusicVolume() * 100)), skin);

        Label musicSelectLabel = new Label("Select Music:", skin);
        musicSelectBox = new SelectBox<>(skin);
        musicSelectBox.setItems("Track 1", "Track 2");
        musicSelectBox.setSelected(settings.selectedMusicTrack);

        Label soundScaleLabel = new Label("Sound Effects:", skin);
        soundScaleSlider = new Slider(0, 100, 1, false, skin);
        soundScaleSlider.setValue(AudioManager.getDesiredSoundVolume() * 100);
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

        // Build table and position components centrally
        Table table = new Table();
        table.center().pad(10f);

        // Add components in top-down fashion
        table.add(musicSelectLabel).left().padRight(15f).padBottom(10f);
        table.add(musicSelectBox).expandX().fillX().padBottom(10f);
        table.row();

        table.add(audioScaleLabel).left().padRight(15f).padBottom(10f);
        table.add(audioScaleSlider).width(200).padBottom(10f);
        table.row();

        table.add(soundScaleLabel).left().padRight(15f).padBottom(10f);
        table.add(soundScaleSlider).width(200).padBottom(10f);
        table.row();

        table.add(fpsLabel).left().padRight(15f).padBottom(10f);
        table.add(fpsText).width(100).padBottom(10f);
        table.row();

        table.add(fullScreenLabel).left().padRight(15f).padBottom(10f);
        table.add(fullScreenCheck).left().padBottom(10f);
        table.row();

        table.add(displayModeLabel).left().padRight(15f).padBottom(10f);
        table.add(displayModeSelect).left().padBottom(10f);

        // Add listeners for interactive elements
        setupListeners();

        return table;
    }

    /**
     * Constructs the bottom table with the Apply button.
     *
     * @return The bottom table.
     */
    // CreateBottomTable method with Apply button and formatting applied.
    public Table createBottomTable() {
        Table bottomTable = new Table();

        CustomButton applyButton = new CustomButton("Apply", skin);
        applyButton.setButtonSize(Math.min(1000f, Gdx.graphics.getWidth() - 100f) * 0.25f, 50f);  // Set button to 20% width, 50px height
        bottomTable.add(applyButton).right().size(Math.min(1000f, Gdx.graphics.getWidth() - 100f) * 0.25f, 50f).pad(10f);  // Position at the bottom right

        // Apply button listener
        applyButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                applyChanges();  // Apply the changes
                hideSettingsMenu();  // Hide the settings menu after applying
            }
        });

        return bottomTable;
    }
    /**
     * Sets up listeners for sliders and select boxes to handle user interaction.
     */
    private void setupListeners() {
        // Update background music volume
        audioScaleSlider.addListener(
                (Event event) -> {
                    float value = audioScaleSlider.getValue();
                    audioScaleValue.setText(String.format("%d", (int) value));
                    AudioManager.setMusicVolume(value / 100f);
                    return true;
                });

        // Update sound effects volume
        soundScaleSlider.addListener(
                (Event event) -> {
                    float value = soundScaleSlider.getValue();
                    soundScaleValue.setText(String.format("%d", (int) value));
                    AudioManager.setSoundVolume(value / 100f);
                    return true;
                });

        // Save selected music track
        musicSelectBox.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                UserSettings.get().selectedMusicTrack = musicSelectBox.getSelected();
            }
        });
    }

    private StringDecorator<DisplayMode> getActiveMode(Array<StringDecorator<DisplayMode>> modes) {
        DisplayMode active = Gdx.graphics.getDisplayMode();
        for (StringDecorator<DisplayMode> stringMode : modes) {
            DisplayMode mode = stringMode.object;
            if (active.width == mode.width && active.height == mode.height && active.refreshRate == mode.refreshRate) {
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
        settings.selectedMusicTrack = musicSelectBox.getSelected();
        UserSettings.set(settings, true);

        AudioManager.setMusicVolume(settings.audioScale / 100f);
        AudioManager.setSoundVolume(settings.soundScale / 100f);
        UserSettings.applyDisplayMode(settings);
    }

    private Integer parseOrNull(String num) {
        try {
            return Integer.parseInt(num);
        } catch (NumberFormatException e) {
            return null;
        }
    }

    public void showSettingsMenu() {
        settingsWindow.setVisible(true);
        settingsWindow.setTouchable(Touchable.enabled);
        settingsWindow.toFront();
        settingsWindow.setPosition(
                (Gdx.graphics.getWidth() - settingsWindow.getWidth()) / 2,
                (Gdx.graphics.getHeight() - settingsWindow.getHeight()) / 2
        );
    }

    public void hideSettingsMenu() {
        settingsWindow.setVisible(false);
        settingsWindow.setTouchable(Touchable.disabled);
    }

    @Override
    public void update() {
        stage.act(ServiceLocator.getTimeSource().getDeltaTime());
    }

    @Override
    public void dispose() {
        settingsWindow.clear();
        super.dispose();
    }

    @Override
    protected void draw(SpriteBatch batch) {
        // draw handled by stage
    }
}
