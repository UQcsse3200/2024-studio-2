package com.csse3200.game.components.gameover;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Dialog;
import com.badlogic.gdx.scenes.scene2d.ui.Window;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.csse3200.game.components.mainmenu.Slides;
import com.csse3200.game.components.settingsmenu.SettingsMenuDisplay;
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
    private TextButton toggleWindowBtn;
    private Texture backgroundTexture;

    /**
     * Called when the component is created. Initializes the Game Over lose UI.
     */
    @Override
    public void create() {
        super.create();
        logger.info("Creating GameOverLoseDisplay");
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

        // Initialises buttons
        TextButton achievementsBtn = new TextButton("Achievements", skin);
        TextButton exitBtn = new TextButton("Exit", skin);
        Label versionLabel = new Label("Version 1.0", skin);


        // Adds UI component (hover over buttons)
        addButtonElevationEffect(achievementsBtn);
        addButtonElevationEffect(exitBtn);

        // Added handles for when clicked
        achievementsBtn.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent changeEvent, Actor actor) {
                logger.debug("Achievements button clicked");
                entity.getEvents().trigger("achievements");
            }
        });

        // Added the pop up when user trys to exit game
        addExitConfirmation(exitBtn);

        // formats sizes of buttons
        table.add(achievementsBtn).padTop(15f).width(180f).height(45f);
        table.row();
        table.add(exitBtn).padTop(15f).height(45f).width(180f);
        table.row();
        table.add(versionLabel).padTop(15f);
        table.row();

        // Enables tables use
        stage.addActor(table);

        // Formats height of buttons on screen
        sizeTable();

        // Add the minimize button to the top-right corner
        addMinimizeButton();
        stage.addActor(table);
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
            toggleWindowBtn = new TextButton("-", skin); // Start with the minus (minimize) icon
        } else {
            toggleWindowBtn = new TextButton("+", skin);
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
                updateToggleWindowButtonText(); // Update text after toggling
                logger.info("Fullscreen toggled: " + !isFullscreen);
                sizeTable();
            }
        });

        Table topRightTable = new Table();
        topRightTable.top().right();
        topRightTable.setFillParent(true);
        topRightTable.add(toggleWindowBtn).size(40, 40).padTop(10).padRight(10);

        stage.addActor(topRightTable);
    }

    /**
     * Updates the text of the minimize button based on screen mode.
     */
    private void updateToggleWindowButtonText() {
        boolean isFullscreen = Gdx.graphics.isFullscreen();
        if (isFullscreen) {
            toggleWindowBtn.setText("-"); // Show minus for minimizing
        } else {
            toggleWindowBtn.setText("+"); // Show plus for maximizing
        }
    }

    /**
     * Adds an elevation effect to buttons when hovered.
     */
    private void addButtonElevationEffect(TextButton button) {
        button.addListener(new ClickListener() {
            @Override
            public void enter(InputEvent event, float x, float y, int pointer, Actor fromActor) {
                button.addAction(Actions.parallel(
                        Actions.moveBy(0, 5, 0.1f),
                        Actions.scaleTo(1.05f, 1.05f, 0.1f)
                ));
                //logger.info("Hover feature activated"); uncomment this if you want to check hover feature
            }

            @Override
            public void exit(InputEvent event, float x, float y, int pointer, Actor toActor) {
                button.addAction(Actions.parallel(
                        Actions.moveBy(0, -5, 0.1f),
                        Actions.scaleTo(1f, 1f, 0.1f)
                ));
            }
        });
    }

    /**
     * Adds an exit confirmation dialog with an enhanced UI when the exit button is clicked.
     */
    private void addExitConfirmation(TextButton exitBtn) {
        exitBtn.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
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

                TextButton yesBtn = new TextButton("Yes", skin);
                TextButton noBtn = new TextButton("No", skin);

                yesBtn.getLabel().setFontScale(1.2f);
                noBtn.getLabel().setFontScale(1.2f);

                yesBtn.addListener(new ChangeListener() {
                    @Override
                    public void changed(ChangeEvent event, Actor actor) {
                        logger.info("Exit confirmed, closing game");
                        Gdx.app.exit();
                    }
                });

                noBtn.addListener(new ChangeListener() {
                    @Override
                    public void changed(ChangeEvent event, Actor actor) {
                        logger.info("Exit canceled");
                        dialog.hide();
                    }
                });

                dialog.getContentTable().add(confirmLabel).padBottom(40f).center();
                dialog.getButtonTable().add(yesBtn).padRight(30f).width(150f).height(60f);
                dialog.getButtonTable().add(noBtn).width(150f).height(60f);

                dialog.setPosition(
                        (Gdx.graphics.getWidth() - dialog.getWidth()) / 2,
                        (Gdx.graphics.getHeight() - dialog.getHeight()) / 2
                );
                dialog.show(stage);
            }
        });
    }

    @Override
    public void draw(SpriteBatch batch) {
        batch = new SpriteBatch();
        batch.begin();
        batch.draw(backgroundTexture, 0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        batch.end();
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
