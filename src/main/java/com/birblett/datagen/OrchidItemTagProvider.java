package com.birblett.datagen;

import com.birblett.datagen.wrapper.TagWrapper;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricTagProvider;
import net.minecraft.data.tag.ProvidedTagBuilder;
import net.minecraft.item.Item;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.RegistryWrapper;

import java.util.ArrayList;
import java.util.concurrent.CompletableFuture;

public class OrchidItemTagProvider extends FabricTagProvider.ItemTagProvider {

    public static ArrayList<TagWrapper<Item, Item>> ITEM_TAGS = new ArrayList<>();

    public OrchidItemTagProvider(FabricDataOutput output, CompletableFuture<RegistryWrapper.WrapperLookup> registriesFuture) {
        super(output, registriesFuture);
    }

    @Override
    protected void configure(RegistryWrapper.WrapperLookup lookup) {
        for (TagWrapper<Item, Item> t : ITEM_TAGS) {
            ProvidedTagBuilder<Item, Item> builder = valueLookupBuilder(t.tagKey);
            t.values.forEach(builder::add);
            t.tags.forEach(builder::forceAddTag);
            t.optionals.forEach(builder::addOptional);
            t.optionalTags.forEach(builder::addOptionalTag);
        }
    }

    public static TagWrapper<Item, Item> create(String id) {
        TagWrapper<Item, Item> t = new TagWrapper<>(RegistryKeys.ITEM, id);
        ITEM_TAGS.add(t);
        return t;
    }

}
