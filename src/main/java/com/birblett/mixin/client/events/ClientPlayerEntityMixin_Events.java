package com.birblett.mixin.client.events;

import com.birblett.interfaces.client.ClientPlayerEnchantTracker;
import com.birblett.util.EnchantmentUtils;
import com.birblett.util.InputRecord;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.minecraft.client.input.Input;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.registry.RegistryKey;
import net.minecraft.util.PlayerInput;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;

import java.util.HashMap;

@Mixin(ClientPlayerEntity.class)
public class ClientPlayerEntityMixin_Events implements ClientPlayerEnchantTracker {

    @Shadow public Input input;

    @Unique private final HashMap<RegistryKey<Enchantment>, Integer> enchants = new HashMap<>();
    @Unique private InputRecord last = new InputRecord(false, false, false, false, false, false, false);

    @Override
    public int orchid_getTempLevel(RegistryKey<Enchantment> key) {
        return this.enchants.getOrDefault(key, 0);
    }

    @Override
    public void orchid_setTempLevel(RegistryKey<Enchantment> key, int level) {
        this.enchants.put(key, level);
    }

    @WrapOperation(method = "tickMovement", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/network/AbstractClientPlayerEntity;tickMovement()V"))
    private void applyMovementMods(ClientPlayerEntity instance, Operation<Void> original) {
        PlayerInput playerInput = input.playerInput;
        InputRecord pressed = new InputRecord(
                playerInput.forward() && !this.last.forward(), playerInput.backward() && !this.last.backward(),
                playerInput.left() && !this.last.left(), playerInput.right() && !this.last.right(), playerInput.jump() &&
                !this.last.jump(), playerInput.sneak() && !this.last.sneak(), playerInput.sprint() && !this.last.sprint()
        );
        this.last = new InputRecord(playerInput.forward(), playerInput.backward(), playerInput.left(), playerInput.right(),
                playerInput.jump(), playerInput.sneak(), playerInput.sprint());
        if (!EnchantmentUtils.equipIterator(instance, (enchant, level) ->
                enchant.onMovementTick(instance, instance.getWorld(), input, pressed, level))) {
            original.call(instance);
        }
    }

}
