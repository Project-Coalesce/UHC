package com.coalesce.uhc.configuration;

import com.coalesce.uhc.users.Participation;
import com.coalesce.uhc.users.User;
import com.coalesce.uhc.users.UserManager;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;

import java.util.Optional;

import static com.coalesce.uhc.utilities.Statics.colour;

public enum DeathAction {
    GAMEMODE() {
        @Override protected void handle(Player player, String deathMesasge) {
            if (UserManager.getInstance().getUser(player.getUniqueId()).orElse(UserManager.getInstance().getEmpty()).getParticipation() == Participation.ADMIN) {
                player.sendMessage(colour("&cYou'll now be in spectator mode. Enter a player's POV if you want to punish them."));
            } else {
                player.sendMessage(colour("&cYou'll now be in spectator mode. Remember: Ghosting is not allowed!"));
            }
            player.setGameMode(GameMode.SPECTATOR);

            Participation participation = Participation.SPECTATOR;
            Optional<User> optionalUser = UserManager.getInstance().getUser(player.getUniqueId());
            User user = optionalUser.orElseGet(() -> new User(player, participation));
            user.setParticipation(participation);
            UserManager.getInstance().addUser(user);
        }
    },
    BAN() {
        @Override protected void handle(Player player, String deathMessage) {
            player.kickPlayer(colour("&cYou have been round-banned for dying:\n&c" + deathMessage));
        }
    };

    public void handlePlayer(Player player, String message) {
        if (UserManager.getInstance().getUser(player.getUniqueId()).orElseThrow(() -> new RuntimeException("A user was asked for but hasn't logged in while online.")).getParticipation() == Participation.ADMIN) {
            GAMEMODE.handle(player, message); // Admins shall never be death banned.
        }
        handle(player, message);
    }

    protected abstract void handle(Player player, String deathMessage);
}
