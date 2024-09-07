package com.csse3200.game.components.mainmenu;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;


public class Slides {

    public static class MovementSlide extends Table {
        private DogActor dogActor;
        private final float speed= 100f;
        public MovementSlide(Skin skin) {
            Label titleLabel = new Label("Movement Instructions", skin, "title");
            Label contentLabel = new Label("Move the dog with WASD keys!!", skin);

            add(titleLabel).padTop(20f).expandX().center().row();
            add(contentLabel).padTop(20f).expandX().center().row();
            // Create the dog actor and add it to the table
            dogActor = new DogActor(new Texture(Gdx.files.internal("images/dog2.png")));
            addActor(dogActor);  // Adds the dog to the stage
        }


        // Custom DogActor class to handle movement
        public class DogActor extends Actor {
            private Texture texture;
            private Drawable drawable;
            private float dogX, dogY;

            public DogActor(Texture texture) {
                this.texture = texture;
                this.drawable = new TextureRegionDrawable(new TextureRegion(texture));
                setSize(texture.getWidth(), texture.getHeight());

                // Initially, the position will be set in the layout method
            }

            @Override
            public void act(float delta) {
                super.act(delta);

                // Update dog position based on user input
                handleInput(delta);

                // Update the actor's position
                setPosition(dogX, dogY);
            }

            @Override
            public void draw(com.badlogic.gdx.graphics.g2d.Batch batch, float parentAlpha) {
                drawable.draw(batch, getX(), getY(), getWidth(), getHeight());
            }

            private void handleInput(float delta) {
                // Initialize position if it's the first time act is called
                if (dogX == 0 && dogY == 0) {
                    dogX = getX();
                    dogY = getY();
                }

                // Handle WASD input
                if (Gdx.input.isKeyPressed(Input.Keys.W)) {
                    dogY += speed * delta;
                }
                if (Gdx.input.isKeyPressed(Input.Keys.S)) {
                    dogY -= speed * delta;
                }
                if (Gdx.input.isKeyPressed(Input.Keys.A)) {
                    dogX -= speed * delta;
                }
                if (Gdx.input.isKeyPressed(Input.Keys.D)) {
                    dogX += speed * delta;

                }

                // Boundary check to keep the dog inside the help menu (MovementSlide)
                dogX = Math.max(0, Math.min(dogX, getParent().getWidth() - getWidth()));
                dogY = Math.max(0, Math.min(dogY, getParent().getHeight() - getHeight()));
            }
        }
    }



    /**
     * Slide displaying Combat instructions.
     */
    public static class CombatSlide extends Table {
        public CombatSlide(Skin skin) {
            Label titleLabel = new Label("Combat Instructions", skin, "title");
            Label contentLabel = new Label("Here are the instructions for combat...", skin);

            add(titleLabel).padTop(20f).expandX().center().row();
            add(contentLabel).padTop(20f).expandX().center().row();
        }
    }

    /**
     * Slide displaying Storyline information.
     */
    public static class StorylineSlide extends Table {
        public StorylineSlide(Skin skin) {
            Label titleLabel = new Label("Storyline Overview", skin, "title");
            Label contentLabel = new Label("Here's the storyline of the game...", skin);

            add(titleLabel).padTop(20f).expandX().center().row();
            add(contentLabel).padTop(20f).expandX().center().row();
        }
    }

    /**
     * Slide displaying Minigames information.
     */
    public static class MinigamesSlide extends Table {
        public MinigamesSlide(Skin skin) {
            Label titleLabel = new Label("Minigames", skin, "title");
            Label contentLabel = new Label("Here are some details about the minigames...", skin);

            add(titleLabel).padTop(20f).expandX().center().row();
            add(contentLabel).padTop(20f).expandX().center().row();
        }
    }

    /**
     * Slide displaying Stats information.
     */
    public static class StatsSlide extends Table {
        public StatsSlide(Skin skin) {
            Label titleLabel = new Label("Game Stats", skin, "title");
            Label contentLabel = new Label("Here are some statistics about the game...", skin);

            add(titleLabel).padTop(20f).expandX().center().row();
            add(contentLabel).padTop(20f).expandX().center().row();
        }
    }

}
