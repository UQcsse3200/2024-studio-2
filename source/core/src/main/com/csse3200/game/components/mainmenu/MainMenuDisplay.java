package com.csse3200.game.components.mainmenu;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Cursor;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.csse3200.game.components.login.LoginRegisterDisplay;
import com.csse3200.game.services.NotifManager;
import com.csse3200.game.components.settingsmenu.SettingsMenuDisplay;
import com.csse3200.game.ui.UIComponent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.csse3200.game.components.settingsmenu.UserSettings;
import com.csse3200.game.services.AudioManager;

/**
 * A UI component for displaying the Main menu.
 */
public class MainMenuDisplay extends UIComponent {
    private static final Logger logger = LoggerFactory.getLogger(MainMenuDisplay.class);
    private static final float Z_INDEX = 2f;
    private Table table;
    private Table menuButtonTable;
    private Table settingMenu;
    private Table userTable;
    private Table loginRegisterTable;
    private SettingsMenuDisplay settingsMenuDisplay;
    private LoginRegisterDisplay loginRegisterDisplay;
    private TextButton toggleWindowBtn;
    private Texture lightBackgroundTexture;
    private Texture settingBackground;
    private Texture userTableBackground;
    private Button muteButton;  // Mute toggle button with texture
    private Texture muteTexture;  // Texture for mute state
    private Texture unmuteTexture;  // Texture for unmute state
    private Texture dog2Texture;
    private Texture crocTexture;
    private Texture birdTexture;
    private Image dog2Image;
    private Image crocImage;
    private Image birdImage;
    private boolean isNightMode = false; // A flag to track whether night mode is enabled
    private Texture nightBackgroundTexture;
    private Sound clickSound; // Loaded click sound file for buttons

    private Button startBtn;
    private Button loadBtn;
    private Button minigamesBtn;
    private Button settingsBtn;
    private TextButton achievementsBtn;
    private Button helpBtn;
    private Button exitBtn;
    private Label versionLabel;
    private final float windowButtonWidth = 180;
    private final float windowButtonHeight = 45;
    private final float windowButtonSpacing = 15;

    private final float fullScreenButtonWidth = 360;
    private final float fullScreenuttonHeight = 90;
    private final float fullScreenButtonSpacing = 45;

    private Label startLabel;
    private Label loadLabel;
    private Label minigameLabel;
    private Label helpLabel;
    private Label settingLabel;
    private Label exitLabel;

    /**
     * Called when the component is created. Initializes the main menu UI.
     */
    @Override
    public void create() {
        super.create();
        logger.info("Creating MainMenuDisplay");
        loadTextures();  // Load textures for the mute button
        logger.info("Background texture loaded");
        addActors();
        animateAnimals();
        applyUserSettings();
    }

    /**
     * Load the textures for the mute and unmute button states.
     */
    private void loadTextures() {
        settingBackground = new Texture("images/SettingBackground.png");
        lightBackgroundTexture = new Texture("images/SplashScreen/SplashTitle.png");
        userTableBackground = new Texture("images/UserTable.png");
        muteTexture = new Texture("images/sound_off.png");  // Add your mute icon here
        unmuteTexture = new Texture("images/sound_on.png");  // Add your unmute icon here
        dog2Texture = new Texture("images/dog2.png");
        crocTexture = new Texture("images/croc.png");
        birdTexture = new Texture("images/bird.png");
        nightBackgroundTexture = new Texture("images/SplashScreen/SplashTitleNight1.png"); // Night background
        clickSound = Gdx.audio.newSound(Gdx.files.internal("sounds/click.mp3")); // Click sound for buttons
    }

    /**
     * Applies user settings to the game.
     */
    private void applyUserSettings() {
        UserSettings.Settings settings = UserSettings.get(); // Retrieve current settings
        UserSettings.applySettings(settings); // Apply settings to the game
    }

    /**
     * Animates the dog2, croc, and bird images to move across the screen repeatedly.
     */
    private void animateAnimals() {
        float screenHeight = Gdx.graphics.getHeight();
//
//        // Use the generic method to animate each animal
//        animateAnimal(dog2Image, screenHeight / 3, 10f);  // Dog moves in 10 seconds
//        animateAnimal(crocImage, screenHeight / 2, 8f);   // Crocodile moves in 8 seconds
        animateAnimal(birdImage, 2 * screenHeight / 3, 6f); // Bird moves in 6 seconds
    }


    /**
     * Adds an animation to the specified animal image to move from left to right across the screen and repeat forever.
     *
     * @param image    The animal image to animate.
     * @param startY   The starting Y position of the animal.
     * @param moveTime The time it takes for the animal to move across the screen.
     */
    private void animateAnimal(Image image, float startY, float moveTime) {
        float screenWidth = Gdx.graphics.getWidth();
        float screenHeight = Gdx.graphics.getHeight();

        // Set starting position (off-screen to the left)
        image.setPosition(-image.getWidth(), startY);

        // Animate the animal to move from left to right and repeat forever
        image.addAction(Actions.forever(Actions.sequence(
                Actions.moveTo(screenWidth, startY, moveTime),  // Move across the screen in moveTime seconds
                Actions.moveTo(-image.getWidth(), startY)       // Reset position to the left
        )));
    }


    /**
     * Adds all UI elements (buttons, labels, etc.) to the main menu.
     */
    private void addActors() {
        initializeTables();
        initializeImages();
        initializeMenuButtons();
        initializeLabels();

        stage.addActor(NotifManager.addNotificationTable());

        addMenuButtonEffects();
        addMenuButtonsListeners();
        addExitConfirmation();
        addMenuButtonIcon();
        addTopLeftToggle();
        addTopRightButtons();
        addSettingMenu();
        addUserTable();
        addLoginRegisterTable();
    }

    private void initializeLabels() {
        startLabel = new Label("Start", skin, "button-red");
        loadLabel = new Label("Load", skin, "button-red");
        minigameLabel = new Label("Minigame", skin, "button-red");
        helpLabel = new Label("Help", skin, "button-red");
        settingLabel = new Label("Setting", skin, "button-red");
        exitLabel = new Label("Exit", skin, "button-red");

    }


    /**
     * Initialize all tables in the main menu
     */
    private void initializeTables() {
        table = new Table();
        menuButtonTable = new Table();
        settingMenu = new Table();
        userTable = new Table();
        loginRegisterTable = new Table();
    }

    /**
     * Initialize all images in the main menu
     */
    private void initializeImages() {
        // Create Image actors for the animals
        //dog2Image = new Image(dog2Texture);
        //crocImage = new Image(crocTexture);
        birdImage = new Image(birdTexture);

        // Add animal images to the stage
        //stage.addActor(dog2Image);
        //stage.addActor(crocImage);
        stage.addActor(birdImage);
    }

    /**
     * Initialize menu buttons in the main menu
     */
    private void initializeMenuButtons() {
        // Initialises buttons
        startBtn = new Button(new TextureRegionDrawable(new TextureRegion(new Texture("images/ButtonsMain/BlankLarge.png"))));
        loadBtn = new Button(new TextureRegionDrawable(new TextureRegion(new Texture("images/ButtonsMain/BlankLarge.png"))));
        minigamesBtn = new Button(new TextureRegionDrawable(new TextureRegion(new Texture("images/ButtonsMain/BlankLarge.png"))));
        settingsBtn = new Button(new TextureRegionDrawable(new TextureRegion(new Texture("images/ButtonsMain/BlankLarge.png"))));
        achievementsBtn = new TextButton("Achievements", skin);
        helpBtn = new Button(new TextureRegionDrawable(new TextureRegion(new Texture("images/ButtonsMain/BlankLarge.png"))));
        exitBtn = new Button(new TextureRegionDrawable(new TextureRegion(new Texture("images/ButtonsMain/BlankLarge.png"))));
    }

    /**
     * Adds UI component (hover over buttons)
     */
    private void addMenuButtonEffects() {
        addButtonElevationEffect(startBtn, startLabel);
        addButtonElevationEffect(loadBtn, loadLabel);
        addButtonElevationEffect(minigamesBtn, minigameLabel);
        addButtonElevationEffect(settingsBtn, settingLabel);
        addButtonElevationEffect(helpBtn, helpLabel);
        addButtonElevationEffect(exitBtn, exitLabel);
    }

    /**
     * Adds an elevation effect to buttons when hovered.
     */
    private void addButtonElevationEffect(Button button, Label label) {
        button.addListener(new ClickListener() {
            @Override
            public void enter(InputEvent event, float x, float y, int pointer, Actor fromActor) {
                Gdx.graphics.setSystemCursor(Cursor.SystemCursor.Hand);
                button.addAction(Actions.parallel(
                        Actions.moveBy(0, 5, 0.1f),
                        Actions.scaleTo(1.05f, 1.05f, 0.1f)
                ));
                label.addAction(Actions.parallel(
                        Actions.moveBy(0, 5, 0.1f),
                        Actions.scaleTo(1.05f, 1.05f, 0.1f)
                ));
            }

            @Override
            public void exit(InputEvent event, float x, float y, int pointer, Actor toActor) {
                Gdx.graphics.setSystemCursor(Cursor.SystemCursor.Arrow);
                button.addAction(Actions.parallel(
                        Actions.moveBy(0, -5, 0.1f),
                        Actions.scaleTo(1f, 1f, 0.1f)
                ));
                label.addAction(Actions.parallel(
                        Actions.moveBy(0, -5, 0.1f),
                        Actions.scaleTo(1f, 1f, 0.1f)
                ));
            }
        });
    }

    /**
     * Add listener for menu buttons
     */
    private void addMenuButtonsListeners() {
        // Added handles for when clicked
        startBtn.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent changeEvent, Actor actor) {
                logger.info("Start button clicked");
                entity.getEvents().trigger("start");
                clickSound.play(); // This will cause a click sound to play when the button is clicked.
            }
        });

        // Added handles for when clicked
        loadBtn.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent changeEvent, Actor actor) {
                logger.info("Load button clicked");
                entity.getEvents().trigger("load");
                clickSound.play();
            }
        });

        // Added handles for when clicked
        minigamesBtn.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent changeEvent, Actor actor) {

                logger.debug("SnakeGame button clicked");
                entity.getEvents().trigger("SnakeGame");
                clickSound.play();
            }
        });

        // Added handles for when clicked
        settingsBtn.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent changeEvent, Actor actor) {
                logger.info("Settings button clicked");
                settingMenu.setVisible(true);
                table.setTouchable(Touchable.disabled);
                clickSound.play();
            }
        });

        // Added handles for when clicked
        achievementsBtn.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent changeEvent, Actor actor) {
                logger.debug("Achievements button clicked");
                entity.getEvents().trigger("achievements");
                clickSound.play();
            }
        });

        // Added handles for when clicked
        helpBtn.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent changeEvent, Actor actor) {
                logger.info("Help button clicked");
                entity.getEvents().trigger("help");
                showHelpWindow();
                clickSound.play();
            }

        });
    }

    /**
     * Add menu buttons icons and update the positions.
     */
    public void addMenuButtonIcon() {
        float buttonWidth;
        float buttonHeight;
        float buttonSpacing;
        float padTopSpacing;
        float labelSpacing;
        if (Gdx.graphics.isFullscreen()) {
            buttonWidth = fullScreenButtonWidth;
            buttonHeight = fullScreenuttonHeight;
            buttonSpacing = fullScreenButtonSpacing;
            padTopSpacing = 700;
            labelSpacing = -100;
            startLabel = new Label("Start", skin, "title-red");
            loadLabel = new Label("Load", skin, "title-red");
            minigameLabel = new Label("Minigame", skin, "title-red");
            helpLabel = new Label("Help", skin, "title-red");
            settingLabel = new Label("Setting", skin, "title-red");
            exitLabel = new Label("Exit", skin, "title-red");
            versionLabel = new Label("Version 1.0", skin, "title-white");
        } else {
            buttonWidth = windowButtonWidth;
            buttonHeight = windowButtonHeight;
            buttonSpacing = windowButtonSpacing;
            padTopSpacing = 350;
            labelSpacing = -48;
            startLabel = new Label("Start", skin, "button-red");
            loadLabel = new Label("Load", skin, "button-red");
            minigameLabel = new Label("Minigame", skin, "button-red");
            helpLabel = new Label("Help", skin, "button-red");
            settingLabel = new Label("Setting", skin, "button-red");
            exitLabel = new Label("Exit", skin, "button-red");
            versionLabel = new Label("Version 1.0", skin, "default-white");
        }
        menuButtonTable.setPosition((float) Gdx.graphics.getWidth() / 2, (float) Gdx.graphics.getHeight() / 2);
        menuButtonTable.clear();
        menuButtonTable.add(startBtn).size(buttonWidth, buttonHeight).padTop(padTopSpacing);
        menuButtonTable.row();
        menuButtonTable.add(startLabel).padTop(labelSpacing);
        menuButtonTable.row();
        menuButtonTable.add(loadBtn).size(buttonWidth, buttonHeight).padTop(buttonSpacing);
        menuButtonTable.row();
        menuButtonTable.add(loadLabel).padTop(labelSpacing);
        menuButtonTable.row();
        menuButtonTable.add(minigamesBtn).size(buttonWidth, buttonHeight).padTop(buttonSpacing);
        menuButtonTable.row();
        menuButtonTable.add(minigameLabel).padTop(labelSpacing);
        menuButtonTable.row();
        menuButtonTable.add(settingsBtn).size(buttonWidth, buttonHeight).padTop(buttonSpacing);
        menuButtonTable.row();
        menuButtonTable.add(settingLabel).padTop(labelSpacing);
        menuButtonTable.row();
        menuButtonTable.add(helpBtn).size(buttonWidth, buttonHeight).padTop(buttonSpacing);
        menuButtonTable.row();
        menuButtonTable.add(helpLabel).padTop(labelSpacing);
        menuButtonTable.row();
        menuButtonTable.add(exitBtn).size(buttonWidth, buttonHeight).padTop(buttonSpacing);
        menuButtonTable.row();
        menuButtonTable.add(exitLabel).padTop(labelSpacing);
        menuButtonTable.row();
        menuButtonTable.add(versionLabel).padTop(buttonSpacing);
        stage.addActor(menuButtonTable);
    }

    /**
     * Adds a toggle button to the top left corner of the screen that allows switching
     * between Night Mode and Day Mode. The button changes its label based on the
     * current mode (Night Mode or Day Mode).
     */
    private void addTopLeftToggle() {
        // Create a new Table to position the toggle button at the top left of the screen.
        Table topLeftTable = new Table();
        topLeftTable.top().left();
        topLeftTable.setFillParent(true);

        // Create a toggle button with the initial label as "Night Mode".
        TextButton nightToggleButton = new TextButton("Night Mode", skin);

        // Add a listener to handle button clicks and switch between Night and Day modes.
        nightToggleButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                isNightMode = !isNightMode;  // Toggle the Night Mode state.

                // Update the button text and apply the corresponding mode.
                if (isNightMode) {
                    nightToggleButton.setText("Day Mode");  // Change text to "Day Mode".
                    applyNightMode();  // Apply Night Mode settings.
                } else {
                    nightToggleButton.setText("Night Mode");  // Change text to "Night Mode".
                    applyDayMode();  // Apply Day Mode settings.
                }
            }
        });

        // Add the button to the top left table with size and padding.
        topLeftTable.add(nightToggleButton).size(150, 50).pad(10);

        // Add the table to the stage to render it on the screen.
        stage.addActor(topLeftTable);
    }

    /**
     * Applies Night Mode by changing the background texture to the night version.
     */
    private void applyNightMode() {
        lightBackgroundTexture = nightBackgroundTexture;  // Set the night mode background.
    }

    /**
     * Applies Day Mode by changing the background texture to the default day version.
     */
    private void applyDayMode() {
        lightBackgroundTexture = new Texture("images/SplashScreen/SplashTitle.png");  // Set the day mode background.
    }

    private void updateMuteButtonIcon() {
        if (AudioManager.isMuted()) {
            muteButton.getStyle().up = new TextureRegionDrawable(new TextureRegion(muteTexture));
        } else {
            muteButton.getStyle().up = new TextureRegionDrawable(new TextureRegion(unmuteTexture));
        }
    }

    private void addUserTable() {
        float screenWidth = Gdx.graphics.getWidth();
        float screenHeight = Gdx.graphics.getHeight();

        userTable.setSize(122, 522);

        userTable.setVisible(true);

        userTable.setPosition(screenWidth - 150, screenHeight - 600);
        Button profileBtn = new Button(new TextureRegionDrawable(new TextureRegion(new Texture("images/ButtonsMain/User.png"))));
        userTable.add(profileBtn).size(100, 100).top().padTop(30).expandY();

        profileBtn.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                loginRegisterTable.setVisible(true);
            }
        });

        stage.addActor(userTable);
    }

    /**
     * Add login register table, which could be open by clicking the profile button
     */
    private void addLoginRegisterTable() {
        loginRegisterDisplay = new LoginRegisterDisplay();
        loginRegisterTable = loginRegisterDisplay.makeLoginRegisterTable();
        loginRegisterTable.setVisible(false);
        loginRegisterTable.setSize(663, 405);

        float screenWidth = Gdx.graphics.getWidth();
        float screenHeight = Gdx.graphics.getHeight();

        // Center the menu on the screen
        loginRegisterTable.setPosition(
                (screenWidth - loginRegisterTable.getWidth()) / 2,
                (screenHeight - loginRegisterTable.getHeight()) / 2
        );

        stage.addActor(loginRegisterTable);
    }

    /**
     * Update the position of login register table.
     */
    public void updateLoginRegisterTable() {
        float screenWidth = Gdx.graphics.getWidth();
        float screenHeight = Gdx.graphics.getHeight();

        // Center the menu on the screen
        loginRegisterTable.setPosition(
                (screenWidth - loginRegisterTable.getWidth()) / 2,
                (screenHeight - loginRegisterTable.getHeight()) / 2
        );
    }

    /**
     * Update the position of user table.
     */
    public void updateUserTable() {
        float screenWidth = Gdx.graphics.getWidth();
        float screenHeight = Gdx.graphics.getHeight();
        userTable.setPosition(screenWidth - 150, screenHeight - 600);
    }

    /**
     * Displays the help window with slides for game instructions.
     */
    private void showHelpWindow() {
        final int NUM_SLIDES = 5;
        final float WINDOW_WIDTH = Math.min(1000f, Gdx.graphics.getWidth() - 100);
        final float WINDOW_HEIGHT = Math.min(600f, Gdx.graphics.getHeight() - 100);

        // Create a Window for the help screen
        Table helpWindow = new Table();
        helpWindow.setSize(WINDOW_WIDTH, WINDOW_HEIGHT);

        // Set the background of the helpWindow using the settingBackground texture
        Drawable settingDrawable = new TextureRegionDrawable(new TextureRegion(settingBackground));
        helpWindow.setBackground(settingDrawable);

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

        Label title = new Label("Help", skin, "title-white");
        // Create a table for the close button
        Table closeButtonTable = new Table();
        Button closeButton = new Button(new TextureRegionDrawable(new TextureRegion(new Texture("images/CloseButton.png"))));
        closeButtonTable.add(closeButton).size(80, 80).right().expandX().padTop(-10).padRight(-10);

        Table topTable = new Table();
        topTable.top().padTop(10);
        topTable.add(title).expandX().center().padTop(20);
        topTable.row();
        topTable.add(closeButton).size(80, 80).right().expandX().padRight(-25).padTop(-110);

        // Add the close button table to the top-right of the helpWindow
        helpWindow.add(topTable).expandX().fillX();
        helpWindow.add().row();

        // Add the slideTable to the helpWindow and position it to fill the window
        helpWindow.add(slideTable).expand().fill().row();

        // Add the navigation table to the bottom of the helpWindow
        helpWindow.add(navigationTable).bottom().expandX().fillX().pad(30).row();

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

        // Set focus to the help window to ensure it receives key events
        stage.setKeyboardFocus(helpWindow);
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
     * Adds a minimize button and mute button to the top-right corner of the screen.
     */
    private void addTopRightButtons() {

        Texture minimizeTexture = new Texture(Gdx.files.internal("images/ButtonsMain/Minimise.png")); // Replace with your minimize icon
        Texture maximizeTexture = new Texture(Gdx.files.internal("images/ButtonsMain/Maxamise.png")); // Replace with your maximize icon

        Drawable minimizeDrawable = new TextureRegionDrawable(new TextureRegion(minimizeTexture));
        Drawable maximizeDrawable = new TextureRegionDrawable(new TextureRegion(maximizeTexture));

        Table topRightTable = new Table();
        topRightTable.top().right();
        topRightTable.setFillParent(true);

        // Adding Icon for the minimax button
        ImageButton toggleWindowBtn;
        if (Gdx.graphics.isFullscreen()) {
            toggleWindowBtn = new ImageButton(minimizeDrawable);
        } else {
            toggleWindowBtn = new ImageButton(maximizeDrawable);
        }

        // Listener for minimizing/maximizing window
        toggleWindowBtn.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                boolean isFullscreen = Gdx.graphics.isFullscreen();
                if (isFullscreen) {
                    // Mini-screen mode
                    UserSettings.Settings settings = UserSettings.get();
                    settings.fullscreen = false;
                    UserSettings.applyDisplayMode(settings);
                    toggleWindowBtn.getStyle().imageUp = maximizeDrawable; // Set to maximize icon
                } else {
                    // Fullscreen mode
                    UserSettings.Settings settings = UserSettings.get();
                    settings.fullscreen = true;
                    UserSettings.applyDisplayMode(settings);
                    toggleWindowBtn.getStyle().imageUp = minimizeDrawable; // Set to minimize icon
                }
                logger.info("Fullscreen toggled: " + !isFullscreen);
                //sizeTable();
            }
        });

        Button.ButtonStyle buttonStyle = new Button.ButtonStyle();
        buttonStyle.up = new TextureRegionDrawable(new TextureRegion(unmuteTexture));
        muteButton = new Button(buttonStyle);

        muteButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                if (AudioManager.isMuted()) {
                    AudioManager.unmuteAudio();
                } else {
                    AudioManager.muteAudio();
                }
                updateMuteButtonIcon();
            }
        });

        updateMuteButtonIcon();

        // Add the minimize/maximize button and mute button to the table
        topRightTable.add(muteButton).size(60, 60).pad(10);  // Add mute button first
        topRightTable.add(toggleWindowBtn).size(60, 60).pad(10);  // Add minimize/maximize button

        // Add the table to the stage
        stage.addActor(topRightTable);
    }


    /**
     * Adds a settings menu to the screen.
     */
    private void addSettingMenu() {

        Drawable settingDrawable = new TextureRegionDrawable(new TextureRegion(settingBackground));

        float screenWidth = Gdx.graphics.getWidth();
        float screenHeight = Gdx.graphics.getHeight();

        settingMenu.setSize(663, 405);

        settingMenu.setBackground(settingDrawable);
        settingMenu.setVisible(false);

        Table topTable = new Table();
        topTable.top().padTop(10);

        Label title = new Label("Settings", skin, "title-white");

        topTable.add(title).expandX().center().padTop(5);
        topTable.row();

        Button closeButton = new Button(new TextureRegionDrawable(new TextureRegion(new Texture("images/CloseButton.png"))));
        topTable.add(closeButton).size(80, 80).right().expandX().padRight(-25).padTop(-110);

        settingsMenuDisplay = new SettingsMenuDisplay();
        Table contentTable = settingsMenuDisplay.makeSettingsTable();

        // Create a table for the "Apply" button
        Table bottomRightTable = new Table();
        bottomRightTable.bottom(); // Align contents to bottom-right

        TextButton applyButton = new TextButton("Apply", skin);
        bottomRightTable.add(applyButton).size(80, 40).padBottom(30f).padRight(30f);

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
                        updateMuteButtonIcon();
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
    private void addExitConfirmation() {
        exitBtn.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                Drawable dialogBackground = new TextureRegionDrawable(new TextureRegion(settingBackground));

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
                        dialog.remove();
                    }
                });

                dialog.getContentTable().add(confirmLabel).padBottom(40f).center();
                dialog.getButtonTable().add(yesBtn).padRight(30f).width(150f).height(60f);
                dialog.getButtonTable().add(noBtn).width(150f).height(60f);

                dialog.setPosition(
                        (Gdx.graphics.getWidth() - dialog.getWidth()) / 2,
                        (Gdx.graphics.getHeight() - dialog.getHeight()) / 2
                );
                stage.addActor(dialog);
            }
        });
    }

    @Override
    public void draw(SpriteBatch batch) {
        batch = new SpriteBatch();
        batch.begin();
        batch.draw(lightBackgroundTexture, 0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        batch.end();
    }

    @Override
    public float getZIndex() {
        return Z_INDEX;
    }

    @Override
    public void dispose() {
        table.clear();
        dog2Texture.dispose();
        crocTexture.dispose();
        birdTexture.dispose();
        super.dispose();
        clickSound.dispose();
    }
}
