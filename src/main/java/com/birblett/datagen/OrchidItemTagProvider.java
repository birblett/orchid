package com.birblett.datagen;

import com.birblett.Orchid;
import com.birblett.enchantment.OrchidEnchantments;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricTagProvider;
import net.minecraft.data.tag.ProvidedTagBuilder;
import net.minecraft.item.Item;
import net.minecraft.item.Items;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.registry.tag.TagKey;
import net.minecraft.util.Identifier;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.concurrent.CompletableFuture;

public class OrchidItemTagProvider extends FabricTagProvider.ItemTagProvider {

    public static ArrayList<ItemTagWrapper> ITEM_TAGS = new ArrayList<>();

    public OrchidItemTagProvider(FabricDataOutput output, CompletableFuture<RegistryWrapper.WrapperLookup> registriesFuture) {
        super(output, registriesFuture);
    }

    @Override
    protected void configure(RegistryWrapper.WrapperLookup lookup) {
        valueLookupBuilder(OrchidEnchantments.BOW_OR_CROSSBOW_ENCHANTABLE)
                .add(Items.BOW)
                .add(Items.CROSSBOW);
        for (ItemTagWrapper t : ITEM_TAGS) {
            ProvidedTagBuilder<Item, Item> builder = valueLookupBuilder(t.tagKey);
            t.values.forEach(builder::add);
            t.tags.forEach(builder::addTag);
            t.optionals.forEach(builder::addOptional);
            t.optionalTags.forEach(builder::addOptionalTag);
        }
    }

    public static ItemTagWrapper create(String id) {
        ItemTagWrapper t = new ItemTagWrapper(id);
        ITEM_TAGS.add(t);
        return t;
    }

    public static class ItemTagWrapper {

        public final TagKey<Item> tagKey;
        protected final ArrayList<Item> values = new ArrayList<>();
        protected final ArrayList<TagKey<Item>> tags = new ArrayList<>();
        protected final ArrayList<Item> optionals = new ArrayList<>();
        protected final ArrayList<TagKey<Item>> optionalTags = new ArrayList<>();

        private ItemTagWrapper(String id) {
            this.tagKey = TagKey.of(RegistryKeys.ITEM, Identifier.of(Orchid.MOD_ID, id));
        }

        public ItemTagWrapper add(Item... items) {
            this.values.addAll(Arrays.asList(items));
            return this;
        }

        public ItemTagWrapper add(TagKey<Item> item) {
            this.tags.add(item);
            return this;
        }

        public ItemTagWrapper addOptionals(Item... items) {
            this.values.addAll(Arrays.asList(items));
            return this;
        }

        public ItemTagWrapper addOptionalTag(TagKey<Item> item) {
            this.optionalTags.add(item);
            return this;
        }

    }

}
