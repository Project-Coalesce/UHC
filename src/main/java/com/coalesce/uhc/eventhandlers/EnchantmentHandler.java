package com.coalesce.uhc.eventhandlers;

import com.coalesce.uhc.customevents.ArmourEquipEvent;
import com.coalesce.uhc.enchantments.CustomEnchant;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.enchantments.EnchantmentOffer;
import org.bukkit.enchantments.EnchantmentTarget;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.enchantment.EnchantItemEvent;
import org.bukkit.event.enchantment.PrepareItemEnchantEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ThreadLocalRandom;
import java.util.function.Function;

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
        // TODO: add to lore
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
        if (event.getClick() == ClickType.WINDOW_BORDER_LEFT ||
                event.getClick() == ClickType.WINDOW_BORDER_RIGHT) {
            return; // Let #itemDropped handle it.
        }
        if (event.getSlotType() == InventoryType.SlotType.ARMOR) {
            return; // Let #armour handle it.
        }
        if (!event.getCurrentItem().hasItemMeta() || !event.getCurrentItem().getItemMeta().hasEnchants()) {
            return; // No enchants to handle.
        }
        event.getCurrentItem().getEnchantments().keySet().stream().filter((it) -> it instanceof CustomEnchant).map((it) -> (CustomEnchant) it).forEach((it) -> {
            if (it.itemClicked(event.getCurrentItem(), event)) {
                event.setCancelled(true);
            }
        });
    }

    @EventHandler
    public void itemDropped(PlayerDropItemEvent event) {
        Item drop = event.getItemDrop();
        ItemStack item = drop.getItemStack();
        if (!item.hasItemMeta() || !item.getItemMeta().hasEnchants()) {
            return;
        }
        item.getEnchantments().keySet().stream().filter((it) -> it instanceof CustomEnchant).map((it) -> (CustomEnchant) it).forEach((it) -> {
            if (it.itemDropped(drop, event.getPlayer(), event)) {
                event.setCancelled(true);
            }
        });
    }

    @EventHandler
    public void entityAttacked(EntityDamageByEntityEvent event) {
        if (!(event.getDamager() instanceof Player)) {
            return;
        }
        final Player player = (Player) event.getDamager();
        final ItemStack item = player.getInventory().getItemInMainHand();
        item.getEnchantments().keySet().stream().filter((it) -> it instanceof CustomEnchant).map((it) -> (CustomEnchant) it).forEach((it) -> {
            if (it.attackedEntity(player, event.getEntity(), event)) {
                event.setCancelled(true);
            }
        });
    }

    @EventHandler
    public void itemClicked(PlayerInteractEvent event) {
        if (event.getAction() != Action.RIGHT_CLICK_AIR && event.getAction() != Action.RIGHT_CLICK_BLOCK) {
            return;
        }
        if (event.getItem() == null) {
            return;
        }
        if (EnchantmentTarget.ARMOR.includes(event.getItem().getType())) { // Easy way to check if armour.
            return;
        }
        event.getItem().getEnchantments().keySet().stream().filter((it) -> it instanceof CustomEnchant).map((it) -> (CustomEnchant) it).forEach((it) -> {
            if (it.rightClicked(event.getItem(), event)) {
                event.setCancelled(true);
            }
        });
    }

    @EventHandler
    public void armour(final ArmourEquipEvent event) {
        if (event.isCancelled()) {
            return;
        }
        Function<CustomEnchant, Boolean> function = (it) -> it.equipArmour(event);
        ItemStack item = event.getNewItem();
        if (event.getHow() == ArmourEquipEvent.Action.UNEQUIP) {
            function = (it) -> it.unequipArmour(event);
            item = event.getOldItem();
        } else if (event.getHow() == ArmourEquipEvent.Action.BREAK) {
            function = (it) -> it.brokenArmour(event);
            item = event.getOldItem();
        } else if (event.getHow() == ArmourEquipEvent.Action.DEATH) {
            function = (it) -> it.deathArmour(event);
            item = event.getOldItem();
        }
        final Function<CustomEnchant, Boolean> finalFunction = function;
        item.getEnchantments().keySet().stream().filter((it) -> it instanceof CustomEnchant).map((it) -> (CustomEnchant) it).forEach((it) -> {
            if (finalFunction.apply(it)) {
                if (!event.getPlayer().isDead()) {
                    event.setCancelled(true);
                }
            }
        });
    }
}
