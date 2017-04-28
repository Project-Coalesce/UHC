package com.coalesce.uhc.enchantments;

import org.bukkit.Material;
import org.bukkit.enchantments.EnchantmentTarget;

import static com.coalesce.uhc.utilities.Statics.colour;

public class Venom extends CustomEnchant {
    /**
     * Applies potion upon the attacked entity.
     */
    public Venom() {
        super(1501);
    }

    @Override
    public String getName() {
        return colour("&cCurse of Venom");
    }

    @Override
    public int getMaxLevel() {
        return 3;
    }

    @Override
    public EnchantmentTarget getItemTarget() {
        return EnchantmentTarget.ALL;
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
