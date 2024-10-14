package com.csse3200.game.components.mainmenu;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.*;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.graphics.Cursor;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.csse3200.game.components.login.LoginRegisterDisplay;
import com.csse3200.game.components.settingsmenu.SettingsMenu;
import com.csse3200.game.minigames.MinigameLeaderboard;
import com.csse3200.game.services.NotifManager;
import com.csse3200.game.ui.CustomButton;
import com.csse3200.game.ui.UIComponent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.csse3200.game.components.settingsmenu.UserSettings;
import com.csse3200.game.services.AudioManager;
import com.badlogic.gdx.math.MathUtils;

/**
 * A UI component for displaying the Main menu.
 */
public class MainMenuDisplay extends UIComponent {
    private static final Logger logger = LoggerFactory.getLogger(MainMenuDisplay.class);
    private static final float Z_INDEX = 2f;
    private Table table;
    private Table menuButtonTable;
    private Table loginRegisterTable;
    private Table leaderboardTable;
    private SettingsMenu settingsMenu;
    private MinigameLeaderboard minigameLeaderboard;
    private Texture mainTitle;
    private Texture lightBackgroundTexture;
    private Texture settingBackground;
    private Texture toggleTexture;
    private Button muteButton;  // Mute toggle button with texture
    private Texture muteTexture;  // Texture for mute state
    private Texture unmuteTexture;  // Texture for unmute state
    private Texture dog2Texture;
    private Texture crocTexture;
    private Texture cursorTexture;
    private ChatbotUI chatbotUI;
    private Sound owlSound;
    private Label factLabel;
    private String[] owlFacts;
    private boolean isNightMode = false; // A flag to track whether night mode is enabled
    private Texture nightBackgroundTexture;
    private Sound clickSound; // Loaded click sound file for buttons
    private boolean dogDirection = true;
    private Cursor customCursor;
    private CustomButton startBtn;
    private CustomButton loadBtn;
    private CustomButton minigamesBtn;
    private CustomButton settingsBtn;
    private CustomButton achievementsBtn;
    private CustomButton helpBtn;
    private CustomButton exitBtn;
    private Image birdAniImage;
    private Image monkeyAniImage;
    private Image dogAniImage;
    private static final float WINDOWBUTTONWIDTH = 200;
    private static final float WINDOWBUTTONHEIGHT = 45;
    private static final float WINDOWBUTTONSPACING = 15;
    private static final float FULLSCREENBUTTONWIDTH = 300;
    private static final float FULLSCREENBUTTONHEIGHT = 60;
    private static final float FULLSCREENBUTTONSPACING = 20;
    private Image owlAniImage;
    private Array<TextureRegion> birdTextures;
    private Array<TextureRegion> monkeyTextures;
    private Array<TextureRegion> dogTextures;
    private Array<TextureRegion> owlTextures;
    private boolean birdDirection = true;
    int birdCurrentFrame = 0;
    int monkeyCurrentFrame = 0;
    int dogCurrentFrame = 0;
    int owlCurrentFrame = 0;
    private float timer;
    private float owlTimer;
    private Image titleAniImage;


    /**
     * Called when the component is created. Initializes the main menu UI.
     */
    @Override
    public void create() {
        super.create();
        logger.info("Creating MainMenuDisplay");
        this.loadTextures();  // Load textures for the mute button
        logger.info("Background texture loaded");
        this.setupCustomCursor();
        this.addDog();
        this.addMonkey();
        chatbotUI = new ChatbotUI(stage, skin, this);
        this.applyUserSettings();
        this.setupOwlFacts();
        this.addBird();
        this.addOwlToMenu(); // Add owl to the menu
        this.addTitle();
        addActors();

        timer = 0f;
        owlTimer = 0f;
    }
    /**
     * Load the textures for the mute and unmute button states.
     */
    private void loadTextures() {
        settingBackground = new Texture("images/SettingBackground.png");
        lightBackgroundTexture = new Texture("images/SplashScreen/MainSplash.png");
        mainTitle = new Texture("images/SplashScreen/MainTitle.png");
        muteTexture = new Texture("images/sound_off.png");  // Add your mute icon here
        unmuteTexture = new Texture("images/sound_on.png");  // Add your unmute icon here
        dog2Texture = new Texture("images/dog2.png");
        crocTexture = new Texture("images/croc.png");
        toggleTexture = new Texture(Gdx.files.internal("images/NightToggle.png"));
        cursorTexture = new Texture(Gdx.files.internal("images/CustomCursor.png")); // Custom cursor image
        nightBackgroundTexture = new Texture("images/SplashScreen/SplashEmptyNight.png"); // Night background
        clickSound = Gdx.audio.newSound(Gdx.files.internal("sounds/click.mp3")); // Click sound for buttons
        owlSound = Gdx.audio.newSound(Gdx.files.internal("sounds/owlhoot1.mp3")); // Owl sound file
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
        initializeTables();
        initializeMenuButtons();
        stage.addActor(NotifManager.addNotificationTable());

        addTopLeftToggle();
        addTopRightButtons();
        addSettingMenu();
        addLoginRegisterTable();
        addLeaderboardTable();
    }
    /**
     * Initialize all tables in the main menu
     */
    private void initializeTables() {
        table = new Table();
        menuButtonTable = new Table();
        loginRegisterTable = new Table();
        leaderboardTable = new Table();
    }

    /**
     * Initialize menu buttons in the main menu
     */
    private void initializeMenuButtons() {
        // Clear previous button settings to avoid duplicates
        if (menuButtonTable != null) {
            menuButtonTable.clear();
        } else {
            menuButtonTable = new Table();
        }

        // Set Z index to ensure it is drawn above other components
        menuButtonTable.setZIndex(10);
        // Create all main menu buttons
        startBtn = createMenuButton("Start", () -> {
            logger.info("Start button clicked");
            entity.getEvents().trigger("start");
        });

        loadBtn = createMenuButton("Load", () -> {
            logger.info("Load button clicked");
            entity.getEvents().trigger("load");
        });

        minigamesBtn = createMenuButton("Minigame", () -> {
            logger.info("Minigames button clicked");
            entity.getEvents().trigger("SnakeGame");
        });
        //minigamesBtn.setButtonStyle(CustomButton.Style.NORMAL, skin);

        settingsBtn = createMenuButton("Settings", () -> {
            logger.info("Settings button clicked");
            settingsMenu.showSettingsMenu();
            setMenuUntouchable();
        });

        achievementsBtn = createMenuButton("Achievements", () -> {
            logger.info("Achievements button clicked");
            entity.getEvents().trigger("achievements");
        });

        entity.getEvents().addListener("help", this::showHelpWindow);
        helpBtn = createMenuButton("Help", () -> {
            logger.info("Help button clicked");
            setMenuUntouchable();
            entity.getEvents().trigger("help");
        });

        entity.getEvents().addListener("exitConfirmation", this::handleExitConfirmation);  // Call the exit handler
        exitBtn = createMenuButton("Exit", () -> {
            logger.info("Exit button clicked");
            entity.getEvents().trigger("exitConfirmation");
        });
        updateMenuButtonLayout();

        stage.addActor(menuButtonTable);
    }

    private void addOwl() {
        owlAniImage = new Image();
        TextureAtlas owlAtlas = new TextureAtlas("spriteSheets/owl.atlas");
        owlTextures = new Array<>(3);
        for (int frameOwl = 1; frameOwl <= 3; frameOwl++) {
            owlTextures.add(owlAtlas.findRegion("owl" + frameOwl));
        }
        TextureRegionDrawable drawableOwl = new TextureRegionDrawable(owlTextures.get(0));
        owlAniImage.setDrawable(drawableOwl);
        owlAniImage.setPosition(Gdx.graphics.getWidth() * 0.87f, Gdx.graphics.getHeight() * 0.55f); // Adjust the position as needed
        owlAniImage.setSize(Gdx.graphics.getWidth() / 7f, Gdx.graphics.getHeight() / 6f);
        stage.addActor(owlAniImage);
    }
    private void addMonkey() {
        monkeyAniImage = new Image();
        TextureAtlas monkeyAtlas = new TextureAtlas("spriteSheets/MonkeySprite.atlas");
        monkeyTextures = new Array<>(6);
        for (int frameMonkey = 1; frameMonkey <= 6; frameMonkey++) {
            monkeyTextures.add(monkeyAtlas.findRegion("monkey" + frameMonkey));
        }
        TextureRegionDrawable drawableMonkey = new TextureRegionDrawable(monkeyTextures.get(0));
        monkeyAniImage.setDrawable(drawableMonkey);
        monkeyAniImage.setSize(Gdx.graphics.getWidth() / 5f, Gdx.graphics.getHeight() / 4.8f);
        monkeyAniImage.setPosition(Gdx.graphics.getWidth() / 1.5f, Gdx.graphics.getHeight() / 6.8f);

        // Add the click listener to open/close the chatbot
        monkeyAniImage.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                Gdx.app.log("Chatbot", "Monkey icon clicked!"); // Log to verify the click
                clickSound.play(); // Play the click sound
                if (chatbotUI.isChatbotDialogVisible()) {
                    chatbotUI.closeChatbotDialog(); // Close the chatbot if it's visible
                } else {
                    chatbotUI.openChatbotDialog(); // Open the chatbot if it's not visible
                }
            }
        });

        stage.addActor(monkeyAniImage);
    }
    private void addDog() {
        // Add dog animation -> before buttons so the buttons are over top
        dogAniImage = new Image();
        TextureAtlas dogAtlas = new TextureAtlas("spriteSheets/Dog.atlas");
        dogTextures = new Array<>(4);
        for (int frameDog = 1; frameDog <= 4; frameDog++) {
            dogTextures.add(dogAtlas.findRegion("dog" + frameDog));
        }
        TextureRegionDrawable drawableDog = new TextureRegionDrawable(dogTextures.get(0));
        dogAniImage.setDrawable(drawableDog);
        dogAniImage.setSize(Gdx.graphics.getWidth() / 7f, Gdx.graphics.getHeight() / 7f);
        dogAniImage.setPosition(-200, Gdx.graphics.getHeight() / 6.8f);
        stage.addActor(dogAniImage);
    }

    private void addBird() {
        //Add bird animation
        birdAniImage = new Image();
        TextureAtlas birdAtlas = new TextureAtlas("spriteSheets/BirdMain.atlas");
        birdTextures = new Array<>(3);
        for (int frameBird = 1; frameBird <= 3; frameBird++) {
            birdTextures.add(birdAtlas.findRegion("fly" + frameBird));
        }
        TextureRegionDrawable drawable = new TextureRegionDrawable(birdTextures.get(0));
        birdAniImage.setDrawable(drawable);
        birdAniImage.setSize(Gdx.graphics.getWidth() / 12f, Gdx.graphics.getHeight() / 10f);
        birdAniImage.setPosition(Gdx.graphics.getWidth() + 200f, Gdx.graphics.getHeight() * 0.6f);
        stage.addActor(birdAniImage);
    }

    private void addTitle() {
        // adds the title -> after bird so bird fly's behind
        titleAniImage = new Image();
        TextureRegionDrawable drawableTitle = new TextureRegionDrawable(new TextureRegion(mainTitle));
        titleAniImage.setDrawable(drawableTitle);
        titleAniImage.setSize(Gdx.graphics.getWidth() / 2.8f,Gdx.graphics.getHeight() / 2.6f);
        titleAniImage.setPosition((Gdx.graphics.getWidth() / 2f) - (titleAniImage.getWidth() / 2), Gdx.graphics.getHeight() / 1.8f );
        stage.addActor(titleAniImage);
    }

    /**
     * Sets up the custom cursor.
     */
    private void setupCustomCursor() {
        try {
            Pixmap pixmap = new Pixmap(Gdx.files.internal("images/CustomCursor.png"));

            customCursor = Gdx.graphics.newCursor(pixmap, pixmap.getWidth() / 4, pixmap.getHeight() / 4);

            Gdx.graphics.setCursor(customCursor);

            pixmap.dispose();

            logger.info("Custom cursor set successfully.");
        } catch (Exception e) {
            logger.error("Failed to set custom cursor", e);
        }
    }

    // Add owl facts
    private void setupOwlFacts() {
        owlFacts = new String[]{
                "A dogs nose print is as unique as a human fingerprint.",
                "Crocodiles have been around for over 200 million years!",
                "Some birds, like the Arctic Tern, migrate over 40,000 miles a year.",
                "Dogs can understand up to 250 words and gestures.",
                "Crocs can gallop on land like a horse for short bursts!",
                "The owl can rotate its head 270 degrees without moving its body.",
                "Dogs can smell diseases like cancer and diabetes!",
                "A crocodiles bite is the strongest in the animal kingdom.",
                "Parrots can mimic human speech better than any other animal.",
                "A Greyhound can reach speeds of 45 mph!",
                "The heart of a hummingbird beats over 1,200 times per minute!"
        };
    }

    private void addOwlToMenu() {
        // Add owl
        this.addOwl();

        // Create label for displaying facts
        factLabel = new Label("", new Label.LabelStyle(new BitmapFont(), Color.WHITE)); // Set fact label style
        factLabel.setPosition(owlAniImage.getX() * 0.8f, owlAniImage.getY()); // Position it near the owl
        factLabel.setFontScale(1f);
        stage.addActor(factLabel);

        // Add click listener for the owl
        owlAniImage.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                owlSound.play(); // Play owl sound
                String randomFact = owlFacts[MathUtils.random(0, owlFacts.length - 1)]; // Get random fact
                factLabel.setText(randomFact); // Set fact text
                factLabel.addAction(Actions.sequence(
                        Actions.alpha(1), // Ensure it's visible
                        Actions.delay(3), // Keep it visible for 3 seconds
                        Actions.alpha(0, 1) // Fade out after
                ));
            }
        });
    }

    /**
     * Dynamically update the layout of menu buttons based on screen size and mode (fullscreen/windowed).
     */
    public void updateMenuButtonLayout() {
        // Get screen dimensions and check fullscreen mode
        boolean isFullscreen = Gdx.graphics.isFullscreen();
        float screenHeight = Gdx.graphics.getHeight();
        float screenWidth = Gdx.graphics.getWidth();

        float buttonWidth;
        float buttonHeight;
        float buttonSpacing;
        float padTopSpacing;

        // Adjust button dimensions based on the screen mode (fullscreen or windowed)
        if (isFullscreen) {
            buttonWidth = FULLSCREENBUTTONWIDTH;
            buttonHeight = FULLSCREENBUTTONHEIGHT;
            buttonSpacing = FULLSCREENBUTTONSPACING;
            padTopSpacing = 500;  // Top padding relative to screen height
        } else {
            buttonWidth = WINDOWBUTTONWIDTH;
            buttonHeight = WINDOWBUTTONHEIGHT;
            buttonSpacing = WINDOWBUTTONSPACING;
            padTopSpacing = 350;  // Top padding relative to screen height
        }

        // Position the button table at the center of the screen
        menuButtonTable.setPosition(screenWidth / 2, screenHeight / 2);
        // Resize each button based on the screen size and button dimensions
        float scaleFactorWidth = buttonWidth / screenWidth;

        startBtn.resize(screenWidth, screenHeight, scaleFactorWidth);
        loadBtn.resize(screenWidth, screenHeight, scaleFactorWidth);
        minigamesBtn.resize(screenWidth, screenHeight, scaleFactorWidth);
        achievementsBtn.resize(screenWidth, screenHeight, scaleFactorWidth);
        settingsBtn.resize(screenWidth, screenHeight, scaleFactorWidth);
        helpBtn.resize(screenWidth, screenHeight, scaleFactorWidth);
        exitBtn.resize(screenWidth, screenHeight, scaleFactorWidth);

        // Clear any existing buttons to prevent duplication
        menuButtonTable.clear();

        // Add buttons to the table with the correct sizes and spacings
        menuButtonTable.add(startBtn).size(buttonWidth, buttonHeight).padTop(padTopSpacing).padBottom(buttonSpacing);
        menuButtonTable.row();
        menuButtonTable.add(loadBtn).size(buttonWidth, buttonHeight).padBottom(buttonSpacing);
        menuButtonTable.row();
        menuButtonTable.add(minigamesBtn).size(buttonWidth, buttonHeight).padBottom(buttonSpacing);
        menuButtonTable.row();
        menuButtonTable.add(achievementsBtn).size(buttonWidth, buttonHeight).padBottom(buttonSpacing);
        menuButtonTable.row();
        menuButtonTable.add(settingsBtn).size(buttonWidth, buttonHeight).padBottom(buttonSpacing);
        menuButtonTable.row();
        menuButtonTable.add(helpBtn).size(buttonWidth, buttonHeight).padBottom(buttonSpacing);
        menuButtonTable.row();
        menuButtonTable.add(exitBtn).size(buttonWidth, buttonHeight).padBottom(buttonSpacing);
    }

    public void setMenuUntouchable() {
        table.setTouchable(Touchable.disabled);
        menuButtonTable.setTouchable(Touchable.disabled);
    }

    public void setMenuTouchable() {
        table.setTouchable(Touchable.enabled);
        menuButtonTable.setTouchable(Touchable.enabled);
    }


    /**
     * Utility method to create a new menu button using CustomButton.
     *
     * @param labelText   The label text for the button.
     * @param clickAction The action to execute when the button is clicked.
     * @return The created CustomButton instance.
     */
    private CustomButton createMenuButton(String labelText, Runnable clickAction) {
        CustomButton button = new CustomButton(labelText, skin);
        button.addClickListener(clickAction);
        button.setButtonSize(300, 80);  // Default size for all buttons
        return button;
    }

    /**
     * Adds a toggle button to the top left corner of the screen that allows switching
     * between Night Mode and Day Mode. The button changes its label based on the
     * current mode (Night Mode or Day Mode).
     */
    private void addTopLeftToggle() {
        Table topLeftTable = new Table();
        topLeftTable.top().left();
        topLeftTable.setFillParent(true);

        Button toggleImage = new Button(new TextureRegionDrawable(new TextureRegion(toggleTexture)));
        Button profileBtn = new Button(new TextureRegionDrawable(new TextureRegion(new Texture("images/ButtonsMain/User.png"))));
        Button trophyBtn = new Button(new TextureRegionDrawable(new TextureRegion(new Texture("images/Achievements.png"))));

        toggleImage.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                isNightMode = !isNightMode;

                if (isNightMode) {
                    applyNightMode();
                } else {
                    applyDayMode();
                }
                clickSound.play();
            }
        });


        profileBtn.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                clickSound.play();
                loginRegisterTable.setVisible(true);
                setMenuUntouchable();
            }
        });


        trophyBtn.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                clickSound.play();
                leaderboardTable.setVisible(true);
                setMenuUntouchable();
            }
        });

        // Add the image to the top left corner
        topLeftTable.add(toggleImage).size(100, 100).pad(10); // Adjust the size as needed
        topLeftTable.row();
        // Add the image to the top left corner
        topLeftTable.add(profileBtn).size(100, 100).pad(10); // Adjust the size as needed
        topLeftTable.row();
        // Add the image to the top left corner
        topLeftTable.add(trophyBtn).size(100, 100).pad(10); // Adjust the size as needed
        topLeftTable.row();

        // Add the table to the stage
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
        lightBackgroundTexture = new Texture("images/SplashScreen/MainSplash.png");  // Set the day mode background.
    }

    private void updateMuteButtonIcon() {
        if (AudioManager.isMuted()) {
            muteButton.getStyle().up = new TextureRegionDrawable(new TextureRegion(muteTexture));
        } else {
            muteButton.getStyle().up = new TextureRegionDrawable(new TextureRegion(unmuteTexture));
        }
    }

    /**
     * Add login register table, which could be open by clicking the profile button
     */
    private void addLoginRegisterTable() {
        LoginRegisterDisplay loginRegisterDisplay = new LoginRegisterDisplay(this);
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
     *
     */
    private void addLeaderboardTable() {
        minigameLeaderboard = new MinigameLeaderboard(this);
        leaderboardTable = minigameLeaderboard.makeLeaderboardTable();
        leaderboardTable.setVisible(false);
        leaderboardTable.setSize(525, 675);

        float screenWidth = Gdx.graphics.getWidth();
        float screenHeight = Gdx.graphics.getHeight();

        // Center the menu on the screen
        leaderboardTable.setPosition(
                (screenWidth - leaderboardTable.getWidth()) / 2,
                (screenHeight - leaderboardTable.getHeight()) / 2
        );

        stage.addActor(leaderboardTable);
    }
    /**
     * Update the size and position of the leaderboard table based on screen size and fullscreen mode.
     */
    public void updateLeaderboardTable() {
        float screenWidth = Gdx.graphics.getWidth();
        float screenHeight = Gdx.graphics.getHeight();

        // Adjust size percentages based on fullscreen or windowed mode
        float widthPercentage = Gdx.graphics.isFullscreen() ? 0.35f : 0.45f;
        float heightPercentage = Gdx.graphics.isFullscreen() ? 0.7f : 0.9f;

        float newWidth = screenWidth * widthPercentage;
        float newHeight = screenHeight * heightPercentage;

        leaderboardTable.setSize(newWidth, newHeight);

        // Center the leaderboard table on the screen
        leaderboardTable.setPosition(
                (screenWidth - newWidth) / 2,
                (screenHeight - newHeight) / 2
        );
    }


    /**
     * Displays the help window with slides for game instructions.
     */
    private void showHelpWindow() {
        setMenuUntouchable();

        // Create the help window
        HelpWindow helpWindow = new HelpWindow(skin, stage, new TextureRegionDrawable(new TextureRegion(settingBackground)));

        // Set onClose logic to re-enable touchable menus
        helpWindow.setOnClose(this::setMenuTouchable);

        // Show the help window
        helpWindow.show();
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
                logger.info("Fullscreen toggled: {}", !isFullscreen);
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
        settingsMenu = new SettingsMenu(this);  // Create an instance of SettingsMenu
        settingsMenu.create();  // Initialize it
    }


    /**
     * Adds an exit confirmation dialog with an enhanced UI when the exit button is clicked.
     */
    private void handleExitConfirmation() {
        Drawable dialogBackground = new TextureRegionDrawable(new TextureRegion(settingBackground));

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


    @Override
    public void update() {
        timer += Gdx.graphics.getDeltaTime();
        owlTimer += Gdx.graphics.getDeltaTime();
        if (timer >= 0.25) {
            timer = 0;
            TextureRegionDrawable drawable = new TextureRegionDrawable(birdTextures.get(birdCurrentFrame));
            birdCurrentFrame++;
            if (birdCurrentFrame >= 2) {
                birdCurrentFrame = 0;
            }
            birdAniImage.setDrawable(drawable);

            TextureRegionDrawable drawableDog = new TextureRegionDrawable(dogTextures.get(dogCurrentFrame));
            dogCurrentFrame++;
            if (dogCurrentFrame >= 3) {
                dogCurrentFrame = 0;
            }
            dogAniImage.setDrawable(drawableDog);

            TextureRegionDrawable drawableMonkey = new TextureRegionDrawable(monkeyTextures.get(monkeyCurrentFrame));
            monkeyCurrentFrame++;
            if (monkeyCurrentFrame >= 6) {
                monkeyCurrentFrame = 0;
            }
            monkeyAniImage.setDrawable(drawableMonkey);
        }
        if (owlTimer >= 0.5f) {
            owlTimer = 0f;

            TextureRegionDrawable drawableOwl = new TextureRegionDrawable(owlTextures.get(owlCurrentFrame));
            owlCurrentFrame++;
            if (owlCurrentFrame >= 3) {
                owlCurrentFrame = 0;
            }
            owlAniImage.setDrawable(drawableOwl);
        }

        // Resize owl with screen
        owlAniImage.setPosition(Gdx.graphics.getWidth() * 0.885f, Gdx.graphics.getHeight() * 0.55f); // Adjust the position as needed
        owlAniImage.setSize(Gdx.graphics.getWidth() / 8f, Gdx.graphics.getHeight() / 6f);

        // Resize label with owl
        factLabel.setPosition(owlAniImage.getX() * 0.8f, owlAniImage.getY()); // Position it near the owl
        factLabel.setFontScale(Gdx.graphics.getHeight() / 1000f);

        // update bird, monkey and dog animations
        this.updateBirdFly();
        this.updateDogRun();

        // resize monkey
        monkeyAniImage.setSize(Gdx.graphics.getWidth() / 5f, Gdx.graphics.getHeight() / 4.8f);
        monkeyAniImage.setPosition(Gdx.graphics.getWidth() / 1.5f, Gdx.graphics.getHeight() / 6.8f);

        // resize title
        titleAniImage.setSize(Gdx.graphics.getWidth() / 2.8f,Gdx.graphics.getHeight() / 2.6f);
        titleAniImage.setPosition((Gdx.graphics.getWidth() / 2f) - (titleAniImage.getWidth() / 2), Gdx.graphics.getHeight() / 1.8f );
    }

    public void updateBirdFly() {
        birdAniImage.setSize(Gdx.graphics.getWidth() / 12f, Gdx.graphics.getHeight() / 10f);
        // animate the bird left to right
        float birdX = birdAniImage.getX();
        if (birdX < -200 && birdDirection) {
            birdDirection = false;
            birdAniImage.setScale(-1, 1);
        } else if (birdX > Gdx.graphics.getWidth() + 200 && !birdDirection) {
            birdDirection = true;
            birdAniImage.setScale(1, 1);
        }

        if (birdDirection) {
            birdX = birdAniImage.getX() + Gdx.graphics.getDeltaTime() * (-Gdx.graphics.getWidth() / 7f);
        } else {
            birdX = birdAniImage.getX() + Gdx.graphics.getDeltaTime() * (Gdx.graphics.getWidth() / 7f);
        }
        birdAniImage.setPosition(birdX, Gdx.graphics.getHeight() * 0.6f);
    }

    public void updateDogRun() {
        // resize
        dogAniImage.setSize(Gdx.graphics.getWidth() / 7f, Gdx.graphics.getHeight() / 7f);

        // flip dog if out of screen
        float dogX = dogAniImage.getX();
        if (dogX < -200 && dogDirection) {
            dogDirection = false;
            dogAniImage.setScale(1, 1);
        } else if (dogX > Gdx.graphics.getWidth() + 200 && !dogDirection) {
            dogDirection = true;
            dogAniImage.setScale(-1, 1);
        }

        // move dog
        if (dogDirection) {
            dogX = dogAniImage.getX() + Gdx.graphics.getDeltaTime() * (-Gdx.graphics.getWidth() / 5f);
        } else {
            dogX = dogAniImage.getX() + Gdx.graphics.getDeltaTime() * (Gdx.graphics.getWidth() / 5f);
        }
        dogAniImage.setPosition(dogX, Gdx.graphics.getHeight() / 6.8f);
    }

    public ChatbotUI getChatbotUI() {
        return chatbotUI;
    }

    @Override
    public void draw(SpriteBatch batch) {
        SpriteBatch batchDupe = new SpriteBatch();
        batchDupe.begin();
        batchDupe.draw(lightBackgroundTexture, 0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        batchDupe.end();
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
        super.dispose();
        clickSound.dispose();
        if (customCursor != null) {
            customCursor.dispose(); // Dispose of the custom cursor to avoid memory leaks
        }
        if (cursorTexture != null) {
            cursorTexture.dispose();
        }
    }
}
