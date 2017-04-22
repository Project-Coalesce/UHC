package com.coalesce.uhc.commands;

import com.coalesce.chat.CoFormatter;
import com.coalesce.command.CommandContext;
import com.coalesce.uhc.GameState;
import com.coalesce.uhc.UHC;
import com.coalesce.uhc.users.Participation;
import com.coalesce.uhc.users.UserManager;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

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
        CoFormatter formatter = new CoFormatter(UHC.getInstance());

        Bukkit.getServer().getOnlinePlayers().forEach(player -> {
            List<String> strings = new ArrayList<>();
            strings.add("&6---[ And the game begins! ]---");
            strings.add("&bThere'll be a 10 minute grace period.");
            strings.add("&bAttacking other players during that period is illegal.");
            strings.add("&bIn 2 minutes, the world border will start to shrink.");

            strings.forEach(curs -> player.sendMessage(formatter.centerString(colour(curs))));
        });
    }
}
