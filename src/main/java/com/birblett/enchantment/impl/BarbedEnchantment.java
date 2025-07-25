package com.birblett.enchantment.impl;

import com.birblett.enchantment.OrchidEnchantWrapper;
import net.minecraft.component.type.AttributeModifierSlot;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.projectile.FishingBobberEntity;
import net.minecraft.entity.projectile.ProjectileEntity;
import net.minecraft.item.Item;
import net.minecraft.registry.tag.TagKey;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.hit.EntityHitResult;

public class BarbedEnchantment extends OrchidEnchantWrapper {

    public BarbedEnchantment(String id, int processPriority, TagKey<Item> supportedItems, int weight, int maxLevel,
                             Enchantment.Cost minCost, Enchantment.Cost maxCost, int anvilCost, AttributeModifierSlot... slots) {
        super(id, processPriority, supportedItems, null, weight, maxLevel, minCost, maxCost, anvilCost, slots);
    }

    @Override
    public ControlFlow onProjectileEntityHit(ProjectileEntity entity, EntityHitResult result, int level) {
        if (entity instanceof FishingBobberEntity bobber && result.getEntity() instanceof LivingEntity target &&
                target.getWorld() instanceof ServerWorld world && entity.getOwner() instanceof LivingEntity owner) {
            target.damage(world, bobber.getDamageSources().mobProjectile(bobber, owner), level);
            bobber.discard();
        }
        return ControlFlow.CONTINUE;
    }

}
