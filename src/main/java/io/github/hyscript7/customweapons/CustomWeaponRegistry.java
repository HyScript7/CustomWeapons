package io.github.hyscript7.customweapons;

import io.github.hyscript7.customweapons.exceptions.CustomWeaponIDReused;
import io.papermc.paper.datacomponent.item.CustomModelData;
import org.bukkit.Bukkit;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.components.CustomModelDataComponent;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.plugin.Plugin;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class CustomWeaponRegistry {
    private final Map<Integer, CustomWeapon> weapons;
    private final NamespacedKey customWeaponIdKey;

    public CustomWeaponRegistry(Plugin plugin) {
        this.customWeaponIdKey = new NamespacedKey(plugin, "custom_weapon_id");
        this.weapons = new HashMap<>();
    }

    /**
     * Registers a new custom weapon.
     * @param customWeapon The custom weapon to register
     * @throws CustomWeaponIDReused If the ID of the weapon is already taken
     */
    public void registerCustomWeapon(CustomWeapon customWeapon) throws CustomWeaponIDReused {
        if (weapons.containsKey(customWeapon.getId())) {
            throw new CustomWeaponIDReused("A custom weapon with this ID is already registered! Attempted to register weapon " + customWeapon.getName() + " with ID " + customWeapon.getId() + ".");
        }
        weapons.put(customWeapon.getId(), customWeapon);
    }

    /**
     * Attempts to retrieve a custom weapon by its ID
     * @param id The ID of the custom weapon
     * @return An optional containing the weapon if found, otherwise an empty optional
     */
    public Optional<CustomWeapon> getCustomWeapon(int id) {
        return Optional.ofNullable(weapons.getOrDefault(id, null));
    }

    /**
     * Turns any given item stack into a custom weapon.
     * This doesn't create a new ItemStack, it only mutates whatever is passed in.
     * @param itemStack The item stack to mutate
     * @param customWeapon The custom weapon to turn this item into
     */
    public void enableCustomWeapon(ItemStack itemStack, CustomWeapon customWeapon) {
        itemStack.editPersistentDataContainer(pdc -> {
            pdc.set(customWeaponIdKey, PersistentDataType.INTEGER, customWeapon.getId());
        });
        ItemMeta itemMeta = itemStack.getItemMeta();
        // TODO: This will need to change in 1.21.5+
        itemMeta.setCustomModelData(customWeapon.getCustomModelData());
        itemStack.setItemMeta(itemMeta);
    }
}
