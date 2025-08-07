package com.birblett.interfaces.client;

import net.minecraft.enchantment.Enchantment;
import net.minecraft.registry.RegistryKey;

public interface ClientPlayerEnchantTracker {

    int orchid_getTempLevel(RegistryKey<Enchantment> key);
    void orchid_setTempLevel(RegistryKey<Enchantment> key, int level);

}
