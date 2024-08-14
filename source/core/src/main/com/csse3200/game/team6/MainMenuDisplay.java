package com.csse3200.game.team6;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.csse3200.game.services.ServiceLocator;
import com.csse3200.game.ui.UIComponent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * A ui component for displaying the Main menu.
 */
public class MainMenuDisplay extends UIComponent {
  private static final Logger logger = LoggerFactory.getLogger(MainMenuDisplay.class);
  private static final float Z_INDEX = 2f;
  private Table table;
  private SettingsMenuDisplay settingsMenuDisplay;

  @Override
  public void create() {
    super.create();
    addActors();
  }

  private void addActors() {
    table = new Table();
      table.setFillParent(true);
      Image title =
              new Image(
                      ServiceLocator.getResourceService()
                              .getAsset("images/box_boy_title.png", Texture.class));

      TextButton startBtn = new TextButton("Start", skin);
      TextButton loadBtn = new TextButton("Load", skin);
      TextButton settingsBtn = new TextButton("Settings", skin);
      TextButton exitBtn = new TextButton("Exit", skin);

      Table settingMenu = new Table();

      // Triggers an event when the button is pressed
      startBtn.addListener(
              new ChangeListener() {
                  @Override
                  public void changed(ChangeEvent changeEvent, Actor actor) {
                      logger.debug("Start button clicked");
                      entity.getEvents().trigger("start");
                  }
              });

      loadBtn.addListener(
              new ChangeListener() {
                  @Override
                  public void changed(ChangeEvent changeEvent, Actor actor) {
                      logger.debug("Load button clicked");
                      entity.getEvents().trigger("load");
                  }
              });


      settingsBtn.addListener(
              new ChangeListener() {
                  @Override
                  public void changed(ChangeEvent changeEvent, Actor actor) {
                      // Toggle the visibility of the small menu
                      settingMenu.setVisible(!settingMenu.isVisible());
                  }
              });

      exitBtn.addListener(
              new ChangeListener() {
                  @Override
                  public void changed(ChangeEvent changeEvent, Actor actor) {

                      logger.debug("Exit button clicked");
                      entity.getEvents().trigger("exit");
                  }
              });

      table.add(title);
      table.row();
      table.add(startBtn).padTop(30f);
      table.row();
      table.add(loadBtn).padTop(15f);
      table.row();
      table.add(settingsBtn).padTop(15f);
      table.row();
      table.add(exitBtn).padTop(15f);

      stage.addActor(table);

      makeSettingMenu(settingMenu);
  }
  private void makeSettingMenu(Table settingMenu) {

      Pixmap pixmap = new Pixmap(1, 1, Pixmap.Format.RGBA8888);
      pixmap.setColor(Color.WHITE); // Set color to blue
      pixmap.fill();

      // Create a Drawable from the Pixmap
      Drawable backgroundDrawable = new TextureRegionDrawable(new TextureRegion(new Texture(pixmap)));

      // Dispose of the Pixmap after creating the texture
      pixmap.dispose();


      int screenWidth = (int) stage.getWidth();
      int screenHeight = (int) stage.getHeight();

      settingMenu.setSize(550, 350);
      settingMenu.setBackground(backgroundDrawable);
      settingMenu.setVisible(false);

      Label title = new Label("Settings", skin, "title");
      settingMenu.add(title).top().padTop(20f).padLeft(40);

      // Center the menu on the screen
      settingMenu.setPosition(
              (screenWidth - settingMenu.getWidth()) / 2,
              (screenHeight - settingMenu.getHeight()) / 2
      );

      TextButton closeButton = new TextButton("X", skin);

      Table closeButtonTable = new Table();
      closeButtonTable.add(closeButton).size(40, 40).expand().top().right();

      settingMenu.add(closeButtonTable).top().right().fillX();
      settingMenu.row();

      settingsMenuDisplay = new SettingsMenuDisplay();
      Table contentTable = settingsMenuDisplay.makeSettingsTable();
      settingMenu.add(contentTable).expand().fill().padLeft(50);

      stage.addActor(settingMenu);
      closeButton.addListener(
              new ChangeListener() {
                  @Override
                  public void changed(ChangeEvent changeEvent, Actor actor) {
                      // Toggle the visibility of the small menu
                      settingMenu.setVisible(!settingMenu.isVisible());
                  }
              });
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
