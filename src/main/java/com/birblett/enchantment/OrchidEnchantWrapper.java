package com.birblett.enchantment;

import com.birblett.Orchid;
import com.birblett.datagen.OrchidEnchantmentTagProvider;
import com.birblett.datagen.Translateable;
import net.minecraft.component.type.AttributeModifierSlot;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.projectile.ProjectileEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.particle.ParticleEffect;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.tag.EnchantmentTags;
import net.minecraft.registry.tag.TagKey;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.Identifier;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.hit.EntityHitResult;
import net.minecraft.util.hit.HitResult;
import net.minecraft.world.World;

import java.util.HashMap;
import java.util.function.BiConsumer;

public class OrchidEnchantWrapper implements Translateable<OrchidEnchantWrapper> {

    private final HashMap<String, String> translation_map = new HashMap<>();

    public final String id;
    public final TagKey<Item> supportedItems;
    public final TagKey<Item> primaryItems;
    public final int weight;
    public final int maxLevel;
    public final Enchantment.Cost minCost;
    public final Enchantment.Cost maxCost;
    public final int anvilCost;
    public final AttributeModifierSlot[] slots;
    public final String translationKey;
    private final RegistryKey<Enchantment> key;

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
        this.translationKey = "enchantment.orchid." + id;
    }

    public OrchidEnchantWrapper(String id, int processPriority, TagKey<Item> supportedItems, int weight, int maxLevel,
                                   Enchantment.Cost minCost, Enchantment.Cost maxCost, int anvilCost, AttributeModifierSlot... slots) {
        this(id, processPriority, supportedItems, null, weight, maxLevel, minCost, maxCost, anvilCost, slots);
    }

    @Override
    public OrchidEnchantWrapper translate(String lang, String value) {
        this.translation_map.put(lang, value);
        return this;
    }

    @Override
    public String getTranslation(String lang) {
        return this.translation_map.get(lang);
    }

    @Override
    public void forEachTranslation(BiConsumer<String, String> langTranslationConsumer) {
        this.translation_map.forEach(langTranslationConsumer);
    }

    public OrchidEnchantWrapper curse() {
        OrchidEnchantmentTagProvider.addOrGetExisting(EnchantmentTags.CURSE).add(this.key);
        return this;
    }

    public RegistryKey<Enchantment> getKey() {
        return this.key;
    }

    public Flow mainhandAttackAttempt(LivingEntity attacker, int level) {
        return Flow.CONTINUE;
    }

    public Flow offhandAttackAttempt(LivingEntity attacker, int level) {
        return Flow.CONTINUE;
    }

    public Flow useTick(LivingEntity user, ItemStack stack, int remainingUseTicks, int level) {
        return Flow.CONTINUE;
    }

    public Flow onEntityTick(Entity e, World world, int level) {
        return Flow.CONTINUE;
    }

    public Flow onProjectileFired(LivingEntity shooter, ProjectileEntity entity, ItemStack stack, ItemStack projectileStack, ServerWorld world, boolean critical, int level, Flag flag) {
        return Flow.CONTINUE;
    }

    public Flow onProjectileTick(ProjectileEntity entity, World world, int level) {
        return Flow.CONTINUE;
    }

    public ParticleEffect projectileParticleModifier(ProjectileEntity entity, int level) {
        return null;
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
        return this.onProjectileHit(entity, result, level);
    }

    public Flow onProjectileEntityHit(ProjectileEntity entity, EntityHitResult result, int level) {
        return this.onProjectileHit(entity, result, level);
    }

    public Flow onProjectileHit(ProjectileEntity entity, HitResult result, int level) {
        return Flow.CONTINUE;
    }

    public boolean allowProjectileType(LivingEntity entity, ItemStack weapon, ItemStack stack) {
        return false;
    }

    public ProjectileEntity getProjectileFromItem(LivingEntity entity, ItemStack weapon, ItemStack stack) {
        return null;
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
