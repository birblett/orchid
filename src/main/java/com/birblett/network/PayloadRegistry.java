package com.birblett.network;

import com.birblett.network.payloads.PlayerAttackAttemptC2SPayload;
import com.birblett.util.EnchantmentUtils;
import net.fabricmc.fabric.api.networking.v1.PayloadTypeRegistry;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;

public class PayloadRegistry {

    public static void register() {
        PayloadTypeRegistry.playC2S().register(PlayerAttackAttemptC2SPayload.ID, PlayerAttackAttemptC2SPayload.CODEC);
        ServerPlayNetworking.registerGlobalReceiver(PlayerAttackAttemptC2SPayload.ID, (payload, context) -> {
            EnchantmentUtils.stackIterator(context.player().getMainHandStack(), (enchant, level) -> enchant.mainhandAttackAttempt(context.player(), level));
            EnchantmentUtils.stackIterator(context.player().getOffHandStack(), (enchant, level) -> enchant.offhandAttackAttempt(context.player(), level));
        });
    }

}
