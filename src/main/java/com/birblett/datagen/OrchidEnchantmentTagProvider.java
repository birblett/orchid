package com.birblett.datagen;

import com.birblett.datagen.wrapper.TagWrapper;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricTagProvider;
import net.minecraft.data.tag.ProvidedTagBuilder;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.registry.tag.TagKey;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.CompletableFuture;

public class OrchidEnchantmentTagProvider extends FabricTagProvider<Enchantment> {

    public static ArrayList<TagWrapper<Enchantment, RegistryKey<Enchantment>>> ENCHANTMENT_TAGS = new ArrayList<>();

    public static HashMap<TagKey<Enchantment>, TagWrapper<Enchantment, RegistryKey<Enchantment>>> EXISTING_ENCHANTMENT_TAGS = new HashMap<>();

    public OrchidEnchantmentTagProvider(FabricDataOutput output, CompletableFuture<RegistryWrapper.WrapperLookup> registriesFuture) {
        super(output, RegistryKeys.ENCHANTMENT, registriesFuture);
    }

    @Override
    protected void configure(RegistryWrapper.WrapperLookup wrapperLookup) {
        for (TagWrapper<Enchantment, RegistryKey<Enchantment>> t : EXISTING_ENCHANTMENT_TAGS.values()) {
            ProvidedTagBuilder<RegistryKey<Enchantment>, Enchantment> builder = this.builder(t.tagKey);
            t.values.forEach(builder::add);
            t.tags.forEach(builder::forceAddTag);
            t.optionals.forEach(builder::addOptional);
            t.optionalTags.forEach(builder::addOptionalTag);
        }
    }

    public static TagWrapper<Enchantment, RegistryKey<Enchantment>> addOrGetExisting(TagKey<Enchantment> key) {
        if (EXISTING_ENCHANTMENT_TAGS.containsKey(key)) {
            return EXISTING_ENCHANTMENT_TAGS.get(key);
        } else {
            TagWrapper<Enchantment, RegistryKey<Enchantment>> t = new TagWrapper<>(key);
            EXISTING_ENCHANTMENT_TAGS.put(key, t);
            return t;
        }
    }

}
