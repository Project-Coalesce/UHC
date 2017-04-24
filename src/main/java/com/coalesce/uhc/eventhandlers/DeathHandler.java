package com.coalesce.uhc.eventhandlers;

import static com.coalesce.uhc.utilities.Statics.colour;

import com.coalesce.chat.CoFormatter;
import com.coalesce.uhc.GameState;
import com.coalesce.uhc.UHC;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.metadata.FixedMetadataValue;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;

public class DeathHandler implements Listener {
    @EventHandler
    public void playerDiedEvent(PlayerDeathEvent event) {
        if (GameState.current() == GameState.LOBBY || GameState.current() == GameState.ENDED) {
            return;
        }

        UHC.getInstance().getMainConfig().getDeathAction().handlePlayer(event.getEntity(), event.getDeathMessage());

        Stream<? extends Player> survivors = Bukkit.getServer().getOnlinePlayers().stream().filter(player -> player.getGameMode()
                .equals(GameMode.SPECTATOR));

        if (Bukkit.getServer().getOnlinePlayers().stream().allMatch(player -> player.getMetadata("wasAlive").isEmpty())) {
            event.setDeathMessage(colour("&e- First death: " + event.getDeathMessage()));

        } else {
            event.setDeathMessage(colour("&e- " + event.getDeathMessage()));
        }

        Bukkit.getServer().getOnlinePlayers().forEach(player -> player.sendMessage(colour("&cThere are " + survivors.count()
                + " men standing.")));

        if (survivors.count() <= 1) {
            Player winner = survivors.findFirst().get();
            GameState.ENDED.setCurrent();
            Bukkit.getServer().getOnlinePlayers().forEach(player -> {
                Arrays.asList(
                        "&e---[ Game over! ]---",
                        "&b" + winner.getName() + "&a has won the game!",
                        "&bThank you for participating!")
                    .forEach(curs -> player.sendMessage(UHC.getInstance().getFormatter().centerString(colour(curs))));
                player.playSound(player.getLocation(), Sound.BLOCK_ANVIL_BREAK, 1f, 1f);
            });
        }

        event.getEntity().setMetadata("wasAlive", new FixedMetadataValue(UHC.getInstance(), "true"));
        event.getEntity().getWorld().strikeLightning(event.getEntity().getLocation());
    }
}
