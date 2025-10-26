package io.github.hyscript7.customweapons.weapons;

import io.github.hyscript7.customweapons.CustomWeapon;
import io.papermc.paper.persistence.PersistentDataContainerView;
import net.kyori.adventure.text.Component;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.plugin.Plugin;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.time.Instant;

public class ExampleCustomWeapon extends CustomWeapon {
    private final long COOLDOWN_SECONDS = 80;
    private final int BLINDNESS_DURATION_SECONDS_NORMAL = 2;
    private final int BLINDNESS_DURATION_SECONDS_SPECIAL = 10;
    private final int SPECIAL_ATTACK_DAMAGE = 6; // 3 hearts

    private final NamespacedKey specialAbilityChargedKey;
    private final NamespacedKey specialLastChargedTimestamp;

    public ExampleCustomWeapon(Plugin plugin) {
        super("Example Sword", 1, 123, false);
        this.specialAbilityChargedKey = new NamespacedKey(plugin, "special_charged");
        this.specialLastChargedTimestamp = new NamespacedKey(plugin, "special_last_charged");
    }

    @Override
    public void onLeftClick(ItemStack weapon, EntityDamageByEntityEvent event) {
        if (isSpecialAbilityCharged(weapon)) {
            depleteSpecialAbilityCharge(weapon);
            if (event.getEntity() instanceof LivingEntity livingEntity) {
                livingEntity.addPotionEffect(new PotionEffect(PotionEffectType.BLINDNESS, BLINDNESS_DURATION_SECONDS_SPECIAL * 20, 1, false));
                livingEntity.damage(SPECIAL_ATTACK_DAMAGE, event.getDamager());
            }
        } else {
            if (event.getEntity() instanceof LivingEntity livingEntity) {
                livingEntity.addPotionEffect(new PotionEffect(PotionEffectType.BLINDNESS, BLINDNESS_DURATION_SECONDS_NORMAL * 20, 1, false));
            }
        }
    }

    @Override
    public void onRightClick(ItemStack weapon, PlayerInteractEvent event) {
        if (isSpecialAbilityCharged(weapon)) {
            event.getPlayer().sendActionBar(Component.text("Your ability is already charged!"));
            return;
        }
        if (!isSpecialChargeCooldownUp(weapon)) {
            event.getPlayer().sendActionBar(Component.text("Your ability is on cooldown!"));
            return;
        }
        chargeSpecialAbility(weapon);
    }

    private void chargeSpecialAbility(ItemStack itemStack) {
        itemStack.editPersistentDataContainer(pdc -> {
            pdc.set(specialAbilityChargedKey, PersistentDataType.BOOLEAN, true);
            pdc.set(specialLastChargedTimestamp, PersistentDataType.LONG, Instant.now().getEpochSecond());
        });
    }

    private void depleteSpecialAbilityCharge(ItemStack itemStack) {
        itemStack.editPersistentDataContainer(pdc -> {
            pdc.set(specialAbilityChargedKey, PersistentDataType.BOOLEAN, false);
            pdc.set(specialLastChargedTimestamp, PersistentDataType.LONG, Instant.now().getEpochSecond());
        });
    }

    private boolean isSpecialAbilityCharged(ItemStack itemStack) {
        PersistentDataContainerView pdc = itemStack.getPersistentDataContainer();
        return pdc.getOrDefault(specialAbilityChargedKey, PersistentDataType.BOOLEAN, false);
    }

    private boolean isSpecialChargeCooldownUp(ItemStack itemStack) {
        return Instant.now().getEpochSecond() - itemStack.getPersistentDataContainer().getOrDefault(specialLastChargedTimestamp, PersistentDataType.LONG, 0L) >= COOLDOWN_SECONDS;
    }
}
