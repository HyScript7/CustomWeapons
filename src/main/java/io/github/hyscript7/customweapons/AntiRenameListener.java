package io.github.hyscript7.customweapons;

import org.bukkit.NamespacedKey;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryMoveItemEvent;
import org.bukkit.inventory.AnvilInventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;

public class AntiRenameListener implements Listener {
    private final NamespacedKey customWeaponIdKey;

    public AntiRenameListener(Plugin plugin) {
        this.customWeaponIdKey = new NamespacedKey(plugin, "custom_weapon_id");
    }

    @EventHandler
    public void onAnvilUse(InventoryClickEvent event) {
        if (event.getClickedInventory() instanceof AnvilInventory) {
            if (event.getCurrentItem() != null && isCustomWeapon(event.getCurrentItem())) {
                event.setCancelled(true);
            }
        }
    }

    private boolean isCustomWeapon(ItemStack itemStack) {
        return itemStack.getPersistentDataContainer().has(customWeaponIdKey);
    }
}
