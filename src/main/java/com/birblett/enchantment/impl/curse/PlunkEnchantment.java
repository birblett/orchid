package com.birblett.enchantment.impl.curse;

import com.birblett.enchantment.OrchidEnchantWrapper;
import com.birblett.util.VectorUtils;
import net.minecraft.component.type.AttributeModifierSlot;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.projectile.ProjectileEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.tag.TagKey;
import net.minecraft.server.world.ServerWorld;

public class PlunkEnchantment extends OrchidEnchantWrapper {

    public PlunkEnchantment(String id, int processPriority, TagKey<Item> supportedItems, int weight, int maxLevel,
                            Enchantment.Cost minCost, Enchantment.Cost maxCost, int anvilCost, AttributeModifierSlot... slots) {
        super(id, processPriority, supportedItems, null, weight, maxLevel, minCost, maxCost, anvilCost, slots);
    }

    @Override
    public Flow onProjectileFired(LivingEntity shooter, ProjectileEntity entity, ItemStack stack, ItemStack projectileStack, ServerWorld world, boolean critical, int level, Flag flag) {
        entity.setVelocity(VectorUtils.applyDivergence(entity.getVelocity(), 0.13).multiply(entity.getVelocity().length()));
        return Flow.CONTINUE;
    }

    @Override
    public double projectileGravityModifier(ProjectileEntity entity, double gravity, int level) {
        double gravDiff = gravity * 4 * level;
        return gravity + gravDiff;
    }

    @Override
    public double projectileDragModifier(ProjectileEntity entity, double drag, int level) {
        double dragDiff = (1.0 - drag) * level;
        return drag + dragDiff;
    }

}
