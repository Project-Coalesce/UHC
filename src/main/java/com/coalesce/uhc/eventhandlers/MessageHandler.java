package com.coalesce.uhc.eventhandlers;

import static com.coalesce.uhc.utilities.Statics.colour;

import com.coalesce.uhc.users.Participation;
import com.coalesce.uhc.users.User;
import com.coalesce.uhc.users.UserManager;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

import java.util.Optional;

public class MessageHandler implements Listener {
    @EventHandler
    public void onMessage(AsyncPlayerChatEvent event) {
        Optional<User> optionalUser = UserManager.getInstance().getUser(event.getPlayer().getUniqueId());
        if (!optionalUser.isPresent()) {
            event.setCancelled(true);
            event.getPlayer().sendMessage(colour("&cWe haven't loaded your profile yet, hold on..."));
            return;
        }

        Participation participation = optionalUser.get().getParticipation();
        event.setFormat(participation.getPrefix() + " %s: &f%s");
    }
}
