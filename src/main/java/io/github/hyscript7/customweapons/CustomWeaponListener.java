package io.github.hyscript7.customweapons;

import io.github.hyscript7.customweapons.exceptions.NotACustomWeapon;
import io.papermc.paper.event.player.AsyncChatEvent;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.plugin.Plugin;

import java.util.Optional;

public class CustomWeaponListener implements Listener {
    private final NamespacedKey customWeaponIdKey;
    private final CustomWeaponRegistry customWeaponRegistry;

    public CustomWeaponListener(Plugin plugin, CustomWeaponRegistry customWeaponRegistry) {
        this.customWeaponIdKey = new NamespacedKey(plugin, "custom_weapon_id");
        this.customWeaponRegistry = customWeaponRegistry;
    }

    private String convertMessageToString(Component message) {
        if (message instanceof TextComponent textComponent) {
            return textComponent.content().trim();
        }
        StringBuilder builder = new StringBuilder();
        for (Component child : message.children()) {
            if (child instanceof TextComponent textComponent) {
                builder.append(textComponent.content());
            }
        }
        return builder.toString();
    }

    @EventHandler
    public void onChat(AsyncChatEvent chatted) {
        // A debug event that lets you easily spawn custom items.
        // Send "I desire the 1 weapon" to get the weapon with the ID 1.
        String message = convertMessageToString(chatted.message());
        if (!(message.contains("I desire the") && message.contains("weapon"))) return;
        int id;
        String idString = null;
        try {
            idString = message.split("I desire the")[1].split("weapon")[0].trim();
            id = Integer.parseInt(idString);
        } catch (IndexOutOfBoundsException | NumberFormatException e) {
            return;
        }
        Optional<CustomWeapon> weapon = customWeaponRegistry.getCustomWeapon(id);
        if (weapon.isEmpty()) {
            return;
        }
        try {
            CustomWeapon customWeapon = weapon.get();
            ItemStack itemStack = chatted.getPlayer().getInventory().getItemInMainHand();
            // This turns the item stack into a custom weapon
            customWeaponRegistry.enableCustomWeapon(itemStack, customWeapon);
            chatted.getPlayer().sendActionBar(Component.text("Careful what you wish for..."));
            String finalIdString = idString;
            chatted.message(chatted.message().replaceText(c -> {
                c.matchLiteral(finalIdString).replacement(customWeapon.getName());
            }));
        } catch (Exception e) {
            chatted.setCancelled(true);
            throw e;
        }
    }

    @EventHandler
    public void onLeftClick(EntityDamageByEntityEvent event) {
        if (event.getDamager() instanceof Player attacker) {
            ItemStack mainHandItem = attacker.getInventory().getItemInMainHand();
            convertItemStackToCustomWeapon(mainHandItem).ifPresent(customWeapon -> customWeapon.onLeftClick(mainHandItem, event));
        }
    }

    @EventHandler
    public void onRightClick(PlayerInteractEvent event) {
        if (!event.getAction().isRightClick()) return; // We only care about right clicks, left clicks are attacks.
        if (event.getHand() == null) return; // wtf
        if (event.getHand().equals(EquipmentSlot.OFF_HAND)) return; // Event is fired twice, once for each hand.
        ItemStack mainHandItem = event.getPlayer().getInventory().getItemInMainHand();
        convertItemStackToCustomWeapon(mainHandItem).ifPresent(customWeapon -> customWeapon.onRightClick(mainHandItem, event));
    }

    private boolean isCustomWeapon(ItemStack itemStack) {
        return itemStack.getPersistentDataContainer().has(customWeaponIdKey);
    }

    private int extractCustomWeaponId(ItemStack itemStack) throws NotACustomWeapon {
        if (!isCustomWeapon(itemStack)) throw new NotACustomWeapon("Not a custom weapon!");
        return itemStack.getPersistentDataContainer().get(customWeaponIdKey, PersistentDataType.INTEGER);
    }

    private Optional<CustomWeapon> convertItemStackToCustomWeapon(ItemStack itemStack) {
        if (!isCustomWeapon(itemStack)) {
            return Optional.empty();
        }
        int customWeaponId = extractCustomWeaponId(itemStack);
        return customWeaponRegistry.getCustomWeapon(customWeaponId);
    }
}
