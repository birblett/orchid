package com.birblett.network.payloads;

import com.birblett.Orchid;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.packet.CustomPayload;
import net.minecraft.util.Identifier;

public class PlayerAttackAttemptC2SPayload implements CustomPayload {

    public static final Identifier IDENTIFIER = Identifier.of(Orchid.MOD_ID, "player_attack_attempt");
    public static final CustomPayload.Id<PlayerAttackAttemptC2SPayload> ID = new CustomPayload.Id<>(IDENTIFIER);
    public static final PacketCodec<RegistryByteBuf, PlayerAttackAttemptC2SPayload> CODEC = PacketCodec.of(PlayerAttackAttemptC2SPayload::noOpEncoder, PlayerAttackAttemptC2SPayload::new);

    public PlayerAttackAttemptC2SPayload() {}

    public PlayerAttackAttemptC2SPayload(PacketByteBuf b) {}

    public void noOpEncoder(PacketByteBuf b) {}

    @Override
    public Id<? extends CustomPayload> getId() {
        return ID;
    }

}