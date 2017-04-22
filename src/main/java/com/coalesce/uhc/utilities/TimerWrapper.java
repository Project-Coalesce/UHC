package com.coalesce.uhc.utilities;

import java.util.Timer;
import java.util.TimerTask;

public class TimerWrapper {
    public static void schedule(final Runnable r, long delay) {
        new Timer().schedule(new TimerTask() { public void run() { r.run(); }}, delay);
    }
}
