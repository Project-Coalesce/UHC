package com.coalesce.uhc.customevents;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

@RequiredArgsConstructor
public class MessageSentEvent extends Event {
    private static HandlerList handlerList = new HandlerList();

    @Getter private final Player receiver;
    @Getter private final CommandSender sender;
    @Getter private final String message;

    public static HandlerList getHandlerList() {
        return handlerList;
    }

    @Override public HandlerList getHandlers() {
        return handlerList;
    }
}
