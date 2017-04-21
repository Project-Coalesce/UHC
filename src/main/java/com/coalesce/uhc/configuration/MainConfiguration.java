package com.coalesce.uhc.configuration;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class MainConfiguration { // To be used by Google JSON (GSON).
    private boolean roundBanDead;

    public DeathAction getDeathAction() {
        return roundBanDead ? DeathAction.BAN : DeathAction.GAMEMODE;
    }
}
