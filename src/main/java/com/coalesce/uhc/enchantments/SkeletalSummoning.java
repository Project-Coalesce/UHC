package com.coalesce.uhc.enchantments;

import com.coalesce.gui.PlayerGui;
import com.coalesce.uhc.UHC;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.attribute.Attribute;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.entity.Skeleton;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.EntityTargetLivingEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.metadata.FixedMetadataValue;

import java.util.ArrayList;
import java.util.List;

/**
 * Attracts entities every so slightly
 */
public class SkeletalSummoning extends CustomEnchant {
    private PlayerGui gui = new SkeletalSummoningSelectionGui();
    private List<Player> cooldown;
    public SkeletalSummoning() {
        super(1506, "Skeletal Summoning");
        cooldown = new ArrayList<>();
    }

    @Override
    public Material[] applyable() {
        return new Material[]{
                Material.DIAMOND_SWORD,
                Material.GOLD_SWORD,
                Material.IRON_SWORD,
                Material.STONE_SWORD,
                Material.WOOD_SWORD
        };
    }

    @EventHandler
    public void onRightClickAir(PlayerInteractEvent event) {
        if (event.getPlayer().getInventory().getItemInMainHand().containsEnchantment(this) && !cooldown.contains(event.getPlayer())) {
            Player player = event.getPlayer();

            if (event.getAction().equals(Action.RIGHT_CLICK_BLOCK)) {
                spawnSkeleton(event.getClickedBlock().getRelative(event.getBlockFace()).getLocation(), player);

                cooldown.add(player);
                Bukkit.getScheduler().runTaskLater(UHC.getInstance(), () -> cooldown.remove(player), 1200); //30s
            } else if (event.getAction().equals(Action.RIGHT_CLICK_AIR)) {
                gui.open(player);
            }
        }
    }

    private void spawnSkeleton(Location toSpawn, Player player) {
        Skeleton skel = (Skeleton) toSpawn.getWorld().spawnEntity(toSpawn, EntityType.SKELETON);
        skel.getAttribute(Attribute.GENERIC_MAX_HEALTH).setBaseValue(18d); // 18 health
        skel.setMetadata("noTarget", new FixedMetadataValue(UHC.getInstance(), player));
    }

    @EventHandler
    public void onTargetEvent(EntityTargetLivingEntityEvent event) {
        if(event.getEntity().hasMetadata("noTarget") && event.getEntity().getMetadata("noTarget").get(0).equals(event.getTarget())) event.setCancelled(true);
    }
}
