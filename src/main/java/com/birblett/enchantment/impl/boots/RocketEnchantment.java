package com.birblett.enchantment.impl.boots;

import com.birblett.enchantment.OrchidEnchantWrapper;
import com.birblett.enchantment.OrchidEnchantments;
import com.birblett.util.EnchantmentUtils;
import com.birblett.util.InputRecord;
import com.birblett.util.WorldUtils;
import net.minecraft.component.type.AttributeModifierSlot;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.entity.Entity;
import net.minecraft.entity.projectile.WindChargeEntity;
import net.minecraft.item.Item;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.registry.Registries;
import net.minecraft.registry.tag.BlockTags;
import net.minecraft.registry.tag.TagKey;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.PlayerInput;
import net.minecraft.world.World;
import net.minecraft.world.explosion.AdvancedExplosionBehavior;
import net.minecraft.world.explosion.ExplosionBehavior;

import java.util.Optional;
import java.util.function.Function;

public class RocketEnchantment extends OrchidEnchantWrapper {

    public RocketEnchantment(String id, int processPriority, TagKey<Item> supportedItems, int weight, int maxLevel,
                                 Enchantment.Cost minCost, Enchantment.Cost maxCost, int anvilCost, AttributeModifierSlot... slots) {
        super(id, processPriority, supportedItems, null, weight, maxLevel, minCost, maxCost, anvilCost, slots);
    }

    private static final ExplosionBehavior EXPLOSION_BEHAVIOR = new AdvancedExplosionBehavior(true, false,
            Optional.of(1.22f), Registries.BLOCK.getOptional(BlockTags.BLOCKS_WIND_CHARGE_EXPLOSIONS).map(Function.identity()));

    @Override
    public ControlFlow onEntityTick(Entity e, World world, int level) {
        if (world instanceof ServerWorld sw) {
            if (level > 150) {
                sw.spawnParticles(ParticleTypes.FIREWORK, e.getX(), e.getY(), e.getZ(), 5,
                        0, 0, 0, 0.08);
            }
            EnchantmentUtils.addToTracked(e, OrchidEnchantments.ROCKET, -1);
            if (level <= 0) {
                EnchantmentUtils.removeTracked(e, OrchidEnchantments.ROCKET);
            }
        }
        return ControlFlow.CONTINUE;
    }

    @Override
    public ControlFlow onServerPlayerInput(ServerPlayerEntity player, ServerWorld world, PlayerInput input, InputRecord pressed, int level) {
        if (EnchantmentUtils.getTrackedLevel(player, OrchidEnchantments.ROCKET) == 0 && pressed.jump() && player.isSneaking() &&
                !player.isOnGround()) {
            WorldUtils.playSound(null, world, player, SoundEvents.ENTITY_FIREWORK_ROCKET_LAUNCH, 1.0f, 1.0f);
            WindChargeEntity e = new WindChargeEntity(player, world, player.getX(), player.getY(), player.getZ());
            world.createExplosion(e, null, EXPLOSION_BEHAVIOR, player.getX(), player.getY(), player.getZ(), 1.2f,
                    false, World.ExplosionSourceType.TRIGGER, ParticleTypes.GUST_EMITTER_SMALL, ParticleTypes.GUST_EMITTER_LARGE,
                    SoundEvents.ENTITY_WIND_CHARGE_WIND_BURST);
            e.discard();
            player.addVelocity(0, 1.9, 0);
            player.velocityDirty = player.velocityModified = true;
            player.setIgnoreFallDamageFromCurrentExplosion(true);
            EnchantmentUtils.setTracked(player, OrchidEnchantments.ROCKET, 160);
        }
        return ControlFlow.CONTINUE;
    }

}
