package com.birblett.enchantment;

import com.birblett.Orchid;
import net.minecraft.component.type.AttributeModifierSlot;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.projectile.ProjectileEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.tag.TagKey;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.Identifier;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.hit.EntityHitResult;
import net.minecraft.world.World;

import java.util.HashMap;

public class OrchidEnchantWrapper {

    public final String id;
    public final RegistryKey<Enchantment> key;
    public final TagKey<Item> supportedItems;
    public final TagKey<Item> primaryItems;
    public final int weight;
    public final int maxLevel;
    public final Enchantment.Cost minCost;
    public final Enchantment.Cost maxCost;
    public final int anvilCost;
    public final AttributeModifierSlot[] slots;

    public OrchidEnchantWrapper(String id, int processPriority, TagKey<Item> supportedItems, TagKey<Item> primaryItems,
                                   int weight, int maxLevel, Enchantment.Cost minCost, Enchantment.Cost maxCost, int anvilCost,
                                   AttributeModifierSlot... slots) {
        this.id = id;
        this.key = RegistryKey.of(RegistryKeys.ENCHANTMENT, Identifier.of(Orchid.MOD_ID, id));
        this.supportedItems = supportedItems;
        this.primaryItems = primaryItems;
        this.weight = weight;
        this.maxLevel = maxLevel;
        this.minCost = minCost;
        this.maxCost = maxCost;
        this.anvilCost = anvilCost;
        this.slots = slots;
        OrchidEnchantments.PROCESS_PRIORITY.put(this.key, processPriority);
        if (OrchidEnchantments.ORCHID_ENCHANTMENTS.get(processPriority) == null) {
            OrchidEnchantments.ORCHID_ENCHANTMENTS.set(processPriority, new HashMap<>());
        }
        OrchidEnchantments.ORCHID_ENCHANTMENTS.get(processPriority).put(this.key, this);
    }

    public OrchidEnchantWrapper(String id, int processPriority, TagKey<Item> supportedItems, int weight, int maxLevel,
                                   Enchantment.Cost minCost, Enchantment.Cost maxCost, int anvilCost, AttributeModifierSlot... slots) {
        this(id, processPriority, supportedItems, null, weight, maxLevel, minCost, maxCost, anvilCost, slots);
    }

    public Flow mainhandAttackAttempt(LivingEntity attacker, int level) {
        return Flow.CONTINUE;
    }

    public Flow offhandAttackAttempt(LivingEntity attacker, int level) {
        return Flow.CONTINUE;
    }

    public Flow onProjectileFired(LivingEntity shooter, ProjectileEntity entity, ItemStack stack, ItemStack projectileStack, ServerWorld world, boolean critical, int level, Flag flag) {
        return Flow.CONTINUE;
    }

    public Flow onProjectileTick(ProjectileEntity entity, World world, int level) {
        return Flow.CONTINUE;
    }

    public float projectileKnockbackMultiplier(ProjectileEntity entity, Entity target, int level) {
        return 1.0f;
    }

    public double projectileGravityModifier(ProjectileEntity entity, double gravity, int level) {
        return gravity;
    }

    public double projectileDragModifier(ProjectileEntity entity, double drag, int level) {
        return drag;
    }

    public Flow onProjectileBlockHit(ProjectileEntity entity, BlockHitResult result, int level) {
        return Flow.CONTINUE;
    }

    public Flow onProjectileEntityHit(ProjectileEntity entity, EntityHitResult result, int level) {
        return Flow.CONTINUE;
    }

    public enum Flag {
        DIRECT,
        SECONDARY,
        SUMMON
    }

    public enum Flow {
        CONTINUE,
        BREAK,
        CANCEL_AFTER,
        CANCEL_BREAK
    }

}
