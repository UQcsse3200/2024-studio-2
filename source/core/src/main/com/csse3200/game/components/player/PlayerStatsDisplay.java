package com.csse3200.game.components.player;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Align;
import com.csse3200.game.components.CombatStatsComponent;
import com.csse3200.game.services.ServiceLocator;
import com.csse3200.game.ui.UIComponent;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Text;


/**
 * A UI component for displaying player stats, e.g. health.
 */

public class PlayerStatsDisplay extends UIComponent {
    Table table;
    private Image healthImage;
    private Image xpImage;
    private Image hungerImage;
    private Label healthLabel;
    private Label experienceLabel;
    private Label hungerLabel;
    private Animation<TextureRegion> healthBarAnimation;
    private Animation<TextureRegion> hungerBarAnimation;
    private Animation<TextureRegion> xpBarAnimation;
    private TextureAtlas[] textureAtlas;
    private static int totalFrames = 11;
    private static final Logger logger = LoggerFactory.getLogger(PlayerStatsDisplay.class);


    /**
     * Creates reusable ui styles and adds actors to the stage.
     */
    @Override
    public void create() {
        super.create();
        addActors();
        entity.getEvents().addListener("updateHealth", this::updatePlayerHealthUI);
        entity.getEvents().addListener("updateExperience", this::updatePlayerExperienceUI);
        entity.getEvents().addListener("updateHunger", this::updatePlayerHungerUI);
    }

    /**
     * Initialises all required variables for health/xp/hunger bar animations
     * @see Animation for animation details
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
            String frameName3 = (i * 10) + "%_xp";
            xpBarFrames[i] = textureAtlas[2].findRegion(frameName3);
        }
        xpBarAnimation = new Animation<>(0.066f, xpBarFrames);
    }

    /**
     * Creates animations and labels, and adds them on the stage using a table.
     *
     * @see Table for positioning options
     */
    private void addActors() {
        table = new Table();
        table.top().left();
        table.setFillParent(true);
        table.padTop(45f).padLeft(5f);

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
        float barImageWidth = (float) (healthImage.getWidth() * 0.8);
        float barImageHeight = (float) (healthImage.getHeight() * 0.5);

        // Aligning the bars one below the other
        table.add(healthImage).size(barImageWidth, barImageHeight).pad(2).padLeft(170);
        table.add(healthLabel).align(Align.left);
        table.row().padTop(10);

        table.add(xpImage).size(barImageWidth, (float) (barImageHeight * 1.25)).pad(2).padLeft(170);
        table.add(experienceLabel).align(Align.left);
        table.row().padTop(10);

        table.add(hungerImage).size(barImageWidth, barImageHeight * 2).pad(2).padLeft(170).padTop(-15);
        table.add(hungerLabel).align(Align.left).padTop(-15);

        // Add the table to the stage
        stage.addActor(table);
        //initialising the character stats
        updatePlayerHealthUI(health);
    }


    @Override
    public void draw(SpriteBatch batch) {
        // handled by stage
    }

    /**
     * Replaces the current frame for the specified statBar based on the current frame provided
     * @param frameIndex The index of the desired frame in the Texture Region
     * @param statBarAnimation The animation for the specific stat bar
     * @param statBar The image that is the placeholder on the stage for the stat bar
     */
    public void setNewFrame(int frameIndex, Animation<TextureRegion> statBarAnimation, Image statBar) {
        // Grab the desired frame at a specified frame rate
        TextureRegion currentFrame = statBarAnimation.getKeyFrame(frameIndex * 0.066f);
        // Replace the frame shown on the stage
        statBar.setDrawable(new TextureRegionDrawable(currentFrame));
    }

    /**
     * Updates the health animation and label in game to reflect current player health
     * @param health the current health stat value of the player
     */
    public void updatePlayerHealthUI(int health) {
        CharSequence text = String.format("HP: %d", health);
        logger.info("Made it to this updateHealth function");
        logger.info("{}", health);
        healthLabel.setText(text);

        // Debugged and Developed with ChatGPT
        // Calculate the frame index based on the current health
        int maxHealth = entity.getComponent(CombatStatsComponent.class).getMaxHealth();
        int frameIndex = totalFrames - 1 - (int) ((float) health / maxHealth * (totalFrames - 1));
        frameIndex = Math.max(0, Math.min(frameIndex, totalFrames - 1));

        // Set the current frame of the health bar animation
        setNewFrame(frameIndex, healthBarAnimation, healthImage);
//        TextureRegion currentFrame = healthBarAnimation.getKeyFrame(frameIndex * 0.066f);
//        heartImage.setDrawable(new TextureRegionDrawable(currentFrame));  // Update the heartImage with the new frame
    }

    /**
     * Updates the hunger animation and label in game to reflect current player hunger
     * @param hunger The current hunger stat value of the player
     */
    public void updatePlayerHungerUI(int hunger) {
        CharSequence text = String.format("HGR: %d", hunger);
        logger.info("Made it to this updateHunger function");
        logger.info("{}", hunger);
        hungerLabel.setText(text);

        // Debugged and Developed with ChatGPT
        // Calculate the frame index based on the current health
        int maxHunger = entity.getComponent(CombatStatsComponent.class).getMaxHunger();
        int frameIndex = totalFrames - 1 - (int) ((float) hunger / maxHunger * (totalFrames - 1));
        frameIndex = Math.max(0, Math.min(frameIndex, totalFrames - 1));

        // Set the current frame of the health bar animation
        setNewFrame(frameIndex, hungerBarAnimation, hungerImage);
    }

    /**
     * Updates the experience animation and label in game to reflect current player experience
     * @param experience The current experience stat value of the player
     */
    public void updatePlayerExperienceUI(int experience) {
        CharSequence text = String.format("EXP: %d", experience);
        experienceLabel.setText(text);
        logger.info("Made it to this updatePlayerExperienceUI function");
        logger.info("{}", experience);
        // Debugged and Developed with ChatGPT
        // Calculate the frame index based on the current health as no xp implementation yet

        int maxExperience = entity.getComponent(CombatStatsComponent.class).getMaxExperience();
        int frameIndex = totalFrames - 1 - (int) ((float) experience / maxExperience * (totalFrames - 1));
        frameIndex = Math.max(0, Math.min(frameIndex, totalFrames - 1));

        // Set the current frame of the health bar animation
        setNewFrame(frameIndex, xpBarAnimation, xpImage);
    }


    @Override
    public void dispose() {
        super.dispose();
        heartImage.remove();
        healthLabel.remove();
        xpImage.remove();
        experienceLabel.remove();
        hungerImage.remove();
        hungerLabel.remove();
        for (int i = 0; i < textureAtlas.length; i++) {
            textureAtlas[i].dispose();
        }
    }
}

