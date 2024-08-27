package com.csse3200.game.components.animal;

import com.badlogic.gdx.scenes.scene2d.EventListener;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.csse3200.game.GdxGame;
import com.csse3200.game.screens.LoadingScreen;
import com.csse3200.game.ui.PopUpDialogBox.PopUpHelper;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.junit.MockitoJUnitRunner;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class AnimalSelectionActionsTest {

    private AnimalSelectionDisplay mockDisplay;
    private PopUpHelper mockDialogHelper;
    private GdxGame mockGame;
    private AnimalSelectionActions actions;
    private Image mockImage1;
    private Image mockImage2;
    private Image mockImage3;
    private TextButton mockSelectButton;
    private TextButton mockBackButton;
    private Skin mockSkin;
    private Stage mockStage;

    @Before
    public void setUp() {
        // Mock dependencies
        mockDisplay = mock(AnimalSelectionDisplay.class);
        mockDialogHelper = mock(PopUpHelper.class);
        mockGame = mock(GdxGame.class);
        mockSkin = mock(Skin.class);
        mockStage = mock(Stage.class);

        // Mock Images and Buttons
        mockImage1 = mock(Image.class);
        mockImage2 = mock(Image.class);
        mockImage3 = mock(Image.class);
        mockSelectButton = mock(TextButton.class);
        mockBackButton = mock(TextButton.class);

        // Set up mockDisplay to return mock objects
        when(mockDisplay.getAnimalImages()).thenReturn(new Image[]{mockImage1, mockImage2, mockImage3});
        when(mockDisplay.getAnimalButtons()).thenReturn(new TextButton[]{mockSelectButton, mockSelectButton, mockSelectButton});
        when(mockDisplay.getSelectButton()).thenReturn(mockSelectButton);
        when(mockDisplay.getBackButton()).thenReturn(mockBackButton);
        when(mockDisplay.getSkin()).thenReturn(mockSkin);
        when(mockDisplay.getStage()).thenReturn(mockStage);

        // Create the instance of AnimalSelectionActions with mocked dependencies
        actions = new AnimalSelectionActions(mockDisplay, mockDialogHelper, mockGame);
    }

    @Test
    public void testSelectAnimal() {
        // Call selectAnimal and verify the image is highlighted
        actions.selectAnimal(mockImage1, "images/dog.png");

        // Verify the selected image is set to red
        verify(mockImage1).setColor(1, 0, 0, 1);

        // Verify that the selectedAnimalImagePath is updated correctly
        assertEquals("images/dog.png", AnimalSelectionActions.getSelectedAnimalImagePath());
    }

//    @Test
//    public void testSelectButtonClickedWithoutSelection() {
//        // No animal selected
//        AnimalSelectionActions.selectedAnimalImage = null;
//
//        // Create a mock InputEvent to simulate the button click
//        InputEvent mockEvent = mock(InputEvent.class);
//
//        // Create an instance of AnimalSelectionActions with mocked dependencies
//        AnimalSelectionActions actions = new AnimalSelectionActions(mockDisplay, mockDialogHelper, mockGame);
//
//        // Manually trigger the select button's click listener
//        actions.getDisplay().getSelectButton().getClickListener().clicked(mockEvent, 0, 0);
//
//        // Verify that the alert dialog is shown when no animal is selected
//        verify(mockDialogHelper).displayDialog(eq("Alert"), eq("Please select an animal first."), isNull(), anyFloat(), anyFloat());
//    }

//    @Test
//    public void testSelectButtonClickedWithSelection() {
//        // Simulate the state as if an animal was selected
//        String animalImagePath = "images/dog.png";
//        AnimalSelectionActions.selectedAnimalImage = mockImage1;
//        AnimalSelectionActions.selectedAnimalImagePath = animalImagePath;
//
//        // Directly invoke the functionality that should be triggered when the "Select" button is clicked
//        actions.getDisplay().getSelectButton().getClickListener().clicked(mock(InputEvent.class), 0, 0);
//
//        // Verify that the screen is changed to LoadingScreen if an animal is selected
//        verify(mockGame).setScreen(any(LoadingScreen.class));
//    }

    @Test
    public void testBackButtonClick() {
        // Simulate clicking the "Go Back" button
        ArgumentCaptor<ClickListener> captor = ArgumentCaptor.forClass(ClickListener.class);
        verify(mockBackButton, times(1)).addListener(captor.capture());

        ClickListener listener = captor.getValue();
        InputEvent mockEvent = mock(InputEvent.class);
        listener.clicked(mockEvent, 0, 0);

        // Verify that the game transitions back to the main menu
        verify(mockGame, times(1)).setScreen(GdxGame.ScreenType.MAIN_MENU);
    }

    @Test
    public void testShowAnimalDialog() {
        int animalIndex = 1; // Example animal index
        String expectedTitle = "Animal " + (animalIndex + 1);
        String expectedContent = "You've selected Animal " + (animalIndex + 1) + ".\n" +
                "This animal has unique characteristics.\n" +
                "It possesses special abilities.";

        // Call the method under test
        actions.showAnimalDialog(animalIndex, "images/animal" + animalIndex + ".png");

        // Capture the arguments passed to dialogHelper.displayDialog
        ArgumentCaptor<String> titleCaptor = ArgumentCaptor.forClass(String.class);
        ArgumentCaptor<String> contentCaptor = ArgumentCaptor.forClass(String.class);
        ArgumentCaptor<String> imagePathCaptor = ArgumentCaptor.forClass(String.class);
        ArgumentCaptor<Float> widthCaptor = ArgumentCaptor.forClass(Float.class);
        ArgumentCaptor<Float> heightCaptor = ArgumentCaptor.forClass(Float.class);

        // Verify that displayDialog was called with the expected arguments
        verify(mockDialogHelper).displayDialog(
                titleCaptor.capture(),
                contentCaptor.capture(),
                imagePathCaptor.capture(),
                widthCaptor.capture(),
                heightCaptor.capture()
        );

        // Assert that the captured values match the expected values
        assertEquals(expectedTitle, titleCaptor.getValue());
        assertEquals(expectedContent, contentCaptor.getValue());
        assertEquals("images/animal" + animalIndex + ".png", imagePathCaptor.getValue());
        assertEquals(900f, widthCaptor.getValue(), 0.01);
        assertEquals(400f, heightCaptor.getValue(), 0.01);
    }
}
