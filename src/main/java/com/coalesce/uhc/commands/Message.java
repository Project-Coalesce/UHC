package com.coalesce.uhc.commands;

import com.coalesce.command.CommandContext;
import com.coalesce.command.CommandExecutor;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;

import static com.coalesce.uhc.utilities.Statics.colour;

public class Message implements CommandExecutor {
    @Override public void execute(CommandContext context) {
        if (!context.hasArgs() || context.getArgs().size() < 2) {
            context.send(colour("&cYou need to specify a receiver and a message."));
            return;
        }

        String targetString = context.getArgs().get(0);
        Player target = Bukkit.getPlayer(targetString);
        if (target == null) {
            context.send(colour("&cNobody was found with name '" + targetString + "'."));
            return;
        }

        if (context.getSender() instanceof Player && ((Player) context.getSender()).getGameMode() == GameMode.SPECTATOR &&
                target.getGameMode() == GameMode.SPECTATOR) {
            context.send(colour("&cGhosting is not allowed!"));
            return;

        }

        StringBuilder finalMessage = new StringBuilder();
        context.getArgs().subList(1, context.getArgs().size()).forEach(arg -> finalMessage.append(arg + " "));

        target.sendMessage(colour("&f<" + context.getSender().getName() + "&f -> " + target.getName() + "&f> " + finalMessage));
        context.send(colour("&f<" + context.getSender().getName() + "&f -> " + target.getName() + "&f> " + finalMessage));
    }
}
