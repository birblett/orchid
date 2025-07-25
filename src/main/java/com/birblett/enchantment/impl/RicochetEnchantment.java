package com.birblett.enchantment.impl;

import com.birblett.enchantment.OrchidEnchantWrapper;
import com.birblett.enchantment.OrchidEnchantments;
import com.birblett.mixin.accessor.PersistentProjectileAccessor;
import com.birblett.util.EnchantmentUtils;
import net.minecraft.component.type.AttributeModifierSlot;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.entity.projectile.PersistentProjectileEntity;
import net.minecraft.entity.projectile.ProjectileEntity;
import net.minecraft.item.Item;
import net.minecraft.registry.tag.TagKey;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.Vec3d;

public class RicochetEnchantment extends OrchidEnchantWrapper {

    public RicochetEnchantment(String id, int processPriority, TagKey<Item> supportedItems, int weight, int maxLevel,
                               Enchantment.Cost minCost, Enchantment.Cost maxCost, int anvilCost, AttributeModifierSlot... slots) {
        super(id, processPriority, supportedItems, null, weight, maxLevel, minCost, maxCost, anvilCost, slots);
    }

    @Override
    public ControlFlow onProjectileBlockHit(ProjectileEntity entity, BlockHitResult result, int level) {
        level = EnchantmentUtils.getTrackedLevel(entity, OrchidEnchantments.RICOCHET);
        if (level > (entity.getWorld().isClient ? -1 : 0)) {
            EnchantmentUtils.addToTracked(entity, OrchidEnchantments.RICOCHET, -1);
            Vec3d reflectionAxis = result.getSide().getDoubleVector();
            Vec3d velocity = entity.getVelocity();
            Vec3d res = velocity.subtract(reflectionAxis.multiply(2 * velocity.dotProduct(reflectionAxis)));
            entity.setVelocity(res.multiply(0.9));
            entity.setPosition(entity.getPos().add(entity.getVelocity()));
            entity.setAngles(entity.getYaw(), entity.getPitch());
            if (entity instanceof PersistentProjectileEntity e) {
                e.setDamage(((PersistentProjectileAccessor) e).orchid_damage() + 0.5);
            }
            return ControlFlow.CANCEL_BREAK;
        }
        return ControlFlow.CONTINUE;
    }

}
