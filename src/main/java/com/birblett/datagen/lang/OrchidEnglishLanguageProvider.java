package com.birblett.datagen.lang;

import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricLanguageProvider;
import net.minecraft.registry.RegistryWrapper;

import java.util.HashMap;
import java.util.concurrent.CompletableFuture;

public class OrchidEnglishLanguageProvider extends FabricLanguageProvider {

    private static final HashMap<String, String> TRANSLATIONS = new HashMap<>();

    public static void addTranslation(String key, String val) {
        TRANSLATIONS.put(key, val);
    }

    public OrchidEnglishLanguageProvider(FabricDataOutput dataOutput, CompletableFuture<RegistryWrapper.WrapperLookup> registryLookup) {
        super(dataOutput, "en_us", registryLookup);
    }

    @Override
    public void generateTranslations(RegistryWrapper.WrapperLookup registryLookup, TranslationBuilder translationBuilder) {
        TRANSLATIONS.forEach(translationBuilder::add);
    }

}
