package com.coalesce.uhc.enchantments;

import org.bukkit.enchantments.EnchantmentTarget;

import static com.coalesce.uhc.utilities.Statics.colour;

public class Vanishment extends CustomEnchant {
    /**
     * Doesn't allow items to be dropped from the inventory and the item is destroyed upon death.
     */
    public Vanishment() {
        super(1500);
    }

    @Override
    public String getName() {
        return colour("Curse of Vanishment");
    }

    @Override
    public int getMaxLevel() {
        return 1;
    }

    @Override
    public EnchantmentTarget getItemTarget() {
        return EnchantmentTarget.ALL;
    }
}
