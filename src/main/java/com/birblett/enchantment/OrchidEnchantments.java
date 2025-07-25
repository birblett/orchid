package com.birblett.enchantment;

import com.birblett.datagen.OrchidItemTagProvider;
import com.birblett.enchantment.impl.*;
import com.birblett.enchantment.impl.curse.HeartseekerEnchantment;
import com.birblett.enchantment.impl.curse.InfernalEnchantment;
import com.birblett.enchantment.impl.curse.PlunkEnchantment;
import net.minecraft.component.type.AttributeModifierSlot;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.item.Item;
import net.minecraft.item.Items;
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
            .add(Items.BOW)
            .add(Items.CROSSBOW)
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

    /**
     * Projectile Enchants
     */

    public static RegistryKey<Enchantment> ADAPTABILITY = new AdaptabilityEnchantment("adaptability", 1, BOW_OR_CROSSBOW_ENCHANTABLE,
            1, 1, Enchantment.constantCost(25), Enchantment.constantCost(50), 1,
            AttributeModifierSlot.ANY)
            .translate("Adaptability")
            .build();

    public static final RegistryKey<Enchantment> AERODYNAMIC = new AerodynamicEnchantment("aerodynamic", 0, PROJECTILE_ENCHANTABLE,
            1, 3, Enchantment.leveledCost(15, 10), Enchantment.leveledCost(25, 10), 1,
            AttributeModifierSlot.ANY)
            .translate("Aerodynamic")
            .build();

    public static final RegistryKey<Enchantment> ARROW_RAIN = new ArrowRainEnchantment("arrow_rain", 1, BOW_OR_CROSSBOW_ENCHANTABLE,
            1, 1, Enchantment.constantCost(25), Enchantment.constantCost(50), 1,
            AttributeModifierSlot.ANY)
            .translate("Arrow Rain")
            .build();

    public static final RegistryKey<Enchantment> BARBED = new BarbedEnchantment("barbed", 3, ItemTags.FISHING_ENCHANTABLE,
            1, 3, Enchantment.leveledCost(15, 10), Enchantment.leveledCost(25, 10), 1,
            AttributeModifierSlot.ANY)
            .translate("Barbed")
            .build();

    public static final RegistryKey<Enchantment> BURST_FIRE = new BurstFireEnchantment("burst_fire", 1, BOW_OR_CROSSBOW_ENCHANTABLE,
            1, 1, Enchantment.constantCost(25), Enchantment.constantCost(50), 1,
            AttributeModifierSlot.ANY)
            .translate("Burst Fire")
            .build();

    public static final RegistryKey<Enchantment> FOCUS = new FocusEnchantment("focus", 1, ItemTags.BOW_ENCHANTABLE,
            1, 1, Enchantment.constantCost(25), Enchantment.constantCost(50), 1,
            AttributeModifierSlot.ANY)
            .translate("Focus")
            .build();

    public static final RegistryKey<Enchantment> GRAPPLING = new GrapplingEnchantment("grappling", 0, GRAPPLING_ENCHANTABLE,
            1, 1, Enchantment.constantCost(25), Enchantment.constantCost(50), 1,
            AttributeModifierSlot.ANY)
            .translate("Grappling")
            .build();

    public static final RegistryKey<Enchantment> HITSCAN = new HitscanEnchantment("hitscan", 8, BOW_OR_CROSSBOW_ENCHANTABLE,
            1, 3, Enchantment.constantCost(25), Enchantment.constantCost(50), 1,
            AttributeModifierSlot.ANY)
            .translate("Hitscan")
            .build();

    public static final RegistryKey<Enchantment> MARKED = new MarkedEnchantment("marked", 6, BOW_OR_CROSSBOW_ENCHANTABLE,
            1, 1, Enchantment.constantCost(25), Enchantment.constantCost(50), 1,
            AttributeModifierSlot.ANY)
            .translate("Marked")
            .build();

    public static final RegistryKey<Enchantment> RICOCHET = new RicochetEnchantment("ricochet", 2, BOW_OR_CROSSBOW_ENCHANTABLE,
            1, 3, Enchantment.leveledCost(15, 10), Enchantment.leveledCost(25, 10), 1,
            AttributeModifierSlot.ANY)
            .translate("Ricochet")
            .build();

    public static final RegistryKey<Enchantment> STASIS = new StasisEnchantment("stasis", 5, BOW_OR_CROSSBOW_ENCHANTABLE,
            1, 1, Enchantment.constantCost(25), Enchantment.constantCost(50), 1,
            AttributeModifierSlot.ANY)
            .translate("Stasis")
            .build();

    /**
     * Curses
     */

    public static RegistryKey<Enchantment> HEARTSEEKER = new HeartseekerEnchantment("heartseeker", 6, BOW_OR_CROSSBOW_ENCHANTABLE,
            1, 3, Enchantment.leveledCost(15, 10), Enchantment.leveledCost(25, 10), 1,
            AttributeModifierSlot.ANY).curse()
            .translate("Heartseeker")
            .build();

    public static RegistryKey<Enchantment> INFERNAL = new InfernalEnchantment("infernal", 0, ItemTags.BOW_ENCHANTABLE,
            1, 1, Enchantment.constantCost(25), Enchantment.constantCost(50), 1,
            AttributeModifierSlot.ANY).curse()
            .translate("Infernal")
            .build();

    public static RegistryKey<Enchantment> PLUNK = new PlunkEnchantment("plunk", 0, PROJECTILE_ENCHANTABLE, 1,
            3, Enchantment.leveledCost(15, 10), Enchantment.leveledCost(25, 10), 1,
            AttributeModifierSlot.ANY).curse()
            .translate("Plunk")
            .build();

}
