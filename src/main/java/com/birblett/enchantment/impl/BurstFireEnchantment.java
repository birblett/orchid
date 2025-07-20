package com.birblett.enchantment.impl;

import com.birblett.enchantment.OrchidEnchantWrapper;
import com.birblett.entity.EntityDamageFlags;
import com.birblett.entity.Ticker;
import net.minecraft.component.type.AttributeModifierSlot;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.ProjectileEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.RangedWeaponItem;
import net.minecraft.registry.tag.TagKey;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.Hand;

public class BurstFireEnchantment extends OrchidEnchantWrapper {

    public BurstFireEnchantment(String id, int processPriority, TagKey<Item> supportedItems, int weight, int maxLevel,
                                Enchantment.Cost minCost, Enchantment.Cost maxCost, int anvilCost, AttributeModifierSlot... slots) {
        super(id, processPriority, supportedItems, null, weight, maxLevel, minCost, maxCost, anvilCost, slots);
    }

    @Override
    public Flow onProjectileFired(LivingEntity shooter, ProjectileEntity entity, ItemStack stack, ItemStack projectileStack, ServerWorld world, boolean critical, int level, Flag flag) {
        if (flag == Flag.DIRECT && critical && !Ticker.contains(shooter, BurstFireTicker.ID) && stack.getItem() instanceof RangedWeaponItem) {
            Hand hand = shooter.getMainHandStack() == stack ? Hand.MAIN_HAND : Hand.OFF_HAND;
            Ticker.set(shooter, BurstFireTicker.ID, new BurstFireTicker(shooter, stack, projectileStack, hand, level));
            if (shooter instanceof PlayerEntity player) {
                player.getItemCooldownManager().set(stack, level + 20);
            }
            ((EntityDamageFlags) entity).orchid_setIgnoreIFrames(true);
        }
        return Flow.CONTINUE;
    }

}
