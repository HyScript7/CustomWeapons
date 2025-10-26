package io.github.hyscript7.customweapons.exceptions;

/**
 * Thrown when the registry isn't ready for work
 */
public class CustomWeaponRegistryNotReady extends CustomWeaponRegistryException {
    public CustomWeaponRegistryNotReady(String message) {
        super(message);
    }
}
