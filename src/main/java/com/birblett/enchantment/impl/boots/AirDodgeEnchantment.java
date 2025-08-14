package com.birblett.enchantment.impl.boots;

import com.birblett.enchantment.OrchidEnchantWrapper;
import com.birblett.enchantment.OrchidEnchantments;
import com.birblett.util.EnchantmentUtils;
import com.birblett.util.InputRecord;
import com.birblett.util.VectorUtils;
import net.minecraft.block.Block;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.component.type.AttributeModifierSlot;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.Item;
import net.minecraft.registry.tag.TagKey;
import net.minecraft.util.PlayerInput;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

public class AirDodgeEnchantment extends OrchidEnchantWrapper {

    public AirDodgeEnchantment(String id, int processPriority, TagKey<Item> supportedItems, int weight, int maxLevel,
                              Enchantment.Cost minCost, Enchantment.Cost maxCost, int anvilCost, AttributeModifierSlot... slots) {
        super(id, processPriority, supportedItems, null, weight, maxLevel, minCost, maxCost, anvilCost, slots);
    }

    @Override
    public ControlFlow onMovementTick(ClientPlayerEntity e, World world, PlayerInput input, InputRecord pressed, int level) {
        int i = EnchantmentUtils.getTempLevel(e, OrchidEnchantments.AIR_DODGE);
        int dodge = i & 0xFF;
        int cd = i - dodge;
        if (cd > 0xFF) {
            if (e.isOnGround()) {
                cd = Math.min(0x500, cd);
            } else {
                if (cd > 0x1000 && e.getVelocity().lengthSquared() > 0.1 + 0.05 * level) {
                    e.setVelocity(e.getVelocity().multiply(0.7));
                }
            }
            cd -= 0x100;
        } else {
            dodge = (dodge << 1) & 0xFF;
        }
        if (pressed.sprint() && cd == 0) {
            if (dodge == 0) {
                dodge = 1;
            } else if (!e.isOnGround() ) {
                dodge = 0;
                Vec3d vel;
                double v = 0.75 + 0.25 * level;
                if (input.forward() || input.backward() || input.left() || input.right()) {
                    vel = VectorUtils.vecFromInput(e, input);
                } else {
                    vel = e.getRotationVector().multiply(1, 0, 1).normalize().multiply(v);
                }
                e.setVelocity(vel.x, input.jump() ? 1.0 + 0.5 * level : -2.0 - level, vel.z);
                cd = 0x1500;
            }
        }
        EnchantmentUtils.setTempLevel(e, OrchidEnchantments.AIR_DODGE, cd + dodge);
        return ControlFlow.CONTINUE;
    }

    @Override
    public float modifySlipperiness(LivingEntity e, World world, Block block, float slipperiness, int level) {
        return e instanceof ClientPlayerEntity c && EnchantmentUtils.getTempLevel(c, OrchidEnchantments.AIR_DODGE) > 0xFF ? 1.1f : slipperiness;
    }

    @Override
    public float modifyStepHeightDirect(LivingEntity e, World world, float s, int level) {
        return e instanceof ClientPlayerEntity c && EnchantmentUtils.getTempLevel(c, OrchidEnchantments.AIR_DODGE) > 0xFF ? s + 0.6f : s;
    }

    @Override
    public Boolean modifyCanStep(LivingEntity e, World world, int level) {
        return e instanceof ClientPlayerEntity c && EnchantmentUtils.getTempLevel(c, OrchidEnchantments.AIR_DODGE) > 0xFF ? true : null;
    }

}
