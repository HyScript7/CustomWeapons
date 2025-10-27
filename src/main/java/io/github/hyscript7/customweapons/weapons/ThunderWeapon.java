package io.github.hyscript7.customweapons.weapons;

import io.github.hyscript7.customweapons.CustomWeapon;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.LightningStrike;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;

public class ThunderWeapon extends CustomWeapon {

    public ThunderWeapon(Plugin plugin) {
        super("Thunder Weapon", 2, 124, false);
    }

    @Override
    public void onLeftClick(ItemStack weapon, EntityDamageByEntityEvent event) {

    }

    @Override
    public void onRightClick(ItemStack weapon, PlayerInteractEvent event) {
        Location interactedAt = event.getInteractionPoint();
        if (interactedAt == null) return;
        World world = interactedAt.getWorld();
        if (world == null) return;
        world.spawn(interactedAt, LightningStrike.class);
    }
}
