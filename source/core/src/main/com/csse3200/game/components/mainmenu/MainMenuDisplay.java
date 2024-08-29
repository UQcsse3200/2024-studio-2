package com.csse3200.game.components.mainmenu;

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
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.csse3200.game.services.ServiceLocator;
import com.csse3200.game.components.settingsmenu.SettingsMenuDisplay;
import com.csse3200.game.ui.UIComponent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.csse3200.game.components.settingsmenu.UserSettings;

/**
 * A UI component for displaying the Main menu.
 */
public class MainMenuDisplay extends UIComponent {
    private static final Logger logger = LoggerFactory.getLogger(MainMenuDisplay.class);
    private static final float Z_INDEX = 2f;
    private Table table;
    private Table settingMenu;
    private SettingsMenuDisplay settingsMenuDisplay;
    private TextButton toggleWindowBtn;
    private Texture backgroundTexture;

    /**
     * Called when the component is created. Initializes the main menu UI.
     */
    @Override
    public void create() {
        super.create();
        logger.info("Creating MainMenuDisplay");
        addActors();
        applyUserSettings();
        backgroundTexture = new Texture("images/BackgroundSplash.png");
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
        TextButton startBtn = new TextButton("Start", skin);
        TextButton loadBtn = new TextButton("Load", skin);
        TextButton minigamesBtn = new TextButton("Minigames", skin); // New Minigames button
        TextButton combatBtn = new TextButton("Combat", skin);
        TextButton settingsBtn = new TextButton("Settings", skin);
        TextButton achievementsBtn = new TextButton("Achievements", skin);
        TextButton helpBtn = new TextButton("Help", skin);
        TextButton exitBtn = new TextButton("Exit", skin);
        Label versionLabel = new Label("Version 1.0", skin);

        // Adds UI component (hover over buttons)
        addButtonElevationEffect(startBtn);
        addButtonElevationEffect(loadBtn);
        addButtonElevationEffect(minigamesBtn); // Apply the elevation effect to Minigames button
        addButtonElevationEffect(combatBtn);
        addButtonElevationEffect(settingsBtn);
        addButtonElevationEffect(achievementsBtn);
        addButtonElevationEffect(helpBtn);
        addButtonElevationEffect(exitBtn);

        // Added handles for when clicked
        startBtn.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent changeEvent, Actor actor) {
                logger.info("Start button clicked");
                entity.getEvents().trigger("start");
            }
        });

        // Added handles for when clicked
        loadBtn.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent changeEvent, Actor actor) {
                logger.info("Load button clicked");
                entity.getEvents().trigger("load");
            }
        });

        // Added handles for when clicked
        minigamesBtn.addListener(
                new ChangeListener() {
                    @Override
                    public void changed(ChangeEvent changeEvent, Actor actor) {

                        logger.debug("SnakeGame button clicked");
                        entity.getEvents().trigger("SnakeGame");
                    }
                });

        combatBtn.addListener(
                new ChangeListener() {
                    @Override
                    public void changed(ChangeEvent changeEvent, Actor actor) {

                        logger.debug("Combat button clicked");
                        entity.getEvents().trigger("combatPop");
                    }
                });

        // Added handles for when clicked
        settingsBtn.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent changeEvent, Actor actor) {
                logger.info("Settings button clicked");
                settingMenu.setVisible(true);
                table.setTouchable(Touchable.disabled);
            }
        });

        // Added handles for when clicked
        achievementsBtn.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent changeEvent, Actor actor) {
                logger.debug("Achievements button clicked");
                entity.getEvents().trigger("achievements");
            }
        });

        // Added handles for when clicked
        helpBtn.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent changeEvent, Actor actor) {
                logger.info("Help button clicked");
                entity.getEvents().trigger("help");
                showHelpWindow();
            }

        });

        // Added the pop up when user trys to exit game
        addExitConfirmation(exitBtn);

        // formats sizes of buttons
        table.add(startBtn).padTop(15f).height(45f).width(180f);
        table.row();
        table.add(loadBtn).padTop(15f).height(45f).width(180f);
        table.row();
        table.add(achievementsBtn).padTop(15f).width(180f).height(45f);
        table.row();
        table.add(minigamesBtn).padTop(15f).width(180f).height(45f); // Add the Minigames button to the layout
        table.row();
        table.add(combatBtn).padTop(15f).height(45f).width(180f);
        table.row();
        table.add(settingsBtn).padTop(15f).height(45f).width(180f);
        table.row();
        table.add(helpBtn).padTop(15f).height(45f).width(180f);
        table.row();
        table.add(exitBtn).padTop(15f).height(45f).width(180f);
        table.row();
        table.add(versionLabel).padTop(140f);

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
            table.setBounds(0,-215,200,1000);
        } else {
            // Small screen sizing
            table.setBounds(0,-230,200,1000);
        }
    }

    /**
     * Displays the help window with slides for game instructions.
     */
    private void showHelpWindow() {
        final int NUM_SLIDES = 5;
        final float WINDOW_WIDTH = Math.min(1200f, Gdx.graphics.getWidth() - 100);
        final float WINDOW_HEIGHT = Math.min(800f, Gdx.graphics.getHeight() - 100);

        // Create a Window for the help screen
        final Window helpWindow = new Window("Help", skin);
        helpWindow.setSize(WINDOW_WIDTH, WINDOW_HEIGHT);
        helpWindow.setResizable(true);
        helpWindow.setMovable(true);

        // Create a table to hold all slides
        final Table slideTable = new Table();
        slideTable.setFillParent(true);

        // Create slide instances
        Table[] slideInstances = new Table[NUM_SLIDES];
        slideInstances[0] = new Slides.MovementSlide(skin);
        slideInstances[1] = new Slides.CombatSlide(skin);
        slideInstances[2] = new Slides.StorylineSlide(skin);
        slideInstances[3] = new Slides.MinigamesSlide(skin);
        slideInstances[4] = new Slides.StatsSlide(skin);

        // Add the first slide to the slideTable
        slideTable.add(slideInstances[0]).expand().fill().row();

        logger.info("Help window opened, displaying Movement slide");

        // Create a table for navigation buttons
        Table navigationTable = new Table();
        TextButton previousButton = new TextButton("Previous", skin);
        TextButton nextButton = new TextButton("Next", skin);
        navigationTable.add(previousButton).padRight(10);
        navigationTable.add(nextButton);

        // Create a table for the close button
        Table closeButtonTable = new Table();
        TextButton closeButton = new TextButton("X", skin);
        closeButtonTable.add(closeButton).size(40, 40).right().pad(10);

        // Add the close button table to the top-right of the helpWindow
        helpWindow.add(closeButtonTable).top().right().pad(1).row();
        helpWindow.add().row();

        // Add the slideTable to the helpWindow and position it to fill the window
        helpWindow.add(slideTable).expand().fill().row();

        // Add the navigation table to the bottom of the helpWindow
        helpWindow.add(navigationTable).bottom().expandX().fillX().pad(10).row();

        final int[] currentSlide = {0};

        // Handles when slide change is clicked
        previousButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                if (currentSlide[0] > 0) {
                    slideInstances[currentSlide[0]].setVisible(false);
                    currentSlide[0]--;
                    slideInstances[currentSlide[0]].setVisible(true);
                    slideTable.clear(); // Clear the table
                    slideTable.add(slideInstances[currentSlide[0]]).expand().fill(); // Add the current slide
                    logger.info("Slide changed to: " + (currentSlide[0] + 1));
                }
            }
        });

        // Handles when slide change is clicked
        nextButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                if (currentSlide[0] < NUM_SLIDES - 1) {
                    slideInstances[currentSlide[0]].setVisible(false);
                    currentSlide[0]++;
                    slideInstances[currentSlide[0]].setVisible(true);
                    slideTable.clear(); // Clear the table
                    slideTable.add(slideInstances[currentSlide[0]]).expand().fill(); // Add the current slide
                    logger.info("Slide changed to: " + (currentSlide[0] + 1));
                }
            }
        });

        // Handles when help menu is exited
        closeButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                helpWindow.remove(); // Close the help window
                logger.info("Help window closed");
            }
        });

        // Initially show only the first slide
        slideInstances[0].setVisible(true);
        // Initially hide all slides except the first
        for (int i = 1; i < NUM_SLIDES; i++) {
            slideInstances[i].setVisible(false);
        }

        slideTable.clear(); // Clear any existing slides
        slideTable.add(slideInstances[0]).expand().fill(); // Add the first slide

        // Center the window on the stage
        helpWindow.setPosition(
                (stage.getWidth() - helpWindow.getWidth()) / 2,
                (stage.getHeight() - helpWindow.getHeight()) / 2
        );

        // Add an InputListener to handle keyboard input
        helpWindow.addListener(new InputListener() {
            @Override
            public boolean keyDown(InputEvent event, int keycode) {
                switch (keycode) {
                    case Input.Keys.LEFT:
                        if (currentSlide[0] > 0) {
                            slideInstances[currentSlide[0]].setVisible(false);
                            currentSlide[0]--;
                            slideInstances[currentSlide[0]].setVisible(true);
                            slideTable.clear(); // Clear the table
                            slideTable.add(slideInstances[currentSlide[0]]).expand().fill(); // Add the current slide
                            logger.info("Slide changed to: " + (currentSlide[0] + 1) + " (via LEFT key)");
                        }
                        return true;
                    case Input.Keys.RIGHT:
                        if (currentSlide[0] < NUM_SLIDES - 1) {
                            slideInstances[currentSlide[0]].setVisible(false);
                            currentSlide[0]++;
                            slideInstances[currentSlide[0]].setVisible(true);
                            slideTable.clear(); // Clear the table
                            slideTable.add(slideInstances[currentSlide[0]]).expand().fill(); // Add the current slide
                            logger.info("Slide changed to: " + (currentSlide[0] + 1) + " (via RIGHT key)");
                        }
                        return true;
                    default:
                        return false;
                }
            }
        });

        // Show the window
        stage.addActor(helpWindow);
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
                updateSettingMenu();
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

        TextButton closeButton = new TextButton("X", skin);
        topTable.add(closeButton).size(40, 40).right().padRight(10).padTop(-40);

        settingsMenuDisplay = new SettingsMenuDisplay();
        Table contentTable = settingsMenuDisplay.makeSettingsTable();

        // Create a table for the "Apply" button
        Table bottomRightTable = new Table();
        bottomRightTable.bottom(); // Align contents to bottom-right

        TextButton applyButton = new TextButton("Apply", skin);
        bottomRightTable.add(applyButton).size(80, 40).padBottom(10f).padRight(10f);

        settingMenu.add(topTable).expandX().fillX(); // Top-right table
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

        closeButton.addListener(
                new ChangeListener() {
                    @Override
                    public void changed(ChangeEvent changeEvent, Actor actor) {
                        settingMenu.setVisible(false);
                        table.setTouchable(Touchable.enabled);
                    }
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
