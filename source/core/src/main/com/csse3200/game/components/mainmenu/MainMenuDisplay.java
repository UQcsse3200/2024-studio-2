package com.csse3200.game.components.mainmenu;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Align;
import com.csse3200.game.components.settingsmenu.SettingsMenuDisplay;
import com.csse3200.game.services.ServiceLocator;
import com.csse3200.game.ui.UIComponent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.util.Optional;

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

        // Add elevation effect to buttons
        addButtonElevationEffect(startBtn);
        addButtonElevationEffect(loadBtn);
        addButtonElevationEffect(settingsBtn);
        addButtonElevationEffect(exitBtn);

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
    private void addButtonElevationEffect(TextButton button) {
        button.addListener(new ClickListener() {
            @Override
            public void enter(InputEvent event, float x, float y, int pointer, Actor fromActor) {
                button.addAction(Actions.parallel(
                        Actions.moveBy(0, 5, 0.1f),
                        Actions.scaleTo(1.05f, 1.05f, 0.1f)
                ));
            }

            @Override
            public void exit(InputEvent event, float x, float y, int pointer, Actor toActor) {
                button.addAction(Actions.parallel(
                        Actions.moveBy(0, -5, 0.1f),
                        Actions.scaleTo(1f, 1f, 0.1f)
                ));
            }
        });
    }
    private void makeSettingMenu(Table settingMenu) {
        Pixmap pixmap = new Pixmap(1, 1, Pixmap.Format.RGBA8888);
        pixmap.setColor(Color.WHITE); // Set color to white
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

        Table topTable = new Table();
        topTable.top().padTop(10);

        Label title = new Label("Settings", skin, "title");
        //settingMenu.row().expandY(); // Ensure title is at the top

        topTable.add(title).expandX().center();
        topTable.row();

        TextButton closeButton = new TextButton("X", skin);
        topTable.add(closeButton).size(40, 40).right().padRight(10).padTop(-40);

        settingsMenuDisplay = new SettingsMenuDisplay();
        Table contentTable = settingsMenuDisplay.makeSettingsTable();

        // Create a table for the "Apply" button
        Table bottomRightTable = new Table();
        bottomRightTable.bottom(); // Align contents to bottom-right

        TextButton applyButton = new TextButton("Apply", skin);
        bottomRightTable.add(applyButton).size(80, 40).padBottom(10f).padRight(10f);

        settingMenu.add(topTable).expandX().fillX(); // Top-right table
        settingMenu.row().padTop(30f);
        settingMenu.add(contentTable).expandX().expandY().padLeft(50);
        settingMenu.row().padTop(30f);
        settingMenu.add(bottomRightTable).expandX().right().padLeft(100); // Bottom-right table

        // Center the menu on the screen
        settingMenu.setPosition(
                (screenWidth - settingMenu.getWidth()) / 2,
                (screenHeight - settingMenu.getHeight()) / 2
        );

        stage.addActor(settingMenu);

        closeButton.addListener(
                new ChangeListener() {
                    @Override
                    public void changed(ChangeEvent changeEvent, Actor actor) {
                        // Toggle the visibility of the small menu
                        settingMenu.setVisible(!settingMenu.isVisible());
                        table.setTouchable(Touchable.enabled);
                    }
                });

        // Add event listener for the "Apply" button
        applyButton.addListener(
                new ChangeListener() {
                    @Override
                    public void changed(ChangeEvent changeEvent, Actor actor) {
                        logger.debug("Apply button clicked");
                        settingsMenuDisplay.applyChanges(); // Apply the settings when clicked
                        settingMenu.setVisible(false); // Optionally hide the settings menu
                        table.setTouchable(Touchable.enabled);
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
//
//
//package com.csse3200.game.components.mainmenu;
//
//import com.badlogic.gdx.graphics.Texture;
//import com.badlogic.gdx.graphics.g2d.SpriteBatch;
//import com.badlogic.gdx.scenes.scene2d.Actor;
//import com.badlogic.gdx.scenes.scene2d.InputEvent;
//import com.badlogic.gdx.scenes.scene2d.actions.Actions;
//import com.badlogic.gdx.scenes.scene2d.ui.Image;
//import com.badlogic.gdx.scenes.scene2d.ui.Table;
//import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
//import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
//import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
//import com.csse3200.game.services.ServiceLocator;
//import com.csse3200.game.ui.UIComponent;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//
///**
// * A ui component for displaying the Main menu.
// */
//public class MainMenuDisplay extends UIComponent {
//    private static final Logger logger = LoggerFactory.getLogger(MainMenuDisplay.class);
//    private static final float Z_INDEX = 2f;
//    private Table table;
//
//    @Override
//    public void create() {
//        super.create();
//        addActors();
//    }
//
//    private void addActors() {
//        table = new Table();
//        table.setFillParent(true);
//        Image title =
//                new Image(
//                        ServiceLocator.getResourceService()
//                                .getAsset("images/box_boy_title.png", Texture.class));
//
//        TextButton startBtn = new TextButton("Start", skin);
//        TextButton loadBtn = new TextButton("Load", skin);
//        TextButton settingsBtn = new TextButton("Settings", skin);
//        TextButton exitBtn = new TextButton("Exit", skin);
//
//        // Add elevation effect to buttons
//        addButtonElevationEffect(startBtn);
//        addButtonElevationEffect(loadBtn);
//        addButtonElevationEffect(settingsBtn);
//        addButtonElevationEffect(exitBtn);
//
//        // Triggers an event when the button is pressed
//        startBtn.addListener(
//                new ChangeListener() {
//                    @Override
//                    public void changed(ChangeEvent changeEvent, Actor actor) {
//                        logger.debug("Start button clicked");
//                        entity.getEvents().trigger("start");
//                    }
//                });
//
//        loadBtn.addListener(
//                new ChangeListener() {
//                    @Override
//                    public void changed(ChangeEvent changeEvent, Actor actor) {
//                        logger.debug("Load button clicked");
//                        entity.getEvents().trigger("load");
//                    }
//                });
//
//        settingsBtn.addListener(
//                new ChangeListener() {
//                    @Override
//                    public void changed(ChangeEvent changeEvent, Actor actor) {
//                        logger.debug("Settings button clicked");
//                        entity.getEvents().trigger("settings");
//                    }
//                });
//
//        exitBtn.addListener(
//                new ChangeListener() {
//                    @Override
//                    public void changed(ChangeEvent changeEvent, Actor actor) {
//                        logger.debug("Exit button clicked");
//                        entity.getEvents().trigger("exit");
//                    }
//                });
//
//        table.add(title);
//        table.row();
//        table.add(startBtn).padTop(30f);
//        table.row();
//        table.add(loadBtn).padTop(15f);
//        table.row();
//        table.add(settingsBtn).padTop(15f);
//        table.row();
//        table.add(exitBtn).padTop(15f);
//
//        stage.addActor(table);
//    }
//
//    /**
//     * An elevation effect on hovering over the button.
//     */
//    private void addButtonElevationEffect(TextButton button) {
//        button.addListener(new ClickListener() {
//            @Override
//            public void enter(InputEvent event, float x, float y, int pointer, Actor fromActor) {
//                button.addAction(Actions.parallel(
//                        Actions.moveBy(0, 5, 0.1f),
//                        Actions.scaleTo(1.05f, 1.05f, 0.1f)
//                ));
//            }
//
//            @Override
//            public void exit(InputEvent event, float x, float y, int pointer, Actor toActor) {
//                button.addAction(Actions.parallel(
//                        Actions.moveBy(0, -5, 0.1f),
//                        Actions.scaleTo(1f, 1f, 0.1f)
//                ));
//            }
//        });
//    }
//
//    @Override
//    public void draw(SpriteBatch batch) {
//        // draw is handled by the stage
//    }
//
//    @Override
//    public float getZIndex() {
//        return Z_INDEX;
//    }
//
//    @Override
//    public void dispose() {
//        table.clear();
//        super.dispose();
//    }
//}
