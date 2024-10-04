//package com.csse3200.game.areas;
//
//import static org.junit.jupiter.api.Assertions.assertEquals;
//import java.util.List;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.junit.jupiter.api.extension.ExtendWith;
//import com.badlogic.gdx.math.Vector2;
//import com.csse3200.game.entities.Entity;
//import com.csse3200.game.extensions.GameExtension;
//
//@ExtendWith(GameExtension.class)
//public class MiniMapDisplayTest {
//    private MiniMapDisplay miniMapDisplay;
//    private GameArea gameArea;
//    private Entity player;
//    private List<Entity> enemies;
//    private Entity enemy1;
//    private Entity enemy2;
//
//    @BeforeEach
//    public void setUp() {
//        miniMapDisplay = new MiniMapDisplay();
//        player = miniMapDisplay.getPlayer();
//        player.setPosition(100, 100);
//        enemy1 = new Entity();
//        enemy1.setPosition(new Vector2(0, 0));
//        enemy2 = new Entity();
//        enemy2.setPosition(new Vector2(200, 200));
//        miniMapDisplay.miniMapX = 0;
//        miniMapDisplay.miniMapY = 0;
//        miniMapDisplay.miniMapSize = 100;
//    }
//    @Test
//    public void testPlayerMiniMapPos() {
//        Vector2 playerMiniMapPos = miniMapDisplay.transferToMiniMapPos(player);
//        assertEquals(new Vector2(miniMapDisplay.miniMapSize/2, miniMapDisplay.miniMapSize/2), playerMiniMapPos);
//    }
//
//    @Test
//    public void testEnemyOneMiniMapPos() {
//        Vector2 enemyOneMiniMapPos = miniMapDisplay.transferToMiniMapPos(enemy1);
//        assertEquals(new Vector2(-150, -150), enemyOneMiniMapPos);
//    }
//
//
//    @Test
//    public void testEnemyTwoMiniMapPos() {
//        Vector2 enemyTwoMiniMapPos = miniMapDisplay.transferToMiniMapPos(enemy2);
//        assertEquals(new Vector2(250, 250), enemyTwoMiniMapPos);
//    }
//}
