package com.birblett.datagen;

import com.birblett.Orchid;
import com.birblett.enchantment.OrchidEnchantWrapper;
import com.birblett.enchantment.OrchidEnchantments;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricDynamicRegistryProvider;
import net.minecraft.enchantment.Enchantment;
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
        forEachEnchantment(registries.getOrThrow(RegistryKeys.ITEM), (e, b) ->
                entries.add(e.key, b.build(Identifier.of(Orchid.MOD_ID, e.id))));
    }

    public static void bootstrap(Registerable<Enchantment> enchantmentRegisterable) {
        forEachEnchantment(enchantmentRegisterable.getRegistryLookup(RegistryKeys.ITEM), (e, b) ->
                enchantmentRegisterable.register(e.key, b.build(Identifier.of(Orchid.MOD_ID, e.id))));
    }

    private static void forEachEnchantment(RegistryEntryLookup<Item> lookup, BiConsumer<OrchidEnchantWrapper, Enchantment.Builder> c) {
        for (HashMap<RegistryKey<Enchantment>, OrchidEnchantWrapper> h : OrchidEnchantments.ORCHID_ENCHANTMENTS) {
            if (h != null) {
                for (OrchidEnchantWrapper e : h.values()) {
                    Enchantment.Builder b = getBuilder(e, lookup);
                    c.accept(e, b);
                }
            }
        }
    }

    @NotNull
    private static Enchantment.Builder getBuilder(OrchidEnchantWrapper e, RegistryEntryLookup<Item> itemLookup) {
        Enchantment.Builder b;
        if (e.primaryItems == null) {
            b = Enchantment.builder(Enchantment.definition(itemLookup.getOrThrow(e.supportedItems), e.weight, e.maxLevel,
                    e.minCost, e.maxCost, e.anvilCost, e.slots));
        } else {
            b = Enchantment.builder(Enchantment.definition(itemLookup.getOrThrow(e.supportedItems),
                    itemLookup.getOrThrow(e.supportedItems), e.weight, e.maxLevel, e.minCost, e.maxCost, e.anvilCost, e.slots));
        }
        return b;
    }

    @Override
    public String getName() {
        return "Enchantment";
    }

}
