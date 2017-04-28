package com.coalesce.uhc.enchantments;

import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;

public abstract class CustomEnchant extends Enchantment {
    public CustomEnchant(int id) {
        super(id);
    }

    @Override
    public int getStartLevel() { // Not final due to override-able
        return 0;
    }

    @Override
    public boolean isTreasure() { // Not final due to override-able
        return false;
    }

    @Override
    public boolean isCursed() { // Not final due to override-able
        return false;
    }

    @Override
    public boolean conflictsWith(Enchantment other) { // Not final due to override-able
        return false;
    }

    @Override
    public boolean canEnchantItem(ItemStack item) { // Not final due to override-able
        return false;
    }

    public Material[] applyable() { // Should be used for e.g. Curse of Venom which is only for swords.
        return Material.values();
    }

    public Enchantment[] requirements() {
        return new Enchantment[0];
    }
}
