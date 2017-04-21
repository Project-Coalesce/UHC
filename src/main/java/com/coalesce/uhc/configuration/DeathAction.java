package com.coalesce.uhc.configuration;

import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;

public enum DeathAction {
    GAMEMODE() {
        @Override protected void handle(Player player) {
            if (player.isOp()) {
                player.sendMessage(ChatColor.RED + "You'll now be in spectator mode. Enter a player's POV if you want to punish them.");
            } else {
                player.sendMessage(ChatColor.RED + "You'll now be in spectator mode. Don't ghost or such!");
            }
            player.setGameMode(GameMode.SPECTATOR);
        }
    },
    BAN() {
        @Override protected void handle(Player player) {
            player.kickPlayer(ChatColor.RED + "You have been killed and are now round-banned.");
        }
    };

    public void handlePlayer(Player player) {
        // TODO: Check if player is ADMIN, if so, set to spectator no matter what.
        handle(player);
    }

    protected abstract void handle(Player player);
}
