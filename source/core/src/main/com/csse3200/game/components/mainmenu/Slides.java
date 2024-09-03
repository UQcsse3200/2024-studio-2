package com.csse3200.game.components.mainmenu;

import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.csse3200.game.files.FileLoader;

import java.util.ArrayList;
import java.util.List;

public class Slides {
    private List<Slide> slides;
    private final int numSlides;
    private int currentSlide;

    public Slides(Skin skin) {
        loadSlidesFromJson(skin);
        this.numSlides = slides.size();
        this.currentSlide = 0;

        // Initially hide all slides except the first
        slides.getFirst().setVisible(true);
        for (int i = 1; i < numSlides; i++) {
            slides.get(i).setVisible(false);
        }
    }

    public Slide getSlide() {return slides.get(currentSlide);}

    public int getNumSlides() {return numSlides;}

    public boolean moveSlidesForward() {
        if (currentSlide < numSlides) {
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

    private void loadSlidesFromJson(Skin skin) {
        this.slides = new ArrayList<>();

        // Read array of SlideData from JSON
        SlideData[] slidesData = FileLoader.readClass(SlideData[].class, "configs/HelpSlides.json");

        if (slidesData != null) {
            for (SlideData data : slidesData) {
                Slide slide = new Slide(data.name, skin, data.title, data.content);
                slides.add(slide);
            }
        } else {
            throw new RuntimeException("Error: Slide data could not be loaded from slides.json.");
        }
    }

    // Helper class to represent slide data loaded from JSON
    private static class SlideData {
        public String name;
        public String title;
        public String content;
    }
}
