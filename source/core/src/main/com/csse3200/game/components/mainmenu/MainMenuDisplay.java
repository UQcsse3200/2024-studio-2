package com.csse3200.game.components.mainmenu;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.*;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.graphics.Cursor;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Align;
import com.csse3200.game.components.login.LoginRegisterDisplay;
import com.csse3200.game.services.NotifManager;
import com.csse3200.game.components.settingsmenu.SettingsMenuDisplay;
import com.csse3200.game.ui.UIComponent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.csse3200.game.components.settingsmenu.UserSettings;
import com.csse3200.game.services.AudioManager;
import com.badlogic.gdx.math.MathUtils;

import java.util.ArrayList;

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
    private Texture lightBackgroundTexture;
    private Texture settingBackground;
    private Texture toggleTexture;
    private Texture userTableBackground;
    private Button muteButton;  // Mute toggle button with texture
    private Texture muteTexture;  // Texture for mute state
    private Texture unmuteTexture;  // Texture for unmute state
    private Texture dog2Texture;
    private Texture crocTexture;
    private Texture cursorTexture;

    private Table chatbotIconTable;
    private Dialog chatbotDialog;
    private TextField userInputField;
    private Label chatbotResponseLabel;
    private java.util.List<String> predefinedQuestions;
    private ChatbotService chatbotService;
    private boolean isChatbotDialogVisible = false;
    private Image birdImage;
    private Image owlImage;
    private Sound owlSound;
    private Label factLabel;
    private String[] owlFacts;
    private boolean isNightMode = false; // A flag to track whether night mode is enabled
    private Texture nightBackgroundTexture;
    private Sound clickSound; // Loaded click sound file for buttons
    private Button startBtn;
    private Button loadBtn;
    private Button minigamesBtn;
    private Button settingsBtn;
    private Cursor customCursor;
    private Button achievementsBtn;
    private Button helpBtn;
    private Button exitBtn;
    private Label versionLabel;
    private final float windowButtonWidth = 200;
    private final float windowButtonHeight = 45;
    private final float windowButtonSpacing = 15;
    private final float fullScreenButtonWidth = 320;
    private final float fullScreenuttonHeight = 80;
    private final float fullScreenButtonSpacing = 30;
    private Label startLabel;
    private Label loadLabel;
    private Label minigameLabel;
    private Label helpLabel;
    private Label settingLabel;
    private Label exitLabel;
    private Label achievementsLabel;
    private Image birdAniImage;
    private TextureAtlas birdAtlas;
    private Array<TextureRegion> birdTextures;
    private boolean birdDirection = true;
    int birdCurrentFrame = 0;
    private float timer;
    /**
     * Called when the component is created. Initializes the main menu UI.
     */
    @Override
    public void create() {
        super.create();
        logger.info("Creating MainMenuDisplay");
        loadTextures();  // Load textures for the mute button
        logger.info("Background texture loaded");
        setupCustomCursor();
        addActors();
        chatbotService = new ChatbotService();
        setupPredefinedQuestions();
        addChatbotIcon();
        applyUserSettings();
        setupOwlFacts();
        addOwlToMenu(); // Add owl to the menu

        //Add bird animation
        birdAniImage = new Image();
        birdAtlas = new TextureAtlas("spriteSheets/BirdMain.atlas");
        birdTextures = new Array<>(3);
        for (int i = 1; i <= 3; i++) {
            birdTextures.add(birdAtlas.findRegion("fly" + i));
        }
        TextureRegionDrawable drawable = new TextureRegionDrawable(birdTextures.get(0));
        birdAniImage.setDrawable(drawable);
        birdAniImage.setSize(128, 112);
        birdAniImage.setPosition(1500, 500);
        timer = 0f;
        stage.addActor(birdAniImage);
    }

    private void addChatbotIcon() {
        chatbotIconTable = new Table();
        chatbotIconTable.bottom().right();
        chatbotIconTable.setFillParent(true);
        chatbotIconTable.pad(20).padBottom(50).padRight(50);

        // Load the chatbot icon texture
        ImageButton chatbotIcon = new ImageButton(new TextureRegionDrawable(new TextureRegion(new Texture("images/chatbot1.png"))));
        chatbotIcon.setSize(100, 100); // Initial size of the chatbot icon

        // Add hover effect to enlarge the icon when mouse enters, and shrink when mouse exits
        chatbotIcon.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                if (isChatbotDialogVisible) {
                    closeChatbotDialog();
                } else {
                    openChatbotDialog();
                }
            }

            @Override
            public void enter(InputEvent event, float x, float y, int pointer, Actor fromActor) {
                // Enlarge the icon on hover
                chatbotIcon.addAction(Actions.scaleTo(1.2f, 1.2f, 0.1f)); // Enlarge to 120% over 0.1 seconds
            }

            @Override
            public void exit(InputEvent event, float x, float y, int pointer, Actor toActor) {
                // Shrink back to original size when mouse exits
                chatbotIcon.addAction(Actions.scaleTo(1f, 1f, 0.1f)); // Shrink back to original size over 0.1 seconds
            }
        });

        // Add the chatbot icon to the table
        chatbotIconTable.add(chatbotIcon);
        stage.addActor(chatbotIconTable);
    }

    /**
     * Opens the chatbot dialog in the center of the screen.
     */
    private void openChatbotDialog() {
        chatbotDialog = new Dialog("", skin) {
            @Override
            protected void result(Object object) {
                logger.info("Chatbot dialog closed.");
            }
        };

        final float DIALOG_WIDTH = Math.min(1000f, Gdx.graphics.getWidth() - 100); // Dynamically set width
        final float DIALOG_HEIGHT = Math.min(800f, Gdx.graphics.getHeight() - 100); // Dynamically set height
        chatbotDialog.setSize(DIALOG_WIDTH, DIALOG_HEIGHT); // Set size

        // Background for the chatbot window
        Drawable backgroundDrawable = new TextureRegionDrawable(new TextureRegion(new Texture("images/SettingBackground.png")));
        chatbotDialog.setBackground(backgroundDrawable);

        // Title
        Label titleLabel = new Label("Chatbot", skin, "title-white");
        titleLabel.setAlignment(Align.center);

        // Predefined questions
        Table questionTable = new Table();
        for (String question : predefinedQuestions) {
            TextButton questionButton = new TextButton(question, skin);
            questionButton.addListener(new ClickListener() {
                @Override
                public void clicked(InputEvent event, float x, float y) {
                    processChatInput(question);
                }
            });
            questionTable.add(questionButton).pad(5).expandX().fillX().row(); // Add each question button with padding and fill
        }

        // User input field
        userInputField = new TextField("", skin);
        userInputField.setMessageText("Type your question...");
        userInputField.setAlignment(Align.center);

        // Submit button
        TextButton sendButton = new TextButton("Send", skin);
        sendButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                processChatInput(userInputField.getText());
            }
        });

        // Response label
        chatbotResponseLabel = new Label("", skin);
        chatbotResponseLabel.setWrap(true);
        chatbotResponseLabel.setAlignment(Align.center);
        chatbotResponseLabel.setWidth(500);

        // Close button
        TextButton closeButton = new TextButton("Close", skin);
        closeButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                chatbotDialog.hide(Actions.sequence(Actions.alpha(0f), Actions.run(() -> isChatbotDialogVisible = false))); // Close without fade effect
            }
        });

        // Layout the dialog
        Table contentTable = new Table();
        contentTable.add(titleLabel).padTop(20).center().row(); // Add title at the top
        contentTable.add(questionTable).expandX().fillX().pad(20).row(); // Add question buttons
        contentTable.add(userInputField).width(600).pad(10).row(); // Add input field
        contentTable.add(sendButton).pad(10).row(); // Add send button
        contentTable.add(chatbotResponseLabel).width(600).pad(10).row(); // Add response label
        contentTable.add(closeButton).pad(10).row(); // Add close button

        chatbotDialog.getContentTable().add(contentTable).expandX().fillX(); // Add all elements to the dialog's content table

        // Show the dialog without fade-in effect
        chatbotDialog.show(stage, Actions.sequence(Actions.alpha(1f))); // Ensure full opacity without any fade effect

        // Center the dialog on screen after showing it
        centerDialogOnScreen();
    }

    private void centerDialogOnScreen() {
        chatbotDialog.setPosition(
                (Gdx.graphics.getWidth() - chatbotDialog.getWidth()) / 2,
                (Gdx.graphics.getHeight() - chatbotDialog.getHeight()) / 2
        );
    }

    private void setupPredefinedQuestions() {
        predefinedQuestions = new ArrayList<>();
        predefinedQuestions.add("How do I move?");
        predefinedQuestions.add("How do I attack?");
        predefinedQuestions.add("What's the objective?");
        predefinedQuestions.add("How can I save my game?");
        predefinedQuestions.add("Hello");
    }

    private void processChatInput(String userInput) {
        String chatbotResponse = chatbotService.getResponse(userInput);
        chatbotResponseLabel.setText(chatbotResponse);
    }

    /**
     * Closes the chatbot dialog.
     */
    private void closeChatbotDialog() {
        if (chatbotDialog != null && isChatbotDialogVisible) {
            chatbotDialog.hide();
            isChatbotDialogVisible = false;
        }
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

    public void updateChatbotDialogPosition() {
        if (chatbotDialog != null) {
            float screenWidth = Gdx.graphics.getWidth();
            float screenHeight = Gdx.graphics.getHeight();
            chatbotDialog.setPosition(
                    (screenWidth - chatbotDialog.getWidth()) / 2,
                    (screenHeight - chatbotDialog.getHeight()) / 2
            );
        }
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
        toggleTexture = new Texture(Gdx.files.internal("images/NightToggle.png"));
        cursorTexture = new Texture(Gdx.files.internal("images/CustomCursor.png")); // Custom cursor image
        //birdTexture = new Texture("images/bird.png");
        nightBackgroundTexture = new Texture("images/SplashScreen/SplashTitleNight1.png"); // Night background
        clickSound = Gdx.audio.newSound(Gdx.files.internal("sounds/click.mp3")); // Click sound for buttons
        owlSound = Gdx.audio.newSound(Gdx.files.internal("sounds/owlhoot.mp3")); // Owl sound file
        Texture owlTexture = new Texture("images/owl3.png"); // Owl texture file
        owlImage = new Image(owlTexture); // Create owl image actor
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
        // Set owl initial position
        owlImage.setPosition(1750, 720);// Adjust the position as needed
        owlImage.setSize(200, 300);
        stage.addActor(owlImage);

        // Create label for displaying facts
        factLabel = new Label("", new Label.LabelStyle(new BitmapFont(), Color.WHITE)); // Set fact label style
        factLabel.setPosition(1400, 130); // Position it near the owl
        factLabel.setFontScale(1f);
        stage.addActor(factLabel);

        // Add click listener for the owl
        owlImage.addListener(new ClickListener() {
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
        settingLabel = new Label("Settings", skin, "button-red");
        exitLabel = new Label("Exit", skin, "button-red");
        achievementsLabel = new Label("Achievements", skin, "button-red");
        versionLabel = new Label("Version 1.0", skin, "default-white");
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
     * Initialize menu buttons in the main menu
     */
    private void initializeMenuButtons() {
        // Initialises buttons
        startBtn = new Button(new TextureRegionDrawable(new TextureRegion(new Texture("images/ButtonsMain/BlankLarge.png"))));
        loadBtn = new Button(new TextureRegionDrawable(new TextureRegion(new Texture("images/ButtonsMain/BlankLarge.png"))));
        minigamesBtn = new Button(new TextureRegionDrawable(new TextureRegion(new Texture("images/ButtonsMain/BlankLarge.png"))));
        settingsBtn = new Button(new TextureRegionDrawable(new TextureRegion(new Texture("images/ButtonsMain/BlankLarge.png"))));
        achievementsBtn = new Button(new TextureRegionDrawable(new TextureRegion(new Texture("images/ButtonsMain/BlankLarge.png"))));
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
        addButtonElevationEffect(achievementsBtn, achievementsLabel);
    }

    /**
     * Adds an elevation effect to buttons when hovered.
     */
    private void addButtonElevationEffect(final Button button, final Label label) {
        // Add hover listener to the button
        button.addListener(new ClickListener() {
            @Override
            public void enter(InputEvent event, float x, float y, int pointer, Actor fromActor) {
                Gdx.graphics.setSystemCursor(Cursor.SystemCursor.Hand);

                // Apply move and scale actions to both button and label
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
                // Restore the custom cursor when the mouse exits the button
                applyCustomCursor();

                // Return to original position and scale for both button and label
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

    private void applyCustomCursor() {
        if (customCursor != null) {
            Gdx.graphics.setCursor(customCursor); // Reapply the custom cursor
        }
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
     * set the label styles of menu buttons' labels
     *
     * @param style the style that is set
     */
    private void setMenuLabelsStyle(String style) {
        startLabel.setStyle(skin.get(style, Label.LabelStyle.class));
        loadLabel.setStyle(skin.get(style, Label.LabelStyle.class));
        minigameLabel.setStyle(skin.get(style, Label.LabelStyle.class));
        helpLabel.setStyle(skin.get(style, Label.LabelStyle.class));
        settingLabel.setStyle(skin.get(style, Label.LabelStyle.class));
        exitLabel.setStyle(skin.get(style, Label.LabelStyle.class));
        achievementsLabel.setStyle(skin.get(style, Label.LabelStyle.class));
        versionLabel.setStyle(skin.get(style, Label.LabelStyle.class));
    }

    /**
     * Add menu buttons icons and update the positions.
     */
    public void addMenuButtonIcon() {
        float buttonWidth;
        float buttonHeight;
        float buttonSpacing;
        float padTopSpacing;

        if (Gdx.graphics.isFullscreen()) {
            buttonWidth = fullScreenButtonWidth;
            buttonHeight = fullScreenuttonHeight;
            buttonSpacing = fullScreenButtonSpacing;
            padTopSpacing = 500;
            setMenuLabelsStyle("title-red");
        } else {
            buttonWidth = windowButtonWidth;
            buttonHeight = windowButtonHeight;
            buttonSpacing = windowButtonSpacing;
            padTopSpacing = 350;
            setMenuLabelsStyle("button-red");
        }

        menuButtonTable.setPosition((float) Gdx.graphics.getWidth() / 2, (float) Gdx.graphics.getHeight() / 2);
        menuButtonTable.clear();

        Stack startStack = createButtonWithLabelStack(startBtn, startLabel, buttonWidth, buttonHeight);
        Stack loadStack = createButtonWithLabelStack(loadBtn, loadLabel, buttonWidth, buttonHeight);
        Stack minigamesStack = createButtonWithLabelStack(minigamesBtn, minigameLabel, buttonWidth, buttonHeight);
        Stack settingsStack = createButtonWithLabelStack(settingsBtn, settingLabel, buttonWidth, buttonHeight);
        Stack helpStack = createButtonWithLabelStack(helpBtn, helpLabel, buttonWidth, buttonHeight);
        Stack exitStack = createButtonWithLabelStack(exitBtn, exitLabel, buttonWidth, buttonHeight);
        Stack achievementStack = createButtonWithLabelStack(achievementsBtn, achievementsLabel, buttonWidth, buttonHeight);

        menuButtonTable.add(startStack).size(buttonWidth, buttonHeight).padTop(padTopSpacing);
        menuButtonTable.row();
        menuButtonTable.add(loadStack).size(buttonWidth, buttonHeight).padTop(buttonSpacing);
        menuButtonTable.row();
        menuButtonTable.add(minigamesStack).size(buttonWidth, buttonHeight).padTop(buttonSpacing);
        menuButtonTable.row();
        menuButtonTable.add(achievementStack).size(buttonWidth, buttonHeight).padTop(buttonSpacing);
        menuButtonTable.row();
        menuButtonTable.add(settingsStack).size(buttonWidth, buttonHeight).padTop(buttonSpacing);
        menuButtonTable.row();
        menuButtonTable.add(helpStack).size(buttonWidth, buttonHeight).padTop(buttonSpacing);
        menuButtonTable.row();
        menuButtonTable.add(exitStack).size(buttonWidth, buttonHeight).padTop(buttonSpacing);

        stage.addActor(menuButtonTable);
    }

    /**
     * Helper method to create a stack with a button and label, properly centered.
     */
    private Stack createButtonWithLabelStack(Button button, Label label, float buttonWidth, float buttonHeight) {
        Stack stack = new Stack();

        label.setTouchable(Touchable.disabled);
        label.setAlignment(Align.center);
        label.setSize(buttonWidth, buttonHeight);
        label.setFontScale(1.2f);

        stack.add(button);
        stack.add(label);

        return stack;
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

        Image toggleImage = new Image(new TextureRegionDrawable(new TextureRegion(toggleTexture)));

        toggleImage.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                isNightMode = !isNightMode;

                if (isNightMode) {
                    applyNightMode();
                } else {
                    applyDayMode();
                }
            }
        });

        // Add the image to the top left corner
        topLeftTable.add(toggleImage).size(175, 175).pad(10); // Adjust the size as needed

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

        userTable.setSize(175, 175);

        userTable.setVisible(true);

        userTable.setPosition(185, Gdx.graphics.getHeight() - 30);
        Button profileBtn = new Button(new TextureRegionDrawable(new TextureRegion(new Texture("images/ButtonsMain/User.png"))));
        userTable.add(profileBtn).size(110, 110).top().padTop(30).expandY();

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
        userTable.setPosition(165, screenHeight - 190);
    }

    /**
     * Displays the help window with slides for game instructions.
     */
    private void showHelpWindow() {
        final int NUM_SLIDES = 7;
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
        slideInstances[4] = new Slides.Minigames1Slide(skin);
        slideInstances[5] = new Slides.Minigames2Slide(skin);
        slideInstances[6] = new Slides.StatsSlide(skin);


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
    public void update() {
        timer += Gdx.graphics.getDeltaTime();
        if (timer >= 0.25) {
            timer = 0;
            TextureRegionDrawable drawable = new TextureRegionDrawable(birdTextures.get(birdCurrentFrame));
            birdCurrentFrame++;
            if (birdCurrentFrame >= 2) {
                birdCurrentFrame = 0;
            }
            birdAniImage.setDrawable(drawable);
        }
        float birdX = birdAniImage.getX();
        if (birdX < -200 && birdDirection) {
            birdDirection = false;
            birdAniImage.setScale(-1,1);
        } else if (birdX > 1500 && !birdDirection) {
            birdDirection = true;
            birdAniImage.setScale(1,1);
        }

        if (birdDirection) {
            birdX = birdAniImage.getX() + Gdx.graphics.getDeltaTime() * -100;
        } else {
            birdX = birdAniImage.getX() + Gdx.graphics.getDeltaTime() * 100;
        }
        birdAniImage.setPosition(birdX, 500);
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
