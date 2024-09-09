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
public class CombatButtonDisplay extends UIComponent {
    private static final Logger logger = LoggerFactory.getLogger(CombatExitDisplay.class);
    private static final float Z_INDEX = 2f;
    private Table table;
    private Screen screen;
    private ServiceContainer container;
    private int iHealthCheck =-100;
    TextButton AttackButton ;
    TextButton GuardButton ;
    TextButton SleepButton;
    private boolean AttackStatus=true;
    private boolean GuardStatus=true;


    public  CombatButtonDisplay(Screen screen, ServiceContainer container ) {
        this.screen = screen;
        this.container = container;
    }


    public  CombatButtonDisplay(Screen screen, ServiceContainer container,int iHealthCheck, boolean AttackStatus , boolean GuardStatus ) {
        this.iHealthCheck = iHealthCheck;
        logger.info("iHealthCheck: {}", iHealthCheck);
        this.AttackStatus = AttackStatus;
        this.GuardStatus = GuardStatus;
        //ChangeActors(iHealthCheck, AttackStatus, BoostStatus);
        logger.info("CombatButtonStagDisplay in const for change actor");
        create();
    }

    @Override
    public void create() {
        super.create();
        if(iHealthCheck== -100)
        {
            logger.info("CombatButtonStagDisplay::Create() , before calling addActors");
            addActors();
        }
        else {
            logger.info("CombatButtonStagDisplay::Create() , before calling ChangeActors");
            ChangeActors(iHealthCheck , AttackStatus, GuardStatus);
        }

    }

    private void addActors() {
        table = new Table();
        table.center().left();
        table.setFillParent(true);

        AttackButton = new TextButton("Attack", skin);
        GuardButton = new TextButton("Guard", skin);
        SleepButton = new TextButton("Sleep", skin);

        AttackButton.addListener(
                new ChangeListener() {
                    @Override
                    public void changed(ChangeEvent changeEvent, Actor actor) {
                        entity.getEvents().trigger("Attack", screen, container);
                    }
                });

        GuardButton.addListener(
                new ChangeListener() {
                    @Override
                    public void changed(ChangeEvent changeEvent, Actor actor) {
                        entity.getEvents().trigger("Guard", screen, container);
                    }
                });
        SleepButton.addListener(
                new ChangeListener() {
                    @Override
                    public void changed(ChangeEvent changeEvent, Actor actor) {
                        entity.getEvents().trigger("Sleep", screen, container);
                    }
                });

        table.add(AttackButton).padBottom(10f).padLeft(10f);
        table.row();
        table.add(GuardButton).padBottom(10f).padLeft(10f);
        table.row();
        table.add(SleepButton).padBottom(10f).padLeft(10f);

        stage.addActor(table);
    }
    private void ChangeActors(int iHealthCheck, boolean AttackStatus, boolean GuardStatus){
        logger.info("CombatButtonStagDisplay::ChangeActors::entering");
        //Button enabling status logic
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
