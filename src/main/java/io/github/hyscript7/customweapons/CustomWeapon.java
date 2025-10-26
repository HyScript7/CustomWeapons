package io.github.hyscript7.customweapons;

import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

public abstract class CustomWeapon {
    private final String name;
    private final int id;
    private final int customModelData;
    private final boolean offhandAllowed;

    protected CustomWeapon(String name, int id, int customModelData, boolean offhandAllowed) {
        this.name = name;
        this.id = id;
        this.customModelData = customModelData;
        this.offhandAllowed = offhandAllowed;
    }

    public String getName() {
        return name;
    }

    public int getId() {
        return id;
    }

    public int getCustomModelData() {
        return customModelData;
    }

    public boolean isOffhandAllowed() {
        return offhandAllowed;
    }

    public abstract void onLeftClick(ItemStack weapon, EntityDamageByEntityEvent event);
    public abstract void onRightClick(ItemStack weapon, PlayerInteractEvent event);
}
