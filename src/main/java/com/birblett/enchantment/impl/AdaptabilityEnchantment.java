package com.birblett.enchantment.impl;

import com.birblett.enchantment.OrchidEnchantWrapper;
import com.birblett.entity.ProjectileFlags;
import net.minecraft.component.type.AttributeModifierSlot;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.projectile.ProjectileEntity;
import net.minecraft.entity.projectile.SmallFireballEntity;
import net.minecraft.entity.projectile.WitherSkullEntity;
import net.minecraft.entity.projectile.thrown.SnowballEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.registry.tag.TagKey;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.Vec3d;

public class AdaptabilityEnchantment extends OrchidEnchantWrapper {

    public AdaptabilityEnchantment(String id, int processPriority, TagKey<Item> supportedItems, int weight, int maxLevel,
                                   Enchantment.Cost minCost, Enchantment.Cost maxCost, int anvilCost, AttributeModifierSlot... slots) {
        super(id, processPriority, supportedItems, null, weight, maxLevel, minCost, maxCost, anvilCost, slots);
    }

    @Override
    public boolean allowProjectileType(LivingEntity entity, ItemStack weapon, ItemStack stack) {
        return stack.isOf(Items.FIRE_CHARGE) || stack.isOf(Items.WITHER_SKELETON_SKULL) || stack.isOf(Items.SNOWBALL);
    }

    @Override
    public ProjectileEntity getProjectileFromItem(LivingEntity entity, ItemStack weapon, ItemStack stack) {
        ProjectileEntity p = null;
        if (stack.isOf(Items.FIRE_CHARGE)) {
            p = new SmallFireballEntity(entity.getWorld(), entity, Vec3d.ZERO);
            p.setPosition(entity.getEyePos());
            ProjectileFlags.setLifetime(p, 40);
        } else if (stack.isOf(Items.WITHER_SKELETON_SKULL)) {
            p = new WitherSkullEntity(entity.getWorld(), entity, Vec3d.ZERO);
            p.setPosition(entity.getEyePos());
            ProjectileFlags.setLifetime(p, 35);
        } else if (stack.isOf(Items.SNOWBALL)) {
            p = new SnowballEntity(entity.getWorld(), entity, stack);
            p.setPosition(entity.getEyePos());
        }
        return p;
    }

    @Override
    public Flow onProjectileFired(LivingEntity shooter, ProjectileEntity entity, ItemStack stack, ItemStack projectileStack, ServerWorld world, boolean critical, int level, Flag flag) {
        if (stack.isOf(Items.FIRE_CHARGE)) {
            entity.setVelocity(entity.getVelocity().multiply(0.5));
        } else if (stack.isOf(Items.WITHER_SKELETON_SKULL)) {
            entity.setVelocity(entity.getVelocity().multiply(0.7));
        }
        return Flow.CONTINUE;
    }

}
