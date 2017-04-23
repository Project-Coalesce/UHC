package com.coalesce.uhc.eventhandlers;

import com.coalesce.uhc.GameState;
import com.coalesce.uhc.UHC;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerPreLoginEvent;

import static com.coalesce.uhc.utilities.Statics.colour;

public class JoiningHandler implements Listener {
    @EventHandler
    public void prejoin(AsyncPlayerPreLoginEvent event){
        if (GameState.current() != GameState.LOBBY && UHC.getInstance().getMainConfig().isRoundBanDead()) {
            event.setKickMessage(colour("&cThe round has already started!"));
            event.setLoginResult(AsyncPlayerPreLoginEvent.Result.KICK_OTHER);
        }
    }
}
