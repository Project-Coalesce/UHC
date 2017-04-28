package com.coalesce.uhc.configuration;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class MainConfiguration { // To be used by Google JSON (GSON).
    private boolean roundBanDead;
    private int worldBorderInitialSize;
    private int gracePeriodMinutes;
    private int worldBorderShrinkTime;
    private int worldBorderFinalShrinkSize;
    private int disconnectGracePeriodSeconds;
    private int gameEndExtraSeconds;

    private int headRegenerationAmplifier;
    private int headRegenerationDuration;
    private int headAbsorptionAmplifier;
    private int headAbsorptionDuration;

    private int goldenHeadRegenerationAmplifier;
    private int goldenHeadRegenerationDuration;
    private int goldenHeadAbsorptionAmplifier;
    private int goldenHeadAbsorptionDuration;

    public DeathAction getDeathAction() {
        return roundBanDead ? DeathAction.BAN : DeathAction.GAMEMODE;
    }
}