//package com.csse3200.game.components.player;
//import com.badlogic.gdx.Files;
//import com.badlogic.gdx.Gdx;
//import com.badlogic.gdx.backends.headless.mock.graphics.MockGraphics;
//import com.badlogic.gdx.files.FileHandle;
//import com.badlogic.gdx.graphics.Texture;
//import com.badlogic.gdx.graphics.g2d.Animation;
//import com.badlogic.gdx.graphics.g2d.TextureAtlas;
//import com.badlogic.gdx.graphics.g2d.TextureRegion;
//import com.badlogic.gdx.scenes.scene2d.Stage;
//import com.badlogic.gdx.scenes.scene2d.ui.Image;
//import com.badlogic.gdx.scenes.scene2d.ui.Label;
//import com.badlogic.gdx.scenes.scene2d.ui.Skin;
//import com.badlogic.gdx.scenes.scene2d.ui.Table;
//import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle;
//import com.csse3200.game.components.CombatStatsComponent;
//import com.csse3200.game.components.player.PlayerStatsDisplay;
//import com.csse3200.game.services.ResourceService;
//import com.csse3200.game.ui.UIComponent;
//import org.junit.Before;
//import org.junit.Test;
//import org.mockito.Mockito;
//import com.csse3200.game.services.ServiceLocator;
//
//
//import com.csse3200.game.entities.Entity;
//
//import static com.csse3200.game.components.player.PlayerStatsDisplay.totalFrames;
//import static org.junit.Assert.*;
//import static org.mockito.ArgumentMatchers.anyString;
//import static org.mockito.ArgumentMatchers.eq;
//import static org.mockito.Mockito.*;
//
//public class PlayerStatsDisplayTest {
//
//    private PlayerStatsDisplay playerStatsDisplay;
//    private TextureAtlas mockHealthAtlas;
//    private TextureAtlas mockHungerAtlas;
//    private TextureAtlas mockXpAtlas;
//    private TextureRegion[] mockHealthRegions;
//    private TextureRegion[] mockHungerRegions;
//    private TextureRegion[] mockXpRegions;
//    private Label healthLabel;
//    private Label hungerLabel;
//    private Label experienceLabel;
//    private Image healthImage;
//    private Image hungerImage;
//    private Image xpImage;
//    private Animation<TextureRegion> healthBarAnimation;
//    private Animation<TextureRegion> hungerBarAnimation;
//    private Animation<TextureRegion> xpBarAnimation;
//    private Stage stage;
//    private CombatStatsComponent combatStatsComponent;
//    private Skin skin;
//
//
//    @Before
//    public void setUpForCreate() {
//        // Mock Gdx.files and other Gdx static fields
//        Gdx.files = mock(Files.class);
//        Gdx.graphics = new MockGraphics();
//
//        // Mock FileHandle for the skin JSON file
//        FileHandle mockSkinFileHandle = mock(FileHandle.class);
//        FileHandle mockAtlasFileHandle = mock(FileHandle.class);
//
//        // Mock the behavior of the FileHandle methods
//        when(Gdx.files.internal("flat-earth/skin/flat-earth-ui.json")).thenReturn(mockSkinFileHandle);
//        when(mockSkinFileHandle.sibling("flat-earth/skin/flat-earth-ui.atlas")).thenReturn(mockAtlasFileHandle);
//        when(mockAtlasFileHandle.exists()).thenReturn(true);
//
//        // Mock Skin initialization
//        skin = mock(Skin.class);
//        when(skin.get("large", LabelStyle.class)).thenReturn(mock(LabelStyle.class));
//
//        // Mock dependencies
//        stage = mock(Stage.class);
//
//        // Mock ResourceService to return the mocked skin
//        ResourceService resourceService = mock(ResourceService.class);
//        when(resourceService.getAsset(anyString(), eq(Skin.class)))
//                .thenReturn(skin);
//        when(resourceService.getAsset(anyString(), eq(Texture.class)))
//                .thenReturn(mock(Texture.class));
//
//        // Register the mocked ResourceService with the ServiceLocator
//        ServiceLocator.registerResourceService(resourceService);
//
//        playerStatsDisplay = new PlayerStatsDisplay();
//        playerStatsDisplay.stage = stage;
//        playerStatsDisplay.skin = skin;
//
//        // Mock entity and components
//        Entity entity = mock(Entity.class);
//        combatStatsComponent = mock(CombatStatsComponent.class);
//        when(entity.getComponent(CombatStatsComponent.class)).thenReturn(combatStatsComponent);
//        when(combatStatsComponent.getHealth()).thenReturn(100); // Example health value
//
//        playerStatsDisplay.setEntity(entity);
//
//        LabelStyle labelStyle = mock(LabelStyle.class);
//        when(skin.get("large", LabelStyle.class)).thenReturn(labelStyle);
//    }
//    @Before
//    public void setUpForTestInitBarAnimations() {
//        playerStatsDisplay = new PlayerStatsDisplay();
//
//        // Mock the TextureAtlas and TextureRegion
//        mockHealthAtlas = mock(TextureAtlas.class);
//        mockHungerAtlas = mock(TextureAtlas.class);
//        mockXpAtlas = mock(TextureAtlas.class);
//
//        mockHealthRegions = new TextureRegion[11];
//        mockHungerRegions = new TextureRegion[11];
//        mockXpRegions = new TextureRegion[11];
//
//
//        // Initialize mock regions
//        for (int i = 0; i < 11; i++) {
//            mockHealthRegions[i] = mock(TextureRegion.class);
//            mockHungerRegions[i] = mock(TextureRegion.class);
//            mockXpRegions[i] = mock(TextureRegion.class);
//
//            // Mock the behavior for finding regions in the atlas
//            when(mockHealthAtlas.findRegion((100 - i * 10) + "%_health")).thenReturn((TextureAtlas.AtlasRegion)mockHealthRegions[i]);
//            when(mockHungerAtlas.findRegion((100 - i * 10) + "%_hunger")).thenReturn((TextureAtlas.AtlasRegion)mockHungerRegions[i]);
//            when(mockXpAtlas.findRegion((i * 10) + "%_xp")).thenReturn((TextureAtlas.AtlasRegion) mockXpRegions[i]);
//        }
//        CombatStatsComponent mockCombatStatsComponent = mock(CombatStatsComponent.class);
//
//        // Mock Labels
//        healthLabel = mock(Label.class);
//        hungerLabel = mock(Label.class);
//        experienceLabel = mock(Label.class);
//
//        // Mock Images
//        healthImage = mock(Image.class);
//        hungerImage = mock(Image.class);
//        xpImage = mock(Image.class);
//
//        // Mock Animations
//        healthBarAnimation = mock(Animation.class);
//        hungerBarAnimation = mock(Animation.class);
//        xpBarAnimation = mock(Animation.class);
//
//        playerStatsDisplay.healthLabel = healthLabel;
//        playerStatsDisplay.hungerLabel = hungerLabel;
//        playerStatsDisplay.experienceLabel = experienceLabel;
//        playerStatsDisplay.healthImage = healthImage;
//        playerStatsDisplay.hungerImage = hungerImage;
//        playerStatsDisplay.xpImage = xpImage;
//        playerStatsDisplay.healthBarAnimation = healthBarAnimation;
//        playerStatsDisplay.hungerBarAnimation = hungerBarAnimation;
//        playerStatsDisplay.xpBarAnimation = xpBarAnimation;
//
//        // Mock CombatStatsComponent methods
//        when(mockCombatStatsComponent.getMaxHealth()).thenReturn(100);
//        when(mockCombatStatsComponent.getMaxHunger()).thenReturn(100);
//        when(mockCombatStatsComponent.getMaxExperience()).thenReturn(100);
//    }
//
//    @Test
//    public void testCreate() {
//        // Call the create method
//        playerStatsDisplay.create();
//
//        // Verify that actors were added to the stage
//        verify(stage).addActor(Mockito.any(Table.class));
//
//        // Check that the labels and images have the correct values
//        Label healthLabel = playerStatsDisplay.healthLabel;
//        assertEquals("HP: 100", healthLabel.getText().toString());
//
//        Label xpLabel = playerStatsDisplay.xpLabel;
//        assertEquals("EXP: 100", xpLabel.getText().toString());
//
//        Label hungerLabel = playerStatsDisplay.hungerLabel;
//        assertEquals("HGR: 100", hungerLabel.getText().toString());
//
//        Image heartImage = playerStatsDisplay.healthImage;
//        Image xpImage = playerStatsDisplay.xpImage;
//        Image hungerImage = playerStatsDisplay.hungerImage;
//        assertNotNull(heartImage);
//        assertNotNull(xpImage);
//        assertNotNull(hungerImage);
//    }
//    @Test
//    public void testInitBarAnimations() {
//        // Initialize the animations with mock TextureAtlas
//        playerStatsDisplay.initBarAnimations();
//
//        // Verify findRegion calls for health bar
//        for (int i = 0; i < 11; i++) {
//            verify(mockHealthAtlas).findRegion((100 - i * 10) + "%_health");
//        }
//
//        // Verify findRegion calls for hunger bar
//        for (int i = 0; i < 11; i++) {
//            verify(mockHungerAtlas).findRegion((100 - i * 10) + "%_hunger");
//        }
//
//        // Verify findRegion calls for XP bar
//        for (int i = 0; i < 11; i++) {
//            verify(mockXpAtlas).findRegion((i * 10) + "%_xp");
//        }
//
//        // Check that the animations are properly initialized
//        assertNotNull(playerStatsDisplay.healthBarAnimation);
//        assertNotNull(playerStatsDisplay.hungerBarAnimation);
//        assertNotNull(playerStatsDisplay.xpBarAnimation);
//
//        // Check the frame duration for the animations
//        assertEquals(0.066f, playerStatsDisplay.healthBarAnimation.getFrameDuration(), 0.001f);
//        assertEquals(0.066f, playerStatsDisplay.hungerBarAnimation.getFrameDuration(), 0.001f);
//        assertEquals(0.066f, playerStatsDisplay.xpBarAnimation.getFrameDuration(), 0.001f);
//    }
//    @Test
//    public void testUpdatePlayerHealthUI() {
//        int health = 75;
//        playerStatsDisplay.updatePlayerHealthUI(health);
//
//        // Verify label text
//        verify(healthLabel).setText("HP: 75");
//
//        // Verify animation frame index
//        int expectedFrameIndex = totalFrames - 1 - (int) ((float) health / 100 * (totalFrames - 1));
//        verify(playerStatsDisplay).setNewFrame(eq(expectedFrameIndex), eq(healthBarAnimation), eq(healthImage));
//    }
//
//    @Test
//    public void testUpdatePlayerHungerUI() {
//        int hunger = 50;
//        playerStatsDisplay.updatePlayerHungerUI(hunger);
//
//        // Verify label text
//        verify(hungerLabel).setText("HGR: 50");
//
//        // Verify animation frame index
//        int expectedFrameIndex = totalFrames - 1 - (int) ((float) hunger / 100 * (totalFrames - 1));
//        verify(playerStatsDisplay).setNewFrame(eq(expectedFrameIndex), eq(hungerBarAnimation), eq(hungerImage));
//    }
//
//    @Test
//    public void testUpdatePlayerExperienceUI() {
//        int experience = 25;
//        playerStatsDisplay.updatePlayerExperienceUI(experience);
//
//        // Verify label text
//        verify(experienceLabel).setText("EXP: 25");
//
//        // Verify animation frame index
//        int expectedFrameIndex = totalFrames - 1 - (int) ((float) experience / 100 * (totalFrames - 1));
//        verify(playerStatsDisplay).setNewFrame(eq(expectedFrameIndex), eq(xpBarAnimation), eq(xpImage));
//    }
//}
//
