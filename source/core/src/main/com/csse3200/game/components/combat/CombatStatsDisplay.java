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

/**
 * Displays the name of the current game area.
 * Current implementation is temporary, to demonstrate that the stats of the specific entities are being passed in.
 */
public class CombatStatsDisplay extends UIComponent {
  private CombatStatsComponent playerStats;
  private CombatStatsComponent enemyStats;
  private Table statsTable;
  private Image playerHealthImage;
  private Image enemyHealthImage;
  private  Image xpImage;
  private Label playerHealthLabel;
  private Label enemyHealthLabel;
  private Label experienceLabel;
  private TextureAtlas[] textureAtlas;
  private static Animation<TextureRegion> playerHealthBarAnimation;
  private static Animation<TextureRegion> enemyHealthBarAnimation;
  private static Animation<TextureRegion> xpBarAnimation;
  private float barImageWidth;
  private float barImageHeight;
  private static final int totalFrames = 11;
  private int playerMaxHealth;
  private int enemyMaxHealth;
  private int maxExperience;

  public CombatStatsDisplay(CombatStatsComponent playerStats, CombatStatsComponent enemyStats) {
    this.playerStats = playerStats;
    this.enemyStats = enemyStats;
    playerMaxHealth = playerStats.getMaxHealth();
    enemyMaxHealth = enemyStats.getMaxHealth();
    maxExperience = playerStats.getMaxExperience();
  }

  @Override
  public void render(float delta) {

  }

  @Override
  public void resize(int width, int height) {

  }

  @Override
  public void create() {
    super.create();
    addActors();
    entity.getEvents().addListener("updateHealth", this::updateHealthUI);
    entity.getEvents().addListener("updateExperience", this::updatePlayerExperienceUI);
  }

  /**
   * Initialises a table containing the images of the player's stats bars and their associated labels
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

    // Health/XP images
    playerHealthImage = new Image(ServiceLocator.getResourceService().getAsset("images/health_bar_x1.png",
            Texture.class));
    xpImage = new Image(ServiceLocator.getResourceService().getAsset("images/xp_bar.png", Texture.class));
    barImageWidth = (float) (playerHealthImage.getWidth() * barWidthScaling);
    barImageHeight = (float) (playerHealthImage.getHeight() * barHeightScaling);

    // Aligning the bars one below the other and adding them to table
    playerTable.add(playerHealthImage).size(barImageWidth, barImageHeight).pad(barLabelGap);
    playerTable.add(playerHealthLabel).align(Align.left);
    playerTable.row();
    playerTable.add(xpImage).size(barImageWidth, (float) (barImageHeight * xpHeightScaling)).pad(barLabelGap);
    playerTable.add(experienceLabel).align(Align.left);

    return playerTable;
  }

  /**
   * Initialises a table containing the enemy's stats bars and lables
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
    int numberOfAtlases = 2;

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
    xpBarAnimation = new Animation<>(0.066f, xpBarFrames);
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

  private void addActors() {
    // Combat Table
    Label title = new Label("Combat Stats", skin, "title");
    title.setFontScale(1.2f);
    Label playerHealthLabelTable = new Label("Player Health: " + playerStats.getHealth(), skin, "large");
    Label playerAttackLabel = new Label("Player Attack: " + playerStats.getStrength(), skin, "large");
    Label enemyHealthLabelTable = new Label("Enemy Health: " + enemyStats.getHealth(), skin, "large");
    Label enemyAttackLabel = new Label("Enemy Attack: " + enemyStats.getStrength(), skin, "large");
    statsTable = new Table();
    statsTable.setFillParent(true);
    statsTable.setDebug(true);
    float paddingTop = 28f;
    statsTable.add(title).center().padTop(paddingTop).row();
    statsTable.add(playerHealthLabelTable).padTop(paddingTop ).row();
    statsTable.add(playerAttackLabel).padTop(paddingTop).row();
    statsTable.add(enemyHealthLabelTable).padTop(paddingTop).row();
    statsTable.add(enemyAttackLabel).padTop(paddingTop);
    stage.addActor(statsTable);

    Table playerTable = initialisePlayerStatBars();
    Table enemyTable = initialiseEnemyStatBars();

    stage.addActor(playerTable);
    stage.addActor(enemyTable);

    initBarAnimations();

    //initialising the character stats
    updateHealthUI(playerStats.getHealth(), playerMaxHealth, true);
    updateHealthUI(enemyStats.getHealth(), enemyMaxHealth, false);
    updatePlayerExperienceUI(playerStats.getExperience());
  }

  /**
   * Updates the health animation and label in game to reflect current player/enemy health
   * including the call to test functions for checking
   * @param health the current health stat value of the player
   */
  public void updateHealthUI(int health, int maxHealth, boolean isPlayer) {
    CharSequence text = String.format("HP: %d", health);

    int frameIndex = totalFrames - 1 - (int) ((float) health / maxHealth * (totalFrames - 1));
    frameIndex = Math.max(0, Math.min(frameIndex, totalFrames - 1));

    if (isPlayer) {
      playerHealthLabel.setText(text);
      setNewFrame(frameIndex, playerHealthBarAnimation, playerHealthImage);
    } else {
      enemyHealthLabel.setText(text);
      setNewFrame(frameIndex, enemyHealthBarAnimation, enemyHealthImage);
    }
  }

  /**
   * Updates the experience animation and label in game to reflect current player experience
   *  including the call to test functions for checking
   * @param experience The current experience stat value of the player
   */
  public void updatePlayerExperienceUI(int experience) {
    CharSequence text = String.format("EXP: %d", experience);
    experienceLabel.setText(text);

    int frameIndex = totalFrames - 1 - (int) ((float) experience / maxExperience * (totalFrames - 1));
    frameIndex = Math.max(0, Math.min(frameIndex, totalFrames - 1));
    // Set the current frame of the health bar animation
    setNewFrame(frameIndex, xpBarAnimation, xpImage);
  }

  @Override
  public void draw(SpriteBatch batch)  {
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
    //title.remove();
    statsTable.remove();
  }
}
