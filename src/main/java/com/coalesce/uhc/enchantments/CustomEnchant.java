package com.coalesce.uhc.enchantments;

import lombok.Setter;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;

import java.util.Arrays;

public abstract class CustomEnchant extends Enchantment {
    @Setter private int startLevel = 0;
    @Setter private boolean treasure = false;
    @Setter private boolean cursed = false;

    public CustomEnchant(int id) {
        super(id);
    }

    @Override
    public int getStartLevel() { // Not final due to override-able
        return startLevel;
    }

    @Override
    public boolean isTreasure() { // Not final due to override-able
        return treasure;
    }

    @Override
    public boolean isCursed() { // Not final due to override-able
        return cursed;
    }

    @Override
    public boolean conflictsWith(Enchantment other) { // Not final due to override-able
        return false;
    }

    @Override
    public boolean canEnchantItem(ItemStack item) { // Not final due to override-able
        return getItemTarget().includes(item.getType()) && Arrays.asList(applyable()).contains(item.getType());
    }

    /**
     * Returns the materials the enchantment is able to be applied to.
     *
     * @return The materials this can be applied to.
     */
    public Material[] applyable() { // Should be used for e.g. Curse of Venom which is only for swords.
        return Material.values();
    }

    /**
     * Returns the enchantments the item has to have before allowing this to be offered and actually enchanted.
     *
     * @return The enchantments required.
     */
    public Enchantment[] requirements() {
        return new Enchantment[0];
    }
}
