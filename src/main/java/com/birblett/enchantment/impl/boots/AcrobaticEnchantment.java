package com.birblett.enchantment.impl.boots;

import com.birblett.enchantment.OrchidEnchantWrapper;
import com.birblett.enchantment.OrchidEnchantments;
import com.birblett.util.EnchantmentUtils;
import com.birblett.util.EntityUtils;
import com.birblett.util.InputRecord;
import com.birblett.util.VectorUtils;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.component.type.AttributeModifierSlot;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.Item;
import net.minecraft.registry.tag.TagKey;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.PlayerInput;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

public class AcrobaticEnchantment extends OrchidEnchantWrapper {

    public AcrobaticEnchantment(String id, int processPriority, TagKey<Item> supportedItems, int weight, int maxLevel,
                                Enchantment.Cost minCost, Enchantment.Cost maxCost, int anvilCost, AttributeModifierSlot... slots) {
        super(id, processPriority, supportedItems, null, weight, maxLevel, minCost, maxCost, anvilCost, slots);
    }

    @Override
    public ControlFlow onEntityTickEquip(LivingEntity e, World world, int level) {
        if (e instanceof ServerPlayerEntity p && (p.getPlayerInput().sneak() && !e.isOnGround() && !e.isTouchingWater() &&
                EntityUtils.isTouchingBlock(e, 0.01, 0, 0.01) && e.getVelocity().y < 0.2)) {
            e.fallDistance = 0;
        }
        return super.onEntityTickEquip(e, world, level);
    }

    @Override
    public ControlFlow onServerPlayerInput(ServerPlayerEntity e, ServerWorld world, PlayerInput input, InputRecord pressed, int level) {
        if (input.sneak() && !e.isOnGround() && !e.isTouchingWater() &&
                EntityUtils.isTouchingBlock(e, 0.01, 0, 0.01) && e.getVelocity().y < 0.2) {
            e.fallDistance = 0;
        }
        return super.onServerPlayerInput(e, world, input, pressed, level);
    }

    @Override
    public ControlFlow onMovementTick(ClientPlayerEntity e, World world, PlayerInput input, InputRecord pressed, int level) {
        if (!e.isOnGround() && !e.isTouchingWater()) {
            Vec3d velocity = e.getVelocity();
            if (EntityUtils.isTouchingBlock(e, 0.01, 0, 0.01)) {
                if (velocity.y < 0.2) {
                    if (input.sneak()) {
                        e.setVelocity(velocity.x * 0.8, 0, velocity.z * 0.8);
                        EnchantmentUtils.setTempLevel(e, OrchidEnchantments.ACROBATIC, 1);
                    } else if (EnchantmentUtils.getTempLevel(e, OrchidEnchantments.ACROBATIC) == 1) {
                        e.setVelocity(e.getRotationVector().multiply(1.5));
                        EnchantmentUtils.setTempLevel(e, OrchidEnchantments.ACROBATIC, 0);
                    }
                }
            } else if (!e.isGliding()) {
                if (input.forward() || input.backward() || input.left() || input.right()) {
                    Vec3d vel = VectorUtils.vecFromInput(e, input);
                    double m = velocity.length() * 0.201;
                    e.setVelocity(velocity.x * 0.8 + vel.x * m, velocity.y, velocity.z * 0.8 + vel.z * m);
                }
            }
        } else {
            EnchantmentUtils.setTempLevel(e, OrchidEnchantments.ACROBATIC, 0);
        }
        return ControlFlow.CONTINUE;
    }

    @Override
    public Boolean modifyCanStep(LivingEntity e, World world, int level) {
        return !e.isOnGround() ? true : null;
    }



}
