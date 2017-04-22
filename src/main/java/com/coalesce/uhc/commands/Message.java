package com.coalesce.uhc.commands;

import com.coalesce.command.CommandContext;
import com.coalesce.command.CommandExecutor;
import com.coalesce.uhc.UHC;
import com.coalesce.uhc.utilities.Conditionals;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

import java.util.HashMap;
import java.util.Map;

import static com.coalesce.uhc.utilities.Statics.colour;

public class Message implements Listener {
    private Map<CommandSender, Player> lastMessaged;

    public Message(){
        lastMessaged = new HashMap<>();
        UHC.getInstance().registerListener(this);
    }

    @EventHandler public void leave(PlayerQuitEvent event) {
        if(lastMessaged.containsKey(event.getPlayer())) lastMessaged.remove(event.getPlayer());
    }

    public void reply(CommandContext context) {
        if (context.getArgs().size() < 1){
            context.send(colour("&cYou need to specify a message."));
            return;
        }

        if (!lastMessaged.containsKey(context.getSender())) {
            context.send(colour("&cYou haven't messaged anybody lately.."));
            return;
        }

        StringBuilder finalMessage = new StringBuilder();
        context.getArgs().forEach(arg -> finalMessage.append(arg + " "));

        Player target = lastMessaged.get(context.getSender());
        target.sendMessage(colour("&f<" + context.getSender().getName() + "&f -> " + target.getName() + "&f> " + finalMessage));
        context.send(colour("&f<" + context.getSender().getName() + "&f -> " + target.getName() + "&f> " + finalMessage));
    }

    public void message(CommandContext context) {
        if (context.getArgs().size() < 2) { // Doesn't need to call hasArgs as it always is a list.
            context.send(colour("&cYou need to specify a receiver and a message."));
            return;
        }

        String targetString = context.getArgs().get(0);
        Player target = Bukkit.getPlayer(targetString);
        if (target == null) {
            context.send(colour("&cNobody was found with name '" + targetString + "'."));
            return;
        }

        if (context.getSender() instanceof Player &&
                Conditionals.ofBoth(((Player) context.getSender()).getGameMode(), target.getGameMode(), GameMode.SURVIVAL, GameMode.SPECTATOR)) {
            context.send(colour("&cGhosting is not allowed!"));
            return;
        }

        StringBuilder finalMessage = new StringBuilder();
        context.getArgs().subList(1, context.getArgs().size()).forEach(arg -> finalMessage.append(arg + " "));

        target.sendMessage(colour("&f<" + context.getSender().getName() + "&f -> " + target.getName() + "&f> " + finalMessage));
        context.send(colour("&f<" + context.getSender().getName() + "&f -> " + target.getName() + "&f> " + finalMessage));

        lastMessaged.put(context.getSender(), target);
    }
}
