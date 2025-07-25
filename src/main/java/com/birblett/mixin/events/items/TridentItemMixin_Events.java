package com.birblett.mixin.events.items;

import com.birblett.interfaces.item.EnchantableItem;
import com.birblett.util.EnchantmentUtils;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.TridentItem;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(TridentItem.class)
public class TridentItemMixin_Events implements EnchantableItem {

    @Inject(method = "use", at = @At("HEAD"), cancellable = true)
    private void onUseEvent(World world, PlayerEntity user, Hand hand, CallbackInfoReturnable<ActionResult> cir) {
        this.enchantUseAction(world, user, hand, cir);
    }

    @Inject(method = "onStoppedUsing", at = @At("HEAD"), cancellable = true)
    private void onStoppedUsingEvent(ItemStack stack, World world, LivingEntity user, int remainingUseTicks, CallbackInfoReturnable<Boolean> cir) {
        this.enchantStoppedUsingAction(user, stack, world, remainingUseTicks, cir);
    }

}
