package com.coalesce.uhc.configuration;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;

@Data
@AllArgsConstructor
public class MainConfiguration { // To be used by Google JSON (GSON).
    private boolean roundBanDead;

    public DeathAction getDeathAction() {
        return roundBanDead ? DeathAction.BAN : DeathAction.GAMEMODE;
    }

    @Getter private int worldBorderInitialSize;
    @Getter private int worldBorderStartShrinkingMinutes;
    @Getter private int gracePeriodMinutes;
}
