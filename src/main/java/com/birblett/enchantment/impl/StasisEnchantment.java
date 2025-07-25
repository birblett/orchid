package com.birblett.enchantment.impl;

import com.birblett.enchantment.OrchidEnchantWrapper;
import com.birblett.enchantment.OrchidEnchantments;
import com.birblett.interfaces.entity.ProjectileFlags;
import com.birblett.entity.Ticker;
import com.birblett.util.EnchantmentUtils;
import net.minecraft.component.type.AttributeModifierSlot;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.projectile.PersistentProjectileEntity;
import net.minecraft.entity.projectile.ProjectileEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.tag.TagKey;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.Hand;
import net.minecraft.world.World;

public class StasisEnchantment extends OrchidEnchantWrapper {

    public StasisEnchantment(String id, int processPriority, TagKey<Item> supportedItems, int weight, int maxLevel,
                              Enchantment.Cost minCost, Enchantment.Cost maxCost, int anvilCost, AttributeModifierSlot... slots) {
        super(id, processPriority, supportedItems, null, weight, maxLevel, minCost, maxCost, anvilCost, slots);
    }

    @Override
    public ControlFlow mainhandAttackAttempt(LivingEntity attacker, int level) {
        Ticker.apply(attacker, StasisTicker.ID, t -> t.fireAll(true));
        return ControlFlow.CONTINUE;
    }

    @Override
    public ControlFlow offhandAttackAttempt(LivingEntity attacker, int level) {
        Ticker.apply(attacker, StasisTicker.ID, t -> t.fireAll(true));
        return ControlFlow.CONTINUE;
    }

    @Override
    public ControlFlow onProjectileFired(LivingEntity shooter, ProjectileEntity entity, ItemStack stack, ItemStack projectileStack, ServerWorld world, boolean critical, int level, Flag flag) {
        if (entity != null && flag == Flag.DIRECT) {
            StasisTicker t;
            if (!Ticker.contains(shooter, StasisTicker.ID)) {
                Hand hand = shooter.getMainHandStack() == stack ? Hand.MAIN_HAND : Hand.OFF_HAND;
                Ticker.set(shooter, StasisTicker.ID, t = new StasisTicker(shooter, stack, projectileStack, hand));
            } else {
                t = Ticker.get(shooter, StasisTicker.ID);
            }
            t.addStasisedProjectile(entity, entity.getVelocity(), critical);
            Ticker.set(entity, StasisTicker.ID, new StasisTicker(entity));
            ProjectileFlags.setIgnoreIFrames(entity, true);
            if (entity instanceof PersistentProjectileEntity p) {
                p.setCritical(false);
            }
            return ControlFlow.BREAK;
        }
        return ControlFlow.CONTINUE;
    }

    @Override
    public ControlFlow onProjectileTick(ProjectileEntity entity, World world, int level) {
        if (world instanceof ServerWorld && (entity.getOwner() == null || !Ticker.contains(entity, StasisTicker.ID))) {
            EnchantmentUtils.removeTracked(entity, OrchidEnchantments.STASIS);
        }
        return EnchantmentUtils.trackedContains(entity, OrchidEnchantments.STASIS) ? ControlFlow.CANCEL_AFTER : ControlFlow.CONTINUE;
    }

}
