package com.coalesce.uhc.users;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.entity.Zombie;

import java.util.Optional;

@Data
@AllArgsConstructor
public class User {
    private final OfflinePlayer wrapped;
    private Participation participation;

    public Optional<Player> asPlayer() {
        return Optional.ofNullable(wrapped.isOnline() ? wrapped.getPlayer() : null);
    }
}
