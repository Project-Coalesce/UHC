package com.coalesce.uhc.eventhandlers;

import com.coalesce.uhc.GameState;
import com.coalesce.uhc.UHC;
import com.coalesce.uhc.configuration.MainConfiguration;
import com.coalesce.uhc.customevents.StateChangeEvent;
import com.coalesce.uhc.utilities.Blocks;
import org.bukkit.*;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.TimeUnit;
import java.util.stream.Stream;

import static com.coalesce.uhc.utilities.Statics.colour;
import static com.coalesce.uhc.utilities.TimerWrapper.schedule;

public class GameInitializeHandler implements Listener {
    @EventHandler
    public void gameInitialize(StateChangeEvent initStateHandle) {
        if (initStateHandle.getTo() != GameState.STARTING) return;

        //Initialize Everything
        World world = GameState.getGameWorld();
        world.setPVP(false);

        WorldBorder border = world.getWorldBorder();
        border.setCenter(new Location(world, 0, 0, 0, 0f, 0f));
        border.setSize(UHC.getInstance().getMainConfig().getWorldBorderInitialSize());

        Bukkit.getOnlinePlayers().forEach(player -> player.sendMessage(colour("&bWe are teleporting players, please wait!")));

        spread();
        shrink();

        schedule(this::gracePeriodEnd, TimeUnit.MILLISECONDS.convert(UHC.getInstance().getMainConfig().getGracePeriodMinutes(), TimeUnit.MINUTES));
    }

    private void spread() {
        World world = GameState.getGameWorld();
        ThreadLocalRandom random = ThreadLocalRandom.current();
        int spreadSize = UHC.getInstance().getMainConfig().getWorldBorderInitialSize();
        Stream<? extends Player> players = Bukkit.getServer().getOnlinePlayers().stream().filter(player -> !player.getGameMode().equals(GameMode.SPECTATOR));

        players.forEach(player -> player.teleport(generateSafe(spreadSize, world, random)));
    }

    private Location generateSafe(int size, World world, Random random) {
        Location where;
        int x, z, y;
        int max = size / 2;
        int min = Math.abs(size / 2);
        do {
            x = random.nextInt(max + min) - min;
            z = random.nextInt(max + min) - min;
            y = world.getHighestBlockAt(x, z).getY();
            where = new Location(world, x, y, z);
        } while (!Blocks.isSafe(where.getBlock().getType()));
        return where;
    }

    private void shrink() {
        MainConfiguration config = UHC.getInstance().getMainConfig();
        GameState.getGameWorld().getWorldBorder().setSize(config.getWorldBorderFinalShrinkSize(),
                TimeUnit.SECONDS.convert(config.getWorldBorderShrinkTime(), TimeUnit.MINUTES));
        Bukkit.getOnlinePlayers().forEach(player -> player.sendMessage(colour("&6The border will shrink until it reaches " + config.getWorldBorderFinalShrinkSize() + " in each direction.")));
    }

    @EventHandler public void playerAttackInGrace(EntityDamageByEntityEvent event) {
        if (event.getDamager() instanceof Player && event.getEntity() instanceof Player) {
            if (GameState.current() != GameState.STARTED) {
                event.setCancelled(true);
                event.getDamager().sendMessage(colour("&cPvP is currently disabled."));
            }
        }
    }

    private void gracePeriodEnd() {
        GameState.getGameWorld().setPVP(true);
        Bukkit.getOnlinePlayers().forEach(player -> {
            player.sendMessage(colour("&cThe grace period has ended, PvP is now allowed!"));
            player.playSound(player.getLocation(), Sound.ENTITY_ENDERDRAGON_GROWL, 1f, 1f);;
        });
        GameState.STARTED.setCurrent();
    }
}
