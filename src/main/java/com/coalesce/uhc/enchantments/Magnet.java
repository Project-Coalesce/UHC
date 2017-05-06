package com.coalesce.uhc.enchantments;

import org.bukkit.enchantments.EnchantmentTarget;

/**
 * Attracts entities every so slightly
 */
public class Magnet extends CustomEnchant {
    public Magnet() {
        super(1505, "Magnet");
    }

    @Override
    public int getMaxLevel() {
        return 3;
    }

    @Override
    public EnchantmentTarget getItemTarget() {
        return EnchantmentTarget.ARMOR;
    }
}
