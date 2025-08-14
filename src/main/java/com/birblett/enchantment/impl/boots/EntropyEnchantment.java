package com.birblett.enchantment.impl.boots;

import com.birblett.Orchid;
import com.birblett.enchantment.OrchidEnchantWrapper;
import com.birblett.enchantment.OrchidEnchantments;
import com.birblett.util.EnchantmentUtils;
import com.birblett.util.EntityUtils;
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

public class EntropyEnchantment extends OrchidEnchantWrapper {

    public EntropyEnchantment(String id, int processPriority, TagKey<Item> supportedItems, int weight, int maxLevel,
                              Enchantment.Cost minCost, Enchantment.Cost maxCost, int anvilCost, AttributeModifierSlot... slots) {
        super(id, processPriority, supportedItems, null, weight, maxLevel, minCost, maxCost, anvilCost, slots);
    }

    @Override
    public ControlFlow onMovementTick(ClientPlayerEntity e, World world, PlayerInput input, InputRecord pressed, int level) {
        if (e.isOnGround()) {
            if (input.forward() || input.backward() || input.left() || input.right()) {
                Vec3d velocity = e.getVelocity();
                double m = velocity.length() * 0.1;
                Vec3d vel = VectorUtils.vecFromInput(e, input);
                e.setVelocity(velocity.x * 0.9 + vel.x * m, velocity.y, velocity.z * 0.9 + vel.z * m);
                m = e.getVelocity().length();
                double max = 0.8 + 0.2 * level;
                if (m > max) {
                    e.setVelocity(e.getVelocity().multiply(max / m));
                }
            }
        }
        return ControlFlow.CONTINUE;
    }

    @Override
    public float modifySlipperiness(LivingEntity e, World world, Block block, float slipperiness, int level) {
        return Math.max(slipperiness, 1.09890109300613f);
    }

    @Override
    public float modifySlipperyMovementSpeed(LivingEntity e, World world, float movementSpeed, float slipperiness, int level) {
        return movementSpeed * 0.3f;
    }
}
