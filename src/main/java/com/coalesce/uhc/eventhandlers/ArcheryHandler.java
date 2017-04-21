package com.coalesce.uhc.eventhandlers;

import com.coalesce.uhc.GameState;
import org.bukkit.ChatColor;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;

import java.math.BigDecimal;

/**
 * Created by deprilula28 on 21/04/2017.
 */
public class ArcheryHandler implements Listener {
    @EventHandler public void playerShotEvent(EntityDamageByEntityEvent event) {
        if (GameState.current() == GameState.LOBBY || GameState.current() == GameState.ENDED) {
            return;
        }

        if (event.getCause() == EntityDamageEvent.DamageCause.PROJECTILE && event.getDamager() instanceof Arrow &&
                ((Arrow) event.getDamager()).getShooter() instanceof Player) {
            Player shot = (Player) event.getEntity();
            Player shooter = (Player) ((Arrow) event.getDamager()).getShooter();
            shooter.sendMessage(ChatColor.GRAY + "➵ " + ChatColor.AQUA + shot.getName() + " " + ChatColor.RED +
                new BigDecimal(shot.getHealth() / 2).setScale(1, BigDecimal.ROUND_HALF_UP));
        }
    }
}
