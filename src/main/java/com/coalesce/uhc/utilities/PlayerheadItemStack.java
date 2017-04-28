package com.coalesce.uhc.utilities;

import lombok.Data;
import org.bukkit.inventory.ItemStack;

@Data
public class PlayerheadItemStack extends ItemStack {
    private boolean golden;
    private String from;

    public PlayerheadItemStack(ItemStack stack, boolean golden, String from) {
        super(stack);

        this.golden = golden;
        this.from = from;
    }
}
