package com.birblett.datagen;

import com.birblett.Orchid;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.tag.TagKey;
import net.minecraft.util.Identifier;

import java.util.ArrayList;

public class TagWrapper<T, E> {

    public final TagKey<T> tagKey;
    protected final ArrayList<E> values = new ArrayList<>();
    protected final ArrayList<TagKey<T>> tags = new ArrayList<>();
    protected final ArrayList<E> optionals = new ArrayList<>();
    protected final ArrayList<TagKey<T>> optionalTags = new ArrayList<>();

    public TagWrapper(RegistryKey<? extends Registry<T>> key, String id) {
        this.tagKey = TagKey.of(key, Identifier.of(Orchid.MOD_ID, id));
    }

    public TagWrapper(TagKey<T> key) {
        this.tagKey = key;
    }

    public TagWrapper<T, E> add(E item) {
        this.values.add(item);
        return this;
    }

    public TagWrapper<T, E> add(TagKey<T> item) {
        this.tags.add(item);
        return this;
    }

    public TagWrapper<T, E> addOptional(E item) {
        this.values.add(item);
        return this;
    }

    public TagWrapper<T, E> addOptionalTag(TagKey<T> item) {
        this.optionalTags.add(item);
        return this;
    }

}
