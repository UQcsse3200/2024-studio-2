package com.csse3200.game.components.mainmenu;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;

public class Slides {

    public static class MovementSlide extends Table {
        public MovementSlide(Skin skin) {
            Label titleLabel = new Label("Movement Instructions", skin, "title");
            Label contentLabel = new Label("Here are the instructions for movement...", skin);

            add(titleLabel).padTop(20f).expandX().center().row();
            add(contentLabel).padTop(20f).expandX().center().row();

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
