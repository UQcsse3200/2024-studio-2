package com.csse3200.game.inventory.items;

import com.csse3200.game.components.CombatStatsComponent;
import com.csse3200.game.entities.Entity;
import com.csse3200.game.services.GameTime;
import com.csse3200.game.services.ServiceLocator;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import com.csse3200.game.inventory.items.food.Foods.Apple;
import com.csse3200.game.inventory.items.food.Foods.Candy;
import com.csse3200.game.inventory.items.food.Foods.Meat;
import com.csse3200.game.inventory.items.food.Foods.Carrot;
import com.csse3200.game.inventory.items.food.Foods.ChickenLeg;


import static org.junit.jupiter.api.Assertions.*;
public class FoodItemTest {
    private Apple apple;
    private Candy candy;
    private Meat meat;
    private Carrot carrot;
    private ChickenLeg chickenLeg;
    private CombatStatsComponent stat;
    private FoodItemTest.TestablePLayer player1;
    private GameTime gameTime;
    private static final long DURATION = 120000;

    private static class TestablePLayer extends ItemUsageContext {
        public TestablePLayer(Entity entity) {
            super(entity);
        }
    }

    @BeforeEach
    void setUp() {
        int maxHealth = 200;
        int maxHunger = 100;

        stat = new CombatStatsComponent(100, 50,0,0,0,0, true);
        player1 = new FoodItemTest.TestablePLayer(new Entity().addComponent(stat));
        gameTime = Mockito.mock(GameTime.class);
        stat.setHealth(0);
        ServiceLocator.registerTimeSource(gameTime);

        apple = new Apple(1);
        candy = new Candy(1);
        meat = new Meat(1);
        carrot = new Carrot(1);
        chickenLeg = new ChickenLeg(1);

        Mockito.when(gameTime.getTime()).thenReturn(System.currentTimeMillis());
    }

    @AfterEach
    void tearDown() {
        ServiceLocator.clear();
    }

    @Test
    void testApple() {
        int originalHunger = player1.player.getComponent(CombatStatsComponent.class).getHunger();
        assertEquals(originalHunger, 50);
        apple.useItem(player1);
        assertEquals(0, apple.getQuantity(), "No more Apples.");
        //assertEquals(originalHunger, player1.player.getComponent(CombatStatsComponent.class).getHunger(), "Should be 25");
        assertTrue(apple.isEmpty(), "No more Apples.");
    }

    @Test
    void testCandy() {
        int originalHunger = player1.player.getComponent(CombatStatsComponent.class).getHunger();

        candy.useItem(player1);
        assertEquals(50, player1.player.getComponent(CombatStatsComponent.class).getHunger());

        assertEquals(0, candy.getQuantity(), "No more Candy.");
        assertTrue(candy.isEmpty(), "No more Candy.");
    }

    @Test
    void testMeat() {
        meat.useItem(player1);
        assertEquals(0, meat.getQuantity(), "No more Meat.");
        assertTrue(meat.isEmpty(), "No more Meat.");
    }

    @Test
    void testCarrot() {
        carrot.useItem(player1);
        assertEquals(0, carrot.getQuantity(), "No more Carrots.");
        assertTrue(carrot.isEmpty(), "No more Carrots.");
    }

    @Test
    void testChickenLeg() {
        chickenLeg.useItem(player1);
        assertEquals(0, chickenLeg.getQuantity(), "No more Chicken Legs.");
        assertTrue(chickenLeg.isEmpty(), "No more Chicken Legs.");
    }
}
