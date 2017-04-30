package com.coalesce.uhc.enchantments;

import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.PlayerDeathEvent;
/**
 * Doesn't allow items to be dropped from the inventory and the item is destroyed upon death.
 */
public class Vanishment extends CustomEnchant {
    public Vanishment() {
        super(1500);
    }

    @EventHandler
    public void onDeath(PlayerDeathEvent event) {
        event.getDrops().forEach(cur -> {
            if(cur.containsEnchantment(this)) event.getDrops().remove(cur);
        });
    }
}
