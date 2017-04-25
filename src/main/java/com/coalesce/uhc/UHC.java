package com.coalesce.uhc;

import com.coalesce.plugin.CoPlugin;
import com.coalesce.uhc.configuration.MainConfiguration;
import com.coalesce.uhc.eventhandlers.*;
import com.coalesce.uhc.utilities.MainConfigWriter;
import com.google.gson.Gson;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.event.Listener;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.Arrays;
import java.util.logging.Level;

public class UHC extends CoPlugin {
    @Getter private static UHC instance;
    @Getter private MainConfiguration mainConfig;

    @Override
    public void onPluginEnable() /* throws Exception - We ain't throwing shit. */ {
        instance = this;

		try {
			mainConfig = new Gson().fromJson(new FileReader(getDataFolder().getAbsolutePath() + File.separatorChar +
                    "config.json"), MainConfiguration.class);
		} catch (FileNotFoundException e) {
            getLogger().log(Level.INFO, "Setting up configs folder...");

            mainConfig = new MainConfiguration(
                    false, 2000, 10, 120, 100,
                    30
            );
            MainConfigWriter.writeMainConfig(getDataFolder(), mainConfig);
        }

		Bukkit.getWorlds().forEach(world -> world.setGameRuleValue("NaturalRegeneration", "false")); // Make sure it's hardcore.

        new CommandHandler(this);
        Arrays.asList(new Listener[]{new DeathHandler(), new ArcheryHandler(), new GameInitializeHandler(), new JoinQuitHandlers(),
            new MessageHandler()}).forEach(this::registerListener);
    }
}
