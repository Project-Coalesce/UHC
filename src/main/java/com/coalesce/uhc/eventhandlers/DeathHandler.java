package com.coalesce.uhc.eventhandlers;

import com.coalesce.uhc.GameState;
import com.coalesce.uhc.UHC;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.metadata.FixedMetadataValue;

public class DeathHandler implements Listener {
    @EventHandler public void playerDiedEvent(PlayerDeathEvent event) {
        if (GameState.current() == GameState.LOBBY || GameState.current() == GameState.ENDED) {
            return;
        }

        UHC.getInstance().getMainConfig().getDeathAction().handlePlayer(event.getEntity());

        if (Bukkit.getServer().getOnlinePlayers().stream().allMatch(player -> player.getMetadata("wasAlive").isEmpty())) {
            event.setDeathMessage(ChatColor.YELLOW + " - First death: " + event.getDeathMessage());
        } else {
            event.setDeathMessage(ChatColor.YELLOW + "- " + event.getDeathMessage());
        }
        event.getEntity().setMetadata("wasAlive", new FixedMetadataValue(UHC.getInstance(), "true"));
    }
}
