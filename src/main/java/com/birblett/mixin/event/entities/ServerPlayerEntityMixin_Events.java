package com.birblett.mixin.event.entities;

import com.birblett.util.EnchantmentUtils;
import com.birblett.util.InputRecord;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.PlayerInput;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ServerPlayerEntity.class)
public class ServerPlayerEntityMixin_Events {

    @Unique private InputRecord last = new InputRecord(false, false, false, false, false, false, false);

    @Inject(method = "setPlayerInput", at = @At("HEAD"))
    private void onServerInputEvent(PlayerInput playerInput, CallbackInfo ci) {
        ServerPlayerEntity p = (ServerPlayerEntity) (Object) this;
        InputRecord pressed = InputRecord.fromLast(playerInput, this.last);
        this.last = new InputRecord(playerInput.forward(), playerInput.backward(), playerInput.left(), playerInput.right(),
                playerInput.jump(), playerInput.sneak(), playerInput.sprint());
        EnchantmentUtils.equipIterator(p, (enchant, level) ->
                enchant.onServerPlayerInput(p, p.getWorld(), playerInput, pressed, level));
    }

}
