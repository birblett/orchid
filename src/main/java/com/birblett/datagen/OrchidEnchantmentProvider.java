package com.birblett.datagen;

import com.birblett.Orchid;
import com.birblett.enchantment.OrchidEnchantWrapper;
import com.birblett.enchantment.OrchidEnchantments;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricDynamicRegistryProvider;
import net.minecraft.component.EnchantmentEffectComponentTypes;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.effect.AttributeEnchantmentEffect;
import net.minecraft.item.Item;
import net.minecraft.registry.*;
import net.minecraft.util.Identifier;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.concurrent.CompletableFuture;
import java.util.function.BiConsumer;

public class OrchidEnchantmentProvider extends FabricDynamicRegistryProvider {

    public OrchidEnchantmentProvider(FabricDataOutput output, CompletableFuture<RegistryWrapper.WrapperLookup> registriesFuture) {
        super(output, registriesFuture);
    }

    @Override
    protected void configure(RegistryWrapper.WrapperLookup registries, Entries entries) {
        forEachEnchantment(registries.getOrThrow(RegistryKeys.ITEM), registries.getOrThrow(RegistryKeys.ENCHANTMENT),
                (e, b) -> entries.add(e.getOrCreateKey(), b.build(Identifier.of(Orchid.MOD_ID, e.id))));
    }

    @Override
    public String getName() {
        return "Enchantment";
    }

    public static void bootstrap(Registerable<Enchantment> enchantmentRegisterable) {
        forEachEnchantment(enchantmentRegisterable.getRegistryLookup(RegistryKeys.ITEM),
                enchantmentRegisterable.getRegistryLookup(RegistryKeys.ENCHANTMENT), (e, b) -> {
            enchantmentRegisterable.register(e.getOrCreateKey(), b.build(Identifier.of(Orchid.MOD_ID, e.id)));
            e.forEachTranslation((l, v) -> Translateable.addTranslation(l, "enchantment.orchid." + e.translationKey, v));
        });
    }

    private static void forEachEnchantment(RegistryEntryLookup<Item> lookup, RegistryEntryLookup<Enchantment> enchLookup, BiConsumer<OrchidEnchantWrapper, Enchantment.Builder> c) {
        for (HashMap<RegistryKey<Enchantment>, OrchidEnchantWrapper> h : OrchidEnchantments.ORCHID_ENCHANTMENTS) {
            if (h != null) {
                for (OrchidEnchantWrapper e : h.values()) {
                    Enchantment.Builder b;
                    if (e.primaryItems == null) {
                        b = Enchantment.builder(Enchantment.definition(lookup.getOrThrow(e.supportedItems), e.weight, e.maxLevel,
                                e.minCost, e.maxCost, e.anvilCost, e.slots));
                    } else {
                        b = Enchantment.builder(Enchantment.definition(lookup.getOrThrow(e.supportedItems),
                                lookup.getOrThrow(e.supportedItems), e.weight, e.maxLevel, e.minCost, e.maxCost, e.anvilCost, e.slots));
                    }
                    for (AttributeEnchantmentEffect effect : e.attributes) {
                        b.addEffect(EnchantmentEffectComponentTypes.ATTRIBUTES, effect);
                    }
                    if (e.exclusiveSet != null) {
                        b.exclusiveSet(enchLookup.getOrThrow(e.exclusiveSet));
                    }
                    c.accept(e, b);
                }
            }
        }
    }

}
