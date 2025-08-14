package com.birblett.enchantment.impl.boots;

import com.birblett.enchantment.OrchidEnchantWrapper;
import com.birblett.enchantment.OrchidEnchantments;
import com.birblett.util.EnchantmentUtils;
import com.birblett.util.InputRecord;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.component.type.AttributeModifierSlot;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.item.Item;
import net.minecraft.registry.tag.TagKey;
import net.minecraft.util.PlayerInput;
import net.minecraft.world.World;

public class DoubleJumpEnchantment extends OrchidEnchantWrapper {

    public DoubleJumpEnchantment(String id, int processPriority, TagKey<Item> supportedItems, int weight, int maxLevel,
                                Enchantment.Cost minCost, Enchantment.Cost maxCost, int anvilCost, AttributeModifierSlot... slots) {
        super(id, processPriority, supportedItems, null, weight, maxLevel, minCost, maxCost, anvilCost, slots);
    }

    @Override
    public ControlFlow onMovementTick(ClientPlayerEntity e, World world, PlayerInput input, InputRecord pressed, int level) {
        if (!e.isOnGround()) {
            if (EnchantmentUtils.getTempLevel(e, OrchidEnchantments.DOUBLE_JUMP) > 0 && pressed.jump()) {
                e.jump();
                EnchantmentUtils.addToTempLevel(e, OrchidEnchantments.DOUBLE_JUMP, -1);
            }
        } else {
            EnchantmentUtils.setTempLevel(e, OrchidEnchantments.DOUBLE_JUMP, level);
        }
        return ControlFlow.CONTINUE;
    }

}
