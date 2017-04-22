package com.coalesce.uhc.eventhandlers;

import static com.coalesce.uhc.utilities.TimerWrapper.schedule;
import static com.coalesce.uhc.utilities.Statics.colour;

import com.coalesce.uhc.GameState;
import com.coalesce.uhc.UHC;
import com.coalesce.uhc.configuration.MainConfiguration;
import com.coalesce.uhc.customevents.StateChangeEvent;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.WorldBorder;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import java.util.concurrent.TimeUnit;

public class GameInitializeHandler implements Listener {
    @EventHandler public void gameInitialize(StateChangeEvent initStateHandle) {
        if(initStateHandle.getTo() != GameState.STARTING) return;

        //Initialize Everything
        World world = GameState.getGameWorld();
        world.setPVP(false);

        WorldBorder border = world.getWorldBorder();
        border.setCenter(new Location(world, 0, 0, 0, 0f, 0f));
        border.setSize(UHC.getInstance().getMainConfig().getWorldBorderInitialSize());

        //TODO Teleport everyone

        schedule(this::shrink, TimeUnit.MILLISECONDS.convert(UHC.getInstance().getMainConfig().getWorldBorderStartShrinkingMinutes(), TimeUnit.MINUTES));
        schedule(this::gracePeriodEnd, TimeUnit.MILLISECONDS.convert(UHC.getInstance().getMainConfig().getGracePeriodMinutes(), TimeUnit.MINUTES));
    }

    private void shrink() {
        MainConfiguration config = UHC.getInstance().getMainConfig();
        GameState.getGameWorld().getWorldBorder().setSize(config.getWorldBorderFinalShrinkSize(),
                TimeUnit.MILLISECONDS.convert(config.getWorldBorderStartShrinkingMinutes(), TimeUnit.MINUTES));
        Bukkit.getOnlinePlayers().forEach(player -> player.sendMessage(colour("&6The border has started to shrink!")));
    }

    private void gracePeriodEnd() {
        GameState.getGameWorld().setPVP(true);
        Bukkit.getOnlinePlayers().forEach(player -> player.sendMessage(colour("&cThe grace period has ended, PvP is now allowed!")));
    }
}
