package com.coalesce.uhc.eventhandlers;

import static com.coalesce.uhc.utilities.Statics.colour;
import static com.coalesce.uhc.utilities.TimerWrapper.schedule;

import com.coalesce.chat.CoFormatter;
import com.coalesce.gui.ItemBuilder;
import com.coalesce.uhc.GameState;
import com.coalesce.uhc.UHC;
import com.coalesce.uhc.utilities.PlayerheadItemStack;
import org.bukkit.*;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.metadata.FixedMetadataValue;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.TimeUnit;
import java.util.stream.Stream;

public class DeathHandler implements Listener {
    @EventHandler
    public void playerDiedEvent(final PlayerDeathEvent event) {
        if (GameState.current() == GameState.LOBBY || GameState.current() == GameState.ENDED) {
            return;
        }

        UHC.getInstance().getMainConfig().getDeathAction().handlePlayer(event.getEntity(), event.getDeathMessage());

        long survivors = Bukkit.getServer().getOnlinePlayers().stream().filter(player -> player.getGameMode() != GameMode.SPECTATOR).count() - 1;

        if (Bukkit.getServer().getOnlinePlayers().stream().allMatch(player -> player.getMetadata("wasAlive").isEmpty())) {
            event.setDeathMessage(colour("&6First death: " + event.getDeathMessage() + "\n&bThere are " + survivors + " players left."));
        } else {
            event.setDeathMessage(colour("&e" + event.getDeathMessage() + "\n&bThere are " + survivors + " players left."));
        }

        if (survivors <= 1) {
            Player winner = Bukkit.getServer().getOnlinePlayers().stream().filter(player -> player.getGameMode() != GameMode.SPECTATOR).findFirst().get();
            GameState.ENDED.setCurrent();
            onGameEnd(winner);
        }

        event.getEntity().setMetadata("wasAlive", new FixedMetadataValue(UHC.getInstance(), "true"));
        event.getEntity().getWorld().strikeLightning(event.getEntity().getLocation());
        event.getDrops().add(new PlayerheadItemStack(new ItemBuilder(Material.SKULL_ITEM).displayName(colour("&a" + event.getEntity().getName() + "'s head")).build(), false,
                event.getEntity().getName()));
        //TODO: Wait until the skull builder is in master to use that
    }

    public static void onGameEnd(Player winner) {
        Bukkit.getServer().getOnlinePlayers().forEach(player -> {
            Arrays.asList(
                    "&e---[ Game over! ]---",
                    "&b" + winner.getName() + "&a has won the game!",
                    "&bThank you for participating!")
                    .forEach(curs -> player.sendMessage(UHC.getInstance().getFormatter().centerString(colour(curs))));
            player.playSound(player.getLocation(), Sound.BLOCK_ANVIL_BREAK, 1f, 1f);
            player.teleport(winner);
        });
        GameState.getGameWorld().getWorldBorder().setSize(30000000);

        int extraSeconds = UHC.getInstance().getMainConfig().getGameEndExtraSeconds();
        if(extraSeconds > 0) schedule(() -> serverClose(winner), TimeUnit.MILLISECONDS.convert(extraSeconds, TimeUnit.SECONDS));
    }

    public static void serverClose(Player winner) {
        Bukkit.getServer().getOnlinePlayers().forEach(player -> player.kickPlayer(colour(
                "&e---[ Game over! ]---\n" +
                "&b" + winner.getName() + "&a has won the game\n" +
                "&bThank you for participating!"
        )));
        Bukkit.getServer().shutdown();
    }
}
