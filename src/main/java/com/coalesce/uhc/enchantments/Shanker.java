package com.coalesce.uhc.enchantments;

import org.bukkit.Material;
import org.bukkit.enchantments.EnchantmentTarget;
import org.bukkit.entity.Item;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerDropItemEvent;

import java.util.Arrays;

/**
 * Ability to throw swords at entities
 */
public class Shanker extends CustomEnchant {
    //TODO Damage players upon impact before you're able to pick the item up
    public Shanker() {
        super(1504, "Shanker");
    }

    @Override
    public EnchantmentTarget getItemTarget() {
        return EnchantmentTarget.ARMOR;
    }

    @EventHandler
    public void onDrop(PlayerDropItemEvent event) {
        if (Arrays.stream(event.getPlayer().getInventory().getArmorContents()).anyMatch(armor -> armor.containsEnchantment(this)) && isSword(event.getItemDrop())) {
            event.getItemDrop().setVelocity(event.getPlayer().getLocation().getDirection().multiply(2d));
            event.getItemDrop().setPickupDelay(150);
        }
    }

    private boolean isSword(Item item) { //TODO Make this better
        Material mat = item.getItemStack().getType();
        return mat.equals(Material.WOOD_SWORD) || mat.equals(Material.GOLD_SWORD) || mat.equals(Material.STONE_SWORD) || mat.equals(Material.IRON_SWORD) ||
                mat.equals(Material.DIAMOND_SWORD);
    }
}
