package com.coalesce.uhc.commands;

import com.coalesce.command.CommandContext;
import com.coalesce.uhc.UHC;
import com.coalesce.uhc.users.Participation;
import com.coalesce.uhc.users.UserManager;
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

    public Message() {
        lastMessaged = new HashMap<>();
        UHC.getInstance().registerListener(this);
    }

    @EventHandler
    public void leave(PlayerQuitEvent event) {
        if (lastMessaged.containsKey(event.getPlayer())) lastMessaged.remove(event.getPlayer());
    }

    public void reply(CommandContext context) {
        if (context.getArgs().size() < 1) {
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

        String finalMessage = String.join(" ", context.getArgs().subList(1, context.getArgs().size()));

        target.sendMessage(colour("&f<" + context.getSender().getName() + "&f -> Me&f> ") + finalMessage);
        context.send(colour("&f<Me -> " + target.getName() + "&f> ") + finalMessage);

        Bukkit.getOnlinePlayers().stream().filter(it -> UserManager.getInstance().getUser(it.getUniqueId()).orElseThrow(() -> new RuntimeException("An offline player is in Bukkit#getOnlinePlayers.")).getParticipation() == Participation.ADMIN)
                .forEach(it -> it.sendMessage(colour("&f<" + context.getSender().getName() + " -> " + target.getName() + "> ") + finalMessage));

        lastMessaged.put(context.getSender(), target);
    }
}
