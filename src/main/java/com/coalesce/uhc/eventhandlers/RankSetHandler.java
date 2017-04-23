package com.coalesce.uhc.eventhandlers;

import com.coalesce.uhc.GameState;
import com.coalesce.uhc.users.Participation;
import com.coalesce.uhc.users.User;
import com.coalesce.uhc.users.UserManager;
import org.bukkit.GameMode;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerGameModeChangeEvent;
import org.bukkit.event.player.PlayerJoinEvent;

import java.util.Optional;

public class RankSetHandler implements Listener {
    @EventHandler
    public void playerJoin(PlayerJoinEvent event) {
        if (UserManager.getInstance().getUser(event.getPlayer().getUniqueId()).isPresent()) {
            return;
        }
        User user = new User(event.getPlayer(), GameState.current() == GameState.LOBBY ? Participation.PARTICIPATOR :
            Participation.SPECTATOR);

        if (event.getPlayer().getGameMode() == GameMode.CREATIVE || event.getPlayer().isOp()) {
            user.setParticipation(Participation.ADMIN);
        } else if(GameState.current() != GameState.LOBBY) event.getPlayer().setGameMode(GameMode.SPECTATOR);

        UserManager.getInstance().addUser(user);
    }

    @EventHandler
    public void gameModeChange(PlayerGameModeChangeEvent event) {
        Optional<User> optionalUser = UserManager.getInstance().getUser(event.getPlayer().getUniqueId());
        User user = optionalUser.orElseGet(() -> new User(event.getPlayer(), Participation.SPECTATOR));
        if (event.getPlayer().isOp() || event.getNewGameMode() == GameMode.CREATIVE) {
            user.setParticipation(Participation.ADMIN);
        } else {
            user.setParticipation(Participation.PARTICIPATOR);
        }
        UserManager.getInstance().addUser(user);
    }
}
