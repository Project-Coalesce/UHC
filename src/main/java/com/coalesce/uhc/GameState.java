package com.coalesce.uhc;

import com.coalesce.uhc.customevents.StateChangeEvent;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.Bukkit;
import org.bukkit.World;

public enum GameState {
    LOBBY,
    STARTING,
    STARTED,
    ENDED;

    @Getter @Setter private static World gameWorld;
    private static GameState current = LOBBY;

    public static GameState current() {
        return current;
    }

    public void setCurrent() {
        Bukkit.getPluginManager().callEvent(new StateChangeEvent(current, this));
        current = this;
    }
}
