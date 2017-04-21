package com.coalesce.uhc;

import com.coalesce.uhc.customevents.StateChangeEvent;
import org.bukkit.Bukkit;

public enum GameState {
    LOBBY,
    STARTING,
    STARTED,
    ENDED;

    private static GameState current = LOBBY;

    public static GameState current() {
        return current;
    }

    public void setCurrent() {
        Bukkit.getPluginManager().callEvent(new StateChangeEvent(current, this));
        current = this;
    }
}
