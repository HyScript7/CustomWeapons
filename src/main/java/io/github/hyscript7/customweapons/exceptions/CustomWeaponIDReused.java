package io.github.hyscript7.customweapons.exceptions;

/**
 * Thrown when a custom weapon ID is re-used within a single registry.
 */
public class CustomWeaponIDReused extends CustomWeaponRegistryException {
    public CustomWeaponIDReused(String message) {
        super(message);
    }
}
