package com.coalesce.uhc.commands;

import com.coalesce.command.CommandContext;
import com.coalesce.command.CommandExecutor;

import static com.coalesce.uhc.utilities.Statics.colour;

public class Message implements CommandExecutor {
    @Override public void execute(CommandContext context) {
        if (!context.hasArgs()){
            context.send(colour("&cYou need to specify a receiver and a message."));
            return;
        }

    }
}
