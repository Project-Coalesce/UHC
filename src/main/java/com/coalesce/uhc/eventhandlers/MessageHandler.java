package com.coalesce.uhc.eventhandlers;

import com.coalesce.uhc.GameState;
import com.coalesce.uhc.users.Participation;
import com.coalesce.uhc.users.User;
import com.coalesce.uhc.users.UserManager;
import org.bukkit.ChatColor;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

import java.util.Optional;

import static com.coalesce.uhc.utilities.Statics.colour;

public class MessageHandler implements Listener {
    @EventHandler
    public void onMessage(final AsyncPlayerChatEvent event) {
        Optional<User> optionalUser = UserManager.getInstance().getUser(event.getPlayer().getUniqueId());
        if (!optionalUser.isPresent()) {
            optionalUser = Optional.of(new User(event.getPlayer(), GameState.current() == GameState.STARTED ? Participation.SPECTATOR : Participation.PARTICIPATOR));
            UserManager.getInstance().addUser(optionalUser.get());
        }

        Participation participation = optionalUser.get().getParticipation();
        event.setFormat(colour(participation.getPrefix()) + "%s: " + ChatColor.WHITE + "%s");
    }
}
