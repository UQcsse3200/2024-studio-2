package com.csse3200.game.entities.configs;

public class HealingPotionsEntityConfig extends BaseEntityItemConfig{
    public String healingPotionName = "Healing Potions";
    public String[] healingPotionDescription = {"This is a healing potion, to pick up press P"};

    @Override
    public String getItemName() {
        return healingPotionName;
    }

    @Override
    public String[] getItemDescription() {
        return healingPotionDescription;
    }
}