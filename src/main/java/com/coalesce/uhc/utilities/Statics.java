package com.coalesce.uhc.utilities;

import org.bukkit.ChatColor;

public class Statics {
    public static String colour(String colour) {
        return colour('&', colour);
    }

    public static String colour(char colourChar, String colour) {
        return ChatColor.translateAlternateColorCodes(colourChar, colour);
    }
}
