package com.birblett.datagen.wrapper;

import com.birblett.datagen.Translateable;
import net.minecraft.entity.damage.DamageType;
import net.minecraft.registry.tag.TagKey;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

public class DamageTypeWrapper implements Translateable<DamageTypeWrapper> {

    private final HashMap<String, String> translationMap = new HashMap<>();
    private final ArrayList<TagKey<DamageType>> applicableTags = new ArrayList<>();
    public String translationKey;
    public final DamageType damageType;

    public DamageTypeWrapper(DamageType type) {
        this.damageType = type;
    }

    public DamageTypeWrapper addToTag(TagKey<DamageType> tag) {
        this.applicableTags.add(tag);
        return this;
    }

    public void forEachTag(Consumer<TagKey<DamageType>> tagConsumer) {
        this.applicableTags.forEach(tagConsumer);
    }

    @Override
    public DamageTypeWrapper translate(String lang, String value) {
        this.translationMap.put(lang, value);
        return this;
    }

    @Override
    public void forEachTranslation(BiConsumer<String, String> langTranslationConsumer) {
        this.translationMap.forEach(langTranslationConsumer);
    }

}
