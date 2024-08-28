package com.csse3200.game.components.player;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
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
 * A UI component for displaying player stats, e.g. health.
 */

public class PlayerStatsDisplay extends UIComponent {

    Table table;
    public  Image healthImage;
    public  Image xpImage;
    public  Image hungerImage;
    public Label healthLabel;
    Label experienceLabel;
    public Label hungerLabel;
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
     * Creates reusable ui styles and adds actors to the stage.
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
        testInitBarAnimation(textureAtlas, healthBarAnimation, hungerBarAnimation, xpBarAnimation);

    }

    /**
     * Creates animations and labels, and adds them on the stage using a table.
     *
     * @see Table for positioning options
     */
    private boolean addActors() {
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
        float barImageWidth = (float) (healthImage.getWidth() * 0.6);
        float barImageHeight = (float) (healthImage.getHeight() * 0.3);

        // Aligning the bars one below the other
        table.add(healthImage).size(barImageWidth, barImageHeight).pad(2).padLeft(170);
        table.add(healthLabel).align(Align.left);
        table.row().padTop(10);

        table.add(xpImage).size(barImageWidth, (float) (barImageHeight * 1.25)).pad(2).padLeft(170);
        table.add(experienceLabel).align(Align.left).padTop(-5);
        table.row().padTop(30);

        table.add(hungerImage).size(barImageWidth, barImageHeight * 2).pad(2).padLeft(170).padTop(-15);
        table.add(hungerLabel).align(Align.left).padTop(-20);


        stage.addActor(table);
        //initialising the character stats
        updatePlayerHealthUI(health);
        updatePlayerHungerUI(hunger);
        updatePlayerExperienceUI(experience);
        testFinalImplementation();
        // Add the table to the stage
        return true;
    }

    public void testFinalImplementation() {


        Timer.schedule(new Timer.Task() {
            @Override
            public void run() {
                // Schedule frame updates with incrementally increasing delays
                for (int i = maxHealth; i >= 0; i -= 10) {
                    int finalI = i;
                    Timer.schedule(new Timer.Task() {
                        @Override
                        public void run() {
                            // Update the animation frames
                            updatePlayerHealthUI(finalI);

                        }
                    }, (100 - finalI) * 0.1f);

                    for (int j = maxExperience; j >= 0; j -= 10) {
                        int finalJ = j;
                        Timer.schedule(new Timer.Task() {
                            @Override
                            public void run() {
                                // Update the animation frames

                                updatePlayerExperienceUI(finalJ);

                            }
                        }, (100 - finalJ) * 0.1f);}

                        for (int k = maxHunger; k >= 0; k -= 10) {
                            int finalK = k;
                            Timer.schedule(new Timer.Task() {
                                @Override
                                public void run() {
                                    // Update the animation frames

                                    updatePlayerHungerUI(finalK);
                                }
                            }, (100 - finalK) * 0.1f);}
                            // Incremental delay in seconds
                }
                delayedActionDone = true;
            }
        }, 1); // Initial delay of 1 second
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

        int frameIndex = totalFrames - 1 - (int) ((float) health / maxHealth * (totalFrames - 1));
        String statName="health";
        frameIndex = Math.max(0, Math.min(frameIndex, totalFrames - 1));
        testUpdatePlayerStatsUI( maxHealth, health,statName );
        // Set the current frame of the health bar animation
        setNewFrame(frameIndex, healthBarAnimation, healthImage);
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

        int frameIndex = totalFrames - 1 - (int) ((float) hunger / maxHunger * (totalFrames - 1));
        frameIndex = Math.max(0, Math.min(frameIndex, totalFrames - 1));
        String statName="hunger";
        testUpdatePlayerStatsUI( maxHunger, hunger,statName );
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


        int frameIndex = totalFrames - 1 - (int) ((float) experience / maxExperience * (totalFrames - 1));
        frameIndex = Math.max(0, Math.min(frameIndex, totalFrames - 1));
        String statName="experience";
        testUpdatePlayerStatsUI( maxExperience, experience,statName );
        // Set the current frame of the health bar animation
        setNewFrame(frameIndex, xpBarAnimation, xpImage);

    }


    @Override
    public void dispose() {
        super.dispose();
        healthImage.remove();
        healthLabel.remove();
        xpImage.remove();
        experienceLabel.remove();
        hungerImage.remove();
        hungerLabel.remove();
        for (TextureAtlas atlas : textureAtlas) {
            atlas.dispose();
        }
    }
}

