package com.birblett.datagen;

import com.birblett.damage_types.OrchidDamageTypes;
import com.birblett.datagen.wrapper.DamageTypeWrapper;
import com.birblett.datagen.wrapper.TagWrapper;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricTagProvider;
import net.minecraft.data.tag.ProvidedTagBuilder;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.entity.damage.DamageType;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.registry.tag.TagKey;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

public class OrchidDamageTypeTagProvider extends FabricTagProvider<DamageType> {


    public OrchidDamageTypeTagProvider(FabricDataOutput output, CompletableFuture<RegistryWrapper.WrapperLookup> registriesFuture) {
        super(output, RegistryKeys.DAMAGE_TYPE, registriesFuture);
    }

    @Override
    protected void configure(RegistryWrapper.WrapperLookup wrapperLookup) {
        for (Map.Entry<RegistryKey<DamageType>, DamageTypeWrapper> e : OrchidDamageTypes.DAMAGE_TYPES.entrySet()) {
            e.getValue().forEachTag(tag -> {
                ProvidedTagBuilder<RegistryKey<DamageType>, DamageType> builder = this.builder(tag);
                builder.add(e.getKey());
            });
        }
    }

}
