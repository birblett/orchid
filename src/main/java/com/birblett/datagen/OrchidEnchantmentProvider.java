package com.birblett.datagen;

import com.birblett.Orchid;
import com.birblett.enchantment.OrchidEnchantWrapper;
import com.birblett.enchantment.OrchidEnchantments;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricDynamicRegistryProvider;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.util.Identifier;

import java.util.HashMap;
import java.util.concurrent.CompletableFuture;

public class OrchidEnchantmentProvider extends FabricDynamicRegistryProvider {

    public OrchidEnchantmentProvider(FabricDataOutput output, CompletableFuture<RegistryWrapper.WrapperLookup> registriesFuture) {
        super(output, registriesFuture);
    }

    @Override
    protected void configure(RegistryWrapper.WrapperLookup registries, Entries entries) {
        for (HashMap<RegistryKey<Enchantment>, OrchidEnchantWrapper> h : OrchidEnchantments.ORCHID_ENCHANTMENTS) {
            if (h != null) {
                for (OrchidEnchantWrapper e : h.values()) {
                    Enchantment.Builder b;
                    if (e.primaryItems == null) {
                        b = Enchantment.builder(Enchantment.definition(registries.getOrThrow(RegistryKeys.ITEM)
                                .getOrThrow(e.supportedItems), e.weight, e.maxLevel, e.minCost, e.maxCost, e.anvilCost, e.slots));
                    } else {
                        b = Enchantment.builder(Enchantment.definition(registries.getOrThrow(RegistryKeys.ITEM)
                                        .getOrThrow(e.supportedItems), registries.getOrThrow(RegistryKeys.ITEM).getOrThrow(e.supportedItems),
                                e.weight, e.maxLevel, e.minCost, e.maxCost, e.anvilCost, e.slots));
                    }
                    entries.add(e.key, b.build(Identifier.of(Orchid.MOD_ID, e.id)));
                }
            }
        }
    }

    @Override
    public String getName() {
        return "Enchantment";
    }

}
