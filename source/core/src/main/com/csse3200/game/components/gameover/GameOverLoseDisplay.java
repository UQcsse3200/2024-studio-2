package com.csse3200.game.components.gameover;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Dialog;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.csse3200.game.components.settingsmenu.SettingsMenuDisplay;
import com.csse3200.game.ui.CustomButton;
import com.csse3200.game.ui.UIComponent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.csse3200.game.components.settingsmenu.UserSettings;

/**
 * A UI component for displaying the Game Over win screen.
 */
public class GameOverLoseDisplay extends UIComponent {
    private static final Logger logger = LoggerFactory.getLogger(GameOverLoseDisplay.class);
    private static final float Z_INDEX = 2f;
    private Table table;
    private Table settingMenu;
    private SettingsMenuDisplay settingsMenuDisplay;
    private CustomButton toggleWindowBtn;
    private Texture backgroundTexture;

    /**
     * Called when the component is created. Initializes the Game Over lose UI.
     */
    @Override
    public void create() {
        super.create();
        logger.info("Creating MainMenuDisplay");
        addActors();
        applyUserSettings();
        backgroundTexture = new Texture("images/GameOverLose.png");
        logger.info("Background texture loaded");
    }

    /**
     * Applies user settings to the game.
     */
    private void applyUserSettings() {
        UserSettings.Settings settings = UserSettings.get(); // Retrieve current settings
        UserSettings.applySettings(settings); // Apply settings to the game
    }

    /**
     * Adds all UI elements (buttons, labels, etc.) to the main menu.
     */
    private void addActors() {
        table = new Table();
        table.setFillParent(true);

        settingMenu = new Table();

        // Initialises buttons
        CustomButton achievementsBtn = new CustomButton("Achievements", skin);
        CustomButton replayBtn = new CustomButton("Replay", skin);
        CustomButton exitBtn = new CustomButton("Exit", skin);
        Label versionLabel = new Label("Version 1.0", skin);

        // Added handles for when clicked
        achievementsBtn.addClickListener(() -> {
            logger.debug("Achievements button clicked");
            entity.getEvents().trigger("achievements");
        });
        // Added handles for when replay clicked
        replayBtn.addClickListener(() -> {
            logger.debug("Replay button clicked");
            entity.getEvents().trigger("replay");
        });

        // Added the pop up when user trys to exit game
        addExitConfirmation(exitBtn);

        // formats sizes of buttons
        table.add(achievementsBtn).padTop(15f).width(180f).height(45f);
        table.row();
        table.add(replayBtn).padTop(15f).height(45f).width(180f);
        table.row();
        table.add(exitBtn).padTop(15f).height(45f).width(180f);
        table.row();
        table.add(versionLabel).padTop(50f);
        table.row();

        // Enables tables use
        stage.addActor(table);

        // Formats height of buttons on screen
        sizeTable();

        // Add the minimize button to the top-right corner
        addMinimizeButton();
        stage.addActor(table);

        // Adds the setting menu to program
        addSettingMenu();
    }

    /**
     * Adjusts the size of the table based on screen mode (fullscreen or windowed).
     */
    private void sizeTable() {
        // Checks if the table is full screen
        if (Gdx.graphics.isFullscreen()) {
            // Full screen sizing
            table.setBounds(0,-325,200,1000);
        } else {
            // Small screen sizing
            table.setBounds(0,-350,200,1000);
        }
    }

    /**
     * Adds a minimize button to the top-right corner of the screen.
     * This button toggles between fullscreen and windowed mode.
     */
    private void addMinimizeButton() {
        if (Gdx.graphics.isFullscreen()) {
            toggleWindowBtn = new CustomButton("-", skin); // Start with the minus (minimize) icon
        } else {
            toggleWindowBtn = new CustomButton("+", skin);
        }

        toggleWindowBtn.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                boolean isFullscreen = Gdx.graphics.isFullscreen();
                if (isFullscreen) {
                    // Switch to windowed mode
                    Gdx.graphics.setWindowedMode(1200, 750);
                } else {
                    // Switch to fullscreen mode
                    Gdx.graphics.setFullscreenMode(Gdx.graphics.getDisplayMode());
                }
                updateSettingMenu();
                updateToggleWindowButtonText(); // Update text after toggling
                logger.info("Fullscreen toggled: {}", !isFullscreen);
                sizeTable();
            }
        });

        Table topRightTable = new Table();
        topRightTable.top().right();
        topRightTable.setFillParent(true);
        topRightTable.add(toggleWindowBtn).size(60, 60).padTop(10).padRight(10);

        stage.addActor(topRightTable);
    }

    /**
     * Updates the text of the minimize button based on screen mode.
     */
    private void updateToggleWindowButtonText() {
        boolean isFullscreen = Gdx.graphics.isFullscreen();
        if (isFullscreen) {
            toggleWindowBtn.setLabelText("-"); // Show minus for minimizing
        } else {
            toggleWindowBtn.setLabelText("+"); // Show plus for maximizing
        }
    }

    /**
     * Adds a settings menu to the screen.
     */
    private void addSettingMenu() {
        Pixmap pixmap = new Pixmap(1, 1, Pixmap.Format.RGBA8888);
        pixmap.setColor(Color.WHITE); // Set color to white
        pixmap.fill();

        // Create a Drawable from the Pixmap
        Drawable backgroundDrawable = new TextureRegionDrawable(new TextureRegion(new Texture(pixmap)));

        // Dispose of the Pixmap after creating the texture
        pixmap.dispose();

        float screenWidth = Gdx.graphics.getWidth();
        float screenHeight = Gdx.graphics.getHeight();

        settingMenu.setSize(550, 350);
        settingMenu.setBackground(backgroundDrawable);
        settingMenu.setVisible(false);

        Table topTable = new Table();
        topTable.top().padTop(10);

        Label title = new Label("Settings", skin, "title");

        topTable.add(title).expandX().center();
        topTable.row();

        CustomButton closeButton = new CustomButton("X", skin);
        topTable.add(closeButton).size(40, 40).right().padRight(10).padTop(-40);

        settingsMenuDisplay = new SettingsMenuDisplay();
        Table contentTable = settingsMenuDisplay.makeSettingsTable();

        // Create a table for the "Apply" button
        Table bottomRightTable = new Table();
        bottomRightTable.bottom(); // Align contents to bottom-right

        CustomButton applyButton = new CustomButton("Apply", skin);
        bottomRightTable.add(applyButton).size(80, 40).padBottom(10f).padRight(10f);

        settingMenu.add(topTable).expandX().fillX(); // Top-right table
        settingMenu.row().padTop(30f);
        settingMenu.add(contentTable).expandX().expandY().padLeft(50);
        settingMenu.row().padTop(30f);
        settingMenu.add(contentTable).expandX().expandY().padLeft(50);
        settingMenu.row().padTop(30f);
        settingMenu.add(contentTable).expandX().expandY().padLeft(50);
        settingMenu.row().padTop(30f);
        settingMenu.add(bottomRightTable).expandX().right().padLeft(100); // Bottom-right table

        // Center the menu on the screen
        settingMenu.setPosition(
                (screenWidth - settingMenu.getWidth()) / 2,
                (screenHeight - settingMenu.getHeight()) / 2
        );

        stage.addActor(settingMenu);

        closeButton.addClickListener(() -> {
            settingMenu.setVisible(false);
            table.setTouchable(Touchable.enabled);
        });

        // Add event listener for the "Apply" button
        applyButton.addListener(
                new ChangeListener() {
                    @Override
                    public void changed(ChangeEvent changeEvent, Actor actor) {
                        logger.info("Apply button clicked");
                        settingsMenuDisplay.applyChanges(); // Apply the settings when clicked
                        settingMenu.setVisible(false); // Optionally hide the settings menu
                        table.setTouchable(Touchable.enabled);
                    }
                });
    }

    /**
     * Updates the position of the settings menu based on screen size.
     */
    public void updateSettingMenu() {
        if (settingMenu != null) {
            // Center the menu on the screen
            float screenWidth = Gdx.graphics.getWidth();
            float screenHeight = Gdx.graphics.getHeight();
            settingMenu.setPosition(
                    (screenWidth - settingMenu.getWidth()) / 2,
                    (screenHeight - settingMenu.getHeight()) / 2
            );
        }
    }
    /**
     * Adds an exit confirmation dialog with an enhanced UI when the exit button is clicked.
     */
    private void addExitConfirmation(CustomButton exitBtn) {
        exitBtn.addClickListener(() -> {
            Pixmap pixmap = new Pixmap(1, 1, Pixmap.Format.RGBA8888);
            pixmap.setColor(Color.WHITE);
            pixmap.fill();

            Drawable dialogBackground = new TextureRegionDrawable(new TextureRegion(new Texture(pixmap)));
            pixmap.dispose();

            final Dialog dialog = new Dialog("", skin);
            dialog.setBackground(dialogBackground);
            dialog.pad(40f);
            dialog.setSize(500f, 300f);
            dialog.setModal(true);

            Label confirmLabel = new Label("Leave the game?", skin);
            confirmLabel.setColor(Color.WHITE);
            confirmLabel.setFontScale(1.5f);

            CustomButton yesBtn = new CustomButton("Yes", skin);
            CustomButton noBtn = new CustomButton("No", skin);

            yesBtn.addClickListener(() -> {
                logger.info("Exit confirmed, closing game");
                Gdx.app.exit();
            });

            noBtn.addClickListener(() -> {
                logger.info("Exit canceled");
                dialog.hide();
            });

            dialog.getContentTable().add(confirmLabel).padBottom(40f).center();
            dialog.getButtonTable().add(yesBtn).padRight(30f).width(150f).height(60f);
            dialog.getButtonTable().add(noBtn).width(150f).height(60f);

            dialog.setPosition(
                    (Gdx.graphics.getWidth() - dialog.getWidth()) / 2,
                    (Gdx.graphics.getHeight() - dialog.getHeight()) / 2
            );
            dialog.show(stage);
        });
    }

    @Override
    public void draw(SpriteBatch batch) {
        SpriteBatch batchDupe = new SpriteBatch();
        batchDupe.begin();
        batchDupe.draw(backgroundTexture, 0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        batchDupe.end();
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
