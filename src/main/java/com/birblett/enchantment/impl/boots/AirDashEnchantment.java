package com.birblett.enchantment.impl.boots;

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
import net.minecraft.world.World;

public class AirDashEnchantment extends OrchidEnchantWrapper {

    public AirDashEnchantment(String id, int processPriority, TagKey<Item> supportedItems, int weight, int maxLevel,
                                Enchantment.Cost minCost, Enchantment.Cost maxCost, int anvilCost, AttributeModifierSlot... slots) {
        super(id, processPriority, supportedItems, null, weight, maxLevel, minCost, maxCost, anvilCost, slots);
    }

    @Override
    public ControlFlow onMovementTick(ClientPlayerEntity e, World world, Input input, InputRecord pressed, int level) {
        int i = EnchantmentUtils.getTempLevel(e, OrchidEnchantments.DASH);
        int dash = i & 0xFF;
        int cd = i - dash;
        if (cd > 0xFF) {
            cd -= 0x100;
        } else {
            dash = dash == 9 ? 0 : (dash << 1) & 0xFF;
        }
        if (pressed.forward() && cd == 0 && dash != 9) {
            if (dash == 0) {
                dash += 1;
            } else {
                dash = 9;
                e.setVelocity(e.getRotationVector().multiply(0.9 + level * 0.2).add(e.getVelocity().multiply(0.3)));
                cd = 0x1A00;
            }
        }
        EnchantmentUtils.setTempLevel(e, OrchidEnchantments.DASH, cd + dash);
        return ControlFlow.CONTINUE;
    }

}
