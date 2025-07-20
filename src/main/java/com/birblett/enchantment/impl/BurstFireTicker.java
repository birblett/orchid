package com.birblett.enchantment.impl;

import com.birblett.entity.Ticker;
import com.birblett.mixin.accessor.RangedWeaponItemAccessor;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.CrossbowItem;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.Hand;

public class BurstFireTicker extends Ticker {

    public static ID<BurstFireTicker> ID = new ID<>("burst_fire");

    private final ItemStack weaponStack;
    private final ItemStack projectileStack;
    private final Hand hand;
    private int ticks;
    private final int ticksDelayed;

    public BurstFireTicker(LivingEntity entity, ItemStack weaponStack, ItemStack projectileStack, Hand hand, int level) {
        super(entity);
        this.weaponStack = weaponStack;
        this.projectileStack = projectileStack;
        this.hand = hand;
        this.ticksDelayed = Math.ceilDiv(8, level + 1);
        this.ticks = this.ticksDelayed * (level + 1);
    }

    @Override
    public void tick() {
        if ((--this.ticks) % this.ticksDelayed == 0 && this.entity instanceof LivingEntity e && this.entity.getWorld() instanceof ServerWorld world) {
            float speed;
            if (this.weaponStack.getItem() instanceof CrossbowItem) {
                speed = this.projectileStack.isOf(Items.FIREWORK_ROCKET) ? 1.6F : 3.15F;
            } else {
                speed = 3.0F;
            }
            ((RangedWeaponItemAccessor) weaponStack.getItem()).orchid_shootAll(world, e, this.hand, this.weaponStack,
                    RangedWeaponItemAccessor.orchid_load(this.weaponStack, this.projectileStack, e), speed, 1, true, null);
        }
    }

    @Override
    public boolean shouldRemove() {
        return !(this.entity instanceof LivingEntity e) || e.getStackInHand(this.hand) != this.weaponStack || this.ticks <= 0;
    }

}
