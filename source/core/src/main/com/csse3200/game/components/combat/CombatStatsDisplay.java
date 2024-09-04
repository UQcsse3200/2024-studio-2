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
import com.badlogic.gdx.utils.Align;
import com.csse3200.game.components.CombatStatsComponent;
import com.csse3200.game.components.player.PlayerStatsDisplay;
import com.csse3200.game.services.ServiceLocator;
import com.csse3200.game.ui.UIComponent;

import static com.csse3200.game.components.player.PlayerStatsDisplayTester.testInitBarAnimation;

/**
 * Displays the name of the current game area.
 * Current implementation is temporary, to demonstrate that the stats of the specific entities are being passed in.
 */
public class CombatStatsDisplay extends UIComponent {
  private CombatStatsComponent playerStats;
  private CombatStatsComponent enemyStats;
  private Table statsTable;
  private Table playerTable;
  private Table enemyTable;
  private Image playerHealthImage;
  private Image enemyHealthImage;
  private  Image xpImage;
  private Label playerHealthLabel;
  private Label enemyHealthLabel;
  private Label experienceLabel;
  private static Animation<TextureRegion> playerHealthBarAnimation;
  private static Animation<TextureRegion> enemyHealthBarAnimation;
  private static Animation<TextureRegion> xpBarAnimation;
  private int playerMaxHealth;
  private int enemyMaxHealth;
  private int maxExperience;

  public CombatStatsDisplay(CombatStatsComponent playerStats, CombatStatsComponent enemyStats) {
    this.playerStats = playerStats;
    this.enemyStats = enemyStats;
    //playerMaxHealth = playerStats.getMaxHealth();
    //enemyMaxHealth = enemyStats.getMaxHealth();
    //maxExperience = playerStats.getMaxExperience();
  }

  @Override
  public void create() {
    super.create();
    addActors();
  }


  private boolean addActors() {
    // Combat Table
    Label title = new Label("Combat Stats", skin, "title");
    title.setFontScale(1.2f);
    Label playerHealthLabel = new Label("Player Health: " + playerStats.getHealth(), skin, "large");
    Label playerAttackLabel = new Label("Player Attack: " + playerStats.getStrength(), skin, "large");
    Label enemyHealthLabel = new Label("Enemy Health: " + enemyStats.getHealth(), skin, "large");
    Label enemyAttackLabel = new Label("Enemy Attack: " + enemyStats.getStrength(), skin, "large");
    statsTable = new Table();
    statsTable.setFillParent(true);
    statsTable.setDebug(true);
    float paddingTop = 28f;
    statsTable.add(title).center().padTop(paddingTop).row();
    statsTable.add(playerHealthLabel).padTop(paddingTop ).row();
    statsTable.add(playerAttackLabel).padTop(paddingTop).row();
    statsTable.add(enemyHealthLabel).padTop(paddingTop).row();
    statsTable.add(enemyAttackLabel).padTop(paddingTop);
    stage.addActor(statsTable);

    // Player Bars Display
    playerTable = new Table();
    playerTable.top().left();
    playerTable.setFillParent(true);
    playerTable.padTop(10f).padLeft(5f);

    // Enemy Bars Display
    enemyTable = new Table();
    enemyTable.right().top();
    enemyTable.setFillParent(true);
    enemyTable.padTop(50);

    // Health text
    int health = playerStats.getHealth();
    CharSequence healthText = String.format("HP: %d", health);
    playerHealthLabel = new Label(healthText, skin, "large");

    int eHealth = enemyStats.getHealth();
    CharSequence eHealthText = String.format("HP: %d", eHealth);
    enemyHealthLabel = new Label(eHealthText, skin, "large");

    // Experience text
    int experience = playerStats.getExperience();
    CharSequence experienceText = String.format("EXP: %d", experience);
    experienceLabel = new Label(experienceText, skin, "large");

    // Images
    playerHealthImage = new Image(ServiceLocator.getResourceService().getAsset("images/health_bar_x1.png", Texture.class));
    enemyHealthImage = new Image(ServiceLocator.getResourceService().getAsset("images/health_bar_x1.png", Texture.class));
    xpImage = new Image(ServiceLocator.getResourceService().getAsset("images/xp_bar.png", Texture.class));
    float barImageWidth = (float) (playerHealthImage.getWidth() * 0.7);
    float barImageHeight = (float) (playerHealthImage.getHeight() * 0.4);

    // Aligning the bars one below the other
    playerTable.add(playerHealthImage).size(barImageWidth, barImageHeight).pad(2);
    playerTable.add(playerHealthLabel).align(Align.left);
    playerTable.row().padTop(0);

    playerTable.add(xpImage).size(barImageWidth, (float) (barImageHeight * 1.15)).pad(2);
    playerTable.add(experienceLabel).align(Align.left);
    playerTable.row().padTop(30);

    enemyTable.add(enemyHealthImage).size(barImageWidth, barImageHeight).pad(2).padTop(30).padRight(10); // Padding between the bar and label
    enemyTable.add(enemyHealthLabel).align(Align.left).padLeft(10).padRight(30).padTop(30); // Add padding on both sides

    stage.addActor(playerTable);
    stage.addActor(enemyTable);

    return true;

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
    //title.remove();
    statsTable.remove();
  }
}
