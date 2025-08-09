package com.birblett.enchantment;

import com.birblett.datagen.OrchidItemTagProvider;
import com.birblett.enchantment.impl.boots.*;
import com.birblett.enchantment.impl.curse.*;
import com.birblett.enchantment.impl.projectile.*;
import net.minecraft.component.type.AttributeModifierSlot;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentLevelBasedValue;
import net.minecraft.entity.attribute.EntityAttributeModifier;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.item.Item;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.tag.ItemTags;
import net.minecraft.registry.tag.TagKey;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;

public class OrchidEnchantments {

    public static final int PROCESS_PRIORITY_MAX = 10;
    public static final ArrayList<HashMap<RegistryKey<Enchantment>, OrchidEnchantWrapper>> ORCHID_ENCHANTMENTS = new ArrayList<>(Collections.nCopies(PROCESS_PRIORITY_MAX, null));
    public static final HashMap<RegistryKey<Enchantment>, Integer> PROCESS_PRIORITY = new HashMap<>();

    public static final TagKey<Item> BOW_OR_CROSSBOW_ENCHANTABLE = OrchidItemTagProvider.create("enchantable/bow_or_crossbow")
            .add(ItemTags.BOW_ENCHANTABLE)
            .add(ItemTags.CROSSBOW_ENCHANTABLE)
            .tagKey;

    public static final TagKey<Item> GRAPPLING_ENCHANTABLE = OrchidItemTagProvider.create("enchantable/grappling")
            .add(ItemTags.FISHING_ENCHANTABLE)
            .add(BOW_OR_CROSSBOW_ENCHANTABLE)
            .tagKey;

    public static final TagKey<Item> PROJECTILE_ENCHANTABLE = OrchidItemTagProvider.create("enchantable/projectile")
            .add(BOW_OR_CROSSBOW_ENCHANTABLE)
            .add(ItemTags.FISHING_ENCHANTABLE)
            .add(ItemTags.TRIDENT_ENCHANTABLE)
            .tagKey;

    /**
     * Boot Enchants
     */

    public static final RegistryKey<Enchantment> DOUBLE_JUMP;
    public static final RegistryKey<Enchantment> HOVER;
    public static final RegistryKey<Enchantment> ROCKET;
    public static final RegistryKey<Enchantment> SLIMED;
    public static final RegistryKey<Enchantment> ACROBATIC;
    public static final RegistryKey<Enchantment> WINDSTEP;

    static {

        ACROBATIC = new AcrobaticEnchantment("acrobatic", 1, ItemTags.FOOT_ARMOR_ENCHANTABLE,
                1, 1, Enchantment.constantCost(25), Enchantment.constantCost(50), 1, AttributeModifierSlot.FEET)
                .translate("Acrobatic")
                .build();

        DOUBLE_JUMP = new DoubleJumpEnchantment("double_jump", 1, ItemTags.FOOT_ARMOR_ENCHANTABLE,
                1, 1, Enchantment.constantCost(25), Enchantment.constantCost(50), 1, AttributeModifierSlot.FEET)
                .translate("Double Jump")
                .build();

        HOVER = new HoverEnchantment("hover", 1, ItemTags.FOOT_ARMOR_ENCHANTABLE,
                1, 3, Enchantment.leveledCost(15, 10), Enchantment.leveledCost(25, 10), 1,
                AttributeModifierSlot.FEET)
                .translate("Hover")
                .build();

        ROCKET = new RocketEnchantment("rocket", 1, ItemTags.FOOT_ARMOR_ENCHANTABLE,
                1, 1, Enchantment.constantCost(25), Enchantment.constantCost(50), 1, AttributeModifierSlot.FEET)
                .translate("Rocket")
                .build();

        SLIMED = new SlimedEnchantment("slimed", 9, ItemTags.FOOT_ARMOR_ENCHANTABLE,
                1, 1, Enchantment.constantCost(25), Enchantment.constantCost(50), 1, AttributeModifierSlot.FEET)
                .translate("Slimed")
                .addAttribute(EntityAttributes.JUMP_STRENGTH, EnchantmentLevelBasedValue.constant(0.3f), EntityAttributeModifier.Operation.ADD_MULTIPLIED_BASE)
                .addAttribute(EntityAttributes.SAFE_FALL_DISTANCE, EnchantmentLevelBasedValue.constant(2), EntityAttributeModifier.Operation.ADD_VALUE)
                .build();

        WINDSTEP = new OrchidEnchantWrapper("windstep", 1, ItemTags.FOOT_ARMOR_ENCHANTABLE,
                1, 1, Enchantment.constantCost(25), Enchantment.constantCost(50), 1, AttributeModifierSlot.FEET)
                .translate("Windstep")
                .addAttribute(EntityAttributes.MOVEMENT_SPEED, EnchantmentLevelBasedValue.linear(0.1f), EntityAttributeModifier.Operation.ADD_MULTIPLIED_TOTAL)
                .addAttribute(EntityAttributes.STEP_HEIGHT, EnchantmentLevelBasedValue.linear(0.6f, 0.5f), EntityAttributeModifier.Operation.ADD_VALUE)
                .addAttribute(EntityAttributes.JUMP_STRENGTH, EnchantmentLevelBasedValue.linear(0.05f), EntityAttributeModifier.Operation.ADD_MULTIPLIED_BASE)
                .addAttribute(EntityAttributes.GRAVITY, EnchantmentLevelBasedValue.constant(-0.1f), EntityAttributeModifier.Operation.ADD_MULTIPLIED_BASE)
                .addAttribute(EntityAttributes.SAFE_FALL_DISTANCE, EnchantmentLevelBasedValue.linear(2), EntityAttributeModifier.Operation.ADD_VALUE)
                .build();

    }

    /**
     * Projectile Enchants
     */

    public static final RegistryKey<Enchantment> ADAPTABILITY;
    public static final RegistryKey<Enchantment> AERODYNAMIC;
    public static final RegistryKey<Enchantment> AIRSTRIKE;
    public static final RegistryKey<Enchantment> BARBED;
    public static final RegistryKey<Enchantment> BURST_FIRE;
    public static final RegistryKey<Enchantment> FOCUS;
    public static final RegistryKey<Enchantment> GRAPPLING;
    public static final RegistryKey<Enchantment> HITSCAN;
    public static final RegistryKey<Enchantment> MARKED;
    public static final RegistryKey<Enchantment> RICOCHET;
    public static final RegistryKey<Enchantment> STASIS;
    public static final RegistryKey<Enchantment> WIND_RIDER;

    static {

        ADAPTABILITY = new AdaptabilityEnchantment("adaptability", 1, BOW_OR_CROSSBOW_ENCHANTABLE,
                1, 1, Enchantment.constantCost(25), Enchantment.constantCost(50), 1,
                AttributeModifierSlot.ANY)
                .translate("Adaptability")
                .build();

        AERODYNAMIC = new AerodynamicEnchantment("aerodynamic", 0, PROJECTILE_ENCHANTABLE,
                1, 3, Enchantment.leveledCost(15, 10), Enchantment.leveledCost(25, 10), 1,
                AttributeModifierSlot.ANY)
                .translate("Aerodynamic")
                .build();

        AIRSTRIKE = new AirstrikeEnchantment("airstrike", 1, BOW_OR_CROSSBOW_ENCHANTABLE,
                1, 1, Enchantment.constantCost(25), Enchantment.constantCost(50), 1,
                AttributeModifierSlot.ANY)
                .translate("Airstrike")
                .build();

        BARBED = new BarbedEnchantment("barbed", 3, ItemTags.FISHING_ENCHANTABLE,
                1, 3, Enchantment.leveledCost(15, 10), Enchantment.leveledCost(25, 10), 1,
                AttributeModifierSlot.ANY)
                .translate("Barbed")
                .build();

        BURST_FIRE = new BurstFireEnchantment("burst_fire", 1, BOW_OR_CROSSBOW_ENCHANTABLE,
                1, 1, Enchantment.constantCost(25), Enchantment.constantCost(50), 1,
                AttributeModifierSlot.ANY)
                .translate("Burst Fire")
                .build();

        FOCUS = new FocusEnchantment("focus", 1, ItemTags.BOW_ENCHANTABLE,
                1, 1, Enchantment.constantCost(25), Enchantment.constantCost(50), 1,
                AttributeModifierSlot.ANY)
                .translate("Focus")
                .build();

        GRAPPLING = new GrapplingEnchantment("grappling", 0, GRAPPLING_ENCHANTABLE,
                1, 1, Enchantment.constantCost(25), Enchantment.constantCost(50), 1,
                AttributeModifierSlot.ANY)
                .translate("Grappling")
                .build();

        HITSCAN = new HitscanEnchantment("hitscan", 8, BOW_OR_CROSSBOW_ENCHANTABLE,
                1, 3, Enchantment.constantCost(25), Enchantment.constantCost(50), 1,
                AttributeModifierSlot.ANY)
                .translate("Hitscan")
                .build();

        MARKED = new MarkedEnchantment("marked", 6, BOW_OR_CROSSBOW_ENCHANTABLE,
                1, 1, Enchantment.constantCost(25), Enchantment.constantCost(50), 1,
                AttributeModifierSlot.ANY)
                .translate("Marked")
                .build();

        RICOCHET = new RicochetEnchantment("ricochet", 0, BOW_OR_CROSSBOW_ENCHANTABLE,
                1, 3, Enchantment.leveledCost(15, 10), Enchantment.leveledCost(25, 10), 1,
                AttributeModifierSlot.ANY)
                .translate("Ricochet")
                .build();

        STASIS = new StasisEnchantment("stasis", 5, BOW_OR_CROSSBOW_ENCHANTABLE,
                1, 1, Enchantment.constantCost(25), Enchantment.constantCost(50), 1,
                AttributeModifierSlot.ANY)
                .translate("Stasis")
                .build();

        WIND_RIDER = new WindRiderEnchantment("wind_rider", 5, ItemTags.TRIDENT_ENCHANTABLE,
                1, 1, Enchantment.constantCost(25), Enchantment.constantCost(50), 1,
                AttributeModifierSlot.ANY)
                .translate("Wind Rider")
                .build();

    }

    /**
     * Curses
     */

    public static final RegistryKey<Enchantment> BUNNY_HOP;
    public static final RegistryKey<Enchantment> CONCRETE_SHOES;
    public static final RegistryKey<Enchantment> HEARTSEEKER;
    public static final RegistryKey<Enchantment> INFERNAL;
    public static final RegistryKey<Enchantment> PLUNK;

    static {

        BUNNY_HOP = new BunnyHopEnchantment("bunny_hop", 1, ItemTags.FOOT_ARMOR_ENCHANTABLE,
                1, 1, Enchantment.constantCost(25), Enchantment.constantCost(50), 1, AttributeModifierSlot.FEET)
                .translate("Bunny Hop")
                .addAttribute(EntityAttributes.MOVEMENT_SPEED, EnchantmentLevelBasedValue.constant(-0.3f), EntityAttributeModifier.Operation.ADD_MULTIPLIED_BASE)
                .addAttribute(EntityAttributes.JUMP_STRENGTH, EnchantmentLevelBasedValue.constant(-0.3f), EntityAttributeModifier.Operation.ADD_MULTIPLIED_BASE)
                .addAttribute(EntityAttributes.STEP_HEIGHT, EnchantmentLevelBasedValue.constant(0.2f), EntityAttributeModifier.Operation.ADD_VALUE)
                .curse()
                .build();

        CONCRETE_SHOES = new ConcreteShoesEnchantment("concrete_shoes", 1, ItemTags.FOOT_ARMOR_ENCHANTABLE,
                1, 1, Enchantment.constantCost(25), Enchantment.constantCost(50), 1, AttributeModifierSlot.FEET)
                .translate("Concrete Shoes")
                .addAttribute(EntityAttributes.MOVEMENT_SPEED, EnchantmentLevelBasedValue.constant(-0.1f), EntityAttributeModifier.Operation.ADD_MULTIPLIED_BASE)
                .addAttribute(EntityAttributes.JUMP_STRENGTH, EnchantmentLevelBasedValue.constant(0.25f), EntityAttributeModifier.Operation.ADD_MULTIPLIED_BASE)
                .addAttribute(EntityAttributes.GRAVITY, EnchantmentLevelBasedValue.constant(1), EntityAttributeModifier.Operation.ADD_MULTIPLIED_BASE)
                .addAttribute(EntityAttributes.STEP_HEIGHT, EnchantmentLevelBasedValue.constant(-1), EntityAttributeModifier.Operation.ADD_MULTIPLIED_BASE)
                .addAttribute(EntityAttributes.ATTACK_DAMAGE, EnchantmentLevelBasedValue.constant(-0.2f), EntityAttributeModifier.Operation.ADD_MULTIPLIED_BASE)
                .curse()
                .build();

        HEARTSEEKER = new HeartseekerEnchantment("heartseeker", 6, BOW_OR_CROSSBOW_ENCHANTABLE,
                1, 3, Enchantment.leveledCost(15, 10), Enchantment.leveledCost(25, 10), 1,
                AttributeModifierSlot.ANY)
                .translate("Heartseeker")
                .curse()
                .build();

        INFERNAL = new InfernalEnchantment("infernal", 0, ItemTags.BOW_ENCHANTABLE,
                1, 1, Enchantment.constantCost(25), Enchantment.constantCost(50), 1,
                AttributeModifierSlot.ANY)
                .translate("Infernal")
                .curse()
                .build();

        PLUNK = new PlunkEnchantment("plunk", 0, PROJECTILE_ENCHANTABLE, 1,
                3, Enchantment.leveledCost(15, 10), Enchantment.leveledCost(25, 10), 1,
                AttributeModifierSlot.ANY)
                .translate("Plunk")
                .curse()
                .build();

    }

}
