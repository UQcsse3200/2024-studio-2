package com.csse3200.game.components.player;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.csse3200.game.components.CombatStatsComponent;
import com.csse3200.game.services.ServiceLocator;
import com.csse3200.game.ui.UIComponent;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;



/**
 * A UI component for displaying player stats, e.g. health.
 */

public class PlayerStatsDisplay extends UIComponent {
    Table table;
    private Image heartImage;
    private Image xpImage;
    private Image hungerImage;
    private Label healthLabel;
    private Label xpLabel;
    private Label hungerLabel;
    private Animation<TextureRegion> healthBarAnimation;
    private Animation<TextureRegion> hungerBarAnimation;
    private TextureAtlas[] textureAtlas;
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
     * Initialises all required variables for health/xp/hunger bars
     *
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
            String frameName = (100 - i * 10) + "%_health";
            healthBarFrames[i] = textureAtlas[0].findRegion(frameName);
        }
        healthBarAnimation = new Animation<>(0.066f, healthBarFrames);

        // HungerBar initialisation
        textureAtlas[1] = new TextureAtlas("images/hungerbar.atlas");
        TextureRegion[] hungerBarFrames = new TextureRegion[11];
        // Names each frame and locates associated frame in txt file
        for (int i = 0; i < hungerBarFrames.length; i++) {
        String frameName = (100 - i * 10) + "%_hunger";
        hungerBarFrames[i] = textureAtlas[1].findRegion(frameName);
        }
        hungerBarAnimation = new Animation<>(0.066f, hungerBarFrames);

        // xpBar initialisation
        //textureAtlas[2] = new TextureAtlas("spriteSheets/xpBars.txt");
        //TextureRegion[] xpBarFrames = new TextureRegion[11];
        // Names each frame and locates associated frame in txt file
        //for (int i = 0; i < xpBarFrames.length; i++) {
        //String frameName = (100 - i * 10) + "%_health";
        //xpBarFrames[i] = textureAtlas[0].findRegion(frameName);
        //}
        //xpBarAnimation = new Animation<>(0.066f, xpBarFrames);
    }

    /**
     * Creates actors and positions them on the stage using a table.
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


        initBarAnimations();

        // Health Dimensions
        heartImage = new Image(ServiceLocator.getResourceService().getAsset("images/health_bar_x1.png", Texture.class));
        xpImage = new Image(ServiceLocator.getResourceService().getAsset("images/xp_bar.png", Texture.class));
        hungerImage = new Image(ServiceLocator.getResourceService().getAsset("images/hunger_bar.png", Texture.class));

        // Get the original width and height of the image
        float barImageWidth = (float) (heartImage.getWidth() * 0.8);
        float barImageHeight = (float) (heartImage.getHeight() * 0.5);

        // Experience text maybe
        CharSequence xpText = String.format("EXP: %d", 100);
        xpLabel = new Label(xpText, skin, "large");

        // Hunger text maybe
        CharSequence hungerText = String.format("HGR: %d", 100);
        hungerLabel = new Label(hungerText, skin, "large");

        // Add all components to table
        table.add(heartImage).size(barImageWidth, barImageHeight).pad(5);
        table.add(healthLabel);
        table.row();
        table.add(xpImage).size(barImageWidth, (float) (barImageHeight * 1.25));
        table.add(xpLabel);
        table.row();
        table.add(hungerImage).size(barImageWidth, barImageHeight * 2);
        table.add(hungerLabel);
        stage.addActor(table);
    }

    @Override
    public void draw(SpriteBatch batch) {
        // handled by stage
    }

    /**
     * Updates the player's health on the ui.
     *
     * @param health player health
     */
    public void updatePlayerHealthUI(int health) {
        CharSequence text = String.format("HP: %d", health);
       logger.info("Made it to this updateHealth function");
        System.out.println(health);
        healthLabel.setText(text);
        int totalFrames = 11;

        // Debugged and Developed with ChatGPT
        // Calculate the frame index based on the current health
        int maxHealth = entity.getComponent(CombatStatsComponent.class).getMaxHealth();
        int frameIndex = totalFrames - 1 - (int) ((float) health / maxHealth * (totalFrames - 1));
        frameIndex = Math.max(0, Math.min(frameIndex, totalFrames - 1));

        // Set the current frame of the health bar animation
        TextureRegion currentFrame = healthBarAnimation.getKeyFrame(frameIndex * 0.066f);
        heartImage.setDrawable(new TextureRegionDrawable(currentFrame));  // Update the heartImage with the new frame
    }
    public void updatePlayerHungerUI(int hunger) {

        CharSequence text = String.format("HGR: %d", hunger);
      logger.info("Made it to this updateHealth function");
        System.out.println(hunger);
        hungerLabel.setText(text);
        int totalFrames = 11;

        // Debugged and Developed with ChatGPT
        // Calculate the frame index based on the current health
        int maxHunger = entity.getComponent(CombatStatsComponent.class).getMaxHunger();
        int frameIndex = totalFrames - 1 - (int) ((float) hunger / maxHunger * (totalFrames - 1));
        frameIndex = Math.max(0, Math.min(frameIndex, totalFrames - 1));

        // Set the current frame of the health bar animation
        TextureRegion currentFrame = hungerBarAnimation.getKeyFrame(frameIndex * 0.066f);
        hungerImage.setDrawable(new TextureRegionDrawable(currentFrame));  // Update the heartImage with the new frame
    }

    public void updatePlayerExperienceUI(int experience) {
        CharSequence text = String.format("EXP: %d", experience);
        xpLabel.setText(text);
    }



    @Override
    public void dispose() {
        super.dispose();
        heartImage.remove();
        healthLabel.remove();
        xpImage.remove();
        xpLabel.remove();
        hungerImage.remove();
        hungerLabel.remove();
        //for (int i = 0; i < textureAtlas.length; i++) {
        //    textureAtlas[i].dispose();
        //}
        textureAtlas[0].dispose();
    }
}

