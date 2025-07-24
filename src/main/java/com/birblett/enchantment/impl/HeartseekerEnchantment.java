package com.birblett.enchantment.impl;

import com.birblett.enchantment.OrchidEnchantWrapper;
import com.birblett.enchantment.OrchidEnchantments;
import com.birblett.mixin.accessor.PersistentProjectileAccessor;
import com.birblett.util.EnchantmentUtils;
import com.birblett.util.VectorUtils;
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
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

public class HeartseekerEnchantment extends OrchidEnchantWrapper {

    public HeartseekerEnchantment(String id, int processPriority, TagKey<Item> supportedItems, int weight, int maxLevel,
                                  Enchantment.Cost minCost, Enchantment.Cost maxCost, int anvilCost, AttributeModifierSlot... slots) {
        super(id, processPriority, supportedItems, null, weight, maxLevel, minCost, maxCost, anvilCost, slots);
    }

    @Override
    public Flow onProjectileFired(LivingEntity shooter, ProjectileEntity entity, ItemStack stack, ItemStack projectileStack, ServerWorld world, boolean critical, int level, Flag flag) {
        if (entity instanceof PersistentProjectileEntity p) {
            p.setDamage(((PersistentProjectileAccessor) p).orchid_damage() + 2);
        }
        return Flow.CONTINUE;
    }

    @Override
    public Flow onProjectileTick(ProjectileEntity entity, World world, int level) {
        if (entity.getOwner() instanceof Entity e) {
            Vec3d velocity = entity.getVelocity();
            Vec3d target = e.getEyePos().subtract(entity.getPos());
            entity.setVelocity(VectorUtils.rotateTowards(velocity, target, level * 0.0872665).multiply(velocity.length()));
        }
        return Flow.CONTINUE;
    }

    @Override
    public Flow onProjectileHit(ProjectileEntity entity, HitResult result, int level) {
        EnchantmentUtils.removeTracked(entity, OrchidEnchantments.HEARTSEEKER);
        return Flow.CONTINUE;
    }

}
