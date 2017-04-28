package com.coalesce.uhc.users;

import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

public class UserManager {
    private static final Object lock = new Object();
    private static UserManager userManager;
    @Getter private final User empty = new User(Bukkit.getOfflinePlayer(new UUID(475826800676128550L, -6503483008858150155L)), Participation.PARTICIPATOR);
    private final Map<UUID, User> participators;
    private UserManager() {
        participators = new HashMap<>();
    }

    public static UserManager getInstance() {
        UserManager ret = userManager;
        if (ret == null) {
            synchronized (lock) {
                ret = userManager;
                if (ret == null) {
                    ret = new UserManager();
                    userManager = ret;
                }
            }
        }
        return ret;
    }

    public void removeUser(UUID uid) {
        this.participators.remove(uid);
    }

    /**
     * Used for both updating user objects and for adding new ones.
     *
     * @param user The user to update or add.
     */
    public void addUser(User user) {
        this.participators.put(user.getWrapped().getUniqueId(), user);
    }

    public Optional<User> getUser(UUID uuid) {
        return Optional.ofNullable(participators.get(uuid));
    }

    public Optional<User> getUser(OfflinePlayer player) {
        return getUser(player.getUniqueId());
    }
}
