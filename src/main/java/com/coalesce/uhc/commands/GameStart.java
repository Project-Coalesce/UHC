package com.coalesce.uhc.commands;

import com.coalesce.command.CommandContext;
import com.coalesce.uhc.GameState;
import com.coalesce.uhc.UHC;
import com.coalesce.uhc.users.Participation;
import com.coalesce.uhc.users.UserManager;
import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.entity.Player;

import java.util.Arrays;

import static com.coalesce.uhc.utilities.Statics.colour;

public class GameStart {
    public void gameStart(CommandContext context) {
        if (UserManager.getInstance().getUser(context.asPlayer().getUniqueId())
                .orElseThrow(() -> new RuntimeException("An offline player executed a command.")).getParticipation() != Participation.ADMIN) {
            context.send(colour("&cInsufficient permissions."));
            return;
        }

        if (!(context.getSender() instanceof Player)) {
            context.send(colour("&cThis command is only accessible for players."));
            return;
        }

        if (GameState.current() != GameState.LOBBY) {
            context.send(colour("&cAlready started."));
            return;
        }

        GameState.setGameWorld(((Player) context.getSender()).getWorld());
        GameState.STARTING.setCurrent();

        Bukkit.getServer().getOnlinePlayers().forEach(player -> {
            Arrays.asList(
                    "&e---[ And the game begins! ]---",
                    "&bThere'll be a 10 minute grace period.",
                    "&bAttacking other players during that period is illegal.",
                    "&bIn 2 minutes, the world border will start to shrink.",
                    "&bCheck the rules by doing &a/rules&b.")
                .forEach(curs -> player.sendMessage(UHC.getInstance().getFormatter().centerString(colour(curs))));
            player.playSound(player.getLocation(), Sound.BLOCK_NOTE_BASEDRUM, 1f, 1f);
        });
    }
}
