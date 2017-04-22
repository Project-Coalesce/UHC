package com.coalesce.uhc;

import com.coalesce.uhc.customevents.StateChangeEvent;
import org.bukkit.Bukkit;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.World;

public enum GameState {
    /**
     * While in lobby this is the game state which will be used.
     */
    LOBBY,
    /**
     * This is the game state which will be used from start until grace period ends.
     */
    STARTING,
    /**
     * In this period PvP is enabled and any player may win.
     * If someone is absent for 8 minutes, they're killed and broadcasted where they died.
     * Absent is here meant as offline.
     */
    STARTED,
    /**
     * A player won and everyone are put in spectator mode.
     */
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
