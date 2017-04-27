package com.coalesce.uhc.commands;

import com.coalesce.command.CommandContext;
import com.coalesce.uhc.users.Participation;
import com.coalesce.uhc.users.User;
import com.coalesce.uhc.users.UserManager;
import com.coalesce.uhc.utilities.Enums;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.Arrays;
import java.util.Optional;
import java.util.stream.Collectors;

import static com.coalesce.uhc.utilities.Statics.colour;

public class Assign {
    public void assign(CommandContext context) {
        if (UserManager.getInstance().getUser(context.asPlayer().getUniqueId())
                .orElseThrow(() -> new RuntimeException("An offline player executed a command.")).getParticipation() != Participation.ADMIN) {
            context.send(colour("&cInsufficient permissions."));
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
