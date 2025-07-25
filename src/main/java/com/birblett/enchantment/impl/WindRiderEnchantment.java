package com.birblett.enchantment.impl;

import com.birblett.enchantment.OrchidEnchantWrapper;
import com.birblett.enchantment.OrchidEnchantments;
import com.birblett.util.EnchantmentUtils;
import net.minecraft.component.type.AttributeModifierSlot;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.tag.TagKey;
import net.minecraft.stat.Stats;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.world.World;

public class WindRiderEnchantment extends OrchidEnchantWrapper {

    public WindRiderEnchantment(String id, int processPriority, TagKey<Item> supportedItems, int weight, int maxLevel,
                                Enchantment.Cost minCost, Enchantment.Cost maxCost, int anvilCost, AttributeModifierSlot... slots) {
        super(id, processPriority, supportedItems, null, weight, maxLevel, minCost, maxCost, anvilCost, slots);
    }

    @Override
    public ActionResult onUse(PlayerEntity user, ItemStack stack, World world, Hand hand) {
        if (!stack.willBreakNextUse() && !user.isTouchingWaterOrRain() && (user.isOnGround() ||
                EnchantmentUtils.trackedContains(user, OrchidEnchantments.WIND_RIDER))) {
            user.setCurrentHand(hand);
            return ActionResult.CONSUME;
        } else {
            return ActionResult.FAIL;
        }
    }

    @Override
    public Boolean onStoppedUsing(LivingEntity user, ItemStack stack, World world, int remainingUseTicks) {
        if (stack.getItem().getMaxUseTime(stack, user) - remainingUseTicks > 10  && user instanceof PlayerEntity p && !user.isTouchingWaterOrRain()) {
            boolean b;
            if ((b = user.isOnGround()) || EnchantmentUtils.trackedContains(user, OrchidEnchantments.WIND_RIDER)) {
                double strength = b ? 1.5 : 3.0;
                p.useRiptide(20, 8.0F, stack);
                p.addVelocity(p.getRotationVector().multiply(strength));
                p.incrementStat(Stats.USED.getOrCreateStat(stack.getItem()));
                if (b) {
                    EnchantmentUtils.addToTracked(user, OrchidEnchantments.WIND_RIDER, 1);
                } else {
                    EnchantmentUtils.removeTracked(user, OrchidEnchantments.WIND_RIDER);
                }
                return true;
            }
        }
        return false;
    }

    @Override
    public ControlFlow onEntityTick(Entity e, World world, int level) {
        if (e.isOnGround()) {
            EnchantmentUtils.removeTracked(e, OrchidEnchantments.WIND_RIDER);
        }
        return ControlFlow.CONTINUE;
    }
}
