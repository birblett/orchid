package com.birblett.mixin.events.items;

import com.birblett.interfaces.item.EnchantableItem;
import com.birblett.util.EnchantmentUtils;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Item.class)
public class ItemMixin_Events implements EnchantableItem {

    @Inject(method = "use", at = @At("HEAD"), cancellable = true)
    private void onUseEvent(World world, PlayerEntity user, Hand hand, CallbackInfoReturnable<ActionResult> cir) {
        this.enchantUseAction(world, user, hand, cir);
    }

    @Inject(method = "usageTick", at = @At("HEAD"))
    private void useTickEvent(World world, LivingEntity user, ItemStack stack, int remainingUseTicks, CallbackInfo ci) {
        EnchantmentUtils.stackIterator(stack, (enchant, level) -> enchant.onUseTick(user, stack, remainingUseTicks, level));
    }

    @Inject(method = "onStoppedUsing", at = @At("HEAD"), cancellable = true)
    private void onStoppedUsingEvent(ItemStack stack, World world, LivingEntity user, int remainingUseTicks, CallbackInfoReturnable<Boolean> cir) {
        this.enchantStoppedUsingAction(user, stack, world, remainingUseTicks, cir);
    }

}
