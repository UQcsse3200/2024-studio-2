package com.csse3200.game.ui.pop_up_dialog_box;

import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

// Developed and Debugged with ChatGPT
class PopupDialogBoxTest {
    private PopupDialogBox dialogBox;
    private Stage stage;
    private Skin skin;
    private String[] titles = {"Title 1", "Title 2"};
    private String[] content = {"Content 1", "Content 2"};
    @BeforeEach
    void setUp() {
        stage = Mockito.mock(Stage.class);
        skin = Mockito.mock(Skin.class);

    }

    @Test
    void testPopUpDialogBoxDisplayed() {
    }
}