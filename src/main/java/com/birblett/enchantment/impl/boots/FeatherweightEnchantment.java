package com.birblett.enchantment.impl.boots;

import com.birblett.enchantment.OrchidEnchantWrapper;
import com.birblett.util.InputRecord;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.component.type.AttributeModifierSlot;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.Item;
import net.minecraft.registry.tag.TagKey;
import net.minecraft.util.PlayerInput;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

public class FeatherweightEnchantment extends OrchidEnchantWrapper {

    public FeatherweightEnchantment(String id, int processPriority, TagKey<Item> supportedItems, int weight, int maxLevel,
                                    Enchantment.Cost minCost, Enchantment.Cost maxCost, int anvilCost, AttributeModifierSlot... slots) {
        super(id, processPriority, supportedItems, null, weight, maxLevel, minCost, maxCost, anvilCost, slots);
    }

    @Override
    public ControlFlow onMovementTick(ClientPlayerEntity e, World world, PlayerInput input, InputRecord pressed, int level) {
        Vec3d v;
        if (!e.isTouchingWater() && !e.isOnGround() && !e.isGliding() && !e.isSneaking() && (v = e.getVelocity()).y < 0) {
            e.setVelocity(v.multiply(1.05, 0.8, 1.05));
        }
        return ControlFlow.CONTINUE;
    }

    @Override
    public ControlFlow onEntityTickEquip(LivingEntity e, World world, int level) {
        if (!e.isTouchingWater() && !e.isOnGround() && !e.isGliding() && !e.isSneaking()) {
            e.fallDistance = 0;
        }
        return ControlFlow.CONTINUE;
    }

}
