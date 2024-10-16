package com.csse3200.game.components.animal;

import com.badlogic.gdx.scenes.scene2d.Stage;
import com.csse3200.game.GdxGame;
import com.csse3200.game.components.Component;
import com.csse3200.game.ui.pop_up_dialog_box.PopUpHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;


public class AnimalRouletteActions extends Component {
    private static final Logger logger = LoggerFactory.getLogger(AnimalRouletteActions.class);
    private final GdxGame game;
    private AnimalRouletteDisplay rouletteDisplay;
    private PopUpHelper popUpHelper;
    private Skin skin;
    private Stage stage;

    public AnimalRouletteActions(GdxGame game) {
        this.game = game;
    }

    @Override
    public void create() {
        super.create();
        this.rouletteDisplay = entity.getComponent(AnimalRouletteDisplay.class);
        this.popUpHelper = new PopUpHelper(skin,stage);

        entity.getEvents().addListener("selectAnimal", this::onSelectAnimal);
    }

    private void onSelectAnimal() {
        String selectedAnimal = rouletteDisplay.getSelectedAnimal();
        int animalIndex = rouletteDisplay.getCurrentAnimalIndex();
        logger.info("Animal selected: {}", selectedAnimal);

        // Display dialog box for the selected animal
        popUpHelper.displayDialog(
                "Animal Stats",
                "Here are the stats for " + selectedAnimal,
                "images/animals/" + selectedAnimal,
                400,
                300,
                animalIndex
        );
    }
}