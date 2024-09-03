package com.csse3200.game.components.mainmenu;

import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.csse3200.game.files.FileLoader;

import java.util.ArrayList;
import java.util.List;

public class Slides {
    private List<Table> slides;
    private List<String> slideNames;
    private final int numSlides;
    private int currentSlide;

    public Slides(Skin skin) {
        loadSlidesFromJson(skin);
        this.currentSlide = 0;
        this.numSlides = slides.size();

        // Initially hide all slides except the first
        slides.getFirst().setVisible(true);
        for (int i = 1; i < numSlides; i++) {
            slides.get(i).setVisible(false);
        }
    }

    public Table getSlide() {return slides.get(currentSlide);}

    public boolean moveSlidesForward() {
        if (currentSlide < numSlides - 1) {
            slides.get(currentSlide).setVisible(false);
            currentSlide++;
            slides.get(currentSlide).setVisible(true);
            return true;
        }
        return false;
    }

    public boolean moveSlidesBackward() {
        if (currentSlide > 0) {
            slides.get(currentSlide).setVisible(false);
            currentSlide--;
            slides.get(currentSlide).setVisible(true);
            return true;
        }
        return false;
    }

    public String getName() {
        return slideNames.get(currentSlide);
    }

    private void loadSlidesFromJson(Skin skin) {
        this.slides = new ArrayList<>();
        this.slideNames = new ArrayList<>();
        this.currentSlide = 0;

        // Read array of SlideData from JSON
        SlideData[] slidesData = FileLoader.readClass(SlideData[].class, "configs/HelpSlides.json");

        if (slidesData != null) {
            for (SlideData data : slidesData) {
                createSlide(data.name, skin, data.title, data.content);
            }
        } else {
            throw new RuntimeException("Error: Slide data could not be loaded from slides.json.");
        }
    }

    private void createSlide(String name, Skin skin, String title, String contents) {
        slideNames.add(name);
        slides.add(new Table());
        Label titleLabel = new Label(title, skin, "title");
        Label contentLabel = new Label(contents, skin);
        slides.get(currentSlide).add(titleLabel).padTop(20f).expandX().center().row();
        slides.get(currentSlide).add(contentLabel).padTop(20f).expandX().center().row();
        currentSlide++;
    }

    // Helper class to represent slide data loaded from JSON
    private static class SlideData {
        public String name;
        public String title;
        public String content;
    }
}
