package com.birblett.enchantment.impl;

import com.birblett.enchantment.OrchidEnchantWrapper;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.component.type.AttributeModifierSlot;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.Item;
import net.minecraft.registry.tag.TagKey;
import net.minecraft.world.World;

public class SlimedEnchantment extends OrchidEnchantWrapper {

    public SlimedEnchantment(String id, int processPriority, TagKey<Item> supportedItems, int weight, int maxLevel,
                                 Enchantment.Cost minCost, Enchantment.Cost maxCost, int anvilCost, AttributeModifierSlot... slots) {
        super(id, processPriority, supportedItems, null, weight, maxLevel, minCost, maxCost, anvilCost, slots);
    }

    @Override
    public Block onBlockLand(LivingEntity e, World world, Block block, int level) {
        if (e.getVelocity().y < 0) {
            return Blocks.SLIME_BLOCK;
        }
        return null;
    }

    @Override
    public BlockState onBlockFeedback(LivingEntity e, World world, BlockState state, int level) {
        return Blocks.SLIME_BLOCK.getDefaultState();
    }

    @Override
    public float modifySlipperiness(LivingEntity e, World world, Block block, float slipperiness, int level) {
        return slipperiness + 0.2f;
    }

    @Override
    public float modifySlipperyMovementSpeed(LivingEntity e, World world, float movementSpeed, float slipperiness, int level) {
        return movementSpeed * slipperiness * slipperiness * slipperiness;
    }

}
