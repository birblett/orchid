package com.birblett.interfaces.item;

import com.birblett.util.EnchantmentUtils;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

public interface EnchantableItem {

    default void enchantUseAction(World world, PlayerEntity user, Hand hand, CallbackInfoReturnable<ActionResult> cir) {
        ActionResult a = EnchantmentUtils.stackIteratorGeneric(user.getStackInHand(hand), (enchant, level) ->
                enchant.onUse(user, user.getStackInHand(hand), world, hand, level));
        if (a != null) {
            cir.setReturnValue(a);
        }
    }

    default void enchantStoppedUsingAction(LivingEntity user, ItemStack stack, World world, int remainingUseTicks, CallbackInfoReturnable<Boolean> cir) {
        Boolean b = EnchantmentUtils.stackIteratorGeneric(stack, (enchant, level) ->
                enchant.onStoppedUsing(user, stack, world, remainingUseTicks, level));
        if (b != null) {
            cir.setReturnValue(b);
        }
    }

}
