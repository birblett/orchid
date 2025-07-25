package com.birblett.enchantment.impl;

import com.birblett.enchantment.OrchidEnchantWrapper;
import net.minecraft.component.type.AttributeModifierSlot;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.projectile.ProjectileEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.registry.tag.TagKey;
import net.minecraft.server.world.ServerWorld;

public class HitscanEnchantment extends OrchidEnchantWrapper {

    public HitscanEnchantment(String id, int processPriority, TagKey<Item> supportedItems, int weight, int maxLevel,
                                Enchantment.Cost minCost, Enchantment.Cost maxCost, int anvilCost, AttributeModifierSlot... slots) {
        super(id, processPriority, supportedItems, null, weight, maxLevel, minCost, maxCost, anvilCost, slots);
    }

    @Override
    public ControlFlow onProjectileFired(LivingEntity shooter, ProjectileEntity entity, ItemStack stack, ItemStack projectileStack, ServerWorld world, boolean critical, int level, Flag flag) {
        if (entity != null && critical) {
            entity.lastRenderX = entity.getX();
            entity.lastRenderY = entity.getY();
            entity.lastRenderZ = entity.getZ();
            for (int i = 0; i < 22 * level; i++) {
                entity.resetPosition();
                entity.tick();
                double x = entity.getX();
                double y = entity.getY();
                double z = entity.getZ();
                entity.lastRenderX = x;
                entity.lastRenderY = y;
                entity.lastRenderZ = z;
                double dx = entity.getVelocity().x;
                double dy = entity.getVelocity().y;
                double dz = entity.getVelocity().z;
                world.spawnParticles(ParticleTypes.ELECTRIC_SPARK, x, y, z, 1, 0, 0, 0, 0);
                world.spawnParticles(ParticleTypes.ELECTRIC_SPARK, x - dx * 0.333, y - dy * 0.333, z - dz * 0.333, 1, 0, 0, 0, 0);
                world.spawnParticles(ParticleTypes.ELECTRIC_SPARK, x - dx * 0.666, y - dy * 0.666, z - dz * 0.666, 1, 0, 0, 0, 0);
                if (entity.isRemoved() || entity.isOnGround()) break;
            }
        }
        return ControlFlow.CONTINUE;
    }

}
