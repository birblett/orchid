package com.birblett.datagen;

import com.birblett.datagen.lang.OrchidEnglishLanguageProvider;
import net.fabricmc.fabric.api.datagen.v1.DataGeneratorEntrypoint;
import net.fabricmc.fabric.api.datagen.v1.FabricDataGenerator;
import net.minecraft.registry.RegistryBuilder;
import net.minecraft.registry.RegistryKeys;

public class OrchidDataGenerator implements DataGeneratorEntrypoint {

	@Override
	public void onInitializeDataGenerator(FabricDataGenerator generator) {
		FabricDataGenerator.Pack pack = generator.createPack();
		pack.addProvider(OrchidEnchantmentProvider::new);
		pack.addProvider(OrchidEnchantmentTagProvider::new);
		pack.addProvider(OrchidDamageTypeProvider::new);
		pack.addProvider(OrchidDamageTypeTagProvider::new);
		pack.addProvider(OrchidItemTagProvider::new);
		pack.addProvider(OrchidEnglishLanguageProvider::new);
	}

	@Override
	public void buildRegistry(RegistryBuilder registryBuilder) {
		DataGeneratorEntrypoint.super.buildRegistry(registryBuilder);
		registryBuilder.addRegistry(RegistryKeys.ENCHANTMENT, OrchidEnchantmentProvider::bootstrap);
		registryBuilder.addRegistry(RegistryKeys.DAMAGE_TYPE, OrchidDamageTypeProvider::bootstrap);
	}

}
