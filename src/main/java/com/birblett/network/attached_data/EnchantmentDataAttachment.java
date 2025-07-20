package com.birblett.network.attached_data;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import io.netty.buffer.ByteBuf;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.codec.PacketCodecs;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;

import java.util.HashMap;
import java.util.Map;

public record EnchantmentDataAttachment(Map<RegistryKey<Enchantment>, Integer> data) {

    public static Codec<EnchantmentDataAttachment> CODEC = RecordCodecBuilder.create(
            instance -> instance.group(Codec.unboundedMap(RegistryKey.createCodec(RegistryKeys.ENCHANTMENT), Codec.INT)
                    .fieldOf("enchantments").forGetter(EnchantmentDataAttachment::data)).apply(instance, EnchantmentDataAttachment::new));

    public static PacketCodec<ByteBuf, EnchantmentDataAttachment> PACKET_CODEC = PacketCodecs.codec(CODEC);

    public static EnchantmentDataAttachment DEFAULT = new EnchantmentDataAttachment(Map.of());

    public int get(RegistryKey<Enchantment> key) {
        return this.data.get(key);
    }

    public int getOrDefault(RegistryKey<Enchantment> key, int defaultValue) {
        return this.data.getOrDefault(key, defaultValue);
    }

    public boolean contains(RegistryKey<Enchantment> key) {
        return this.data.containsKey(key);
    }

    public EnchantmentDataAttachment setEntry(RegistryKey<Enchantment> key, int value) {
        Map<RegistryKey<Enchantment>, Integer> map = new HashMap<>(this.data);
        map.put(key, value);
        return new EnchantmentDataAttachment(Map.copyOf(map));
    }

    public EnchantmentDataAttachment removeEntry(RegistryKey<Enchantment> key) {
        if (!this.data.containsKey(key)) {
            return this;
        }
        Map<RegistryKey<Enchantment>, Integer> map = new HashMap<>(this.data);
        map.remove(key);
        return new EnchantmentDataAttachment(Map.copyOf(map));
    }

    public Map<RegistryKey<Enchantment>, Integer> getAll() {
        return this.data;
    }

    public EnchantmentDataAttachment clear() { // clear method, just returns the default empty component
        return DEFAULT;
    }

}
