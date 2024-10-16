package com.csse3200.game.components.combat;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.csse3200.game.components.CombatStatsComponent;
import com.csse3200.game.entities.Entity;
import com.csse3200.game.ui.CustomButton;
import com.csse3200.game.ui.UIComponent;

/**
 * Displays two buttons during combat to exit the Main Game screen and return to the Main Menu screen.
 * One button allows for an "instant kill" win, and the other triggers a loss, simulating the player losing the combat.
 */
public class CombatExitDisplay extends UIComponent {
  private static final float Z_INDEX = 2f;
  private Table table;
  private final Entity enemy;

  public CombatExitDisplay(Entity enemy) {
    this.enemy = enemy;
  }

  /**
   * Initializes the UI component by calling the parent class' create method and adding actors (buttons).
   */
  @Override
  public void create() {
    super.create();
    addActors();
  }

  /**
   * Adds the buttons for "Insta-kill enemy" and "Exiting - lose" to the UI.
   * Sets the positions of the buttons and defines the actions they trigger.
   */
  private void addActors() {
    table = new Table(); // Create a new table for the layout
    table.top().right(); // Align the table to the top-right corner of the screen
    table.setFillParent(true); // Ensure the table fills the parent container

    // Create a button for instantly defeating the enemy
    CustomButton win = new CustomButton("Insta-kill enemy", skin);
    win.setButtonStyle(CustomButton.Style.BROWN_WIDE, skin);

    // Create a button for triggering a combat loss
    CustomButton lose = new CustomButton("Exiting - lose", skin);
    lose.setButtonStyle(CustomButton.Style.BROWN_WIDE, skin);

    // Listener for the "win" button, triggers the combatWin event when clicked
    win.addClickListener(() -> {
      if (enemy.getComponent(CombatStatsComponent.class).isBoss()) {
        entity.getEvents().trigger("bossCombatWin", enemy);
      } else {
        entity.getEvents().trigger("combatWin", enemy);
      }
    });

    // Listener for the "lose" button, triggers the combatLoss event when clicked
    lose.addClickListener(() -> {
      if (enemy.getComponent(CombatStatsComponent.class).isBoss()) {
        entity.getEvents().trigger("bossCombatLoss", enemy);
      } else {
        entity.getEvents().trigger("combatLoss", enemy);
      }
    });

    // Add the buttons to the table with padding for layout spacing
    table.add(win).size(300, 50).padTop(10f).padRight(10f);
    table.add(lose).size(300, 50).padTop(10f).padRight(10f);

    // Add the table (with the buttons) to the stage
    stage.addActor(table);
  }

  /**
   * The drawing is handled by the Stage, so no additional drawing logic is needed here.
   *
   * @param batch the SpriteBatch used for drawing
   */
  @Override
  public void draw(SpriteBatch batch) {
    // Drawing is handled by the stage, no direct drawing here
  }

  /**
   * Gets the Z-index, which determines the rendering order of this UI component.
   * A higher Z-index indicates that this component will be rendered on top of others.
   *
   * @return the Z-index of this UI component
   */
  @Override
  public float getZIndex() {
    return Z_INDEX;
  }

  /**
   * Cleans up the UI component by clearing the table and disposing of resources.
   */
  @Override
  public void dispose() {
    table.clear(); // Clear the table to release resources
    super.dispose(); // Call parent dispose method
  }
}
