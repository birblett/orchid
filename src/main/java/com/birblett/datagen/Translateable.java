package com.birblett.datagen;

import com.birblett.datagen.lang.OrchidEnglishLanguageProvider;

import java.util.function.BiConsumer;

public interface Translateable<T> {

    String getTranslation(String lang);

    default T translate(String value) {
        return this.translate("en_us", value);
    }

    T translate(String lang, String value);

    static void addTranslation(String lang, String key, String val) {
        switch (lang) {
            case "en_us" -> OrchidEnglishLanguageProvider.addTranslation(key, val);
        }
    }

    void forEachTranslation(BiConsumer<String, String> langTranslationConsumer);
}
