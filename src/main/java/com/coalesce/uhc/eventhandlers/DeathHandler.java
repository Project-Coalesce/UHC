package com.coalesce.uhc.eventhandlers;

import static com.coalesce.uhc.utilities.Statics.colour;

import com.coalesce.chat.CoFormatter;
import com.coalesce.uhc.GameState;
import com.coalesce.uhc.UHC;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.metadata.FixedMetadataValue;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

public class DeathHandler implements Listener {
    @EventHandler public void playerDiedEvent(PlayerDeathEvent event) {
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

        CoFormatter formatter = new CoFormatter(UHC.getInstance());

        if (survivors.count() <= 1) {
            Player winner = survivors.findFirst().get();
            GameState.ENDED.setCurrent();
            Bukkit.getServer().getOnlinePlayers().forEach(player -> {
                List<String> strings = new ArrayList<>();
                strings.add("&6---[ Game over! ]---");
                strings.add("&b" + winner.getName() + "&a has won the game!");
                strings.add("&bThank you for participating!");

                strings.forEach(curs -> player.sendMessage(formatter.centerString(colour(curs))));
            });
        }

        event.getEntity().setMetadata("wasAlive", new FixedMetadataValue(UHC.getInstance(), "true"));
        event.getEntity().getWorld().strikeLightning(event.getEntity().getLocation());
    }
}
