package com.birblett.enchantment.impl;

import com.birblett.enchantment.OrchidEnchantWrapper;
import net.minecraft.component.type.AttributeModifierSlot;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.projectile.ProjectileEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.tag.TagKey;
import net.minecraft.server.world.ServerWorld;

public class AerodynamicEnchantment extends OrchidEnchantWrapper {

    public AerodynamicEnchantment(String id, int processPriority, TagKey<Item> supportedItems, int weight, int maxLevel,
                                  Enchantment.Cost minCost, Enchantment.Cost maxCost, int anvilCost, AttributeModifierSlot... slots) {
        super(id, processPriority, supportedItems, null, weight, maxLevel, minCost, maxCost, anvilCost, slots);
    }

    @Override
    public Flow onProjectileFired(LivingEntity shooter, ProjectileEntity entity, ItemStack stack, ItemStack projectileStack, ServerWorld world, boolean critical, int level, Flag flag) {
        entity.setVelocity(entity.getVelocity().multiply(1 + level * 0.05));
        return Flow.CONTINUE;
    }

    @Override
    public double projectileGravityModifier(ProjectileEntity entity, double gravity, int level) {
        double gravDiff = gravity / 6.0 * level;
        return gravity - gravDiff;
    }

    @Override
    public double projectileDragModifier(ProjectileEntity entity, double drag, int level) {
        double dragDiff = (1.0 - drag) / 5.0 * level;
        return drag + dragDiff;
    }

}
