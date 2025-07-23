package com.birblett.damage_types;

import com.birblett.Orchid;
import com.birblett.datagen.wrapper.DamageTypeWrapper;
import net.minecraft.entity.damage.DamageEffects;
import net.minecraft.entity.damage.DamageType;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.tag.DamageTypeTags;
import net.minecraft.util.Identifier;

import java.util.HashMap;
import java.util.Map;

public class OrchidDamageTypes {

    public static final Map<RegistryKey<DamageType>, DamageTypeWrapper> DAMAGE_TYPES = new HashMap<>();

    public static RegistryKey<DamageType> IMMOLATION = register("immolation",
            new DamageTypeWrapper(new DamageType("orchid.immolation", 0.1f, DamageEffects.BURNING))
                    .addToTag(DamageTypeTags.NO_KNOCKBACK)
                    .translate("en_us", "%1$s was immolated")
    );

    private static RegistryKey<DamageType> register(String id, DamageTypeWrapper damageType) {
        RegistryKey<DamageType> k = RegistryKey.of(RegistryKeys.DAMAGE_TYPE, Identifier.of(Orchid.MOD_ID, id));
        damageType.translationKey = "death.attack.orchid." + id;
        DAMAGE_TYPES.put(k, damageType);
        return k;
    }

}
