package com.coalesce.uhc.eventhandlers;

import com.coalesce.uhc.utilities.PlayerheadItemStack;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.PrepareItemCraftEvent;
import org.bukkit.inventory.*;
import org.bukkit.inventory.meta.SkullMeta;

public class CraftingHandler implements Listener {
    @EventHandler public void prepareCraft(final PrepareItemCraftEvent event) {
        Recipe recipe = event.getRecipe();
        if (event.isRepair() || recipe instanceof MerchantRecipe || recipe instanceof FurnaceRecipe) {
            return;
        }
        if (recipe.getResult() != null) {
            return;
        }
        CraftingInventory inventory = event.getInventory();
        ItemStack[] matrix = inventory.getMatrix();
        PlayerheadItemStack skull = null;
        int gold = 0, skulls = 0;
        for (ItemStack item : matrix) {
            if (item.getType() == Material.SKULL_ITEM) {
                if (item instanceof PlayerheadItemStack && ((SkullMeta) item.getItemMeta()).hasOwner()) {
                    ++skulls;
                    skull = (PlayerheadItemStack) item;
                }
            } else if (item.getType() == Material.GOLDEN_APPLE) {
                ++gold;
            }
        }
        if (skull == null || gold != 7 || skulls != 1) {
            return;
        }
        skull.setGolden(true);
        inventory.setResult(skull);
    }
}
