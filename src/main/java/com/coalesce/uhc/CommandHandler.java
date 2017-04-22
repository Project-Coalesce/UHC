package com.coalesce.uhc;

import com.coalesce.chat.CoFormatter;
import com.coalesce.command.CoCommand;
import com.coalesce.command.CommandBuilder;
import com.coalesce.command.CommandContext;
import com.coalesce.plugin.CoPlugin;
import com.coalesce.uhc.users.Participation;
import com.coalesce.uhc.users.User;
import com.coalesce.uhc.users.UserManager;
import com.coalesce.uhc.utilities.Conditionals;
import com.coalesce.uhc.utilities.Enums;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static com.coalesce.uhc.utilities.Statics.colour;

public class CommandHandler {
    private CoFormatter formatter;

    public CommandHandler(CoPlugin plugin) {
        formatter = new CoFormatter(plugin);

        List<CoCommand> commands = new ArrayList<>();

        commands.add(new CommandBuilder(plugin, "Private Message").aliases("pm", "m", "w", "whisper", "msg", "tell").executor
                (this::messageCommand).build());
        commands.add(new CommandBuilder(plugin, "Game Start").aliases("start", "begin").executor(this::gameStartCommand).build());
        commands.add(new CommandBuilder(plugin, "Assign Participation").aliases("rank", "setrank", "participate").executor(this::assignCommand).build());

        commands.forEach(plugin::addCommand);
    }

    public void gameStartCommand(CommandContext context) {
        if (UserManager.getInstance().getUser(context.asPlayer().getUniqueId())
                .orElseThrow(() -> new RuntimeException("An offline player executed a command.")).getParticipation() != Participation.ADMIN) {
            context.send(colour("&cInsufficient permissions."));
            return;
        }

        if (!(context.getSender() instanceof Player)) {
            context.send(colour("&cThis command is only accessible for players."));
            return;
        }

        if (GameState.current() != GameState.LOBBY) {
            context.send(colour("&cAlready started."));
            return;
        }

        GameState.setGameWorld(((Player) context.getSender()).getWorld());
        GameState.STARTING.setCurrent();

        Bukkit.getServer().getOnlinePlayers().forEach(player -> {
            List<String> strings = new ArrayList<>();
            strings.add("&6---[ And the game begins! ]---");
            strings.add("&bThere'll be a 10 minute grace period.");
            strings.add("&bAttacking other players during that period is illegal.");
            strings.add("&bIn 2 minutes, the world border will start to shrink.");

            strings.forEach(curs -> player.sendMessage(formatter.centerString(colour(curs))));
        });
    }

    public void messageCommand(CommandContext context) { // TODO: Add replies.
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
    }

    private void assignCommand(CommandContext context) {
        if (context.getArgs().size() != 2) {
            context.send(colour("&cYou need to specify a user and a participation to assign."));
            return;
        }

        String targetString = context.getArgs().get(0);
        Player target = Bukkit.getPlayer(targetString);
        if (target == null) {
            context.send(colour("&cNobody was found with name '" + targetString + "'."));
            return;
        }

        Optional<Participation> optionalParticipation = Enums.getEnum(Participation.class, context.getArgs().get(1));
        if (!optionalParticipation.isPresent()) {
            context.send(colour("&cYou need to specify one of the following values: " + String.join(", ", Arrays.stream(Participation.values()).map(Enum::name).map(String::toLowerCase).collect(Collectors.toList()))));
            return;
        }

        Optional<User> optionalUser = UserManager.getInstance().getUser(target.getUniqueId());
        User user = optionalUser.orElseGet(() -> new User(target, optionalParticipation.get()));
        user.setParticipation(optionalParticipation.get());
        UserManager.getInstance().addUser(user);

        context.send(colour("&a" + targetString + " was assigned the participation " + user.getParticipation().name().toLowerCase() + '!'));
    }
}
