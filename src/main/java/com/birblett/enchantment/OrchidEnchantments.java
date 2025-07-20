package com.birblett.enchantment;

import com.birblett.datagen.OrchidItemTagProvider;
import com.birblett.enchantment.impl.*;
import net.minecraft.component.type.AttributeModifierSlot;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.item.Item;
import net.minecraft.item.Items;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.tag.TagKey;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;

public class OrchidEnchantments {

    public static final int PROCESS_PRIORITY_MAX = 10;
    public static final ArrayList<HashMap<RegistryKey<Enchantment>, OrchidEnchantWrapper>> ORCHID_ENCHANTMENTS = new ArrayList<>(Collections.nCopies(PROCESS_PRIORITY_MAX, null));
    public static final HashMap<RegistryKey<Enchantment>, Integer> PROCESS_PRIORITY = new HashMap<>();

    public static final TagKey<Item> BOW_OR_CROSSBOW_ENCHANTABLE = OrchidItemTagProvider.create("enchantable/bow_or_crossbow")
            .add(Items.BOW, Items.CROSSBOW)
            .tagKey;

    public static final TagKey<Item> GRAPPLING_ENCHANTABLE = OrchidItemTagProvider.create("enchantable/grappling")
            .add(Items.FISHING_ROD)
            .add(BOW_OR_CROSSBOW_ENCHANTABLE)
            .tagKey;

    public static final TagKey<Item> PROJECTILE_ENCHANTABLE = OrchidItemTagProvider.create("enchantable/projectile")
            .add(BOW_OR_CROSSBOW_ENCHANTABLE)
            .add(Items.FISHING_ROD)
            .add(Items.TRIDENT)
            .tagKey;

    public static RegistryKey<Enchantment> AERODYNAMIC = new AerodynamicEnchantment("aerodynamic", 0, PROJECTILE_ENCHANTABLE,
            1, 3, Enchantment.leveledCost(15, 10), Enchantment.leveledCost(25, 10), 1, AttributeModifierSlot.ANY).key;

    public static RegistryKey<Enchantment> ARROW_RAIN = new ArrowRainEnchantment("arrow_rain", 1, BOW_OR_CROSSBOW_ENCHANTABLE,
            1, 1, Enchantment.constantCost(25), Enchantment.constantCost(50), 1, AttributeModifierSlot.ANY).key;

    public static RegistryKey<Enchantment> GRAPPLING = new GrapplingEnchantment("grappling", 0, GRAPPLING_ENCHANTABLE,
            1, 1, Enchantment.constantCost(25), Enchantment.constantCost(50), 1, AttributeModifierSlot.ANY).key;

    public static RegistryKey<Enchantment> HITSCAN = new HitscanEnchantment("hitscan", 8, BOW_OR_CROSSBOW_ENCHANTABLE,
            1, 3, Enchantment.constantCost(25), Enchantment.constantCost(50), 1, AttributeModifierSlot.ANY).key;

    public static RegistryKey<Enchantment> MARKED = new MarkedEnchantment("marked", 6, BOW_OR_CROSSBOW_ENCHANTABLE,
            1, 1, Enchantment.constantCost(25), Enchantment.constantCost(50), 1, AttributeModifierSlot.ANY).key;

    public static RegistryKey<Enchantment> RICOCHET = new RicochetEnchantment("ricochet", 2, BOW_OR_CROSSBOW_ENCHANTABLE,
            1, 3, Enchantment.leveledCost(15, 10), Enchantment.leveledCost(25, 10), 1, AttributeModifierSlot.ANY).key;

    public static RegistryKey<Enchantment> STASIS = new StasisEnchantment("stasis", 5, BOW_OR_CROSSBOW_ENCHANTABLE,
            1, 1, Enchantment.constantCost(25), Enchantment.constantCost(50), 1, AttributeModifierSlot.ANY).key;

}
