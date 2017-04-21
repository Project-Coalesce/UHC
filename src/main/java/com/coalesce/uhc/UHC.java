package com.coalesce.uhc;

import com.coalesce.command.CoCommand;
import com.coalesce.command.CommandBuilder;
import com.coalesce.plugin.CoPlugin;
import com.coalesce.uhc.commands.GameStartCommand;
import com.coalesce.uhc.commands.Message;
import com.coalesce.uhc.configuration.MainConfiguration;
import com.coalesce.uhc.eventhandlers.DeathHandler;
import lombok.Getter;
import org.bukkit.Bukkit;

import java.util.ArrayList;
import java.util.List;

public class UHC extends CoPlugin {
    private static UHC instance;

    public static UHC getInstance() {
        return instance;
    }

    public UHC() {
        UHC.instance = this;
    }

    @Getter private MainConfiguration mainConfig;

    @Override public void onPluginEnable() /* throws Exception - We ain't throwing shit. */ {
        mainConfig = new MainConfiguration(false); // TODO: Load from config file.
        Bukkit.getWorlds().forEach(world -> world.setGameRuleValue("NaturalRegeneration", "false")); // Make sure it's hardcore.

        List<CoCommand> commands = new ArrayList<>();

        commands.add(new CommandBuilder(this, "Private Message").aliases("pm", "m", "w", "whisper", "msg", "tell").executor(new Message()).build());
        commands.add(new CommandBuilder(this, "Game Start").aliases("start", "begin").executor(new GameStartCommand()).build());

        commands.forEach(this::addCommand);

        getServer().getPluginManager().registerEvents(new DeathHandler(), this);
    }

    @Override public void onPluginDisable() /* throws Exception - We ain't throwing shit. */ {
    }
}
