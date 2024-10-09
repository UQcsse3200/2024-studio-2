package com.csse3200.game.components.combat;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Align;
import com.csse3200.game.components.CombatStatsComponent;
import com.csse3200.game.services.ServiceLocator;
import com.csse3200.game.ui.UIComponent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Displays the stats bars of both the player and the enemy on the CombatScreen.
 */
public class CombatStatsDisplay extends UIComponent {
    private CombatStatsComponent playerStats;
    private CombatStatsComponent enemyStats;
    private Image playerHealthImage;
    private Image playerHungerImage;
    private Image enemyHealthImage;
    private Image xpImage;
    private Image statusEffectImage;
    private Label playerHealthLabel;
    private Label playerHungerLabel;
    private Label enemyHealthLabel;
    private Label experienceLabel;
    private Label statusEffectLabel;
    private TextureAtlas[] textureAtlas;
    private static Animation<TextureRegion> playerHealthBarAnimation;
    private static Animation<TextureRegion> enemyHealthBarAnimation;
    private static Animation<TextureRegion> playerHungerBarAnimation;
    private static Animation<TextureRegion> xpBarAnimation;
    private static Table statusTable;
    private float barImageWidth;
    private float barImageHeight;
    private static final int totalFrames = 11;
    private static final Logger logger = LoggerFactory.getLogger(CombatStatsDisplay.class);
    private Table hoverTextTable;
    private Label hoverTextLabel;
    private Image backgroundImage;
    private static final Texture BACKGROUND_TEXTURE = new Texture(Gdx.files.internal("images/blue-bar.png"));
    private static final Skin SKIN = new Skin(Gdx.files.internal("flat-earth/skin/flat-earth-ui.json"));

    /**
     * Initialises the required components for the CombatStatsDisplays
     *
     * @param playerStats CombatStatsComponent of the player
     * @param enemyStats  CombatStatsComponent of the enemy
     */
    public CombatStatsDisplay(CombatStatsComponent playerStats, CombatStatsComponent enemyStats) {
        logger.trace("CombatStatsDisplay constructor called");
        this.playerStats = playerStats;
        this.enemyStats = enemyStats;
    }

    @Override
    public void create() {
        super.create();
        addActors();
        entity.getEvents().addListener("onAttack", this::updateHealthUI);
        entity.getEvents().addListener("onAttack", this::updatePlayerHungerUI);
        entity.getEvents().addListener("onGuard", this::updateHealthUI);
        entity.getEvents().addListener("onGuard", this::updatePlayerHungerUI);
        entity.getEvents().addListener("onSleep", this::updateHealthUI);
        entity.getEvents().addListener("onSleep", this::updatePlayerHungerUI);
        entity.getEvents().addListener("onCombatWin", this::updatePlayerExperienceUI);
        entity.getEvents().addListener("useItem", this::updateHealthUI);
        entity.getEvents().addListener("useItem", this::updatePlayerHungerUI);
        entity.getEvents().addListener("statusEffectAdded", (CombatStatsComponent.StatusEffect statusEffect) -> {
            updateStatusEffectUI(statusEffect);
        });
        entity.getEvents().addListener("statusEffectRemoved", this::removeStatusUI);

        createBackgroundForHints();
        createTextForHints();
    }

    /**
     * Initialises a table containing the images of the player's stats bars and their associated labels
     *
     * @return A table to be added to the stage containing the player stats bars and labels
     */
    private Table initialisePlayerStatBars() {
        logger.trace("Player stat bars are being initialised");
        float barWidthScaling = 0.7f;
        float barHeightScaling = 0.4f;
        float barLabelGap = 2f;
        float xpHeightScaling = 1.15f;
        float tableTopPadding = 10f;
        float tableLeftPadding = 5f;

        // Player Bars Display
        Table playerTable = new Table();
        playerTable.top().left();
        playerTable.setFillParent(true);
        playerTable.padTop(tableTopPadding).padLeft(tableLeftPadding);

        // Health text
        int health = playerStats.getHealth();
        CharSequence healthText = String.format("HP: %d", health);
        playerHealthLabel = new Label(healthText, skin, "large");

        // Experience text
        int experience = playerStats.getExperience();
        CharSequence experienceText = String.format("EXP: %d", experience);
        experienceLabel = new Label(experienceText, skin, "large");

        // Hunger text
        int hunger = playerStats.getHunger();
        CharSequence hungerText = String.format("Hunger: %d", hunger);
        playerHungerLabel = new Label(hungerText, skin, "large");

        // Health/XP images
        playerHealthImage = new Image(ServiceLocator.getResourceService().getAsset("images/health_bar_x1.png",
                Texture.class));
        xpImage = new Image(ServiceLocator.getResourceService().getAsset("images/xp_bar.png", Texture.class));
        playerHungerImage = new Image(ServiceLocator.getResourceService().getAsset("images/hunger_bar.png",
                Texture.class));
        barImageWidth = (float) (playerHealthImage.getWidth() * barWidthScaling);
        barImageHeight = (float) (playerHealthImage.getHeight() * barHeightScaling);

        // Aligning the bars one below the other and adding them to table
        playerTable.add(playerHealthImage).size(barImageWidth, barImageHeight).pad(barLabelGap);
        playerTable.add(playerHealthLabel).align(Align.left);
        playerTable.row();
        playerTable.add(playerHungerImage).size(barImageWidth, barImageHeight * barLabelGap).pad(barLabelGap);
        playerTable.add(playerHungerLabel).align(Align.left);
        playerTable.row();
        playerTable.add(xpImage).size(barImageWidth, (float) (barImageHeight * xpHeightScaling)).pad(barLabelGap);
        playerTable.add(experienceLabel).align(Align.left);

        return playerTable;
    }

    /**
     * Initialises a table containing the enemy's stats bars and labels
     *
     * @return A table containing the enemy's health bar and its associated label
     */
    private Table initialiseEnemyStatBars() {
        logger.trace("Enemy stat bars are being initialised");
        // Padding to separate enemy bar from buttons
        float barButtonPadding = 50f;
        float barLabelGap = 2f;
        float generalPadding = 30f;

        // Enemy Bars Display
        Table enemyTable = new Table();
        enemyTable.right().top();
        enemyTable.setFillParent(true);
        enemyTable.padTop(barButtonPadding);

        // Enemy health text
        int eHealth = enemyStats.getHealth();
        CharSequence eHealthText = String.format("HP: %d", eHealth);
        enemyHealthLabel = new Label(eHealthText, skin, "large");

        // Images
        enemyHealthImage = new Image(ServiceLocator.getResourceService().getAsset("images/health_bar_x1.png",
                Texture.class));
        enemyTable.add(enemyHealthImage).size(barImageWidth, barImageHeight).pad(barLabelGap).padTop(generalPadding);
        enemyTable.add(enemyHealthLabel).align(Align.left).padRight(generalPadding).padTop(generalPadding);
        return enemyTable;
    }

    /**
     * Initializes the animations for the health, hunger, and experience bars.
     * Each bar's animation consist of series of consecutive frames from a texture atlas given in the assets folder.
     * The animations reflect the current status of the player's health, hunger, and experience.
     */
    public void initBarAnimations() {
        logger.trace("Stat Bar animations are being initialised");
        float animationFrameRate = 0.66f;
        int numberOfAtlases = 3;

        // Initialise textureAtlas for 2 bars
        textureAtlas = new TextureAtlas[numberOfAtlases];
        // HealthBar initialisation
        textureAtlas[0] = new TextureAtlas("spriteSheets/healthBars.txt");
        TextureRegion[] healthBarFrames = new TextureRegion[totalFrames];
        // Names each frame and locates associated frame in txt file
        for (int i = 0; i < healthBarFrames.length; i++) {
            String healthFrameNames = (100 - i * 10) + "%_health";
            healthBarFrames[i] = textureAtlas[0].findRegion(healthFrameNames);
        }
        playerHealthBarAnimation = new Animation<>(animationFrameRate, healthBarFrames);
        enemyHealthBarAnimation = new Animation<>(animationFrameRate, healthBarFrames);

        // xpBar initialisation
        textureAtlas[1] = new TextureAtlas("spriteSheets/xpBars.atlas");
        TextureRegion[] xpBarFrames = new TextureRegion[totalFrames];
        // Names each frame and locates associated frame in txt file
        for (int i = 0; i < xpBarFrames.length; i++) {
            String xpFrameNames = (100 - (i * 10)) + "%_xp";
            xpBarFrames[i] = textureAtlas[1].findRegion(xpFrameNames);
        }
        xpBarAnimation = new Animation<>(animationFrameRate, xpBarFrames);

        // hungerBar initialisation
        textureAtlas[2] = new TextureAtlas("images/hungerbar.atlas");
        TextureRegion[] hungerBarFrames = new TextureRegion[totalFrames];
        // Names each frame and locates associated frame in txt file
        for (int i = 0; i < hungerBarFrames.length; i++) {
            String hungerFrameNames = (100 - i * 10) + "%_hunger";
            hungerBarFrames[i] = textureAtlas[2].findRegion(hungerFrameNames);
        }
        playerHungerBarAnimation = new Animation<>(animationFrameRate, hungerBarFrames);
    }

    /**
     * Sets a new frame for a stat bar animation on the stage.
     *
     * @param frameIndex       The index of the desired frame in the animation to be changed
     * @param statBarAnimation The animation for the specific stat bar to be changed
     * @param statBar          image that stores the current frame on the stage for the stat bar.
     */
    public void setNewFrame(int frameIndex, Animation<TextureRegion> statBarAnimation, Image statBar) {
        logger.trace("Frame change being made for the stat bar");
        // Grab the desired frame at a specified frame rate
        TextureRegion[] keyFrames = statBarAnimation.getKeyFrames();
        TextureRegion currentFrame = keyFrames[frameIndex];
        // Replace the frame shown on the stage
        statBar.setDrawable(new TextureRegionDrawable(currentFrame));
    }

    /**
     * Adds the playerTable and enemyTable into the stage of the game to be displayed
     */
    private void addActors() {
        logger.trace("UI Components being added to combat stage");
        Table playerTable = initialisePlayerStatBars();
        Table enemyTable = initialiseEnemyStatBars();

        stage.addActor(playerTable);
        stage.addActor(enemyTable);

        initBarAnimations();
        updateHealthUI(playerStats, enemyStats);
        updatePlayerExperienceUI(playerStats);
        updatePlayerHungerUI(playerStats, enemyStats);
    }

    /**
     * Updates the labels and animations associated with the player and enemy's stats
     *
     * @param playerStats CombatStatsComponent of the player
     * @param enemyStats  CombatStatsComponent of the enemy
     */
    public void updateHealthUI(CombatStatsComponent playerStats, CombatStatsComponent enemyStats) {
        logger.trace("Detected health change in combat and is updating UI");
        int playerCurHealth = playerStats.getHealth();
        int playerMaxHealth = playerStats.getMaxHealth();
        int enemyCurHealth = enemyStats.getHealth();
        int enemyMaxHealth = enemyStats.getMaxHealth();

        CharSequence playerText = String.format("HP: %d/%d", playerCurHealth, playerMaxHealth);
        CharSequence enemyText = String.format("HP: %d/%d", enemyCurHealth, enemyMaxHealth);

        // Adjusts position as lists start at index 0
        int indexAdjustment = totalFrames - 1;
        int playerFrameIndex = indexAdjustment - (int) ((float) playerCurHealth / playerMaxHealth * (totalFrames - 1));
        playerFrameIndex = Math.max(0, Math.min(playerFrameIndex, totalFrames - 1));

        int enemyFrameIndex = indexAdjustment - (int) ((float) enemyCurHealth / enemyMaxHealth * (totalFrames - 1));
        enemyFrameIndex = Math.max(0, Math.min(enemyFrameIndex, totalFrames - 1));

        // Update player stats
        playerHealthLabel.setText(playerText);
        setNewFrame(playerFrameIndex, playerHealthBarAnimation, playerHealthImage);

        // Update enemy stats
        enemyHealthLabel.setText(enemyText);
        setNewFrame(enemyFrameIndex, enemyHealthBarAnimation, enemyHealthImage);
    }


    /**
     * Updates the experience label and bar of the character after defeating a specific enemy
     *
     * @param playerStats CombatStatsComponent of the player
     */
    public void updatePlayerExperienceUI(CombatStatsComponent playerStats) {
        logger.trace("Detected experience change in combat and is updating UI");
        int experience = playerStats.getExperience();
        int maxExperience = playerStats.getMaxExperience();
        CharSequence text = String.format("EXP: %d/%d", experience, maxExperience);
        experienceLabel.setText(text);

        int frameIndex = totalFrames - 1 - (int) ((float) experience / maxExperience * (totalFrames - 1));
        frameIndex = Math.max(0, Math.min(frameIndex, totalFrames - 1));
        // Set the current frame of the health bar animation
        setNewFrame(frameIndex, xpBarAnimation, xpImage);
    }

    /**
     * Updates the hunger bar animation of the player
     *
     * @param playerStats The CombatStatsComponent of the player
     *                    THE HUNGER BAR IS USED TEMPORARILY FOR DISPLAYING THE PLAYER'S STAMINA
     */
    public void updatePlayerHungerUI(CombatStatsComponent playerStats, CombatStatsComponent enemyStats) {
        logger.trace("Detected stamina change in combat and is updating UI");
        int hunger = playerStats.getStamina();
        int maxHunger = playerStats.getMaxStamina();
        CharSequence text = String.format("Stamina: %d/%d", hunger, maxHunger);
        playerHungerLabel.setText(text);

        int frameIndex = totalFrames - 1 - (int) ((float) hunger / maxHunger * (totalFrames - 1));
        frameIndex = Math.max(0, Math.min(frameIndex, totalFrames - 1));
        // Set the current frame of the health bar animation
        setNewFrame(frameIndex, playerHungerBarAnimation, playerHungerImage);

    }

    /**
     * Method used to display the relevant status effect that the player is afflicted with
     * @param statusEffect An ENUM that stores the type of status effect the player has been afflicted with
     */
    public void updateStatusEffectUI(CombatStatsComponent.StatusEffect statusEffect) {
        logger.trace("Adding status effect label and bar in CombatStatsDisplay");
        float tableTopPadding = 40f;
        float tableLeftPadding = 750f;
        String statusFilePath = String.format("images/statuses/%s_stat.png", statusEffect.name().toLowerCase());
        statusEffectImage = new Image (ServiceLocator.getResourceService().getAsset(statusFilePath, Texture.class));
        statusTable = new Table();
        statusTable.add(statusEffectImage);
        statusTable.top().left();
        statusTable.row();

        String statusMessage = String.format("You are currently %s", statusEffect.name().toLowerCase());
        statusEffectLabel = new Label(statusMessage, skin, "large");
        statusTable.add(statusEffectLabel);
        statusTable.setFillParent(true);
        statusTable.padTop(tableTopPadding).padLeft(tableLeftPadding);
        statusTable.addListener(new InputListener() {
            // Brings up the status effect description when the user hovers over the statusTable
            @Override
            public boolean mouseMoved(InputEvent event, float x, float y) {
                setTextForStatusEffectHint();
                return true;
            }
            // hides the combat hint when the user is no longer hovering over the attack button
            @Override
            public void exit(InputEvent event, float x, float y, int pointer, Actor fromActor) {
                hoverTextTable.setVisible(false);  // Hide the hover text when not hovering
                backgroundImage.setVisible(false);
            }
        });

        stage.addActor(statusTable);
    }

    /**
     * Method to remove the status effect bar and text from the combat screen
     */
    private void removeStatusUI() {
        logger.trace("Removing status bar assest in CombatStatsDisplay");
        statusTable.remove();
    }

    /**
     * Create a text box pop up to provide the user with description of status effects when hovering over with mouse
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

    private void setTextForStatusEffectHint() {
        String effectDescription = "";
        if (playerStats.hasStatusEffect(CombatStatsComponent.StatusEffect.BLEEDING)) {
            effectDescription = "While bleeding, your GUARDs are less effective.";
        } else if (playerStats.hasStatusEffect(CombatStatsComponent.StatusEffect.SHOCKED)) {
            effectDescription = "While shocked, Your ATTACKs are weakened.";
        } else if (playerStats.hasStatusEffect(CombatStatsComponent.StatusEffect.POISONED)) {
            effectDescription = "While poisoned, SLEEPing won't heal you.";
        } else if (playerStats.hasStatusEffect(CombatStatsComponent.StatusEffect.CONFUSED)) {
            effectDescription = "While confused, your animal might make a wrong move.";
        }
        hoverTextLabel.setText(effectDescription);  // Set hover text
        // set the position of the status effect hint text
        hoverTextTable.setPosition(Gdx.graphics.getWidth() * 0.5f,
                Gdx.graphics.getHeight() * 0.70f);
        // set the position of the background for the status effect hint text
        float combatHintBackgroundHeight = Gdx.graphics.getHeight() * 0.1f;  // 7% of the screen height
        // combat background is proportional to the combatHintTextLength
        float combatHintBackgroundWidth = hoverTextLabel.getWidth() + Gdx.graphics.getWidth() * 0.1f;
        backgroundImage.setSize(combatHintBackgroundWidth, combatHintBackgroundHeight);
        backgroundImage.setPosition(Gdx.graphics.getWidth() * 0.5f - backgroundImage.getWidth() * 0.5f
                , Gdx.graphics.getHeight() * 0.7f -
                        combatHintBackgroundHeight * 0.5f);
        backgroundImage.setVisible(true); // Show the background for status effect hints
        hoverTextTable.setVisible(true);  // Show the status effect hint text
    }

    @Override
    public void draw(SpriteBatch batch) {
        int screenHeight = Gdx.graphics.getHeight();
        float offsetX = 10f;
        float offsetY = 30f;

        //title.setPosition(offsetX, screenHeight - offsetY);
    }

    @Override
    public void dispose() {
        logger.trace("Disposing assets in CombatStatsDisplay");
        super.dispose();
        playerHealthImage.remove();
        enemyHealthImage.remove();
        xpImage.remove();
        playerHealthLabel.remove();
        enemyHealthLabel.remove();
        experienceLabel.remove();
        if (statusEffectLabel != null) {
            statusEffectLabel.remove();
        }
        if (statusEffectImage != null) {
            statusEffectImage.remove();
        }
    }
}
