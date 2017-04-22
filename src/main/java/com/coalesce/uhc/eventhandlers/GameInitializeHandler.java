package com.coalesce.uhc.eventhandlers;

import com.coalesce.uhc.GameState;
import com.coalesce.uhc.customevents.StateChangeEvent;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

public class GameInitializeHandler implements Listener {
    @EventHandler public void gameInitialize(StateChangeEvent initStateHandle) {
        if(initStateHandle.getTo() != GameState.STARTING) return;

        //Initialize Everything

    }
}
