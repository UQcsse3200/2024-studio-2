package com.csse3200.game.combat;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.csse3200.game.components.combat.CombatButtonDisplay;
import com.csse3200.game.services.ServiceContainer;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.lang.reflect.Field;

import static org.junit.jupiter.api.Assertions.*;
        import static org.mockito.Mockito.verify;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

class CombatButtonDisplayTest {
    public CombatButtonDisplay combatButtonDisplay;
    public Screen screen;
    public ServiceContainer container;
    public Stage stage;
    public Skin skin;

    @BeforeEach
    void setUp() {
        screen = Mockito.mock(Screen.class);
        container = Mockito.mock(ServiceContainer.class);
        stage = Mockito.mock(Stage.class);
        skin = Mockito.mock(Skin.class);

        // Set up CombatButtonDisplay with mocked dependencies
        combatButtonDisplay = new CombatButtonDisplay(screen, container);

        // Use reflection to access private fields like 'stage' and 'skin'
        try {
            Field stageField = CombatButtonDisplay.class.getDeclaredField("stage");
            stageField.setAccessible(true);
            stageField.set(combatButtonDisplay, stage);

            Field skinField = CombatButtonDisplay.class.getDeclaredField("skin");
            skinField.setAccessible(true);
            skinField.set(combatButtonDisplay, skin);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    @Test
    void testConstructorInitialization() {
        assertNotNull(combatButtonDisplay);
        assertEquals(screen, getPrivateField(combatButtonDisplay, "screen"));
        assertEquals(container, getPrivateField(combatButtonDisplay, "container"));
    }

    @Test
    void testButtonCreationAndAdditionToStage() {
        // Call the method that adds actors using reflection
        invokePrivateMethod(combatButtonDisplay, "addActors");

        // Use reflection to verify buttons are created
        assertNotNull(getPrivateField(combatButtonDisplay, "AttackButton"));
        assertNotNull(getPrivateField(combatButtonDisplay, "GuardButton"));
        assertNotNull(getPrivateField(combatButtonDisplay, "SleepButton"));
        assertNotNull(getPrivateField(combatButtonDisplay, "ItemsButton"));

        // Verify the buttons are added to the stage
        verify(stage).addActor(Mockito.any());  // Check if actors are added to the stage
    }

    // Helper method to access private fields via reflection
    private Object getPrivateField(Object obj, String fieldName) {
        try {
            Field field = obj.getClass().getDeclaredField(fieldName);
            field.setAccessible(true);
            return field.get(obj);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    // Helper method to invoke private methods via reflection
    private void invokePrivateMethod(Object obj, String methodName) {
        try {
            Method method = obj.getClass().getDeclaredMethod(methodName);
            method.setAccessible(true);
            method.invoke(obj);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
