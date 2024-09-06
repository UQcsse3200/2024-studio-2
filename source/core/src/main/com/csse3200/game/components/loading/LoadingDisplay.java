package com.csse3200.game.components.loading;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.ProgressBar;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.csse3200.game.services.GameTime;
import com.csse3200.game.services.ResourceService;
import com.csse3200.game.services.ServiceLocator;
import com.csse3200.game.ui.UIComponent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.csse3200.game.components.settingsmenu.UserSettings;

import java.awt.*;

/**
 * A UI component for displaying the Main menu.
 */
public class LoadingDisplay extends UIComponent {
    private static final Logger logger = LoggerFactory.getLogger(LoadingDisplay.class);
    private static final float Z_INDEX = 2f;
    private Table table;
    public ProgressBar progressBar;
    private Label loadingLabel;
    private Label tipsLabel;

    private float progress;
    public LoadingDisplay() {
        progress = 0;
        progressBar = new ProgressBar(0, 100, 1, false, skin);
        progressBar.setValue(0);
        loadingLabel = new Label("Loading..." + progress + "%", skin, "large-white");
        tipsLabel = new Label("Tips: If you're having trobule winning in combat, try getting better at the game.", skin, "default-white");
    }

    @Override
    public void create() {
        super.create();
        addActors();
    }

    private void addActors() {
        table = new Table();
        table.setFillParent(true);
        table.setBackground(new TextureRegionDrawable(new TextureRegion(new Texture("images/SplashScreen/SplashTitleNight.png"))));

        table.add(loadingLabel).padTop(350);
        table.row();
        table.add(progressBar).width(300).padTop(20);
        table.row();
        table.add(tipsLabel).expandY().bottom().padBottom(30);


        stage.addActor(table);
    }

    public boolean isLoadingFinished() {
        return progressBar.getValue() >= 100;
    }

    @Override
    public void update() {
        super.update();
        progress += 1f;
        if (progress >= 100) {
            progress = 100;
        }
        loadingLabel.setText("Loading..." + (int) (progress) + "%");
        progressBar.setValue(progress);
        
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
