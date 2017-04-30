package com.coalesce.uhc.enchantments;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityShootBowEvent;

/**
 * Only potion arrows can be fired
 */
public class Magic extends CustomEnchant {
    public Magic() {
        super(1502);
    }

    @EventHandler
    public void onShoot(EntityShootBowEvent event) {
        if (event.getBow().containsEnchantment(this) && event.getEntity() instanceof Player) {
            //TODO Finish this
        }
    }
}