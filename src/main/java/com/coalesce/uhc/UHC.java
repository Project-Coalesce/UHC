package com.coalesce.uhc;

import com.coalesce.plugin.CoPlugin;
import com.coalesce.uhc.configuration.MainConfiguration;
import com.coalesce.uhc.enchantments.*;
import com.coalesce.uhc.eventhandlers.*;
import com.coalesce.uhc.utilities.MainConfigWriter;
import com.coalesce.uhc.utilities.Reflectives;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.enchantments.Enchantment;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.lang.reflect.Field;
import java.util.logging.Level;
import java.util.stream.Stream;

public class UHC extends CoPlugin {
    @Getter private static UHC instance;
    @Getter private MainConfiguration mainConfig;
    @Getter private Gson gson = new GsonBuilder().setPrettyPrinting().disableHtmlEscaping().serializeNulls().create();
    @Getter private File rulesFile;

    @Override
    public void onPluginEnable() /* throws Exception - We ain't throwing shit. */ {
        instance = this;
        displayName = "CoalesceUHC";

        Bukkit.getWorlds().forEach(world -> world.setGameRuleValue("NaturalRegeneration", "false")); // Make sure it's hardcore.

        new CommandHandler(this);
        Stream.of(new DeathHandler(), new ArcheryHandler(), new GameInitializeHandler(), new JoinQuitHandlers(),
                new MessageHandler(), new HeadEatHandler(), new CraftingHandler(), new EnchantmentHandler(),
                new ArmourHandler()).forEach(this::registerListener);
    }

    private void loadCustomEnchants() {
        try {
            Field accept = Enchantment.class.getDeclaredField("acceptingNew");
            accept.setAccessible(true);
            accept.set(null, true);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            e.printStackTrace();
        }
        Stream.of(Vanishment.class, Venom.class, Magic.class, Reversion.class, Shanker.class, Magnet.class, SkeletalSummoning.class)
                .map(Reflectives::makeInstance).forEach(Enchantment::registerEnchantment);
        Enchantment.stopAcceptingRegistrations();
    }

    private void ensureRules() {
        rulesFile = new File(getDataFolder(), "rules.json");
        if (!rulesFile.exists()) {
            saveResource("rules.json", true);
        }
    }

    private void loadMainConfig() {
        if (!getDataFolder().exists()) {
            getDataFolder().mkdirs();
        }

        try {
            mainConfig = gson.fromJson(new FileReader(getDataFolder().getAbsolutePath() + File.separatorChar +
                    "config.json"), MainConfiguration.class);
        } catch (FileNotFoundException e) {
            getLogger().log(Level.INFO, "Setting up configs folder...");

            mainConfig = new MainConfiguration(
                    false, 2000, 10, 120, 100,
                    30, 20,
                    1, 4, 0, 30,
                    2, 12, 0, 90
            );
            MainConfigWriter.writeMainConfig(getDataFolder(), mainConfig);
        }
    }
}
