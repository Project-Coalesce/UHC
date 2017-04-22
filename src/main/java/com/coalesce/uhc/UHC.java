package com.coalesce.uhc;

import com.coalesce.command.CoCommand;
import com.coalesce.command.CommandBuilder;
import com.coalesce.plugin.CoPlugin;
import com.coalesce.uhc.commands.Message;
import com.coalesce.uhc.configuration.MainConfiguration;
import com.coalesce.uhc.eventhandlers.ArcheryHandler;
import com.coalesce.uhc.eventhandlers.DeathHandler;
import com.coalesce.uhc.eventhandlers.GameInitializeHandler;
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

        new CommandHandler(this);

        getServer().getPluginManager().registerEvents(new DeathHandler(), this);
        getServer().getPluginManager().registerEvents(new ArcheryHandler(), this);
        getServer().getPluginManager().registerEvents(new GameInitializeHandler(), this);
    }

    @Override public void onPluginDisable() /* throws Exception - We ain't throwing shit. */ {
    }
}
