package com.birblett.enchantment.impl;

import com.birblett.enchantment.OrchidEnchantWrapper;
import com.birblett.enchantment.OrchidEnchantments;
import com.birblett.util.EnchantmentUtils;
import net.minecraft.component.type.AttributeModifierSlot;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.registry.tag.TagKey;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.stat.Stats;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraft.world.explosion.AdvancedExplosionBehavior;

import java.util.Optional;

public class WindRiderEnchantment extends OrchidEnchantWrapper {

    public WindRiderEnchantment(String id, int processPriority, TagKey<Item> supportedItems, int weight, int maxLevel,
                                Enchantment.Cost minCost, Enchantment.Cost maxCost, int anvilCost, AttributeModifierSlot... slots) {
        super(id, processPriority, supportedItems, null, weight, maxLevel, minCost, maxCost, anvilCost, slots);
    }

    @Override
    public ActionResult onUse(PlayerEntity user, ItemStack stack, World world, Hand hand, int level) {
        if (EnchantmentUtils.getTrackedLevel(user, OrchidEnchantments.WIND_RIDER) == -1) {
            EnchantmentUtils.setTracked(user, OrchidEnchantments.WIND_RIDER, 1);
        }
        if (!stack.willBreakNextUse() && !user.isTouchingWaterOrRain() && (user.isOnGround() ||
                EnchantmentUtils.getTrackedLevel(user, OrchidEnchantments.WIND_RIDER) > 0)) {
            user.setCurrentHand(hand);
            return ActionResult.CONSUME;
        } else {
            return ActionResult.FAIL;
        }
    }

    @Override
    public Boolean onStoppedUsing(LivingEntity user, ItemStack stack, World world, int remainingUseTicks, int level) {
        if (stack.getItem().getMaxUseTime(stack, user) - remainingUseTicks > 10  && user instanceof PlayerEntity p && !user.isTouchingWaterOrRain()) {
            boolean b;
            if ((b = user.isOnGround()) || EnchantmentUtils.trackedContains(user, OrchidEnchantments.WIND_RIDER)) {
                Vec3d v = p.getRotationVector();
                if (b) {
                    if (!world.isClient) {
                        p.incrementStat(Stats.USED.getOrCreateStat(stack.getItem()));
                        p.addVelocity(new Vec3d(v.x, 0, v.z).normalize().add(0, 2, 0).normalize().add(0, 0.5, 0));
                        p.velocityModified = true;
                        world.createExplosion(user, null, new AdvancedExplosionBehavior(false, false,
                                        Optional.of(0.0f), Optional.empty()), user.getX(), user.getY(),
                                user.getZ(), 1.2f, false, World.ExplosionSourceType.TRIGGER, ParticleTypes.GUST_EMITTER_SMALL,
                                ParticleTypes.GUST_EMITTER_LARGE, SoundEvents.ENTITY_WIND_CHARGE_WIND_BURST);
                        EnchantmentUtils.setTracked(user, OrchidEnchantments.WIND_RIDER, -1);
                    }
                } else {
                    p.incrementStat(Stats.USED.getOrCreateStat(stack.getItem()));
                    int i = EnchantmentUtils.getTrackedLevel(user, OrchidEnchantments.WIND_RIDER);
                    p.useRiptide(20, i * 8, stack);
                    p.addVelocity(v.multiply(2.4));
                    if (user instanceof ServerPlayerEntity serverPlayerEntity) {
                        serverPlayerEntity.currentExplosionImpactPos = user.getPos();
                        serverPlayerEntity.setIgnoreFallDamageFromCurrentExplosion(true);
                    }
                    world.playSoundFromEntity(null, user, SoundEvents.ITEM_TRIDENT_RIPTIDE_1.value(), SoundCategory.PLAYERS, 1.0F, 1.0F);
                    if (i >= level) {
                        EnchantmentUtils.removeTracked(user, OrchidEnchantments.WIND_RIDER);
                    } else {
                        EnchantmentUtils.setTracked(user, OrchidEnchantments.WIND_RIDER, i + 1);
                    }
                }
                return true;
            }
        }
        return false;
    }

    @Override
    public ControlFlow onEntityTick(Entity e, World world, int level) {
        if (!world.isClient) {
            if (level == -1) {
                EnchantmentUtils.setTracked(e, OrchidEnchantments.WIND_RIDER, 1);
            } else if (e.isOnGround()) {
                EnchantmentUtils.removeTracked(e, OrchidEnchantments.WIND_RIDER);
            }
        }
        return ControlFlow.CONTINUE;
    }

    @Override
    public Float attackModifier(LivingEntity attacker, Entity target, ItemStack stack, float damage, DamageSource source, int level) {
        if (attacker.isUsingRiptide() && EnchantmentUtils.trackedContains(attacker, OrchidEnchantments.WIND_RIDER)) {
            Vec3d v = attacker.getVelocity();
            attacker.addVelocity(v.x, v.y * 2, v.z);
            attacker.getWorld().createExplosion(attacker, null, new AdvancedExplosionBehavior(false, false,
                            Optional.of(0.0f), Optional.empty()), attacker.getX(), attacker.getY(),
                    attacker.getZ(), 1.2f, false, World.ExplosionSourceType.TRIGGER, ParticleTypes.GUST_EMITTER_SMALL,
                    ParticleTypes.GUST_EMITTER_LARGE, SoundEvents.ENTITY_WIND_CHARGE_WIND_BURST);
            if (attacker instanceof ServerPlayerEntity serverPlayerEntity) {
                serverPlayerEntity.currentExplosionImpactPos = attacker.getPos();
                serverPlayerEntity.setIgnoreFallDamageFromCurrentExplosion(true);
            }
        }
        return damage;
    }

}
