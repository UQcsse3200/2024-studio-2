package com.csse3200.game.components.combat;

import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.csse3200.game.services.ServiceContainer;
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
  private final Screen screen;
  private final ServiceContainer container;

  public CombatExitDisplay(Screen screen, ServiceContainer container) {
    this.screen = screen;
    this.container = container;
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
          entity.getEvents().trigger("KangaDefeated", "add", 1);
          entity.getEvents().trigger("combatWin", screen, container);

        }
      });

    // Triggers an event when the button is pressed.
    lose.addListener(
      new ChangeListener() {
        @Override
        public void changed(ChangeEvent changeEvent, Actor actor) {
          entity.getEvents().trigger("combatLose", screen, container);
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
