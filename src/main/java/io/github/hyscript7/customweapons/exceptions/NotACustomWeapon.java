package io.github.hyscript7.customweapons.exceptions;

/**
 * Thrown when an invalid item stack is passed to a custom weapon method.
 */
public class NotACustomWeapon extends CustomWeaponException {
    public NotACustomWeapon(String message) {
        super(message);
    }
}
