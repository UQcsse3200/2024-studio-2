package com.csse3200.game.inventory.items.lootbox.configs;

import com.csse3200.game.inventory.items.food.Foods;
import com.csse3200.game.inventory.items.potions.AttackPotion;
import com.csse3200.game.inventory.items.potions.DefensePotion;
import com.csse3200.game.inventory.items.potions.HealingPotion;
import com.csse3200.game.inventory.items.potions.SpeedPotion;

public class LateGameLootTable extends BaseLootTable {

    public LateGameLootTable() {
        addItem(HealingPotion.class, 100.0, new Class<?>[]{int.class}, new Object[]{1});
        addItem(Foods.Apple.class, 10.0, new Class<?>[]{int.class}, new Object[]{1});
        addItem(Foods.Carrot.class, 20.0, new Class<?>[]{int.class}, new Object[]{1});
        addItem(Foods.Meat.class, 30.0, new Class<?>[]{int.class}, new Object[]{1});
        addItem(Foods.ChickenLeg.class, 40.0, new Class<?>[]{int.class}, new Object[]{1});
        addItem(Foods.Candy.class, 60.0, new Class<?>[]{int.class}, new Object[]{1});
        addItem(AttackPotion.class, 70.0, new Class<?>[]{int.class}, new Object[]{1});
        addItem(DefensePotion.class, 70.0, new Class<?>[]{int.class}, new Object[]{1});
        addItem(SpeedPotion.class, 70.0, new Class<?>[]{int.class}, new Object[]{1});
    }
}
