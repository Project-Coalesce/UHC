package com.coalesce.uhc.commands;

import com.coalesce.command.CommandContext;
import com.coalesce.command.CommandExecutor;
import com.coalesce.uhc.GameState;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;

import static com.coalesce.uhc.utilities.Statics.colour;

public class GameStartCommand implements CommandExecutor {
    @Override public void execute(CommandContext context) {
        if (!context.getSender().isOp()) {
            context.send(colour("&cInsufficient permissions."));
            return;
        }
        if (!context.getSender().isOp()) {
            context.send(colour("&cAccess denied."));
            return;
        }

        GameState.STARTING.setCurrent();
        //TODO Initialize stuff
        GameState.STARTED.setCurrent();
        Bukkit.getServer().getOnlinePlayers().forEach(player -> player.sendMessage(ChatColor.GREEN + ""));
    }
}
