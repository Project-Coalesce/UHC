package com.coalesce.uhc.enchantments;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

/**
 * The sword gives twice the damage, but at the same time damages the damgee and the damager upon attack
 */
public class Reversion extends CustomEnchant {
    public Reversion() {
        super(1503);
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

    @EventHandler
    public void onEntityDamagedByEntity(EntityDamageByEntityEvent event) {
        if (event.getDamager() instanceof Player) {
            Player player = (Player) event.getDamager();

            if (player.getInventory().getItemInMainHand().containsEnchantment(this)) {
                player.damage(event.getDamage(), event.getEntity());
                event.setDamage(event.getDamage() * 2);
            }
        }
    }
}
