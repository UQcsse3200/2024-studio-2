package com.csse3200.game.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Align;
import com.csse3200.game.GdxGame;
import com.csse3200.game.areas.ForestGameArea;
import com.csse3200.game.areas.terrain.TerrainFactory;
import com.csse3200.game.components.gamearea.PerformanceDisplay;
import com.csse3200.game.components.maingame.MainGameActions;
import com.csse3200.game.components.maingame.MainGameExitDisplay;
import com.csse3200.game.entities.Entity;
import com.csse3200.game.entities.EntityService;
import com.csse3200.game.entities.factories.RenderFactory;
import com.csse3200.game.input.InputComponent;
import com.csse3200.game.input.InputDecorator;
import com.csse3200.game.input.InputService;
import com.csse3200.game.physics.PhysicsEngine;
import com.csse3200.game.physics.PhysicsService;
import com.csse3200.game.rendering.RenderService;
import com.csse3200.game.rendering.Renderer;
import com.csse3200.game.services.GameTime;
import com.csse3200.game.services.ResourceService;
import com.csse3200.game.services.ServiceLocator;
import com.csse3200.game.ui.terminal.Terminal;
import com.csse3200.game.ui.terminal.TerminalDisplay;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.awt.*;
import java.awt.image.ImageObserver;
import java.awt.image.ImageProducer;

import com.badlogic.gdx.graphics.Color;

import static com.badlogic.gdx.graphics.Color.*;
import static javax.swing.text.html.HTML.Attribute.ALIGN;

public class CombatScreen extends ScreenAdapter {
    private static final Logger logger = LoggerFactory.getLogger(CombatScreen.class);
    private static final String[] combatTextures = {"images/heart.png"}; // Add relevant combat assets
    private static final Vector2 CAMERA_POSITION = new Vector2(7.5f, 7.5f);

    private final GdxGame game;
    private final Renderer renderer;
    private final PhysicsEngine physicsEngine;

    // Following UI features to be initialized and used throughout combat
    private Label combatDialogue;
    private Table tb;
    private Table buttonTable;
    private Skin skin;
    private TextButton attackButton;
    private TextButton defendButton;
    private TextButton accessInventoryButton;
    private TextButton potionsButton;
    private TextButton hitButton;

    public CombatScreen(GdxGame game) {
        this.game = game;

        logger.debug("Initialising main game screen services");
        ServiceLocator.registerTimeSource(new GameTime());

        PhysicsService physicsService = new PhysicsService();
        ServiceLocator.registerPhysicsService(physicsService);
        physicsEngine = physicsService.getPhysics();

        ServiceLocator.registerInputService(new InputService());
        ServiceLocator.registerResourceService(new ResourceService());

        ServiceLocator.registerEntityService(new EntityService());
        ServiceLocator.registerRenderService(new RenderService());

        renderer = RenderFactory.createRenderer();
        renderer.getCamera().getEntity().setPosition(CAMERA_POSITION);
        renderer.getDebug().renderPhysicsWorld(physicsEngine.getWorld());

        loadAssets();
        createUI(); //

        logger.debug("Initialising combat game screen entities");
        TerrainFactory terrainFactory = new TerrainFactory(renderer.getCamera());
        ForestGameArea forestGameArea = new ForestGameArea(terrainFactory);
        forestGameArea.create();

        logger.debug("Initializing combat screen entities");
        createCombatEntities();
        // handleCombat(); -- call separate class to handle combat
    }

    @Override
    public void render(float delta) {
        physicsEngine.update();
        ServiceLocator.getEntityService().update();
        renderer.render();
    }

    @Override
    public void resize(int width, int height) {
        renderer.resize(width, height);
        logger.trace("Resized renderer: ({} x {})", width, height);
    }

    @Override
    public void pause() {
        logger.info("Combat paused");
    }

    @Override
    public void resume() {
        logger.info("Combat resumed");
    }

    @Override
    public void dispose() {
        logger.debug("Disposing combat screen");

        renderer.dispose();
        unloadAssets();

        ServiceLocator.getEntityService().dispose();
        ServiceLocator.getRenderService().dispose();

        ServiceLocator.clear();
    }

    private void loadAssets() {
        logger.debug("Loading combat assets");
        ResourceService resourceService = ServiceLocator.getResourceService();
        resourceService.loadTextures(combatTextures);
        ServiceLocator.getResourceService().loadAll();
    }

    private void unloadAssets() {
        logger.debug("Unloading combat assets");
        ResourceService resourceService = ServiceLocator.getResourceService();
        resourceService.unloadAssets(combatTextures);
    }

    private void createUI() {
        logger.debug("Creating combat UI");
        // Create and register UI elements specific to combat, like in MainGameScreen
        Stage stage = ServiceLocator.getRenderService().getStage();

        createDialogueBoxAndButtons(stage);

        InputComponent inputComponent =
                ServiceLocator.getInputService().getInputFactory().createForTerminal();

        Entity ui = new Entity();
        ui.addComponent(new InputDecorator(stage, 10))
                .addComponent(new PerformanceDisplay())
                .addComponent(new MainGameActions(this.game))
                .addComponent(new MainGameExitDisplay())
                .addComponent(new Terminal())
                .addComponent(inputComponent)
                .addComponent(new TerminalDisplay());

        ServiceLocator.getEntityService().register(ui);

        // Create Combat Screen Additional Components
        // eventually add some method to load inventory slots
    }

    private void createDialogueBoxAndButtons(Stage st){
        skin = new Skin();

        // Create BitmapFont
        BitmapFont font = new BitmapFont();  // Default font
        skin.add("default", font);

        // Create a LabelStyle and set the font and font color
        LabelStyle combatLabel = new LabelStyle(); // label style
        combatLabel.font = skin.getFont("default");
        skin.add("default", combatLabel);

        TextButton.TextButtonStyle buttonStyle = new TextButton.TextButtonStyle();
        buttonStyle.font = font; // Set the font for the button
        buttonStyle.fontColor = Color.ORANGE;
        skin.add("default", buttonStyle); // Register the style with the name "default"

        /*
        tb = new Table(); // table to store buttons for actions & combat dialogue
        // tb.setFillParent(true);
        tb.setSize(0, 900);
        tb.setPosition(0,0);
        tb.align(Align.left | Align.top);
        tb.pad(0);
        tb.left().top();
        tb.defaults().pad(0);
        tb.setDebug(true);
        st.addActor(tb);
       */

        combatDialogue = new Label("Begin Combat!", skin); // text will be incrementally updated during combat
        combatDialogue.setFontScale(2.0f);
        combatDialogue.setColor(BLACK);
        combatDialogue.setAlignment(Align.left);
        combatDialogue.setPosition(5,100);
        // tb.add(combatDialogue).expandX().fillX().padBottom(10).padLeft(5); // add the combat dialogue to the table

        // Create new texture and drawable instance for hit image to be used as imagebutton
        Texture hitTexture = new Texture(Gdx.files.internal("source/core/assets/images/hit_button.png")); // load image
        Drawable hitDrawable = new TextureRegionDrawable(new TextureRegion(hitTexture));
        ImageButton hitButton = new ImageButton(hitDrawable);
        hitButton.setSize(240,230);
        hitButton.setPosition(0,700, Align.left);
        // tb.add(hitButton).size(270,270).height(100); // add button to table
        st.addActor(hitButton);

        Texture bagTexture = new Texture(Gdx.files.internal("source/core/assets/images/bag_button.png")); // load image
        Drawable bagDrawable = new TextureRegionDrawable(new TextureRegion(bagTexture));
        ImageButton bagButton = new ImageButton(bagDrawable);
        bagButton.setSize(240,230);
        bagButton.setPosition(0,570, Align.left);
        st.addActor(bagButton);
        // tb.row(); // create new row for button
        // tb.padTop(10);
        // tb.add(bagButton).size(270,200).height(100); // add button to table

        Texture potionsTexture = new Texture(Gdx.files.internal("source/core/assets/images/potions_button.png")); //load image
        Drawable potionsDrawable = new TextureRegionDrawable(new TextureRegion(potionsTexture));
        ImageButton potionsButton = new ImageButton(potionsDrawable);
        potionsButton.setSize(240,230);
        potionsButton.setPosition(0,440, Align.left);
        st.addActor(potionsButton);
        // tb.row(); // create new row for button
        // tb.padTop(10);
        // tb.add(potionsButton).size(185,185).height(100);


        /*
        Texture hitTexture = new Texture(Gdx.files.internal("source/core/assets/images/hit_button.png"));
        Drawable hitDrawable = new TextureRegionDrawable(new TextureRegion(hitTexture));
        ImageButton hitButton = new ImageButton(hitDrawable);
        hitButton.setSize(100,100);
        // hitButton.setPosition(0,0);
        tb.add(hitButton).size(140,150).height(100);

        Texture bagTexture = new Texture(Gdx.files.internal("source/core/assets/images/bag_button.png"));
        Drawable bagDrawable = new TextureRegionDrawable(new TextureRegion(bagTexture));
        ImageButton bagButton = new ImageButton(bagDrawable);
        bagButton.setSize(100,100);
        // bagButton.setPosition(0,0);
        tb.row();
        tb.padTop(10);
        tb.add(bagButton).size(270,200).height(100);
        //.expandX()
        //.fillX()

        Texture potionsTexture = new Texture(Gdx.files.internal("source/core/assets/images/potions_button.png"));
        Drawable potionsDrawable = new TextureRegionDrawable(new TextureRegion(potionsTexture));
        ImageButton potionsButton = new ImageButton(potionsDrawable);
        potionsButton.setSize(100,100);
        // potionsButton.setPosition(0,0);
        // tb.row();
        tb.padTop(10);
        tb.add(potionsButton).size(185,185).height(100);
         */



        // Add actionlisteners for buttons
        hitButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                // hit event
                System.out.println("Hit Button Pressed");
            }
        });

    }

    private void createCombatEntities() {
        // Create and initialize entities relevant to the combat, such as the player, enemies, etc.
    }

    // Function will handle player health, manage player/enemy turns

}

