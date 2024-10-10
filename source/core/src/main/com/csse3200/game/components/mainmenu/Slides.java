package com.csse3200.game.components.mainmenu;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.graphics.g2d.TextureRegion;


public class Slides {
    private static final String TITLE_TEXT = "title";

    private Slides() {
        throw new IllegalArgumentException("Do not instantiate static util class!");
    }

    public static class MovementSlide extends Table {
        private DogActor dogActor;
        private final float speed= 100f;
        public MovementSlide(Skin skin) {
            Label titleLabel = new Label("Movement Instructions", skin, TITLE_TEXT);
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
            private float dogX;
            private float dogY;

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
            Label titleLabel = new Label("Combat Instructions", skin, TITLE_TEXT);
            Label contentLabel = new Label("""
                   Fight Like a Pro: Command Your Combat with On-Screen Buttons
                   
                   Attack: Deal damage to your opponent
                   Guard: Reduce damage taken by 50%
                   Sleep: Restore 25% stamina and 10% health. It is the only way to regain stamina
                   Item: Use items each turn for various effects:
                   
                   HP Potions: Restore health
                   
                   Status Potions: Boost stats for the battle
                   
                   Remedies: Cure status ailments or confusion
                   """
                    ,skin);

            add(titleLabel).padTop(10f).expandX().center().row();
            add(contentLabel).padTop(20f).padLeft(30f).expandX().left().row();
            padBottom(-200f);

        }
    }

    /**
     * Slide displaying Storyline information.
     */
    public static class StorylineSlide extends Table {
        public StorylineSlide(Skin skin) {
            Label titleLabel = new Label("Storyline Overview", skin, TITLE_TEXT);
            Label contentLabel = new Label("""
               
               Welcome to Attack on Animals, a real-time strategy adventure of animal conquest
               Control powerful creatures to conquer kingdoms and restore balance to the land
               Choose from a variety of animals with unique stats and abilities
               Explore three kingdoms: Water, Land, and Air, each with unique ecosystems
               Dogs rule the Land, while birds reign over the skies of Air
               Gather resources, craft tools, and adapt to changing environments for survival
               Face fierce animal rulers and the ultimate challenge the last standing
               Your strategies and choices will shape the fate of this animal world
               Pick your kingdom, choose your favorite animal, and lead them to victory!
               """, skin);

            add(titleLabel).padTop(10f).expandX().center().row();
            add(contentLabel).padTop(20f).padLeft(30f).expandX().left().row();
            padBottom(-200f);

        }
    }

    /**
     * Slide displaying Minigames information.
     */
    public static class MinigamesSlide extends Table {
        public MinigamesSlide(Skin skin) {
            Label titleLabel = new Label("Snake Minigame", skin, TITLE_TEXT);
            Label keysLabel = new Label("""
            Arrow Keys: Move your snake in any direction:up, down, left, or right
            W, A, S, D: Alternative movement controls for pros!
            
            Objective: Gobble up as many apples as you can!
            Each apple adds +1 point to your score.
            Keep an eye on your score displayed onscreen. How high can you go?
            
            Medals:
            Bronze Medal: A solid start:get the basics down.
            Silver Medal: You have got skill! But can you go further?
            Gold Medal: Legendary! Only the best can claim this title.
            """, skin);
            add(titleLabel).padTop(10f).expandX().center().row();
            add(keysLabel).padTop(20f).padLeft(30f).expandX().left().row();
            padBottom(-200f);

        }
    }

    public static class Minigames1Slide extends Table {
        public Minigames1Slide(Skin skin) {
            Label titleLabel = new Label("Birdie Minigame",skin, TITLE_TEXT);
            Label keysLabel = new Label("""
                    BirdieDash Mini-Game brings a fresh twist inspired by Flappy Bird!
                    Guide your bird through pipes,dodge spikes,
                    and grab coins as you aim for the top score
                    Simple, addictive, and a whole lot of fun!
                    
                    The BirdieDash features simple yet challenging controls:
                    Spacebar / Tap Screen: Flap the bird to ascend
                    The bird falls automatically stay airborne and navigate through obstacles.
                    Colliding with the pipes and spikes results in a game over.
                    Collecting coins adds points to the score.
                    The score pops up front and center, updating in real-time as you play
                    Earn your bragging rights: bronze at 2-4, silver at 4-6, and hit gold with 6+.
                    """
                    ,skin);
            add(titleLabel).padTop(10f).expandX().center().row();
            add(keysLabel).padTop(20f).padLeft(30f).expandX().left().row();
            padBottom(-200f);
        }
    }
    public static class Minigames2Slide extends Table {
        public Minigames2Slide(Skin skin) {
            Label titleLabel = new Label("Underwatermaze Minigame",skin, TITLE_TEXT);
            Label keysLabel = new Label("""
                    Dive into MazeUnderwater!
                    Navigate a dark maze, recover scattered fish eggs
                    Dodge sea creatures as you explore the depths
                    The more eggs you collect, the better your medal
                    
                    W S A D: Move up, down, left, and right
                    
                    Collect eggs while dodging angler fish, eels, and jellyfish
                    More eggs = higher score and medal
                    
                    Bronze: Fewer eggs, Silver: Moderate collection, Gold: Most or all eggs
                    
                    Angler Fish: 100 health, 20 damage per hit
                    Jellyfish: 10 damage, Electric Eel: 5 damage
                    Game ends if health reaches zero
                    """
                    ,skin);
            add(titleLabel).padTop(10f).expandX().center().row();
            add(keysLabel).padTop(20f).padLeft(30f).expandX().left().row();
            padBottom(-250f);
        }
    }


    /**
     * Slide displaying Stats information.
     */
    public static class StatsSlide extends Table {
        public StatsSlide(Skin skin) {
            Label titleLabel = new Label("Game Stats", skin, TITLE_TEXT);
            Label contentLabel = new Label("""
                    Each animal in every kingdom has five core stats
                    
                    Health: Determines how long they survive during battle
                    Strength: Indicates how much damage they are capable of dealing
                    Defense: Reflects their resistance to enemy attacks
                    Speed: Measures how quickly they move and strike in combat
                    Stamina: Determines how long they can keep fighting
                    
                    Every animal has unique levels, so choose them carefully
                    
                    You can collect various items like apples during the game
                    Press P to add them to your inventory, and use E to access it
                    """
                    , skin);

            add(titleLabel).padTop(-10f).expandX().center().row();
            add(contentLabel).padTop(20f).padLeft(30f).expandX().left().row();
            padBottom(-200f);
        }
    }

}