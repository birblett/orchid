package com.birblett.enchantment.impl;

import com.birblett.enchantment.OrchidEnchantWrapper;
import com.birblett.enchantment.OrchidEnchantments;
import com.birblett.entity.Ticker;
import com.birblett.util.EnchantmentUtils;
import com.birblett.util.VectorUtils;
import net.minecraft.component.type.AttributeModifierSlot;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.projectile.ProjectileEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.tag.TagKey;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.hit.EntityHitResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

public class MarkedEnchantment extends OrchidEnchantWrapper {

    public MarkedEnchantment(String id, int processPriority, TagKey<Item> supportedItems, int weight, int maxLevel,
                             Enchantment.Cost minCost, Enchantment.Cost maxCost, int anvilCost, AttributeModifierSlot... slots) {
        super(id, processPriority, supportedItems, null, weight, maxLevel, minCost, maxCost, anvilCost, slots);
    }

    @Override
    public Flow onProjectileFired(LivingEntity shooter, ProjectileEntity entity, ItemStack stack, ItemStack projectileStack, ServerWorld world, boolean critical, int level, Flag flag) {
        MarkedTicker t;
        if ((t = Ticker.get(shooter, MarkedTicker.ID)) != null && t.getTarget() != null) {
            EnchantmentUtils.setTracked(entity, OrchidEnchantments.MARKED, t.getTarget().getId());
        } else {
            EnchantmentUtils.setTracked(entity, OrchidEnchantments.MARKED, -1);
        }
        return Flow.CONTINUE;
    }

    @Override
    public Flow onProjectileTick(ProjectileEntity entity, World world, int level) {
        if (EnchantmentUtils.trackedContains(entity, OrchidEnchantments.MARKED)) {
            int id = EnchantmentUtils.getTrackedLevel(entity, OrchidEnchantments.MARKED);
            if (id != -1 && world.getEntityById(id) instanceof Entity e && e.isAlive()) {
                Vec3d velocity = entity.getVelocity();
                double d = velocity.length();
                Vec3d target = e.getPos().add(0, e.getHeight() / 2, 0).subtract(entity.getPos());
                entity.setVelocity(VectorUtils.rotateTowards(velocity, target, 0.1).multiply(d));
            }
        }
        return Flow.CONTINUE;
    }

    @Override
    public Flow onProjectileEntityHit(ProjectileEntity entity, EntityHitResult result, int level) {
        if (entity.getOwner() instanceof LivingEntity owner) {
            MarkedTicker t;
            if ((t = Ticker.get(owner, MarkedTicker.ID)) != null) {
                t.setTarget(result.getEntity());
            } else {
                Ticker.set(owner, MarkedTicker.ID, new MarkedTicker(owner, result.getEntity()));
            }
        }
        return Flow.CONTINUE;
    }

}
