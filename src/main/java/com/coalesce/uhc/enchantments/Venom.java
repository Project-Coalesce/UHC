package com.coalesce.uhc.enchantments;

import org.bukkit.Material;
import org.bukkit.enchantments.EnchantmentTarget;

import static com.coalesce.uhc.utilities.Statics.colour;

/**
 * Applies potion upon the attacked entity.
 */
public class Venom extends CustomEnchant {
    public Venom() {
        super(1501);
    }

    @Override
    public Material[] applyable() {
        return new Material[]{
                Material.DIAMOND_SWORD,
                Material.GOLD_SWORD,
                Material.IRON_SWORD,
                Material.STONE_SWORD,
                Material.WOOD_SWORD
        };
    }
}
