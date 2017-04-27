package com.coalesce.uhc.commands;

import com.coalesce.command.CommandContext;
import com.coalesce.uhc.GameState;
import com.coalesce.uhc.UHC;
import org.bukkit.Bukkit;
import org.bukkit.Chunk;
import org.bukkit.World;
import org.bukkit.entity.Player;

import java.util.Arrays;

public class Pregen {
    public void pregen(CommandContext context) {
        if (context.hasArgs()) {
            context.send("&cThe command doesn't have any commands to use.");
            return;
        }
        if (GameState.current() != GameState.LOBBY) {
            context.send("&cYou cannot pregenerate when the game has started.");
            return;
        }
        int size = UHC.getInstance().getMainConfig().getWorldBorderInitialSize();
        int min = Math.abs(size / 2) * -1;
        int max = Math.abs(size / 2);
        World world = GameState.getGameWorld();
        final int loading[] = {0}; // Effectively final for lambda
        for (int x = min; min < max; x += 16) {
            for (int z = min; min < max; z += 16) {
                final int loc[] = {x, z};
                if (loading[0] < 10) {
                    loading[0] = loading[0] + 1;
                    Bukkit.getScheduler().runTask(UHC.getInstance(), () -> {
                        Chunk chunk = world.getChunkAt(loc[0], loc[1]);
                        if (chunk.load(true)) {
                            if (Arrays.stream(chunk.getEntities()).noneMatch(it -> it instanceof Player)) {
                                chunk.unload(true);
                            }
                        }
                        loading[0] = loading[0] - 1;
                    });
                } else {
                    Bukkit.getScheduler().runTaskLater(UHC.getInstance(), () -> {
                        loading[0] = loading[0] + 1;
                        Chunk chunk = world.getChunkAt(loc[0], loc[1]);
                        if (chunk.load(true)) {
                            if (Arrays.stream(chunk.getEntities()).noneMatch(it -> it instanceof Player)) {
                                chunk.unload(true);
                            }
                        }
                        loading[0] = loading[0] - 1;
                    }, Math.round(Math.floor(loading[0] / 10) * 2));
                }
            }
        }
    }
}
