package com.csse3200.game.components.story;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.csse3200.game.components.settingsmenu.UserSettings;
import com.csse3200.game.services.DialogueBoxService;
import com.csse3200.game.services.ServiceLocator;
import com.csse3200.game.ui.UIComponent;
import com.csse3200.game.ui.dialoguebox.DialogueBox;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * A UI component for displaying the story screen. Contains all 6 background images for the StoryScreen.
 * Is iterated through using next and back buttons. All displays can be skipped using skip button.
 */
public class StoryDisplay extends UIComponent {
    private static final Logger logger = LoggerFactory.getLogger(StoryDisplay.class);
    private static final float Z_INDEX = 2f;
    private Table table;
    private final Texture[] backgroundTextures;
    private final int screenNum;
    private final int finalScreen;
    private final String storyName;  // The name of the selected story (dog, croc, bird)
    private DialogueBox dialogueBox;  // DialogueBox instance for displaying the story dialogues

    public StoryDisplay(Texture[] backgroundTextures, int screenNum, String storyName) {
        super();
        this.backgroundTextures = backgroundTextures;
        this.screenNum = screenNum;
        finalScreen = backgroundTextures.length - 1;
        this.storyName = storyName;
    }

    /**
     * Called when the component is created. Initializes the story UI.
     */
    @Override
    public void create() {
        super.create();
        logger.info("Creating StoryDisplay");
        addActors();
        applyUserSettings();

        entity.getEvents().addListener("nextDisplay", this::onNextDisplay);
        entity.getEvents().addListener("backDisplay", this::onBackDisplay);
        entity.getEvents().addListener("skip", this::onSkip);
        displayStoryDialogue();
    }
    /**
     * Display the story dialogue using DialogueBoxService.
     */
    private void displayStoryDialogue() {
        StoryDialogueData dialogueData = new StoryDialogueData();
        String[][] dialogueText = dialogueData.getDialogue(storyName, screenNum);  // Get the dialogue for this screen

        // Initialize DialogueBox if it isn't already initialized
        if (dialogueBox == null) {
            Stage stage = ServiceLocator.getRenderService().getStage();
            dialogueBox = new DialogueBox(stage);  // Pass the stage to the DialogueBox
        }

        // Show the dialogue for the current screen
        dialogueBox.showDialogueBox(dialogueText);
        dialogueBox.removeContinueButton();
    }


    /**
     * Applies user settings to the game.
     */
    private void applyUserSettings() {
        UserSettings.Settings settings = UserSettings.get(); // Retrieve current settings
        UserSettings.applySettings(settings); // Apply settings to the game
    }

    /**
     * Adds all UI elements (buttons, labels, etc.) to the story display.
     */
    private void addActors() {
        table = new Table();
        table.setFillParent(true);

        Table bottomLeftTable = new Table();
        bottomLeftTable.bottom().left();
        bottomLeftTable.setFillParent(true);

        Table bottomRightTable = new Table();
        bottomRightTable.bottom().right();
        bottomRightTable.setFillParent(true);

        table.add(bottomLeftTable).padTop(15f).width(180f).height(45f);
        table.add(bottomRightTable).padTop(15f).width(180f).height(45f);
        table.row();

        // Initialises buttons
        TextButton nextBtn = new TextButton("Next", skin);
        TextButton backBtn = new TextButton("Back", skin);
        TextButton skipBtn = new TextButton(">>", skin);


        // Adds UI component (hover over buttons)
        addButtonElevationEffect(nextBtn);
        addButtonElevationEffect(backBtn);
        addButtonElevationEffect(skipBtn);

        // Added handles for when clicked
        nextBtn.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent changeEvent, Actor actor) {
                logger.debug("Next button clicked");
                entity.getEvents().trigger("nextDisplay");
            }
        });

        if (screenNum != 0) {
            backBtn.addListener(new ChangeListener() {
                @Override
                public void changed(ChangeEvent changeEvent, Actor actor) {
                    logger.debug("Back button clicked");
                    entity.getEvents().trigger("backDisplay");
                }
            });
        }

        skipBtn.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent changeEvent, Actor actor) {
                logger.debug("Skip button clicked");
                entity.getEvents().trigger("skip");
            }
        });

        // formats sizes of buttons
        bottomRightTable.add(nextBtn).padBottom(15f).padRight(15f).width(180f).height(45f);
        bottomRightTable.row();

        if (screenNum != 0) {
            bottomLeftTable.add(backBtn).padBottom(15f).padLeft(15f).width(180f).height(45f);
            bottomLeftTable.row();
        }

        // Formats height of buttons on screen
        sizeTable();

        // Add the minimize button to the top-right corner
        Table topRightTable = new Table();
        topRightTable.top().right();
        topRightTable.setFillParent(true);
        topRightTable.add(skipBtn).size(40, 40).padTop(10).padRight(10);

        stage.addActor(topRightTable);
        stage.addActor(table);
        stage.addActor(bottomLeftTable);
        stage.addActor(bottomRightTable);


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

    @Override
    public void draw(SpriteBatch batch) {
        // batch isn't used, batchDupe is to make SonarCloud happy, unsure why batch doesn't just work, but it causes
        // the game to crash :/
        SpriteBatch batchDupe = new SpriteBatch();
        batchDupe.begin();
        batchDupe.draw(backgroundTextures[screenNum], 0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
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

    /**
     * Disposes the current display. Triggers onNext function in storyActions.
     */
    private void onNextDisplay() {
        dispose();
        entity.getEvents().trigger("next", screenNum);
    }

    /**
     * Disposes the current display. Triggers onBack function in storyActions.
     */
    private void onBackDisplay() {
        dispose();
        entity.getEvents().trigger("back", screenNum);
    }

    /**
     * Disposes the current display. Triggers onNext function in storyActions.
     * Sends the final screen to make the triggered function believe it's on the last display.
     */
    private void onSkip() {
        dispose();
        entity.getEvents().trigger("next", finalScreen);
    }
}
