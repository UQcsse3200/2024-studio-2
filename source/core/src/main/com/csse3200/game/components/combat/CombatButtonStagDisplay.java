package com.csse3200.game.components.combat;

import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.csse3200.game.services.ServiceContainer;
import com.csse3200.game.ui.UIComponent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Displays a button to exit the Main Game screen to the Main Menu screen.
 */
public class CombatButtonStagDisplay extends UIComponent {
    private static final Logger logger = LoggerFactory.getLogger(CombatExitDisplay.class);
    private static final float Z_INDEX = 2f;
    private Table table;
    private Screen screen;
    private ServiceContainer container;
    private int iHealthCheck = 0;
    TextButton AttackButton = new TextButton("Attack", skin);
    TextButton BoostButton = new TextButton("Boost", skin);
    private boolean AttackStatus=true;
    private boolean BoostStatus=true;


    public  CombatButtonStagDisplay(Screen screen, ServiceContainer container ) {
        this.screen = screen;
        this.container = container;
    }


    public  CombatButtonStagDisplay(int iHealthCheck, boolean AttackStatus , boolean BoostStatus ) {
        this.iHealthCheck = iHealthCheck;
        logger.info("iHealthCheck: " + iHealthCheck);
        this.AttackStatus = AttackStatus;
        this.BoostStatus = BoostStatus;
        ChangeActors(iHealthCheck, AttackStatus, BoostStatus);
        logger.info("CombatButtonStagDisplay in const for change actor");
    }


    @Override
    public void render(float delta) {

    }

    @Override
    public void resize(int width, int height) {

    }

    @Override
    public void create() {
        super.create();
        if(iHealthCheck== -100)
        {
            addActors();
        }
        else {
            ChangeActors(iHealthCheck , AttackStatus, BoostStatus);
        }

    }

    private void addActors() {
        table = new Table();
        table.top().right();
        table.setFillParent(true);

//        TextButton AttackButton = new TextButton("Attack", skin);
//        TextButton BoostButton = new TextButton("Boost", skin);

        AttackButton.addListener(
                new ChangeListener() {
                    @Override
                    public void changed(ChangeEvent changeEvent, Actor actor) {
                        entity.getEvents().trigger("Attack", screen, container);
                    }
                });

        BoostButton.addListener(
                new ChangeListener() {
                    @Override
                    public void changed(ChangeEvent changeEvent, Actor actor) {
                        entity.getEvents().trigger("Boast", screen, container);
                    }
                });

        table.add(AttackButton).padTop(10f).padRight(10f);
        table.add(BoostButton).padTop(10f).padRight(10f);

        stage.addActor(table);
    }
    private void ChangeActors(int iHealthCheck, boolean AttackStatus, boolean BoostStatus){
        logger.info("CombatButtonStagDisplay::ChangeActors::entering");
        this.AttackButton.setVisible(AttackStatus);
        this.BoostButton.setVisible(BoostStatus);
        logger.info("CombatButtonStagDisplay::ChangeActors::ending ");
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
