package com.csse3200.game.components.gamearea;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.Align;
import com.csse3200.game.ui.UIComponent;

/**
 * Displays the name of the current game area.
 */
public class GameAreaDisplay extends UIComponent {
  private String gameAreaName = "";
  private Label title;
  private Texture playerIconTexture;
  private Image playerIcon;
  private static Integer iconScale = 3;

  public GameAreaDisplay(String gameAreaName) {
    this.gameAreaName = gameAreaName;
  }

  @Override
  public void create() {
    super.create();
    addActors();
  }

    private void addActors() {
      title = new Label(this.gameAreaName, skin, "large");
      if (gameAreaName == "Sky Kingdom") {
        playerIconTexture = new Texture(Gdx.files.internal("images/player_icon_sky.png"));
      } else if (gameAreaName == "Sea Kingdom") {
        playerIconTexture = new Texture(Gdx.files.internal("images/player_icon_sea.png"));
      } else {
        playerIconTexture = new Texture(Gdx.files.internal("images/player_icon_forest.png"));
      }
      playerIcon = new Image(playerIconTexture);
      // Set the size of the icon to match the label's height
      float titleHeight = title.getHeight();
      //ayerIcon.setSize(titleHeight * iconScale, titleHeight * iconScale);
      //ayerIcon.setPosition(10, Gdx.graphics.getHeight() - 10);
      //tle.setPosition(20 + (titleHeight * iconScale), Gdx.graphics.getHeight() - 10);
      // Creating table to hold player Icon and area name together (Debugged with ChatGPT)
      Table table = new Table();
      table.top().left();

      table.add(playerIcon).size(titleHeight, titleHeight).align(Align.left | Align.top);; // Set size and add padding between icon and label
      table.add(title).align(Align.left | Align.top);;

      stage.addActor(playerIcon);
      stage.addActor(title);
      table.padTop(45f).padLeft(5f);
      table.row();

    // Add the table to the stage
      stage.addActor(table);
  }

  @Override
  public void draw(SpriteBatch batch)  {
    int screenHeight = Gdx.graphics.getHeight();
    float offsetX = 10f;
    float offsetY = 30f;

    title.setPosition(offsetX, screenHeight - offsetY);
  }

  @Override
  public void dispose() {
    super.dispose();
    title.remove();
  }
}
