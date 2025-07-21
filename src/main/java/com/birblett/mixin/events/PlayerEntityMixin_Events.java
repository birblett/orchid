package com.birblett.mixin.events;

import com.birblett.enchantment.OrchidEnchantWrapper;
import com.birblett.util.EnchantmentUtils;
import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(PlayerEntity.class)
public class PlayerEntityMixin_Events {

    @Inject(method = "getProjectileType", at = @At(value = "INVOKE", target = "Ljava/util/function/Predicate;test(Ljava/lang/Object;)Z"), cancellable = true)
    private void projectileTypeOverride(ItemStack stack, CallbackInfoReturnable<ItemStack> cir, @Local(ordinal = 2) ItemStack projectileStack) {
        if (EnchantmentUtils.stackIterator(stack, (enchant, level) ->
                enchant.allowProjectileType((PlayerEntity) (Object) this, stack, projectileStack) ? OrchidEnchantWrapper.Flow.CANCEL_BREAK :
                        OrchidEnchantWrapper.Flow.CONTINUE)) {
            cir.setReturnValue(projectileStack);
        }
    }

}
