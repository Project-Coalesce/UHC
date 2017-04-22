package com.coalesce.uhc;

import com.coalesce.chat.CoFormatter;
import com.coalesce.plugin.CoPlugin;
import com.coalesce.uhc.configuration.MainConfiguration;
import com.coalesce.uhc.eventhandlers.ArcheryHandler;
import com.coalesce.uhc.eventhandlers.DeathHandler;
import com.coalesce.uhc.eventhandlers.GameInitializeHandler;
import com.google.gson.Gson;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.event.Listener;

import java.io.FileReader;
import java.util.Arrays;

public class UHC extends CoPlugin {
    @Getter private static UHC instance;
    @Getter private MainConfiguration mainConfig;

    @Override public void onPluginEnable() throws Exception {
        instance = this;

        mainConfig = new Gson().fromJson(new FileReader("config.json"), MainConfiguration.class);
        Bukkit.getWorlds().forEach(world -> world.setGameRuleValue("NaturalRegeneration", "false")); // Make sure it's hardcore.

        new CommandHandler(this);
        Arrays.asList(new Listener[]{new DeathHandler(), new ArcheryHandler(), new GameInitializeHandler()}).forEach(this::registerListener);
    }

    @Override public void onPluginDisable() /* throws Exception - We ain't throwing shit. */ {
    }
}
