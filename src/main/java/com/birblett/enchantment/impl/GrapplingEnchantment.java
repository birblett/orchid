package com.birblett.enchantment.impl;

import com.birblett.enchantment.OrchidEnchantWrapper;
import com.birblett.enchantment.OrchidEnchantments;
import com.birblett.entity.Ticker;
import com.birblett.mixin.accessor.PersistentProjectileAccessor;
import com.birblett.util.EnchantmentUtils;
import com.birblett.util.EntityUtils;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.component.type.AttributeModifierSlot;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.FishingBobberEntity;
import net.minecraft.entity.projectile.PersistentProjectileEntity;
import net.minecraft.entity.projectile.ProjectileEntity;
import net.minecraft.item.FishingRodItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.tag.TagKey;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

public class GrapplingEnchantment extends OrchidEnchantWrapper {

    public GrapplingEnchantment(String id, int processPriority, TagKey<Item> supportedItems, int weight, int maxLevel,
                                Enchantment.Cost minCost, Enchantment.Cost maxCost, int anvilCost, AttributeModifierSlot... slots) {
        super(id, processPriority, supportedItems, null, weight, maxLevel, minCost, maxCost, anvilCost, slots);
    }

    @Override
    public Flow mainhandAttackAttempt(LivingEntity attacker, int level) {
        Ticker.apply(attacker, GrapplingTicker.ID, GrapplingTicker::removeAll);
        return Flow.CONTINUE;
    }

    @Override
    public Flow offhandAttackAttempt(LivingEntity attacker, int level) {
        Ticker.apply(attacker, GrapplingTicker.ID, GrapplingTicker::removeAll);
        return Flow.CONTINUE;
    }

    @Override
    public Flow onProjectileFired(LivingEntity shooter, ProjectileEntity entity, ItemStack stack, ItemStack projectileStack, ServerWorld world, boolean critical, int level, Flag flag) {
        if (!critical && entity instanceof FishingBobberEntity fishingBobberEntity) {
            Vec3d pullStrength = null;
            if (EntityUtils.isTouchingBlock(entity, 0.05)) {
                pullStrength = entity.getPos().subtract(shooter.getPos()).normalize();
                if (shooter instanceof PlayerEntity player) {
                    stack.damage(1, player);
                }
            } else if (fishingBobberEntity.getHookedEntity() != null) {
                pullStrength = fishingBobberEntity.getHookedEntity().getPos().subtract(shooter.getPos()).normalize();
            }
            if (pullStrength != null) {
                double sneakMult = shooter.isSneaking() ? 1 : 1.6;
                double touchingWaterPullSpeed = shooter.isTouchingWater() ? 0.4 : 1.0;
                shooter.fallDistance = 0;
                shooter.setVelocity(shooter.getVelocity().add(pullStrength.multiply(touchingWaterPullSpeed * sneakMult)));
                shooter.velocityModified = true;
            }
        } else if (entity instanceof PersistentProjectileEntity p) {
            EnchantmentUtils.setTracked(p, OrchidEnchantments.GRAPPLING, level);
            if (Ticker.get(shooter, GrapplingTicker.ID) instanceof GrapplingTicker t) {
                t.addEntity(p);
            } else {
                Ticker.set(shooter, GrapplingTicker.ID, new GrapplingTicker(shooter, p, stack));
            }
        }
        return Flow.CONTINUE;
    }

    @Override
    public Flow onProjectileTick(ProjectileEntity entity, World world, int level) {
        if (entity instanceof FishingBobberEntity fishingBobberEntity && fishingBobberEntity.getHookedEntity() == null &&
                entity.getOwner() instanceof LivingEntity livingEntity && EntityUtils.isTouchingBlock(fishingBobberEntity, 0.05)) {
            boolean mh = livingEntity.getMainHandStack().getItem() instanceof FishingRodItem &&
                    EnchantmentUtils.hasEnchant(livingEntity.getMainHandStack(), OrchidEnchantments.GRAPPLING, world);
            boolean oh = !mh && livingEntity.getOffHandStack().getItem() instanceof FishingRodItem &&
                    EnchantmentUtils.hasEnchant(livingEntity.getOffHandStack(), OrchidEnchantments.GRAPPLING, world);
            return mh || oh ? Flow.CANCEL_AFTER : Flow.CONTINUE;
        } else if (entity instanceof PersistentProjectileEntity) {
            if (world instanceof ServerWorld && (!(entity.getOwner() instanceof LivingEntity owner) ||
                    owner.squaredDistanceTo(entity) >= 2500)) {
                EnchantmentUtils.removeTracked(entity, OrchidEnchantments.GRAPPLING);
            } else {
                if (world instanceof ClientWorld && entity instanceof PersistentProjectileEntity p &&
                        ((PersistentProjectileAccessor) p).orchid_inGround() && entity.getOwner() instanceof PlayerEntity owner) {
                    Vec3d diff = entity.getPos().subtract(owner.getPos());
                    double strength = owner.getFinalGravity() * Math.min(Math.max(0, (diff.lengthSquared() - 25) / 8), 1.5) / diff.length();
                    owner.addVelocity(diff.multiply(strength, strength * 2, strength));
                }
            }
        }
        return Flow.CONTINUE;
    }

}
