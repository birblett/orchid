package com.birblett.enchantment.impl;

import com.birblett.enchantment.OrchidEnchantWrapper;
import com.birblett.entity.Ticker;
import net.minecraft.component.type.AttributeModifierSlot;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.projectile.PersistentProjectileEntity;
import net.minecraft.entity.projectile.ProjectileEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.tag.TagKey;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.hit.EntityHitResult;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.Vec3d;

public class ArrowRainEnchantment extends OrchidEnchantWrapper {

    public ArrowRainEnchantment(String id, int processPriority, TagKey<Item> supportedItems, int weight, int maxLevel,
                                Enchantment.Cost minCost, Enchantment.Cost maxCost, int anvilCost, AttributeModifierSlot... slots) {
        super(id, processPriority, supportedItems, null, weight, maxLevel, minCost, maxCost, anvilCost, slots);
    }

    @Override
    public Flow onProjectileFired(LivingEntity shooter, ProjectileEntity entity, ItemStack stack, ItemStack projectileStack, ServerWorld world, boolean critical, int level, Flag flag) {
        if (critical && flag != Flag.SUMMON && world instanceof ServerWorld) {
            Ticker.set(entity, ArrowRainTicker.ID, new ArrowRainTicker(stack, projectileStack));
        }
        return Flow.CONTINUE;
    }

    @Override
    public Flow onProjectileHit(ProjectileEntity entity, HitResult result, int level) {
        if (entity.getOwner() instanceof LivingEntity owner && owner.getWorld() instanceof ServerWorld world) {
            Ticker.apply(entity, ArrowRainTicker.ID, t -> {
                boolean critical = entity instanceof PersistentProjectileEntity p && p.isCritical() || !(entity instanceof PersistentProjectileEntity);
                if (critical && t.isDirect()) {
                    Vec3d target = result instanceof EntityHitResult entityHitResult ?
                            entityHitResult.getEntity().getPos().add(0, entityHitResult.getEntity().getHeight() / 2, 0) :
                            entity.getPos();
                    Ticker.addAnonymous(owner, new ArrowRainTicker(owner, world, target, t.weaponStack, t.projectileStack));
                } else if (result instanceof BlockHitResult && t.isSummon() && entity instanceof PersistentProjectileEntity) {
                    entity.discard();
                }
            });
        }
        return Flow.CONTINUE;
    }

    @Override
    public float projectileKnockbackMultiplier(ProjectileEntity entity, Entity target, int level) {
        return 0;
    }

}
