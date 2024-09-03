package com.csse3200.game.components.mainmenu;

import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;

// TODO: Update this to have slide info in a txt or json file and load them in
// TODO: statically - that way in game stats can be updated by just writing to
// TODO: that file and then just opening that file when opening help slides
public class Slides {
    public static int NUM_SLIDES = 5;

    public static Slide[] getAllSlides(Skin skin) {
        Slide[] slides = new Slide[NUM_SLIDES];
        slides[0] = getMovementSlide(skin);
        slides[1] = getCombatSlide(skin);
        slides[2] = getStorylineSlide(skin);
        slides[3] = getMinigamesSlide(skin);
        slides[4] = getStatsSlide(skin);
        return slides;
    }

    public static class Slide extends Table {
        private final String name;

        public Slide(String name, Skin skin, String title, String contents) {
            this.name = name;
            Label titleLabel = new Label(title, skin, "title");
            Label contentLabel = new Label(contents, skin);
            add(titleLabel).padTop(20f).expandX().center().row();
            add(contentLabel).padTop(20f).expandX().center().row();
        }

        public String getName() {return this.name;}
    }

    /**
     * Slide displaying Movement information.
     */
    private static Slide getMovementSlide(Skin skin) {
        return new Slide("movement", skin,
                "Movement Instructions", "Here are the instructions for movement...");
    }

    /**
     * Slide displaying Combat information.
     */
    private static Slide getCombatSlide(Skin skin) {
        return new Slide("combat", skin,
                "Combat Instructions", "Here are the instructions for combat...");
    }

    /**
     * Slide displaying Storyline information.
     */
    private static Slide getStorylineSlide(Skin skin) {
        return new Slide("storyline", skin,
                "Storyline Overview", "Here's the storyline of the game...");
    }

    /**
     * Slide displaying Mini-games information.
     */
    private static Slide getMinigamesSlide(Skin skin) {
        return new Slide("minigames", skin,
                "Minigames", "Here are the instructions for movement...");
    }

    /**
     * Slide displaying Stats information.
     */
    private static Slide getStatsSlide(Skin skin) {
        return new Slide("stats", skin,
                "Game Stats", "Here are some statistics about the game...");
    }
}
