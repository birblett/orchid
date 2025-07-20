package com.birblett.network;

import com.birblett.Orchid;
import com.birblett.network.attached_data.EnchantmentDataAttachment;
import net.fabricmc.fabric.api.attachment.v1.AttachmentRegistry;
import net.fabricmc.fabric.api.attachment.v1.AttachmentSyncPredicate;
import net.fabricmc.fabric.api.attachment.v1.AttachmentType;
import net.minecraft.util.Identifier;

@SuppressWarnings("UnstableApiUsage")
public class AttachedDataRegistry {

    public static final AttachmentType<EnchantmentDataAttachment> ENCHANTMENT_DATA_ATTACHMENT = AttachmentRegistry.create(
            Identifier.of(Orchid.MOD_ID,"enchantment_data"), b -> b.initializer(() -> EnchantmentDataAttachment.DEFAULT)
                            .persistent(EnchantmentDataAttachment.CODEC)
                            .syncWith(EnchantmentDataAttachment.PACKET_CODEC, AttachmentSyncPredicate.all())
        );

    public static void init() {}

}
