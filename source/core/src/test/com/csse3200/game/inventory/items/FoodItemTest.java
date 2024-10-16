package com.csse3200.game.inventory.items;

import com.csse3200.game.components.CombatStatsComponent;
import com.csse3200.game.entities.Entity;
import com.csse3200.game.extensions.GameExtension;
import com.csse3200.game.inventory.items.food.AbstractFood;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import com.csse3200.game.inventory.items.food.Foods;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(GameExtension.class)
class FoodItemTest {
    private Entity player;
    private ItemUsageContext context;

    @BeforeEach
    void setUp() {
        // Initialise stats component with hunger half full:
        int maxHunger = 100;
        CombatStatsComponent stat = new CombatStatsComponent(0, maxHunger, 0, 0, 0, 0,true, false, 1);
        stat.setHunger(50);

        // Create a sample player to test:
        player = new Entity().addComponent(stat);
        context = new ItemUsageContext(player);
    }

    void helperTestFood(AbstractFood food) {
        int originalHunger = player.getComponent(CombatStatsComponent.class).getHunger();
        assertEquals(50, originalHunger);
        food.useItem(context);
        int newHunger = player.getComponent(CombatStatsComponent.class).getHunger();
        assertTrue(newHunger > originalHunger, "Hunger did not increase when food was used!");
    }

    @Test
    void testApple() {
        Foods.Apple apple = new Foods.Apple(1);
        helperTestFood(apple);
    }

    @Test
    void testCandy() {
        Foods.Candy candy = new Foods.Candy(1);
        helperTestFood(candy);
    }

    @Test
    void testMeat() {
        Foods.Meat meat = new Foods.Meat(1);
        helperTestFood(meat);
    }

    @Test
    void testCarrot() {
        Foods.Carrot carrot = new Foods.Carrot(1);
        helperTestFood(carrot);
    }

    @Test
    void testChickenLeg() {
        Foods.ChickenLeg chickenLeg = new Foods.ChickenLeg(1);
        helperTestFood(chickenLeg);
    }

    @Test
    void testMilk() {
        Foods.Milk milk = new Foods.Milk(1);
        helperTestFood(milk);
    }

    @Test
    void testCaviar() {
        Foods.Sushi sushi = new Foods.Sushi(1);
        helperTestFood(sushi);
    }
}
