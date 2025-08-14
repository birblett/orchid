package com.birblett.enchantment.impl.curse;

import com.birblett.enchantment.OrchidEnchantWrapper;
import com.birblett.enchantment.OrchidEnchantments;
import com.birblett.util.EnchantmentUtils;
import com.birblett.util.InputRecord;
import net.minecraft.client.network.ClientPlayerEntity;
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
import net.minecraft.util.PlayerInput;
import net.minecraft.world.World;

public class ConcreteShoesEnchantment extends OrchidEnchantWrapper {

    public ConcreteShoesEnchantment(String id, int processPriority, TagKey<Item> supportedItems, int weight, int maxLevel,
                               Enchantment.Cost minCost, Enchantment.Cost maxCost, int anvilCost, AttributeModifierSlot... slots) {
        super(id, processPriority, supportedItems, null, weight, maxLevel, minCost, maxCost, anvilCost, slots);
    }

    @Override
    public Float attackModifier(LivingEntity attacker, Entity target, ItemStack stack, float damage, DamageSource source, int level) {
        if (attacker instanceof PlayerEntity p && p.getAttackCooldownProgress(0.5F) > 0.9f &&
                (p.isInFluid() || p.fallDistance > 0.0) && !p.isOnGround() && !p.isClimbing() &&
                !p.hasStatusEffect(StatusEffects.BLINDNESS) && !p.hasVehicle() && target instanceof LivingEntity && !p.isSprinting()) {
            damage *= attacker.isInFluid() ? 1.37f : 2;
        }
        return damage;
    }

    @Override
    public Boolean shouldCrit(LivingEntity attacker, Entity target, int level) {
        return attacker.isTouchingWater() ? true : null;
    }

    @Override
    public ControlFlow onMovementTick(ClientPlayerEntity e, World world, PlayerInput input, InputRecord pressed, int level) {
        if (EnchantmentUtils.getTempLevel(e, OrchidEnchantments.CONCRETE_SHOES) > 0) {
            EnchantmentUtils.addToTempLevel(e, OrchidEnchantments.CONCRETE_SHOES, -1);
        }
        if (e.isInFluid() && !e.getAbilities().flying) {
            if (input.jump() && e.isOnGround() && EnchantmentUtils.getTempLevel(e, OrchidEnchantments.CONCRETE_SHOES) <= 0) {
                e.jump();
                EnchantmentUtils.setTempLevel(e, OrchidEnchantments.CONCRETE_SHOES, 8);
            }
            e.addVelocity(0, -0.065, 0);
        }
        return ControlFlow.CONTINUE;
    }

    @Override
    public Boolean modifyCanSwim(LivingEntity e, World world, int level) {
        return e.isTouchingWater() ? false : null;
    }

}
