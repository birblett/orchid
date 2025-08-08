package com.birblett.mixin.event.entities;

import com.birblett.enchantment.OrchidEnchantWrapper;
import com.birblett.util.EnchantmentUtils;
import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.entity.Entity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import org.apache.commons.lang3.mutable.MutableFloat;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(PlayerEntity.class)
public class PlayerEntityMixin_Events {

    @ModifyVariable(method = "attack", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/player/PlayerEntity;resetLastAttackedTicks()V"), ordinal = 1)
    private float onAttackEvent(float bonus, @Local(argsOnly = true) Entity target, @Local ItemStack weaponStack, @Local DamageSource source, @Local(ordinal = 0) float damage) {
        MutableFloat dmg = new MutableFloat(damage + bonus);
        PlayerEntity p = (PlayerEntity) (Object) this;
        EnchantmentUtils.equipIterator(p, (enchant, level) -> {
            dmg.setValue(enchant.attackModifier(p, target, weaponStack, dmg.getValue(), source, level));
            return dmg.getValue() != 0 ? OrchidEnchantWrapper.ControlFlow.CONTINUE : OrchidEnchantWrapper.ControlFlow.BREAK;
        });
        return dmg.getValue() - damage;
    }

    @Inject(method = "attack", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/player/PlayerEntity;getWorld()Lnet/minecraft/world/World;", ordinal = 8))
    private void postAttackEvent(Entity target, CallbackInfo ci, @Local(ordinal = 0) float damage, @Local ItemStack weaponStack, @Local DamageSource source) {
        PlayerEntity p = (PlayerEntity) (Object) this;
        EnchantmentUtils.equipIterator(p, (enchant, level) ->
                enchant.postAttack(p, target, weaponStack, damage, source, level));
    }

    @Inject(method = "getProjectileType", at = @At(value = "INVOKE", target = "Ljava/util/function/Predicate;test(Ljava/lang/Object;)Z"), cancellable = true)
    private void projectileTypeOverride(ItemStack stack, CallbackInfoReturnable<ItemStack> cir, @Local(ordinal = 2) ItemStack projectileStack) {
        if (EnchantmentUtils.stackIterator(stack, (enchant, level) ->
                enchant.allowProjectileType((PlayerEntity) (Object) this, stack, projectileStack) ? OrchidEnchantWrapper.ControlFlow.CANCEL_BREAK :
                        OrchidEnchantWrapper.ControlFlow.CONTINUE)) {
            cir.setReturnValue(projectileStack);
        }
    }

}
