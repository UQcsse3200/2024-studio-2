package com.csse3200.game.components.loading;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.ProgressBar;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.csse3200.game.services.GameTime;
import com.csse3200.game.services.ResourceService;
import com.csse3200.game.services.ServiceLocator;
import com.csse3200.game.ui.UIComponent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.csse3200.game.components.settingsmenu.UserSettings;
/**
 * A UI component for displaying the Main menu.
 */
public class LoadingDisplay extends UIComponent {
    private static final Logger logger = LoggerFactory.getLogger(LoadingDisplay.class);
    private static final float Z_INDEX = 2f;
    private Table table;
    private ProgressBar progressBar;
    private Label loadingLabel;

    private float progress;
    public LoadingDisplay() {
        progress = 0;
    }

    @Override
    public void create() {
        super.create();
        addActors();
    }

    private void addActors() {
        table = new Table();
        table.setFillParent(true);

        loadingLabel = new Label("Loading...", skin);
        progressBar = new ProgressBar(0, 1, 0.01f, false, skin);
        progressBar.setValue(0);

        table.add(loadingLabel).expandX().padTop(50);
        table.row();
        table.add(progressBar).width(300).padTop(20);

        stage.addActor(table);
    }

    public boolean isLoadingFinished() {
        return progressBar.getValue() >= 1;
    }

    @Override
    public void update() {
        super.update();
        progress += 0.01f;
        System.out.println(progress);
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
