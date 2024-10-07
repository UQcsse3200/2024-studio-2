package com.csse3200.game.ui.pop_up_dialog_box;


import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import static org.junit.Assert.assertNotNull;
class PopUpHelperTest {
    private Skin skin;

    private Stage stage;
    String title;
    String content;
    String animalImagePath;
    float width;
    float height;
    @BeforeEach
    void setUp() {
        skin = Mockito.mock(Skin.class);
        stage = Mockito.mock(Stage.class);
        title = "MockTitle";
        content = "MockContent";
        animalImagePath = "images/dog.png";
        width = 500;
        height = 600;
    }

    @AfterEach
    void tearDown() {
        // Clean up resources
        stage.dispose();
        skin.dispose();
    }
    @Test
    void testPopUpHelperInitialization() {
        PopUpHelper helper = new PopUpHelper(skin, stage);
        assertNotNull(helper);
    }
}