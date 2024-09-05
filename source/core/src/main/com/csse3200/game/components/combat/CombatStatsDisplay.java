package com.csse3200.game.components.combat;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.csse3200.game.components.CombatStatsComponent;
import com.csse3200.game.ui.UIComponent;

/**
 * Displays the name of the current game area.
 * Current implementation is temporary, to demonstrate that the stats of the specific entities are being passed in.
 */
public class CombatStatsDisplay extends UIComponent {
  private CombatStatsComponent playerStats;
  private CombatStatsComponent enemyStats;
  private Table statsTable;

  public CombatStatsDisplay(CombatStatsComponent playerStats, CombatStatsComponent enemyStats) {
    this.playerStats = playerStats;
    this.enemyStats = enemyStats;
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
  }

  private void addActors() {
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
