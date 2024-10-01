package com.csse3200.game.ui.PopUpDialogBox;

import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import static org.junit.jupiter.api.Assertions.*;

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