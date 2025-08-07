package com.birblett.enchantment;

import com.birblett.Orchid;
import com.birblett.datagen.OrchidEnchantmentTagProvider;
import com.birblett.datagen.Translateable;
import com.birblett.util.InputManager;
import net.minecraft.client.input.Input;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.component.type.AttributeModifierSlot;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.ProjectileEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.particle.ParticleEffect;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.tag.EnchantmentTags;
import net.minecraft.registry.tag.TagKey;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
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
    public final int processPriority;
    private RegistryKey<Enchantment> key;

    public OrchidEnchantWrapper(String id, int processPriority, TagKey<Item> supportedItems, TagKey<Item> primaryItems,
                                   int weight, int maxLevel, Enchantment.Cost minCost, Enchantment.Cost maxCost, int anvilCost,
                                   AttributeModifierSlot... slots) {
        this.id = id;
        this.supportedItems = supportedItems;
        this.primaryItems = primaryItems;
        this.weight = weight;
        this.maxLevel = maxLevel;
        this.minCost = minCost;
        this.maxCost = maxCost;
        this.anvilCost = anvilCost;
        this.slots = slots;
        this.translationKey = id;
        this.processPriority = processPriority;
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
    public void forEachTranslation(BiConsumer<String, String> langTranslationConsumer) {
        this.translation_map.forEach(langTranslationConsumer);
    }

    public OrchidEnchantWrapper curse() {
        OrchidEnchantmentTagProvider.addOrGetExisting(EnchantmentTags.CURSE).add(this.getOrCreateKey());
        return this;
    }

    public RegistryKey<Enchantment> getOrCreateKey() {
        if (this.key == null) {
            this.key = RegistryKey.of(RegistryKeys.ENCHANTMENT, Identifier.of(Orchid.MOD_ID, id));
        }
        return this.key;
    }

    public RegistryKey<Enchantment> build() {
        OrchidEnchantments.PROCESS_PRIORITY.put(this.getOrCreateKey(), processPriority);
        if (OrchidEnchantments.ORCHID_ENCHANTMENTS.get(processPriority) == null) {
            OrchidEnchantments.ORCHID_ENCHANTMENTS.set(processPriority, new HashMap<>());
        }
        OrchidEnchantments.ORCHID_ENCHANTMENTS.get(processPriority).put(this.getOrCreateKey(), this);
        return this.getOrCreateKey();
    }

    public ControlFlow mainhandAttackAttempt(LivingEntity attacker, int level) {
        return ControlFlow.CONTINUE;
    }

    public ControlFlow offhandAttackAttempt(LivingEntity attacker, int level) {
        return ControlFlow.CONTINUE;
    }

    public Float attackModifier(LivingEntity attacker, Entity target, ItemStack stack, float damage, DamageSource source, int level) {
        return damage;
    }

    public ControlFlow postAttack(LivingEntity attacker, Entity target, ItemStack stack, float damage, DamageSource source, int level) {
        return ControlFlow.CONTINUE;
    }

    public ActionResult onUse(PlayerEntity user, ItemStack stack, World world, Hand hand, int level) { return null; }

    public Boolean onStoppedUsing(LivingEntity user, ItemStack stack, World world, int remainingUseTicks, int level) {
        return null;
    }

    /**
     * called when item use is held, distinct from use
     */
    public ControlFlow onUseTick(LivingEntity user, ItemStack stack, int remainingUseTicks, int level) {
        return ControlFlow.CONTINUE;
    }

    /**
     * called when a non-projectile entity has a synced enchant component and is ticked
     */
    public ControlFlow onEntityTick(Entity e, World world, int level) {
        return ControlFlow.CONTINUE;
    }

    /**
     * called when a living entity has an enchanted equip held or worn in armor slots
     */
    public ControlFlow onEntityTickEquip(LivingEntity e, World world, int level) {
        return ControlFlow.CONTINUE;
    }

    public ControlFlow onMovementTick(ClientPlayerEntity e, World world, Input input, InputManager pressed, int level) {
        return ControlFlow.CONTINUE;
    }

    /**
     * called when various projectiles are created
     */
    public ControlFlow onProjectileFired(LivingEntity shooter, ProjectileEntity entity, ItemStack stack, ItemStack projectileStack, ServerWorld world, boolean critical, int level, Flag flag) {
        return ControlFlow.CONTINUE;
    }

    /**
     * called when a projectile entity has a synced enchant component and is ticked
     */
    public ControlFlow onProjectileTick(ProjectileEntity entity, World world, int level) {
        return ControlFlow.CONTINUE;
    }

    /**
     * for arrows only - modifies particle trail
     */
    public ParticleEffect projectileParticleModifier(ProjectileEntity entity, int level) {
        return null;
    }

    /**
     * modifies knockback for all projectiles - may behave differently with arrows
     */
    public float projectileKnockbackMultiplier(ProjectileEntity entity, Entity target, int level) {
        return 1.0f;
    }

    /**
     * modifies the gravity of a projectile entity
     */
    public double projectileGravityModifier(ProjectileEntity entity, double gravity, int level) {
        return gravity;
    }

    public double projectileDragModifier(ProjectileEntity entity, double drag, int level) {
        return drag;
    }

    public ControlFlow onProjectileBlockHit(ProjectileEntity entity, BlockHitResult result, int level) {
        return this.onProjectileHit(entity, result, level);
    }

    public ControlFlow onProjectileEntityHit(ProjectileEntity entity, EntityHitResult result, int level) {
        return this.onProjectileHit(entity, result, level);
    }

    public ControlFlow onProjectileHit(ProjectileEntity entity, HitResult result, int level) {
        return ControlFlow.CONTINUE;
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

    public enum ControlFlow {
        CONTINUE,
        BREAK,
        CANCEL_AFTER,
        CANCEL_BREAK
    }

}
