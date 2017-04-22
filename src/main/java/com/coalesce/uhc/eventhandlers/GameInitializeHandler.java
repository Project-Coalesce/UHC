package com.coalesce.uhc.eventhandlers;

import com.coalesce.uhc.GameState;
import com.coalesce.uhc.UHC;
import com.coalesce.uhc.customevents.StateChangeEvent;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.WorldBorder;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

public class GameInitializeHandler implements Listener {
    @EventHandler public void gameInitialize(StateChangeEvent initStateHandle) {
        if(initStateHandle.getTo() != GameState.STARTING) return;

        //Initialize Everything
        World world = GameState.getGameWorld();
        WorldBorder border = world.getWorldBorder();
        border.setCenter(new Location(world, 0, 0, 0, 0f, 0f));
        border.setSize(UHC.getInstance().getMainConfig().getWorldBorderInitialSize());

        //TODO Teleport everyone
    }
}
