package com.coalesce.uhc.enchantments;

import com.coalesce.uhc.customevents.ArmourEquipEvent;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.enchantments.EnchantmentTarget;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

import java.util.Arrays;

public abstract class CustomEnchant extends Enchantment {
    private final String name;

    public CustomEnchant(final int id, final String name) {
        super(id);
        this.name = name;
    }

    @Override
    public String getName() {
        return this.name;
    }

    @Override
    public int getStartLevel() { // Not final due to override-able
        return 1;
    }

    @Override
    public int getMaxLevel() {
        return 1;
    }

    @Override
    public EnchantmentTarget getItemTarget() {
        return EnchantmentTarget.ALL;
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
        return canEnchantItem(item, false);
    }

    /**
     * Checks whether or not the item can be enchanted with this enchantment.
     *
     * @param item  The item to check.
     * @param force Whether or not to ignore conflicts.
     * @return Whether or not the enchantment is applicable.
     */
    @SuppressWarnings("WeakerAccess")
    public boolean canEnchantItem(ItemStack item, boolean force) {
        if (!force) {
            for (Enchantment enchant : item.getEnchantments().keySet()) {
                if (conflictsWith(enchant)) {
                    return false;
                }
            }
        }
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

    public boolean itemClicked(ItemStack item, InventoryClickEvent event) {
        return false;
    }

    public boolean itemDropped(Item drop, Player dropper, PlayerDropItemEvent event) {
        return false;
    }

    public boolean unequipArmour(ArmourEquipEvent event) {
        return false;
    }

    public boolean equipArmour(ArmourEquipEvent event) {
        return false;
    }

    public boolean brokenArmour(ArmourEquipEvent event) {
        return false;
    }

    public boolean deathArmour(ArmourEquipEvent event) {
        return false;
    }

    public boolean attackedEntity(Player attacker, Entity what, EntityDamageByEntityEvent event) {
        return false;
    }

    public boolean rightClicked(ItemStack item, PlayerInteractEvent event) {
        return false;
    }
}
