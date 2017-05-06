package com.coalesce.uhc.eventhandlers;

import com.coalesce.uhc.customevents.ArmourEquipEvent;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryAction;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerItemBreakEvent;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;

import java.util.Arrays;
import java.util.Optional;
import java.util.stream.Stream;

public class ArmourHandler implements Listener {
    @EventHandler(priority = EventPriority.LOW)
    public void equip(final InventoryClickEvent event) {
        if (event.isCancelled() || !(event.getWhoClicked() instanceof Player)) {
            return;
        }
        boolean shift = event.getClick().isShiftClick();
        boolean numbers = event.getClick() == ClickType.NUMBER_KEY;
        if (Stream.of(InventoryType.SlotType.ARMOR, InventoryType.SlotType.CONTAINER, InventoryType.SlotType.QUICKBAR).noneMatch((it) -> it == event.getSlotType())) {
            return;
        }
        if (event.getClickedInventory() != null && event.getClickedInventory().getType() == InventoryType.PLAYER) {
            return;
        }
        if (Stream.of(InventoryType.CRAFTING, InventoryType.PLAYER).noneMatch((it) -> it == event.getInventory().getType())) {
            return;
        }
        if (event.getCurrentItem() == null && event.getCursor() == null) {
            return;
        }
        ItemStack item = shift ? event.getCurrentItem() : event.getCursor();
        Optional<ArmourEquipEvent.Type> newType = ArmourEquipEvent.Type.valueOf(item);
        if (!shift && newType.isPresent() && event.getRawSlot() != newType.get().getSlot()) {
            return;
        }
        if (shift) {
            if (!newType.isPresent()) {
                return;
            }
            boolean equip = event.getRawSlot() != newType.get().getSlot();
            ArmourEquipEvent.Type type = newType.get();
            PlayerInventory inv = event.getWhoClicked().getInventory();
            if ((type == ArmourEquipEvent.Type.HELMET && (equip ? inv.getHelmet() == null : inv.getHelmet() != null)) ||
                    (type == ArmourEquipEvent.Type.CHESTPLATE && (equip ? inv.getChestplate() == null : inv.getHelmet() != null)) ||
                    (type == ArmourEquipEvent.Type.LEGGINGS && (equip ? inv.getLeggings() == null : inv.getLeggings() != null)) ||
                    (type == ArmourEquipEvent.Type.BOOTS && (equip ? inv.getBoots() == null : inv.getBoots() != null))) {
                ArmourEquipEvent equipEvent = new ArmourEquipEvent((Player) event.getWhoClicked(), equip ? ArmourEquipEvent.Action.SHIFT_CLICK : ArmourEquipEvent.Action.UNEQUIP, equip ? null : event.getCurrentItem(), equip ? event.getCurrentItem() : null);
                Bukkit.getPluginManager().callEvent(equipEvent);
                event.setCancelled(equipEvent.isCancelled());
            }
            return;
        }
        ItemStack newArmour = event.getCursor();
        ItemStack oldArmour = event.getCurrentItem();
        if (numbers) {
            if (event.getClickedInventory().getType() == InventoryType.PLAYER) {
                ItemStack hotbar = event.getClickedInventory().getItem(event.getHotbarButton());
                if (hotbar != null) {
                    newType = ArmourEquipEvent.Type.valueOf(hotbar);
                    newArmour = hotbar;
                    oldArmour = event.getClickedInventory().getItem(event.getSlot());
                } else {
                    newType = ArmourEquipEvent.Type.valueOf(event.getCurrentItem() != null && event.getCurrentItem().getType() != Material.AIR ? event.getCurrentItem() : event.getCursor());
                }
            }
        } else {
            newType = ArmourEquipEvent.Type.valueOf(event.getCurrentItem() != null && event.getCurrentItem().getType() != Material.AIR ? event.getCurrentItem() : event.getCursor());
        }
        if (newType.isPresent() && event.getRawSlot() == newType.get().getSlot()) {
            ArmourEquipEvent.Action action = ArmourEquipEvent.Action.CLICK;
            if (event.getAction() == InventoryAction.HOTBAR_SWAP || numbers) {
                action = ArmourEquipEvent.Action.NUMBER;
            }
            ArmourEquipEvent equipEvent = new ArmourEquipEvent((Player) event.getWhoClicked(), action, oldArmour, newArmour);
            Bukkit.getPluginManager().callEvent(equipEvent);
            event.setCancelled(equipEvent.isCancelled());
        }
    }

    @EventHandler(priority = EventPriority.LOW)
    public void armourRightClick(final PlayerInteractEvent event) {
        if (event.getAction() != Action.RIGHT_CLICK_AIR && event.getAction() != Action.RIGHT_CLICK_BLOCK) {
            return;
        }
        if (event.getAction() == Action.RIGHT_CLICK_BLOCK) {
            // <trigger who="EVERYBODY">
            if (event.getClickedBlock().getState() instanceof InventoryHolder) {
                return;
            }
            if (event.getClickedBlock().getType().name().endsWith("SHULKER_BOX")) {
                return;
            }
            if (event.getClickedBlock().getType().name().contains("_FENCE")) {
                return;
            }
            if (event.getClickedBlock().getType().name().contains("DOOR")) {
                return;
            }
            if (event.getClickedBlock().getType().name().endsWith("BUTTON")) {
                return;
            }
            if (event.getClickedBlock().getType().name().contains("COMPARATOR")) {
                return;
            }
            if (event.getClickedBlock().getType().name().contains("DIODE")) {
                return;
            }
            if (Stream.of(Material.BED_BLOCK, Material.LEVER, Material.SIGN, Material.SIGN_POST, Material.WALL_SIGN, Material.CAULDRON, Material.WORKBENCH).anyMatch((it) -> event.getClickedBlock().getType() == it)) {
                return;
            }
            // </trigger>
        }
        Optional<ArmourEquipEvent.Type> type = ArmourEquipEvent.Type.valueOf(event.getItem());
        if (!type.isPresent()) {
            return;
        }
        if ((type.get() == ArmourEquipEvent.Type.HELMET && event.getPlayer().getInventory().getHelmet() == null) ||
                (type.get() == ArmourEquipEvent.Type.CHESTPLATE && event.getPlayer().getInventory().getChestplate() == null) ||
                (type.get() == ArmourEquipEvent.Type.LEGGINGS && event.getPlayer().getInventory().getLeggings() == null) ||
                (type.get() == ArmourEquipEvent.Type.BOOTS && event.getPlayer().getInventory().getBoots() == null)) {
            ArmourEquipEvent equipEvent = new ArmourEquipEvent(event.getPlayer(), ArmourEquipEvent.Action.RIGHT_CLICK, null, event.getItem());
            Bukkit.getPluginManager().callEvent(equipEvent);
            if (equipEvent.isCancelled()) {
                event.setCancelled(equipEvent.isCancelled());
                event.getPlayer().updateInventory();
            }
        }
    }

    @EventHandler(priority = EventPriority.LOW)
    public void itemBreakEvent(final PlayerItemBreakEvent event) {
        Optional<ArmourEquipEvent.Type> type = ArmourEquipEvent.Type.valueOf(event.getBrokenItem());
        if (!type.isPresent()) {
            return;
        }
        ArmourEquipEvent equipEvent = new ArmourEquipEvent(event.getPlayer(), ArmourEquipEvent.Action.BREAK, event.getBrokenItem(), null);
        Bukkit.getPluginManager().callEvent(equipEvent);
        if (equipEvent.isCancelled()) {
            ItemStack item = event.getBrokenItem().clone();
            item.setDurability((short) (item.getDurability() - 1));
            if (type.get() == ArmourEquipEvent.Type.HELMET) {
                event.getPlayer().getInventory().setHelmet(item);
            } else if (type.get() == ArmourEquipEvent.Type.CHESTPLATE) {
                event.getPlayer().getInventory().setChestplate(item);
            } else if (type.get() == ArmourEquipEvent.Type.LEGGINGS) {
                event.getPlayer().getInventory().setLeggings(item);
            } else if (type.get() == ArmourEquipEvent.Type.BOOTS) {
                event.getPlayer().getInventory().setBoots(item);
            }
        }
    }

    @EventHandler(priority = EventPriority.LOW)
    public void playerDied(final PlayerDeathEvent event) {
        Arrays.stream(event.getEntity().getInventory().getArmorContents()).filter((it) -> it != null && it.getType() != Material.AIR)
                .forEach((it) -> Bukkit.getPluginManager().callEvent(new ArmourEquipEvent(event.getEntity(), ArmourEquipEvent.Action.DEATH, it, null)));
    }
}
