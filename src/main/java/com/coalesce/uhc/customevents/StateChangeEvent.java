package com.coalesce.uhc.customevents;

import com.coalesce.uhc.GameState;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

@RequiredArgsConstructor // Makes constructor wanting from and to.
public class StateChangeEvent extends Event {
    private static HandlerList handlerList = new HandlerList();

    @Getter private final GameState from, to;

    public static HandlerList getHandlerList() {
        return handlerList;
    }

    @Override public HandlerList getHandlers() {
        return handlerList;
    }
}
