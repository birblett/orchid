package com.birblett.enchantment.impl.boots;

import com.birblett.Orchid;
import com.birblett.enchantment.OrchidEnchantWrapper;
import com.birblett.enchantment.OrchidEnchantments;
import com.birblett.util.EnchantmentUtils;
import com.birblett.util.InputRecord;
import net.minecraft.client.input.Input;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.component.type.AttributeModifierSlot;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.item.Item;
import net.minecraft.registry.tag.TagKey;
import net.minecraft.util.PlayerInput;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

public class HoverEnchantment extends OrchidEnchantWrapper {

    public HoverEnchantment(String id, int processPriority, TagKey<Item> supportedItems, int weight, int maxLevel,
                            Enchantment.Cost minCost, Enchantment.Cost maxCost, int anvilCost, AttributeModifierSlot... slots) {
        super(id, processPriority, supportedItems, null, weight, maxLevel, minCost, maxCost, anvilCost, slots);
    }

    @Override
    public ControlFlow onMovementTick(ClientPlayerEntity e, World world, Input input, InputRecord pressed, int level) {
        if (!e.isOnGround()) {
            if (pressed.sneak()) {
                if (EnchantmentUtils.getTempLevel(e, OrchidEnchantments.HOVER) == 0) {
                    EnchantmentUtils.setTempLevel(e, OrchidEnchantments.HOVER, (level) * 40 + 1);
                } else {
                    EnchantmentUtils.setTempLevel(e, OrchidEnchantments.HOVER, 1);
                }
            }
            if (EnchantmentUtils.getTempLevel(e, OrchidEnchantments.HOVER) > 1) {
                Vec3d vel = Vec3d.ZERO;
                PlayerInput in = input.playerInput;
                if (in.forward() || in.backward() || in.left() || in.right()) {
                    Vec3d base = e.getRotationVector().multiply(1e17, 0, 1e17).normalize();
                    if (in.forward()) {
                        vel = vel.add(base);
                    }
                    if (in.backward()) {
                        vel = vel.add(new Vec3d(-base.x, 0, -base.z));
                    }
                    if (in.left()) {
                        vel = vel.add(new Vec3d(base.z, 0, -base.x));
                    }
                    if (in.right()) {
                        vel = vel.add(new Vec3d(-base.z, 0, base.x));
                    }
                    vel = vel.normalize().multiply(0.5);
                }
                e.setVelocity(vel);
                EnchantmentUtils.addToTempLevel(e, OrchidEnchantments.HOVER, -1);
            }
        } else {
            EnchantmentUtils.setTempLevel(e, OrchidEnchantments.HOVER, 0);
        }
        return ControlFlow.CONTINUE;
    }

}
