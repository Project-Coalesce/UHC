package com.coalesce.uhc.enchantments;

import org.bukkit.enchantments.EnchantmentTarget;

import static com.coalesce.uhc.utilities.Statics.colour;
/**
 * Doesn't allow items to be dropped from the inventory and the item is destroyed upon death.
 */
public class Vanishment extends CustomEnchant {
    public Vanishment() {
        super(1500);
    }

    @Override
    public EnchantmentTarget getItemTarget() {
        return EnchantmentTarget.ALL;
    }
}
