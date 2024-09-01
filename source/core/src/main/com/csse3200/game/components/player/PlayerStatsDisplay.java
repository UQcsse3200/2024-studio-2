package com.csse3200.game.components.player;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.Timer;
import com.csse3200.game.components.CombatStatsComponent;
import com.csse3200.game.services.ServiceLocator;
import com.csse3200.game.ui.UIComponent;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


import static com.csse3200.game.components.player.PlayerStatsDisplayTester.*;

/**
 * A UI component for displaying player stats, such as health, hunger, and experience.
 * This component handles the creation, updating, and disposing
 * the player's stats on the screen.
 */

public class PlayerStatsDisplay extends UIComponent {

    Table table;
    public  Image healthImage;
    public  Image xpImage;
    public  Image hungerImage;
    public Label healthLabel;
    Label experienceLabel;
    public Label hungerLabel;
    private Image vignetteImage;
    static Animation<TextureRegion> healthBarAnimation;
    static Animation<TextureRegion> hungerBarAnimation;
    static Animation<TextureRegion> xpBarAnimation;
    public TextureAtlas[] textureAtlas;
    public  int totalFrames = 11;
    private  final Logger logger = LoggerFactory.getLogger(PlayerStatsDisplay.class);
    public boolean addActorsTester=false;
    public int maxHealth;
    public int maxHunger;
    public int maxExperience;


    /**
     * Initializes and adds actors (UI elements) to the stage.
     * Registers listeners for updating health, experience, and hunger.
     * Makes call to the test method testCreate
     */
    @Override
    public void create() {
        super.create();
       addActorsTester= addActors();
        stage = ServiceLocator.getRenderService().getStage();

        entity.getEvents().addListener("updateHealth", this::updatePlayerHealthUI);
        entity.getEvents().addListener("updateExperience", this::updatePlayerExperienceUI);
        entity.getEvents().addListener("updateHunger", this::updatePlayerHungerUI);

        testCreate(addActorsTester,entity);
        maxExperience = entity.getComponent(CombatStatsComponent.class).getMaxExperience();
        maxHealth= entity.getComponent(CombatStatsComponent.class).getMaxHealth();
        maxHunger=entity.getComponent(CombatStatsComponent.class).getMaxHunger();

        entity.getEvents().addListener("startHealthBarBeating", this::startHealthBarBeating);
        entity.getEvents().addListener("stopHealthBarBeating", this::stopHealthBarBeating);
    }

    /**
     * Initializes the animations for the health, hunger, and experience bars.
     * Each bar's animation consist of series of consecutive frames from a texture atlas given in the assets folder.
     * The animations reflect the current status of the player's health, hunger, and experience.
     */
    public void initBarAnimations() {
        // Initialise textureAtlas for 3 bars
        textureAtlas = new TextureAtlas[3];
        // HealthBar initialisation
        textureAtlas[0] = new TextureAtlas("spriteSheets/healthBars.txt");
        TextureRegion[] healthBarFrames = new TextureRegion[11];
        // Names each frame and locates associated frame in txt file
        for (int i = 0; i < healthBarFrames.length; i++) {
            String frameName1 = (100 - i * 10) + "%_health";
            healthBarFrames[i] = textureAtlas[0].findRegion(frameName1);
        }
        healthBarAnimation = new Animation<>(0.066f, healthBarFrames);

        // HungerBar initialisation
        textureAtlas[1] = new TextureAtlas("images/hungerbar.atlas");
        TextureRegion[] hungerBarFrames = new TextureRegion[11];
        // Names each frame and locates associated frame in txt file
        for (int i = 0; i < hungerBarFrames.length; i++) {
            String frameName2 = (100 - i * 10) + "%_hunger";
            hungerBarFrames[i] = textureAtlas[1].findRegion(frameName2);
        }
        hungerBarAnimation = new Animation<>(0.066f, hungerBarFrames);

        // xpBar initialisation
        textureAtlas[2] = new TextureAtlas("spriteSheets/xpBars.atlas");
        TextureRegion[] xpBarFrames = new TextureRegion[11];
        // Names each frame and locates associated frame in txt file
        for (int i = 0; i < xpBarFrames.length; i++) {
            String frameName3 = (100 - (i * 10)) + "%_xp";
            xpBarFrames[i] = textureAtlas[2].findRegion(frameName3);
        }
        xpBarAnimation = new Animation<>(0.066f, xpBarFrames);
        testInitBarAnimation(textureAtlas, healthBarAnimation, hungerBarAnimation, xpBarAnimation);

    }

    /**
     * Creates the UI elements the bars and labels for each stat
     * and adds them to the stage using a table for position management
     * @return true if actors were successfully added to the stage, false otherwise for testing in PlayerStatsDisplayTester
     * @see Table for positioning options.
     */
    private boolean addActors() {
        table = new Table();
        table.top().left();
        table.setFillParent(true);
        table.padTop(10f).padLeft(5f);

        // Health text
        int health = entity.getComponent(CombatStatsComponent.class).getHealth();
        CharSequence healthText = String.format("HP: %d", health);
        healthLabel = new Label(healthText, skin, "large");
        // Hunger text
        int hunger = entity.getComponent(CombatStatsComponent.class).getHunger();
        CharSequence hungerText = String.format("HGR: %d", hunger);
        hungerLabel = new Label(hungerText, skin, "large");
        // Experience text
        int experience = entity.getComponent(CombatStatsComponent.class).getExperience();
        CharSequence experienceText = String.format("EXP: %d", experience);
        experienceLabel = new Label(experienceText, skin, "large");

        initBarAnimations();

        // Health Dimensions
        healthImage = new Image(ServiceLocator.getResourceService().getAsset("images/health_bar_x1.png", Texture.class));
        xpImage = new Image(ServiceLocator.getResourceService().getAsset("images/xp_bar.png", Texture.class));
        hungerImage = new Image(ServiceLocator.getResourceService().getAsset("images/hunger_bar.png", Texture.class));

        // Get the original width and height of the image
        float barImageWidth = (float) (healthImage.getWidth() * 0.7);
        float barImageHeight = (float) (healthImage.getHeight() * 0.4);

        // Vignette image setup
        vignetteImage = new Image(ServiceLocator.getResourceService().getAsset("images/vignette.png", Texture.class));
        vignetteImage.setFillParent(true); // Cover the entire screen
        vignetteImage.setVisible(false); // Initially invisible

        // Aligning the bars one below the other
        table.add(healthImage).size(barImageWidth, barImageHeight).pad(2).padLeft(170);
        table.add(healthLabel).align(Align.left);
        table.row().padTop(0);

        table.add(xpImage).size(barImageWidth, (float) (barImageHeight * 1.15)).pad(2).padLeft(170);
        table.add(experienceLabel).align(Align.left);
        table.row().padTop(30);

        table.add(hungerImage).size(barImageWidth, barImageHeight*2).pad(2).padLeft(170).padTop(-15);
        table.add(hungerLabel).align(Align.left).padTop(-15);


        stage.addActor(table);
        stage.addActor(vignetteImage);

        //initialising the character stats
        updatePlayerHealthUI(health);
        updatePlayerHungerUI(hunger);
        updatePlayerExperienceUI(experience);
        startHungerDecreaseTimer();
        // Add the table to the stage
        return true;
    }
    public void startHungerDecreaseTimer() {
        Timer.schedule(new Timer.Task() {
            @Override
            public void run() {
                // Call addHunger on the instance of CombatStatsComponent
                CombatStatsComponent combatStats = entity.getComponent(CombatStatsComponent.class);
                if (combatStats != null) {
                    combatStats.addHunger(-1); // Correctly call instance method
                }
            }
        }, 5, 5); // Initial delay of 15 seconds, then repeat every 15 seconds
    }


    @Override
    public void draw(SpriteBatch batch) {
        // handled by stage
    }

    /**
     * Sets a new frame for a stat bar animation on the stage.
     *
     * @param frameIndex The index of the desired frame in the animation to be changed
     * @param statBarAnimation The animation for the specific stat bar to be changed
     * @param statBar image that stores the current frame on the stage for the stat bar.
     */
    public void setNewFrame(int frameIndex, Animation<TextureRegion> statBarAnimation, Image statBar) {
        // Grab the desired frame at a specified frame rate
        TextureRegion currentFrame = statBarAnimation.getKeyFrame(frameIndex * 0.066f);
        // Replace the frame shown on the stage
        statBar.setDrawable(new TextureRegionDrawable(currentFrame));
    }

    /**
     * Updates the health animation and label in game to reflect current player health
     * including the call to test functions for checking
     * @param health the current health stat value of the player
     */
    public void updatePlayerHealthUI(int health) {
        CharSequence text = String.format("HP: %d", health);
        logger.info("Made it to this updateHealth function");
        logger.info("{}", health);
        healthLabel.setText(text);
        int totalFrames = 11;

        // Debugged and Developed with ChatGPT
        // Calculate the frame index based on the current health

        int frameIndex = totalFrames - 1 - (int) ((float) health / maxHealth * (totalFrames - 1));
        String statName="health";
        frameIndex = Math.max(0, Math.min(frameIndex, totalFrames - 1));
        testUpdatePlayerStatsUI( maxHealth, health,statName );
        // Set the current frame of the health bar animation
        setNewFrame(frameIndex, healthBarAnimation, healthImage);
    }

    /**
     * Updates the hunger animation and label in game to reflect current player hunger
     *  including the call to test functions for checking
     * @param hunger The current hunger stat value of the player
     */
    public void updatePlayerHungerUI(int hunger) {
        CharSequence text = String.format("HGR: %d", hunger);
        logger.info("Made it to this updateHunger function");
        logger.info("{}", hunger);
        hungerLabel.setText(text);

        // Debugged and Developed with ChatGPT
        // Calculate the frame index based on the current health

        int frameIndex = totalFrames - 1 - (int) ((float) hunger / maxHunger * (totalFrames - 1));
        frameIndex = Math.max(0, Math.min(frameIndex, totalFrames - 1));
        String statName="hunger";
        testUpdatePlayerStatsUI( maxHunger, hunger,statName );
        // Set the current frame of the health bar animation
        setNewFrame(frameIndex, hungerBarAnimation, hungerImage);

    }

    /**
     * Updates the experience animation and label in game to reflect current player experience
     *  including the call to test functions for checking
     * @param experience The current experience stat value of the player
     */
    public void updatePlayerExperienceUI(int experience) {
        CharSequence text = String.format("EXP: %d", experience);
        experienceLabel.setText(text);
        logger.info("Made it to this updatePlayerExperienceUI function");
        logger.info("{}", experience);
        // Debugged and Developed with ChatGPT
        // Calculate the frame index based on the current health as no xp implementation yet


        int frameIndex = totalFrames - 1 - (int) ((float) experience / maxExperience * (totalFrames - 1));
        frameIndex = Math.max(0, Math.min(frameIndex, totalFrames - 1));
        String statName="experience";
        testUpdatePlayerStatsUI( maxExperience, experience,statName );
        // Set the current frame of the health bar animation
        setNewFrame(frameIndex, xpBarAnimation, xpImage);

    }

    /**
     * Starts the beating animation for the health bar during boss chase.
     */
    public void startHealthBarBeating() {
        // Stop any existing beating actions
        healthImage.clearActions();
        vignetteImage.clearActions();

        vignetteImage.setVisible(true);

        healthImage.addAction(Actions.forever(
                Actions.sequence(
                        Actions.scaleTo(1.0f, 1.05f, 0.3f), // Slightly enlarge
                        Actions.scaleTo(1.0f, 0.95f, 0.3f)  // Return to normal size
                )
        ));

        vignetteImage.addAction(Actions.forever(
                Actions.sequence(
                        Actions.fadeIn(0.3f), // Fade in for vignette effect
                        Actions.fadeOut(0.3f)  // Fade out for vignette effect
                )
        ));
    }

    public void stopHealthBarBeating() {
        healthImage.clearActions();
        vignetteImage.clearActions();
        vignetteImage.setVisible(false); // Hide vignette when not beating
        healthImage.setScale(1.0f); // Reset to normal scale
    }

     /**
     * Disposes of the resources used by the PlayerStatsDisplay component, including textures and labels.
     */
    @Override
    public void dispose() {
        super.dispose();
        healthImage.remove();
        healthLabel.remove();
        xpImage.remove();
        experienceLabel.remove();
        hungerImage.remove();
        hungerLabel.remove();
        vignetteImage.remove();
        for (TextureAtlas atlas : textureAtlas) {
            atlas.dispose();
        }
    }
}

