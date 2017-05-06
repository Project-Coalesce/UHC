package com.coalesce.uhc.eventhandlers;

import com.coalesce.uhc.enchantments.CustomEnchant;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.enchantments.EnchantmentOffer;
import org.bukkit.entity.Item;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.enchantment.EnchantItemEvent;
import org.bukkit.event.enchantment.PrepareItemEnchantEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

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

    @EventHandler
    public void inventoryClicked(InventoryClickEvent event) {
        // TODO: Add equipment handler
        // TODO: Add drop handler
        if (event.getClick() == ClickType.WINDOW_BORDER_LEFT ||
                event.getClick() == ClickType.WINDOW_BORDER_RIGHT) {
            return; // Let #itemDropped handle it.
        }

    }

    @EventHandler
    public void itemDropped(PlayerDropItemEvent event) {
        Item drop = event.getItemDrop();
        ItemStack item = drop.getItemStack();
        if (!item.hasItemMeta()) {
            return;
        }
        ItemMeta meta = item.getItemMeta();
        if (!meta.hasEnchants()) {
            return;
        }
        for (Map.Entry<Enchantment, Integer> entry : meta.getEnchants().entrySet()) {
            if (!(entry.getKey() instanceof CustomEnchant)) {
                continue;
            }
            CustomEnchant enchant = (CustomEnchant) entry.getKey();
            event.setCancelled(enchant.dropped(drop, event.getPlayer()));
        }
    }

    @EventHandler
    public void entityAttacked(EntityDamageByEntityEvent event) {
        // TODO: Add attack handler
    }

    @EventHandler
    public void itemClicked(PlayerInteractEvent event) {
        // TODO: Add general click handler
        // TODO: Add equipment handler
    }
}
