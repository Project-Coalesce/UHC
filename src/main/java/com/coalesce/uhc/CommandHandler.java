package com.coalesce.uhc;

import com.coalesce.chat.CoFormatter;
import com.coalesce.command.CoCommand;
import com.coalesce.command.CommandBuilder;
import com.coalesce.command.CommandContext;
import com.coalesce.plugin.CoPlugin;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

import static com.coalesce.uhc.utilities.Statics.colour;

public class CommandHandler {
    private CoFormatter formatter;

    public CommandHandler(CoPlugin plugin) {
        formatter = new CoFormatter(plugin);

        List<CoCommand> commands = new ArrayList<>();

        commands.add(new CommandBuilder(plugin, "Private Message").aliases("pm", "m", "w", "whisper", "msg", "tell").executor
                (this::messageCommand).build());
        commands.add(new CommandBuilder(plugin, "Game Start").aliases("start", "begin").executor(this::gameStartCommand).build());

        commands.forEach(plugin::addCommand);
    }

    public void gameStartCommand(CommandContext context) {
        if (!context.getSender().isOp()) {
            context.send(colour("&cInsufficient permissions."));
            return;
        }

        if (!(context.getSender() instanceof Player)){
            context.send(colour("&cThis command is only accessible for players."));
            return;
        }

        if (GameState.current() != GameState.LOBBY) {
            context.send(colour("&cAlready started."));
            return;
        }

        GameState.setGameWorld(((Player) context.getSender()).getWorld());
        GameState.STARTING.setCurrent();
        GameState.STARTED.setCurrent();
        Bukkit.getServer().getOnlinePlayers().forEach(player -> {
            List<String> strings = new ArrayList<>();
            strings.add("&6---[ And the game begins! ]---");
            strings.add("&bThere'll be a 10 minute grace period.");
            strings.add("&bAttacking other players during that period is illegal.");
            strings.add("&bIn 2 minutes, the world border will start to shrink.");

            strings.forEach(curs -> player.sendMessage(formatter.centerString(colour(curs))));
        });
    }

    public void messageCommand(CommandContext context) {
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
