package com.csse3200.game.components.combat;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.csse3200.game.areas.combat.CombatArea;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.csse3200.game.entities.Entity;
import com.csse3200.game.services.DialogueBoxService;
import com.csse3200.game.inventory.items.AbstractItem;
import com.csse3200.game.inventory.items.ItemUsageContext;
import com.csse3200.game.services.ServiceContainer;
import com.csse3200.game.services.ServiceLocator;
import com.csse3200.game.ui.UIComponent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Displays a button to exit the Main Game screen to the Main Menu screen.
 */
public class CombatButtonDisplay extends UIComponent {
    private static final Logger logger = LoggerFactory.getLogger(CombatButtonDisplay.class);
    private static final float Z_INDEX = 1f;
    private Table table;
    private final Screen screen;
    private final ServiceContainer container;
    TextButton attackButton;
    TextButton guardButton;
    TextButton sleepButton;
    TextButton itemsButton;
    ChangeListener dialogueBoxListener;
    CombatArea combatArea;
    // Create a Table to hold the hover text with a background
    private Table hoverTextTable;
    private Label hoverTextLabel;
    private Image backgroundImage;
    private static final Texture BACKGROUND_TEXTURE = new Texture(Gdx.files.internal("images/blue-bar.png"));
    private static final Skin SKIN = new Skin(Gdx.files.internal("flat-earth/skin/flat-earth-ui" +
            ".json"));


    /**
     * Initialises the CombatButtonDisplay UIComponent
     *
     * @param screen    The current screen that the buttons are being rendered onto
     * @param container The container that
     */
    public CombatButtonDisplay(Screen screen, ServiceContainer container, CombatArea combatArea) {
        this.screen = screen;
        this.container = container;
        this.combatArea = combatArea;
    }

    @Override
    public void create() {
        super.create();
        logger.debug("CombatButtonDisplay::Create() , before calling addActors");
        addActors();
        entity.getEvents().addListener("displayCombatResults", this::hideButtons);
        entity.getEvents().addListener("itemClicked", this::onItemClicked);
        entity.getEvents().addListener("hideCurrentOverlay", this::addActors);
        entity.getEvents().addListener("disposeCurrentOverlay", this::addActors);
        entity.getEvents().addListener("endOfCombatDialogue", this::displayEndCombatDialogue);

        // Start idle animations
        combatArea.startEnemyAnimation(CombatArea.CombatAnimation.IDLE);

        // Add a listener to the stage to monitor the DialogueBox visibility
        dialogueBoxListener = new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                if (!ServiceLocator.getDialogueBoxService().getIsVisible()) {
                    logger.info("DialogueBox is no longer visible, adding actors back.");
                    addActors();
                }
            }
        };
        entity.getEvents().addListener("endOfBossCombatDialogue", this::displayBossEndCombatDialogue);
        dialogueBoxListener = new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                if (!ServiceLocator.getDialogueBoxService().getIsVisible()) {
                    logger.debug("DialogueBox is no longer visible, adding actors back.");
                    addActors();
                    // Resume idle animations
                    combatArea.startEnemyAnimation(CombatArea.CombatAnimation.IDLE);
                }
            }
        };
        stage.addListener(dialogueBoxListener);
        createBackgroundForHints();
        createTextForHints();
    }

    /**
     * Create a text box pop up to provide the user with description on moves when hovering over with mouse upon
     */
    private void createTextForHints() {
        hoverTextLabel = new Label("", SKIN, "default-white");
        hoverTextTable = new Table(SKIN);
        hoverTextTable.clear();
        hoverTextTable.setBackground("white");  // Set a white background (ensure you have this drawable in your skin)
        hoverTextTable.add(hoverTextLabel).pad(10f);  // Add padding around the text
        hoverTextTable.setVisible(false);  // Initially hidden
        stage.addActor(hoverTextTable);
    }

    /**
     * Create a background for the text hints
     */
    private void createBackgroundForHints() {
        // Create a label and a table to display the hover text
        backgroundImage = new Image(new TextureRegionDrawable(BACKGROUND_TEXTURE));
        backgroundImage.setVisible(false);
        stage.addActor(backgroundImage);
    }

    /**
     * Used to set the text for move description upon hovering over with mouse
     * @param moveDescription text to display that describes the move
     */
    private void setTextForCombatHint(String moveDescription) {
        hoverTextLabel.setText(moveDescription);  // Set hover text
        // set the position of the combat hint text
        hoverTextTable.setPosition(Gdx.graphics.getWidth() * 0.5f,
                Gdx.graphics.getHeight() * 0.20f);
        // set the position of the background for the combat hint text
        float combatHintBackgroundHeight = Gdx.graphics.getHeight() * 0.1f;  // 7% of the screen height
        // combat background is proportional to the combatHintTextLength
        float combatHintBackgroundWidth = hoverTextLabel.getWidth() + Gdx.graphics.getWidth() * 0.1f;
        backgroundImage.setSize(combatHintBackgroundWidth, combatHintBackgroundHeight);
        backgroundImage.setPosition(Gdx.graphics.getWidth() * 0.5f - backgroundImage.getWidth() * 0.5f
                , Gdx.graphics.getHeight() * 0.2f -
                combatHintBackgroundHeight * 0.5f);
        backgroundImage.setVisible(true); // Show the background for combat hints
        hoverTextTable.setVisible(true);  // Show the combat hint text
    }


    /**
     * Initialises the buttons, adds listeners to them, and adds them into the game's stage
     */
    private void addActors() {
        table = new Table();
        table.bottom();
        table.setFillParent(true);

        attackButton = new TextButton("Attack", skin);
        guardButton = new TextButton("Guard", skin);
        sleepButton = new TextButton("Sleep", skin);
        itemsButton = new TextButton("Items", skin);

        attackButton.addListener(
                new ChangeListener() {
                    @Override
                    public void changed(ChangeEvent changeEvent, Actor actor) {
                        entity.getEvents().trigger("Attack", screen, container);
                        combatArea.startEnemyAnimation(CombatArea.CombatAnimation.MOVE);
                    }
                });
        attackButton.addListener(new InputListener() {
            // Brings up the combat hint when the user hovers over attack button
            @Override
            public boolean mouseMoved(InputEvent event, float x, float y) {
                setTextForCombatHint("Lower enemy HP but drains hunger!");
                return true;
            }
            // hides the combat hint when the user is no longer hovering over the attack button
            @Override
            public void exit(InputEvent event, float x, float y, int pointer, Actor fromActor) {
                hoverTextTable.setVisible(false);  // Hide the hover text when not hovering
                backgroundImage.setVisible(false);
            }
        });

        guardButton.addListener(
                new ChangeListener() {
                    @Override
                    public void changed(ChangeEvent changeEvent, Actor actor) {
                        entity.getEvents().trigger("Guard", screen, container);
                        combatArea.startEnemyAnimation(CombatArea.CombatAnimation.MOVE);
                    }
                });
        guardButton.addListener(new InputListener() {
            // Brings up the combat hint when the user hovers over guard button
            @Override
            public boolean mouseMoved(InputEvent event, float x, float y) {
                setTextForCombatHint("Reduces damage of the next attack but drains hunger!");
                return true;
            }
            // hides the combat hint when the user is no longer hovering over the guard button
            @Override
            public void exit(InputEvent event, float x, float y, int pointer, Actor fromActor) {
                hoverTextTable.setVisible(false);  // Hide the hover text when not hovering
                backgroundImage.setVisible(false);
            }
        });

        sleepButton.addListener(
                new ChangeListener() {
                    @Override
                    public void changed(ChangeEvent changeEvent, Actor actor) {
                        entity.getEvents().trigger("Sleep", screen, container);
                        combatArea.startEnemyAnimation(CombatArea.CombatAnimation.MOVE);
                    }
                });
        sleepButton.addListener(new InputListener() {
            @Override
            public boolean mouseMoved(InputEvent event, float x, float y) {
                setTextForCombatHint("Recover health and hunger but potentially take more damage!");
                return true;
            }

            @Override
            public void exit(InputEvent event, float x, float y, int pointer, Actor fromActor) {
                hoverTextTable.setVisible(false);  // Hide the hover text when not hovering
                backgroundImage.setVisible(false);
            }
        });
        itemsButton.addListener(
                new ChangeListener() {
                    @Override
                    public void changed(ChangeEvent changeEvent, Actor actor) {
                        entity.getEvents().trigger("Items", screen, container);
                        combatArea.startEnemyAnimation(CombatArea.CombatAnimation.MOVE);
                        hideButtons();
                    }
                });
        itemsButton.addListener(new InputListener() {
            @Override
            public boolean mouseMoved(InputEvent event, float x, float y) {
                setTextForCombatHint("Access items to either buff yourself or de-buff the enemy");
                return true;
            }

            @Override
            public void exit(InputEvent event, float x, float y, int pointer, Actor fromActor) {
                hoverTextTable.setVisible(false);  // Hide the hover text when not hovering
                backgroundImage.setVisible(false);
            }
        });

        // Position the button on the central bottom part and make them a lil bigger
        table.add(attackButton).padBottom(50).width(300).height(60).padLeft(10f);
        table.add(guardButton).padBottom(50).width(300).height(60).padLeft(10f);
        table.add(sleepButton).padBottom(50).width(300).height(60).padLeft(10f);
        table.add(itemsButton).padBottom(50).width(300).height(60).padLeft(10f);

        stage.addActor(table);
    }

    /**
     * Hides the buttons on the combat screen
     */
    public void hideButtons() {
        logger.debug(String.format("The dialogue box is present in CombatButDisplay: %b", ServiceLocator.getDialogueBoxService().getIsVisible()));
        table.remove();
    }

    /**
     * Function used to display the specific text for the DialogueBox at the end of combat
     * @param enemyEntity Entity of the enemy that was encountered in combat
     * @param winStatus Boolean that states if the player has won in combat or not (false)
     */
    public void displayEndCombatDialogue(Entity enemyEntity, boolean winStatus) {
        String[][] endText;
        stage.removeListener(dialogueBoxListener);

        // Hide buttons before displaying dialogue
        entity.getEvents().trigger("displayCombatResults");

        ChangeListener endDialogueListener = new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                // Check if the DialogueBox is not visible
                if (!ServiceLocator.getDialogueBoxService().getIsVisible()) {
                    logger.debug("DialogueBox is no longer visible, combat screen can be exited.");
                    entity.getEvents().trigger("finishedEndCombatDialogue", enemyEntity);
                }
            }
        };

        // New listener for end of game
        stage.addListener(endDialogueListener);
        if (winStatus) {
            endText = new String[][]{{"You tamed the wild animal. Say hi to your new friend!"}};
        } else {
            endText = new String[][]{{"You lost to the beast. Try leveling up, and powering up " +
                    "before battling again."}};
        }
        ServiceLocator.getDialogueBoxService().updateText(endText, DialogueBoxService.DialoguePriority.BATTLE);
    }

    /**
     * Function used to display the specific text for the DialogueBox at the end of combat
     * with the land Kangaroo Boss
     * @param bossEntity Entity of the boss enemy that was encountered in combat
     * @param winStatus Boolean that states if the player has won in combat or not (false)
     */
    public void displayBossEndCombatDialogue(Entity bossEntity, boolean winStatus) {
        String[][] endText;
        stage.removeListener(dialogueBoxListener);

        // Hide buttons before displaying dialogue
        entity.getEvents().trigger("displayCombatResults");

        ChangeListener endDialogueListener = new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                // Check if the DialogueBox is not visible
                if (!ServiceLocator.getDialogueBoxService().getIsVisible()) {
                    if (!winStatus) {
                        logger.info("Switching screens to gamer over lose after losing to boss.");
                        entity.getEvents().trigger("finishedBossLossCombatDialogue");
                    } else if (bossEntity.getEnemyType() == Entity.EnemyType.AIR_BOSS){
                        logger.info("Switching screen to end game stats.");
                        entity.getEvents().trigger("finishedFinalCombatDialogue");
                    } else {
                        logger.info("DialogueBox is no longer visible, combat screen can be exited.");
                        entity.getEvents().trigger("finishedEndCombatDialogue", bossEntity);
                    }
                }
            }
        };

        // New listener for end of game
        stage.addListener(endDialogueListener);

        // Get dialogue from DialogueData class
        BossCombatDialogueData dialogueData = new BossCombatDialogueData();
        endText = dialogueData.getDialogue(bossEntity.getEnemyType().toString(), winStatus);

        ServiceLocator.getDialogueBoxService().updateText(endText, DialogueBoxService.DialoguePriority.BATTLE);
    }

    /**
     * Handles when an item is selected in the CombatInventoryDisplay.
     * Prompts the user if they would like to confirm their choice, then upon confirmation uses the item.
     * If the user does not confirm that they would like to use the item, nothing happens.
     * @param item selected from the inventory.
     */
    private void onItemClicked(AbstractItem item, int index, ItemUsageContext context) {
        logger.debug(String.format("Item %s was clicked.", item.getName()));
        String[][] checkText = {{String.format("You are selecting %s as your move.", item.getName())}};
        ServiceLocator.getDialogueBoxService().updateText(checkText, DialogueBoxService.DialoguePriority.BATTLE);

        entity.getEvents().trigger("toggleCombatInventory");
        entity.getEvents().trigger("itemConfirmed", item, index, context);
    }

    @Override
    public void draw(SpriteBatch batch) {
        // draw is handled by the stage
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
