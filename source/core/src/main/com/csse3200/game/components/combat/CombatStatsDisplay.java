package com.csse3200.game.components.combat;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
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
    private TextureAtlas[] textureAtlas;
    private static Animation<TextureRegion> playerHealthBarAnimation;
    private static Animation<TextureRegion> enemyHealthBarAnimation;
    private static Animation<TextureRegion> playerHungerBarAnimation;
    private static Animation<TextureRegion> xpBarAnimation;
    private float barImageWidth;
    private float barImageHeight;
    private static final int totalFrames = 11;
    private static final Logger logger = LoggerFactory.getLogger(CombatStatsDisplay.class);

    /**
     * Initialises the required components for the CombatStatsDisplays
     *
     * @param playerStats CombatStatsComponent of the player
     * @param enemyStats  CombatStatsComponent of the enemy
     */
    public CombatStatsDisplay(CombatStatsComponent playerStats, CombatStatsComponent enemyStats) {
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
        entity.getEvents().addListener("statusEffectAdded", (CombatStatsComponent.StatusEffect statusEffect) -> {
            updateStatusEffectUI(statusEffect);
        });
    }

    /**
     * Initialises a table containing the images of the player's stats bars and their associated labels
     *
     * @return A table to be added to the stage containing the player stats bars and labels
     */
    private Table initialisePlayerStatBars() {
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
        Table playerTable = initialisePlayerStatBars();
        Table enemyTable = initialiseEnemyStatBars();

        stage.addActor(playerTable);
        stage.addActor(enemyTable);
        updateStatusEffectUI(CombatStatsComponent.StatusEffect.BLEEDING);

        initBarAnimations();
    }

    /**
     * Updates the labels and animations associated with the player and enemy's stats
     *
     * @param playerStats CombatStatsComponent of the player
     * @param enemyStats  CombatStatsComponent of the enemy
     */
    public void updateHealthUI(CombatStatsComponent playerStats, CombatStatsComponent enemyStats) {
        int playerCurHealth = playerStats.getHealth();
        int playerMaxHealth = playerStats.getMaxHealth();
        int enemyCurHealth = enemyStats.getHealth();
        int enemyMaxHealth = enemyStats.getMaxHealth();

        CharSequence playerText = String.format("HP: %d", playerCurHealth);
        CharSequence enemyText = String.format("HP: %d", enemyCurHealth);

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
        int experience = playerStats.getExperience();
        int maxExperience = playerStats.getMaxExperience();
        CharSequence text = String.format("EXP: %d", experience);
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
        int hunger = playerStats.getStamina();
        int maxHunger = playerStats.getMaxStamina();
        CharSequence text = String.format("Stamina: %d", hunger);
        playerHungerLabel.setText(text);

        int frameIndex = totalFrames - 1 - (int) ((float) hunger / maxHunger * (totalFrames - 1));
        frameIndex = Math.max(0, Math.min(frameIndex, totalFrames - 1));
        // Set the current frame of the health bar animation
        setNewFrame(frameIndex, playerHungerBarAnimation, playerHungerImage);

    }

    public void updateStatusEffectUI(CombatStatsComponent.StatusEffect statusEffect) {
        float tableTopPadding = 40f;
        float tableLeftPadding = 750f;
        String statusFilePath = String.format("images/statuses/%s_stat.png", statusEffect.name().toLowerCase());
        statusEffectImage = new Image (ServiceLocator.getResourceService().getAsset(statusFilePath, Texture.class));
        Table statusTable = new Table();
        statusTable.add(statusEffectImage);
        statusTable.top().left();
        statusTable.row();

        String statusMessage = String.format("You are currently %s", statusEffect.name().toLowerCase());
        Label statusLabel = new Label(statusMessage, skin, "large");
        statusTable.add(statusLabel);
        statusTable.setFillParent(true);
        statusTable.padTop(tableTopPadding).padLeft(tableLeftPadding);
        stage.addActor(statusTable);
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
        super.dispose();
        playerHealthImage.remove();
        enemyHealthImage.remove();
        xpImage.remove();
        playerHealthLabel.remove();
        enemyHealthLabel.remove();
        experienceLabel.remove();
    }
}
