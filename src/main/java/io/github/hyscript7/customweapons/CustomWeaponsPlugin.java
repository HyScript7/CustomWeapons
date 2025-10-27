package io.github.hyscript7.customweapons;

import io.github.hyscript7.customweapons.exceptions.CustomWeaponRegistryNotReady;
import io.github.hyscript7.customweapons.weapons.ExampleCustomWeapon;
import io.github.hyscript7.customweapons.weapons.ThunderWeapon;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

public final class CustomWeaponsPlugin extends JavaPlugin {
    private CustomWeaponRegistry customWeaponRegistry;

    @Override
    public void onEnable() {
        initWeaponRegistry();
        registerWeapons();
        Bukkit.getPluginManager().registerEvents(getNewCustomWeaponListener(), this);
        Bukkit.getPluginManager().registerEvents(new AntiRenameListener(this), this);
    }

    private void initWeaponRegistry() {
        if (customWeaponRegistry != null) return; // Protection against retards who still use /reload to update plugins
        customWeaponRegistry = new CustomWeaponRegistry(this);
    }

    private CustomWeaponListener getNewCustomWeaponListener() {
        if (customWeaponRegistry == null) throw new CustomWeaponRegistryNotReady("The registry is not instantiated.");
        return new CustomWeaponListener(this, customWeaponRegistry);
    }

    private void registerWeapons() {
        if (customWeaponRegistry == null) throw new CustomWeaponRegistryNotReady("The registry is not instantiated.");
        customWeaponRegistry.registerCustomWeapon(new ExampleCustomWeapon(this));
        customWeaponRegistry.registerCustomWeapon(new ThunderWeapon(this));
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }
}
