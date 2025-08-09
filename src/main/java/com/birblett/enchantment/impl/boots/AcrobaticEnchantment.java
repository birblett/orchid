package com.birblett.enchantment.impl.boots;

import com.birblett.enchantment.OrchidEnchantWrapper;
import com.birblett.enchantment.OrchidEnchantments;
import com.birblett.util.EnchantmentUtils;
import com.birblett.util.EntityUtils;
import com.birblett.util.InputRecord;
import net.minecraft.client.input.Input;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.component.type.AttributeModifierSlot;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.Item;
import net.minecraft.registry.tag.TagKey;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

public class AcrobaticEnchantment extends OrchidEnchantWrapper {

    public AcrobaticEnchantment(String id, int processPriority, TagKey<Item> supportedItems, int weight, int maxLevel,
                                Enchantment.Cost minCost, Enchantment.Cost maxCost, int anvilCost, AttributeModifierSlot... slots) {
        super(id, processPriority, supportedItems, null, weight, maxLevel, minCost, maxCost, anvilCost, slots);
    }

    @Override
    public ControlFlow onMovementTick(ClientPlayerEntity e, World world, Input input, InputRecord pressed, int level) {
        Vec3d velocity;
        if (!e.isOnGround() && !e.isTouchingWater() && (velocity = e.getVelocity()).y < 0.2 &&
                EntityUtils.isTouchingBlock(e, 0.01, 0, 0.01)) {
            if (input.playerInput.sneak()) {
                e.setVelocity(velocity.x * 0.8, 0, velocity.z * 0.8);
                EnchantmentUtils.setTempLevel(e, OrchidEnchantments.ACROBATIC, 1);
            } else if (EnchantmentUtils.getTempLevel(e, OrchidEnchantments.ACROBATIC) == 1) {
                e.setVelocity(e.getRotationVector().multiply(1.5));
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
