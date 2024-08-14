package com.csse3200.game.ui;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.EventListener;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.csse3200.game.services.ServiceLocator;
import com.csse3200.game.ui.UIComponent;

import javax.swing.event.ChangeEvent;

public class AnimalSelectionUI extends UIComponent {
    private Table table;
    private Skin skin;

    public AnimalSelectionUI() {
        table = new Table();
        table.setFillParent(true);
        skin = new Skin(Gdx.files.internal("flat-earth/skin/flat-earth-ui.json"));

        // Load textures
        Texture animal1Texture = new Texture(Gdx.files.internal("images/animal1.png"));
        Texture animal2Texture = new Texture(Gdx.files.internal("images/animal2.png"));

        // Create buttons
        TextButton animal1Button = new TextButton("Animal 1", skin);
        TextButton animal2Button = new TextButton("Animal 2", skin);

        // Set up button listeners
        animal1Button.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                selectAnimal("Animal 1");
            }
        });

        animal2Button.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                selectAnimal("Animal 2");
            }
        });

        // Add components to the table
        table.add(new Image(animal1Texture)).pad(10);
        table.row();
        table.add(animal1Button).pad(10);
        table.row();
        table.add(new Image(animal2Texture)).pad(10);
        table.row();
        table.add(animal2Button).pad(10);

        // Add table to the stage
        ServiceLocator.getRenderService().getStage().addActor(table);
    }

    private void selectAnimal(String animalName) {
        System.out.println(animalName + " selected");
        // Add logic here to handle animal selection
    }

    @Override
    public void render(SpriteBatch batch) {
        table.act(Gdx.graphics.getDeltaTime());
        table.draw(batch, 1);
    }

    @Override
    protected void draw(SpriteBatch batch) {

    }

    @Override
    public void dispose() {
        skin.dispose();
        // Dispose of textures
    }

    public Actor getTable() {
        return table;
    }
}


