package com.coalesce.uhc.enchantments;

import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.PlayerDeathEvent;

import static com.coalesce.uhc.utilities.Statics.colour;

/**
 * Doesn't allow items to be dropped from the inventory and the item is destroyed upon death.
 */
public class Vanishment extends CustomEnchant {
    public Vanishment() {
        super(1500, colour("&cCurse of Vanishment"));
    }

    @EventHandler
    public void onDeath(PlayerDeathEvent event) {
        event.getDrops().forEach(cur -> {
            if(cur.containsEnchantment(this)) event.getDrops().remove(cur);
        });
    }
}
