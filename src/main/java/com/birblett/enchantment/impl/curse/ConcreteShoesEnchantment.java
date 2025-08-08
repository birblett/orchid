package com.birblett.enchantment.impl.curse;

import com.birblett.enchantment.OrchidEnchantWrapper;
import net.minecraft.component.type.AttributeModifierSlot;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.tag.TagKey;

public class ConcreteShoesEnchantment extends OrchidEnchantWrapper {

    public ConcreteShoesEnchantment(String id, int processPriority, TagKey<Item> supportedItems, int weight, int maxLevel,
                               Enchantment.Cost minCost, Enchantment.Cost maxCost, int anvilCost, AttributeModifierSlot... slots) {
        super(id, processPriority, supportedItems, null, weight, maxLevel, minCost, maxCost, anvilCost, slots);
    }

    @Override
    public Float attackModifier(LivingEntity attacker, Entity target, ItemStack stack, float damage, DamageSource source, int level) {
        if (attacker instanceof PlayerEntity p && p.getAttackCooldownProgress(0.5F) > 0.9f && p.fallDistance > 0.0
                && !p.isOnGround() && !p.isClimbing() && !p.isTouchingWater() && !p.hasStatusEffect(StatusEffects.BLINDNESS)
                && !p.hasVehicle() && target instanceof LivingEntity && !p.isSprinting()) {
            damage *= 5f/3;
        }
        return damage;
    }

}
