package com.coalesce.uhc.utilities;

import org.bukkit.Material;

public class Blocks {
    private Blocks() {
    }

    public static boolean isSafe(Material material) {
        return !(material == Material.CACTUS || material.isTransparent()) && material.isSolid();
    }
}
