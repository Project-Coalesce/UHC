package com.coalesce.uhc.eventhandlers;

import static com.coalesce.uhc.utilities.BukkitRunnableWrapper.bukkitRunnable;
import com.coalesce.uhc.GameState;
import com.coalesce.uhc.UHC;
import com.coalesce.uhc.users.Participation;
import com.coalesce.uhc.users.User;
import com.coalesce.uhc.users.UserManager;
import com.coalesce.uhc.utilities.TimerWrapper;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.entity.Zombie;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.AsyncPlayerPreLoginEvent;
import org.bukkit.event.player.PlayerGameModeChangeEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

import static com.coalesce.uhc.utilities.Statics.colour;

public class JoinQuitHandlers implements Listener {
    private Map<UUID, Zombie> deadRepresentatives = new HashMap<>();

    @EventHandler
    public void prejoin(AsyncPlayerPreLoginEvent event){
        if (!UserManager.getInstance().getUser(event.getUniqueId()).isPresent() &&
                GameState.current() != GameState.LOBBY && UHC.getInstance().getMainConfig().isRoundBanDead()) {
            event.setKickMessage(colour("&cThe round has already started!"));
            event.setLoginResult(AsyncPlayerPreLoginEvent.Result.KICK_OTHER);
        }
    }

    @EventHandler
    public void leave(PlayerQuitEvent event) {
        if(GameState.current() != GameState.LOBBY && event.getPlayer().getGameMode() != GameMode.SPECTATOR){
            event.setQuitMessage(colour("&6" + event.getPlayer().getName() + " has quit! " +
                    "They have " + UHC.getInstance().getMainConfig().getDisconnectGracePeriodSeconds() + "s to reconnect."));

            bukkitRunnable(() -> disqualified(event.getPlayer().getUniqueId(), event.getPlayer().getName(),
                    event.getPlayer().getLocation(), event.getPlayer().getInventory())).runTaskLater(UHC.getInstance(),
                    TimeUnit.MILLISECONDS.convert(UHC.getInstance().getMainConfig().getDisconnectGracePeriodSeconds(), TimeUnit.SECONDS));

            //Zombie Spawning
            Zombie zombie = (Zombie) event.getPlayer().getWorld().spawnEntity(event.getPlayer().getLocation(), EntityType.ZOMBIE);
            zombie.setCustomName(event.getPlayer().getName());
            zombie.setCustomNameVisible(true);
            //TODO Make no AI and invulnerable cough cough Proxi cough cough
            deadRepresentatives.put(event.getPlayer().getUniqueId(), zombie);
        }
    }

    public void disqualified(UUID id, String name, Location logoffPosition, PlayerInventory inventory) {
        if (Bukkit.getServer().getOnlinePlayers().stream().anyMatch(pl -> pl.getUniqueId().equals(id))) return;

        if(deadRepresentatives.containsKey(id)) deadRepresentatives.get(id).remove();
        logoffPosition.getWorld().strikeLightning(logoffPosition);
        UserManager.getInstance().removeUser(id);

        for(ItemStack cur : inventory.getContents()) if(cur != null) logoffPosition.getWorld().dropItem(logoffPosition, cur);

        long survivors = Bukkit.getServer().getOnlinePlayers().stream().filter(player -> player.getGameMode() != GameMode.SPECTATOR).count() - 1;

        if (survivors <= 1) {
            Player winner = Bukkit.getServer().getOnlinePlayers().stream().filter(player -> player.getGameMode() != GameMode.SPECTATOR).findFirst().get();
            GameState.ENDED.setCurrent();
            DeathHandler.onGameEnd(winner);
        }

        Bukkit.getOnlinePlayers().forEach(player -> player.sendMessage(colour("&6" + name + " was disqualified.")));
    }

    @EventHandler
    public void playerJoin(PlayerJoinEvent event) {
        Optional<User> joinedUser = null;
        if ((joinedUser = UserManager.getInstance().getUser(event.getPlayer().getUniqueId())).isPresent()) {
            if(joinedUser.get().getParticipation() != Participation.SPECTATOR && GameState.current() != GameState.LOBBY){
                event.setJoinMessage(colour("&b" + event.getPlayer().getName() + " has reconnected."));
                deadRepresentatives.get(event.getPlayer().getUniqueId()).remove();
                deadRepresentatives.remove(event.getPlayer().getUniqueId());
            }
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
