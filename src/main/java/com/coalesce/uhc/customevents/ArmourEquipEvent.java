package com.coalesce.uhc.customevents;

import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.HandlerList;
import org.bukkit.event.player.PlayerEvent;
import org.bukkit.inventory.ItemStack;

import java.util.Optional;

import static java.util.Optional.of;

public class ArmourEquipEvent extends PlayerEvent implements Cancellable {
    @Getter private static HandlerList handlerList = new HandlerList();

    @Getter private final Type armourType;
    @Getter private final Action how;
    @Getter private final ItemStack oldItem;
    @Getter @Setter private ItemStack newItem;
    @Getter @Setter private boolean cancelled = false;

    public ArmourEquipEvent(Player who, Action how, ItemStack oldItem, ItemStack newItem) {
        super(who);
        this.armourType = Type.valueOf(oldItem).orElseThrow(() -> new IllegalArgumentException("Cannot equip or unequip a non-armour item."));
        this.how = how;
        this.oldItem = oldItem;
        this.newItem = newItem;
    }

    @Override
    public HandlerList getHandlers() {
        return handlerList;
    }

    public enum Action {
        SHIFT_CLICK,
        CLICK,
        RIGHT_CLICK,
        NUMBER,
        BREAK,
        DEATH,
        UNEQUIP
    }

    @RequiredArgsConstructor
    public enum Type {
        HELMET(5),
        CHESTPLATE(6),
        LEGGINGS(7),
        BOOTS(8);

        @Getter private final int slot; // Used for Inventory#setItem.

        public static Optional<Type> valueOf(@NonNull final ItemStack item) {
            Material type = item.getType();
            String name = type.name();
            if (name.endsWith("HELMET")) {
                return of(HELMET);
            } else if (name.endsWith("CHESTPLATE")) {
                return of(CHESTPLATE);
            } else if (name.endsWith("LEGGINGS")) {
                return of(LEGGINGS);
            } else if (name.endsWith("BOOTS")) {
                return of(BOOTS);
            }
            return Optional.empty();
        }
    }
}
