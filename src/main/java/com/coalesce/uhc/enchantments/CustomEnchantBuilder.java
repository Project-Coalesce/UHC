package com.coalesce.uhc.enchantments;

import org.bukkit.enchantments.EnchantmentTarget;

public class CustomEnchantBuilder{
    private CustomEnchant enchant;

    public CustomEnchantBuilder(CustomEnchant enchant) {

        this.enchant = enchant;

    }

    public CustomEnchantBuilder maxLevel(int level) {
        enchant.setMaxLevel(level);
        return this;
    }

    public CustomEnchantBuilder treasure(boolean treasure) {
        enchant.setTreasure(treasure);
        return this;
    }

    public CustomEnchantBuilder cursed(boolean cursed) {
        enchant.setCursed(cursed);
        return this;
    }

    public CustomEnchantBuilder name(String name) {
        enchant.setName(name);
        return this;
    }

    public CustomEnchantBuilder itemTarget(EnchantmentTarget itemTarget) {
        enchant.setItemTarget(itemTarget);
        return this;
    }

    public CustomEnchant build() {
        return enchant;
    }
}
