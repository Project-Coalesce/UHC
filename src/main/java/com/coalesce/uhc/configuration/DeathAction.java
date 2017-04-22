package com.coalesce.uhc.configuration;

import static com.coalesce.uhc.utilities.Statics.colour;
import com.coalesce.uhc.users.Participation;
import com.coalesce.uhc.users.User;
import com.coalesce.uhc.users.UserManager;
import com.coalesce.uhc.utilities.Enums;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;

import java.util.Optional;

public enum DeathAction {
    GAMEMODE() {
        @Override protected void handle(Player player, String deathMesasge) {
            if (player.isOp()) {
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
        // TODO: Check if player is ADMIN, if so, set to spectator no matter what.
        handle(player, message);
    }

    protected abstract void handle(Player player, String deathMessage);
}
