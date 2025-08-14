package com.birblett.enchantment.impl.boots;

import com.birblett.enchantment.OrchidEnchantWrapper;
import net.minecraft.component.type.AttributeModifierSlot;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.Item;
import net.minecraft.registry.tag.TagKey;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

public class LungingEnchantment extends OrchidEnchantWrapper {

    public LungingEnchantment(String id, int processPriority, TagKey<Item> supportedItems, int weight, int maxLevel,
                               Enchantment.Cost minCost, Enchantment.Cost maxCost, int anvilCost, AttributeModifierSlot... slots) {
        super(id, processPriority, supportedItems, null, weight, maxLevel, minCost, maxCost, anvilCost, slots);
    }

    @Override
    public Vec3d modifyJumpDirect(LivingEntity e, World world, Vec3d jumpVelocity, int level) {
        return jumpVelocity.multiply(1 + level, 1, 1 + level);
    }

}
