package com.csse3200.game.components.animal;

import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Dialog;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.csse3200.game.GdxGame;
import com.csse3200.game.screens.MainGameScreen;
import com.csse3200.game.ui.DialogueBox.DialogHelper;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class AnimalSelectionActionsTest {
    private AnimalSelectionDisplay display;
    private DialogHelper dialogHelper;
    private GdxGame game;
    private AnimalSelectionActions actions;

    @Before
    public void setup() {
        display = mock(AnimalSelectionDisplay.class);
        dialogHelper = mock(DialogHelper.class);
        game = mock(GdxGame.class);

        actions = new AnimalSelectionActions(display, dialogHelper, game);
    }

    @Test
    public void testAnimalImageClick() {
        Image mockImage = mock(Image.class);
        when(display.getAnimalImages()).thenReturn(new Image[]{mockImage, mockImage, mockImage});
        when(display.getAnimalButtons()).thenReturn(new TextButton[]{mock(TextButton.class), mock(TextButton.class), mock(TextButton.class)});

        actions = new AnimalSelectionActions(display, dialogHelper, game);

        // Trigger click listener
        ArgumentCaptor<ClickListener> clickListenerCaptor = ArgumentCaptor.forClass(ClickListener.class);
        verify(mockImage, times(3)).addListener(clickListenerCaptor.capture());
        clickListenerCaptor.getAllValues().get(0).clicked(mock(InputEvent.class), 0, 0);

        // Verify methods called upon click
        verify(mockImage).setColor(1, 0, 0, 1);
        verify(dialogHelper).displayDialog(any(String[].class), any(String[].class));
    }

    @Test
    public void testSelectButtonClickWithoutSelection() {
        TextButton selectButton = mock(TextButton.class);
        when(display.getSelectButton()).thenReturn(selectButton);

        actions = new AnimalSelectionActions(display, dialogHelper, game);

        ArgumentCaptor<ClickListener> clickListenerCaptor = ArgumentCaptor.forClass(ClickListener.class);
        verify(selectButton).addListener(clickListenerCaptor.capture());
        clickListenerCaptor.getValue().clicked(mock(InputEvent.class), 0, 0);

        // Verify that the selection alert is shown when no animal is selected
        verify(display).getStage();
        verify(dialogHelper, never()).displayDialog(any(), any());

    }

    @Test
    public void testSelectButtonClickWithSelection() {
        TextButton selectButton = mock(TextButton.class);
        Image selectedImage = mock(Image.class);
        when(display.getSelectButton()).thenReturn(selectButton);
        when(display.getAnimalImages()).thenReturn(new Image[]{selectedImage});

        actions = new AnimalSelectionActions(display, dialogHelper, game);

        // Simulate animal selection
        actions.selectAnimal(selectedImage);

        // Simulate select button click
        ArgumentCaptor<ClickListener> clickListenerCaptor = ArgumentCaptor.forClass(ClickListener.class);
        verify(selectButton).addListener(clickListenerCaptor.capture());
        clickListenerCaptor.getValue().clicked(mock(InputEvent.class), 0, 0);

        // Verify that the screen transition happens
        verify(game).setScreen(any(MainGameScreen.class));
    }

    @Test
    public void testBackButtonClick() {
        TextButton backButton = mock(TextButton.class);
        when(display.getBackButton()).thenReturn(backButton);

        actions = new AnimalSelectionActions(display, dialogHelper, game);

        // Simulate back button click
        ArgumentCaptor<ClickListener> clickListenerCaptor = ArgumentCaptor.forClass(ClickListener.class);
        verify(backButton).addListener(clickListenerCaptor.capture());
        clickListenerCaptor.getValue().clicked(mock(InputEvent.class), 0, 0);

        // Verify that the screen transitions back to the main menu
        verify(game).setScreen(GdxGame.ScreenType.MAIN_MENU);
    }
}
