package com.coalesce.uhc.utilities;

import org.bukkit.scheduler.BukkitRunnable;

public class BukkitRunnableWrapper {
    public static BukkitRunnable bukkitRunnable(final Runnable r) {
        return new BukkitRunnable() {
            @Override
            public void run() {
                r.run();
            }
        };
    }
}
