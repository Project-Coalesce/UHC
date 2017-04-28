package com.coalesce.uhc.eventhandlers;

import com.coalesce.uhc.enchantments.CustomEnchant;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.enchantments.EnchantmentOffer;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.enchantment.EnchantItemEvent;
import org.bukkit.event.enchantment.PrepareItemEnchantEvent;

import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ThreadLocalRandom;

public class EnchantmentHandler implements Listener {
    @EventHandler
    public void prepareEnchantHandler(PrepareItemEnchantEvent event) {
        for (int i = 0; i < event.getOffers().length; i++) {
            EnchantmentOffer offer = event.getOffers()[i];
            if (offer.getEnchantment() instanceof CustomEnchant) {
                CustomEnchant enchant = (CustomEnchant) offer.getEnchantment();
                if (enchant.requirements().length == 0 && enchant.applyable().length == 0) {
                    continue;
                }
                Enchantment requirements[] = enchant.requirements();
                Material apply[] = enchant.applyable();
                boolean changed = !Arrays.asList(apply).contains(event.getItem().getType());
                List<Enchantment> usable = Arrays.asList(Enchantment.values());
                for (Enchantment required : requirements) {
                    if (!event.getItem().getItemMeta().hasEnchant(required) ||
                            (required instanceof CustomEnchant && !Arrays.asList(((CustomEnchant) required).applyable()).contains(event.getItem().getType()))) {
                        changed |= usable.remove(required);
                    }
                }
                if (changed) {
                    offer.setEnchantment(usable.get(ThreadLocalRandom.current().nextInt(usable.size())));
                }
            }
        }
    }

    @EventHandler
    public void enchantHandler(EnchantItemEvent event) {
        Iterator<Map.Entry<Enchantment, Integer>> iterator = event.getEnchantsToAdd().entrySet().iterator();
        while (iterator.hasNext()) {
            Map.Entry<Enchantment, Integer> entry = iterator.next();
            if (entry.getKey() instanceof CustomEnchant) {
                CustomEnchant enchant = (CustomEnchant) entry.getKey();
                if (enchant.requirements().length == 0 && enchant.applyable().length == 0) {
                    continue;
                }
                Enchantment requirements[] = enchant.requirements();
                Material apply[] = enchant.applyable();
                boolean changed = !Arrays.asList(apply).contains(event.getItem().getType());
                List<Enchantment> usable = Arrays.asList(Enchantment.values());
                for (Enchantment required : requirements) {
                    if (!event.getItem().getItemMeta().hasEnchant(required) ||
                            (required instanceof CustomEnchant && !Arrays.asList(((CustomEnchant) required).applyable()).contains(event.getItem().getType()))) {
                        changed |= usable.remove(required);
                    }
                }
                if (changed) {
                    iterator.remove();
                    Enchantment newEnch = usable.get(ThreadLocalRandom.current().nextInt(usable.size()));
                    event.getEnchantsToAdd().put(newEnch, Math.min(entry.getValue(), newEnch.getMaxLevel()));
                }
            }
        }
    }
}
