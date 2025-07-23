package com.birblett.datagen;

import com.birblett.damage_types.OrchidDamageTypes;
import com.birblett.datagen.wrapper.DamageTypeWrapper;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricDynamicRegistryProvider;
import net.minecraft.entity.damage.DamageType;
import net.minecraft.registry.Registerable;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryWrapper;

import java.util.Map;
import java.util.concurrent.CompletableFuture;

public class OrchidDamageTypeProvider extends FabricDynamicRegistryProvider {

    public OrchidDamageTypeProvider(FabricDataOutput output, CompletableFuture<RegistryWrapper.WrapperLookup> registriesFuture) {
        super(output, registriesFuture);
    }

    @Override
    protected void configure(RegistryWrapper.WrapperLookup registries, Entries entries) {
        for (Map.Entry<RegistryKey<DamageType>, DamageTypeWrapper> e : OrchidDamageTypes.DAMAGE_TYPES.entrySet()) {
            entries.add(e.getKey(), e.getValue().damageType);
            e.getValue().forEachTranslation((l, v) -> Translateable.addTranslation(l, e.getValue().translationKey, v));
        }
    }

    public static void bootstrap(Registerable<DamageType> damageTypeRegisterable) {
        for (Map.Entry<RegistryKey<DamageType>, DamageTypeWrapper> e : OrchidDamageTypes.DAMAGE_TYPES.entrySet()) {
            damageTypeRegisterable.register(e.getKey(), e.getValue().damageType);
        }
    }

    @Override
    public String getName() {
        return "Damage Types";
    }

}
