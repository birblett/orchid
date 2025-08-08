package com.birblett.enchantment.impl.projectile;

import com.birblett.enchantment.OrchidEnchantWrapper;
import com.birblett.interfaces.entity.ProjectileFlags;
import net.minecraft.component.type.AttributeModifierSlot;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.*;
import net.minecraft.entity.projectile.thrown.*;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.registry.tag.TagKey;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.Vec3d;

import java.util.HashMap;
import java.util.function.BiFunction;

public class AdaptabilityEnchantment extends OrchidEnchantWrapper {

    private static class AdaptabilityData {

        public double velocityMult = 1;
        public int lifetime = 0;
        private final BiFunction<LivingEntity, ItemStack, ProjectileEntity> projectileProvider;

        private AdaptabilityData(BiFunction<LivingEntity, ItemStack, ProjectileEntity> projectileProvider) {
            this.projectileProvider = projectileProvider;
        }

        private AdaptabilityData lifetime(int lifetime) {
            this.lifetime = lifetime;
            return this;
        }

        private AdaptabilityData velocityMult(double velocityMult) {
            this.velocityMult = velocityMult;
            return this;
        }

        public ProjectileEntity apply(LivingEntity entity, ItemStack projectileStack) {
            return this.projectileProvider.apply(entity, projectileStack);
        }

    }

    private static final HashMap<Item, AdaptabilityData> ADAPTABILITY_MAP = new HashMap<>();

    static {
        ADAPTABILITY_MAP.put(Items.FIRE_CHARGE, new AdaptabilityData((entity, stack) ->
                new SmallFireballEntity(entity.getWorld(), entity, Vec3d.ZERO)).lifetime(40).velocityMult(0.5));
        ADAPTABILITY_MAP.put(Items.WITHER_SKELETON_SKULL, new AdaptabilityData((entity, stack) ->
                new WitherSkullEntity(entity.getWorld(), entity, Vec3d.ZERO)).lifetime(35).velocityMult(0.7));
        ADAPTABILITY_MAP.put(Items.WIND_CHARGE, new AdaptabilityData((entity, stack) ->
                entity instanceof PlayerEntity p ? new WindChargeEntity(p, p.getWorld(), 0, 0, 0) : null).lifetime(35));
        ADAPTABILITY_MAP.put(Items.SNOWBALL, new AdaptabilityData((entity, stack) ->
                new SnowballEntity(entity.getWorld(), entity, stack)));
        ADAPTABILITY_MAP.put(Items.EGG, new AdaptabilityData((entity, stack) ->
                new EggEntity(entity.getWorld(), entity, stack)));
        ADAPTABILITY_MAP.put(Items.ENDER_PEARL, new AdaptabilityData((entity, stack) ->
                new EnderPearlEntity(entity.getWorld(), entity, stack)).velocityMult(0.9));
        ADAPTABILITY_MAP.put(Items.FIREWORK_STAR, new AdaptabilityData((entity, stack) ->
                new FireballEntity(entity.getWorld(), entity, Vec3d.ZERO, 2)).lifetime(50).velocityMult(0.6));
        ADAPTABILITY_MAP.put(Items.EXPERIENCE_BOTTLE, new AdaptabilityData((entity, stack) ->
                new ExperienceBottleEntity(entity.getWorld(), entity, stack)));
        ADAPTABILITY_MAP.put(Items.SPLASH_POTION, new AdaptabilityData((entity, stack) ->
                new SplashPotionEntity(entity.getWorld(), entity, stack)).velocityMult(0.7));
    }

    public AdaptabilityEnchantment(String id, int processPriority, TagKey<Item> supportedItems, int weight, int maxLevel,
                                   Enchantment.Cost minCost, Enchantment.Cost maxCost, int anvilCost, AttributeModifierSlot... slots) {
        super(id, processPriority, supportedItems, null, weight, maxLevel, minCost, maxCost, anvilCost, slots);
    }

    @Override
    public boolean allowProjectileType(LivingEntity entity, ItemStack weapon, ItemStack stack) {
        return ADAPTABILITY_MAP.containsKey(stack.getItem());
    }

    @Override
    public ProjectileEntity getProjectileFromItem(LivingEntity entity, ItemStack weapon, ItemStack stack) {
        ProjectileEntity p = null;
        if (ADAPTABILITY_MAP.get(stack.getItem()) instanceof AdaptabilityData a) {
            p = a.apply(entity, stack);
            if (a.lifetime > 0) {
                ProjectileFlags.setLifetime(p, a.lifetime);
            }
        }
        if (p != null) {
            p.setPosition(entity.getEyePos());
        }
        return p;
    }

    @Override
    public ControlFlow onProjectileFired(LivingEntity shooter, ProjectileEntity entity, ItemStack stack, ItemStack projectileStack, ServerWorld world, boolean critical, int level, Flag flag) {
        if (ADAPTABILITY_MAP.get(projectileStack.getItem()) instanceof AdaptabilityData a) {
            entity.setVelocity(entity.getVelocity().multiply(a.velocityMult));
        }
        return ControlFlow.CONTINUE;
    }

}
