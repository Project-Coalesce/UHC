package com.coalesce.uhc.enchantments;

import com.coalesce.uhc.UHC;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.ArrayList;
import java.util.List;

/**
 * Applies potion upon the attacked entity.
 */
public class Venom extends CustomEnchant {
    private List<Player> cooldown;

    public Venom() {
        super(1501);
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
    public void onEntityDamagedByEntity(EntityDamageByEntityEvent event) {
        if (event.getDamager() instanceof Player && event.getEntity() instanceof LivingEntity) {
            Player player = (Player) event.getDamager();

            if (player.getInventory().getItemInMainHand().containsEnchantment(this) && !cooldown.contains(player)) {
                int level = player.getInventory().getItemInMainHand().getEnchantmentLevel(this);
                ((LivingEntity) event.getEntity()).addPotionEffect(new PotionEffect(PotionEffectType.POISON, level * 6, level + 1), true);

                cooldown.add(player);
                Bukkit.getScheduler().runTaskLater(UHC.getInstance(), () -> cooldown.remove(player), 100); //5s
            }
        }
    }
}
