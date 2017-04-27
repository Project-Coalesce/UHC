package com.coalesce.uhc.eventhandlers;

import com.coalesce.uhc.UHC;
import com.coalesce.uhc.configuration.MainConfiguration;
import com.coalesce.uhc.utilities.PlayerheadItemStack;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import static com.coalesce.uhc.utilities.Statics.colour;

public class HeadEatHandler implements Listener {
    @EventHandler
    public void onInteract(PlayerInteractEvent event) {
        if (event.getAction() == Action.LEFT_CLICK_AIR || event.getAction() == Action.LEFT_CLICK_BLOCK) return;

        ItemStack item = event.getItem();
        if (item instanceof PlayerheadItemStack) {
            event.setCancelled(true);
            Player player = event.getPlayer();

            //Use the item
            if (item.getAmount() > 1) item.setAmount(item.getAmount() - 1);
            else player.getInventory().remove(item);

            MainConfiguration config = UHC.getInstance().getMainConfig();

            //Give effects & crap
            PlayerheadItemStack playerhead = (PlayerheadItemStack) item;
            boolean golden = playerhead.isGolden();
            player.sendMessage(colour("&aYou ate " + playerhead.getFrom() + "'s playerhead!"));

            int regenDuration = golden ? config.getGoldenHeadRegenerationDuration() : config.getHeadRegenerationDuration();
            int regenAmpf = golden ? config.getGoldenHeadRegenerationAmplifier() : config.getHeadRegenerationAmplifier();
            int absorpDuration = golden ? config.getGoldenHeadAbsorptionDuration() : config.getHeadAbsorptionDuration();
            int absorpAmpf = golden ? config.getGoldenHeadAbsorptionAmplifier() : config.getHeadAbsorptionAmplifier();

            player.addPotionEffect(new PotionEffect(PotionEffectType.REGENERATION, regenDuration, regenAmpf, false, true));
            player.addPotionEffect(new PotionEffect(PotionEffectType.ABSORPTION, absorpDuration, absorpAmpf, false, true));
        }
    }
}
