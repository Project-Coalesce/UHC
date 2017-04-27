package com.coalesce.uhc.commands;

import static com.coalesce.uhc.utilities.Statics.colour;

import com.coalesce.command.CommandContext;
import com.coalesce.uhc.GameState;
import com.coalesce.uhc.users.Participation;
import com.coalesce.uhc.users.User;
import com.coalesce.uhc.users.UserManager;
import org.bukkit.entity.Player;

import java.util.Optional;


public class Spectate {
    public void spectate(CommandContext context) {
        if (GameState.current() != GameState.LOBBY) {
            context.send(colour("&cThat command can only be executed before the game starts!"));
            return;
        }
        if (!(context.getSender() instanceof Player)) {
            context.send(colour("&cThat command can only be executed by a player."));
            return;
        }

        Player player = (Player) context.getSender();

        Optional<User> optionalUser = UserManager.getInstance().getUser(player.getUniqueId());
        User user = optionalUser.orElseGet(() -> new User(player, Participation.PARTICIPATOR));
        Participation participation = user.getParticipation() == Participation.PARTICIPATOR ? Participation.SPECTATOR : Participation.SPECTATOR;
        user.setParticipation(participation);
        context.send(colour("&bYou will start the game as a " + participation.toString().toLowerCase() + "."));
        UserManager.getInstance().addUser(user);
    }
}
