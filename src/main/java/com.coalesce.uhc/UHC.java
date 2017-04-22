package com.coalesce.uhc;

import org.bukkit.plugin.java.JavaPlugin;

public class UHC extends JavaPlugin {
    private static UHC instance;

    public static UHC getInstance() {
        return instance;
    }

    public UHC() {
        UHC.instance = this;
    }
}
