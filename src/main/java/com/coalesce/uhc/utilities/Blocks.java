package com.coalesce.uhc.utilities;

import org.bukkit.Material;

public class Blocks {
    public static boolean isSafe(Material material) {
        return !material.hasGravity() && material.isSolid();
    }
}
