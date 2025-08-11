package com.birblett.enchantment.impl.boots;

import com.birblett.enchantment.OrchidEnchantWrapper;
import com.birblett.enchantment.OrchidEnchantments;
import com.birblett.util.EnchantmentUtils;
import com.birblett.util.InputRecord;
import net.minecraft.component.type.AttributeModifierSlot;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.entity.EntityStatuses;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.Item;
import net.minecraft.registry.tag.TagKey;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.PlayerInput;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.TeleportTarget;
import net.minecraft.world.World;

import java.util.Set;

public class BlinkEnchantment extends OrchidEnchantWrapper {

    public BlinkEnchantment(String id, int processPriority, TagKey<Item> supportedItems, int weight, int maxLevel,
                                Enchantment.Cost minCost, Enchantment.Cost maxCost, int anvilCost, AttributeModifierSlot... slots) {
        super(id, processPriority, supportedItems, null, weight, maxLevel, minCost, maxCost, anvilCost, slots);
    }

    @Override
    public ControlFlow onEntityTickEquip(LivingEntity e, World world, int level) {
        if (EnchantmentUtils.getTrackedLevel(e, OrchidEnchantments.BLINK) > 0) {
            EnchantmentUtils.addToTracked(e, OrchidEnchantments.BLINK, -1);
        } else {
            EnchantmentUtils.removeTracked(e, OrchidEnchantments.BLINK);
        }
        return super.onEntityTickEquip(e, world, level);
    }

    @Override
    public ControlFlow onServerPlayerInput(ServerPlayerEntity e, ServerWorld world, PlayerInput input, InputRecord pressed, int level) {
        if ((pressed.sneak() && input.sprint() || pressed.sprint() && input.sneak()) &&
                EnchantmentUtils.getTrackedLevel(e, OrchidEnchantments.BLINK) == 0) {
            EnchantmentUtils.setTracked(e, OrchidEnchantments.BLINK, 40);
            Vec3d dir = e.getRotationVector().multiply(0.1);
            Box box = e.getBoundingBox();
            Vec3d finalPos = e.getPos();
            Vec3d prev = e.getPos();
            for (int i = 0; i < 80; ++i) {
                if (!world.getBlockCollisions(null, box).iterator().hasNext()) {
                    finalPos = box.getCenter();
                    finalPos = new Vec3d(finalPos.x, box.minY, finalPos.z);
                }
                box = box.offset(dir);
            }
            e.teleportTo(new TeleportTarget(world, finalPos, e.getVelocity(), e.getYaw(), e.getPitch(), Set.of(), TeleportTarget.NO_OP));
            world.sendEntityStatus(e, EntityStatuses.ADD_PORTAL_PARTICLES);
            world.playSound(null, prev.x, prev.y, prev.z, SoundEvents.ENTITY_ENDERMAN_TELEPORT, e.getSoundCategory(),
                    1.0F, 1.0F);
        }
        return ControlFlow.CONTINUE;
    }

}
