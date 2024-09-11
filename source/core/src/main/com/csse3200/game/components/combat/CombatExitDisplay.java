package com.csse3200.game.components.combat;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.csse3200.game.components.CombatStatsComponent;
import com.csse3200.game.entities.Entity;
import com.csse3200.game.screens.CombatScreen;
import com.csse3200.game.ui.UIComponent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Displays a button to exit the Main Game screen to the Main Menu screen.
 */
public class CombatExitDisplay extends UIComponent {
  private static final Logger logger = LoggerFactory.getLogger(CombatExitDisplay.class);
  private static final float Z_INDEX = 2f;
  private Table table;
  private Entity enemy;

  public CombatExitDisplay(Entity enemy) {
    this.enemy = enemy;
  }

    @Override
  public void create() {
    super.create();
    addActors();
  }

  private void addActors() {
    table = new Table();
    table.top().right();
    table.setFillParent(true);

    TextButton win = new TextButton("Insta-kill enemy", skin);
    TextButton lose = new TextButton("Exiting - lose", skin);

    win.addListener(
      new ChangeListener() {
        @Override
        public void changed(ChangeEvent changeEvent, Actor actor) {
          logger.info("enemy is: {}", enemy.getComponent(CombatStatsComponent.class).isBoss());
          if (enemy.getComponent(CombatStatsComponent.class).isBoss()) {
            entity.getEvents().trigger("kangaDefeated");
          } else {
            entity.getEvents().trigger("combatWin");
          }
        }
      });

    // Triggers an event when the button is pressed.
    lose.addListener(
      new ChangeListener() {
        @Override
        public void changed(ChangeEvent changeEvent, Actor actor) {
          entity.getEvents().trigger("combatLoss");
        }
      });

    table.add(win).padTop(10f).padRight(10f);
    table.add(lose).padTop(10f).padRight(10f);
    stage.addActor(table);
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
