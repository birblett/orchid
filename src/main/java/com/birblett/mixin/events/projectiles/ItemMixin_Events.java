package com.birblett.mixin.events.projectiles;

import com.birblett.util.EnchantmentUtils;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Item.class)
public class ItemMixin_Events {

    @Inject(method = "usageTick", at = @At("HEAD"))
    private void useTickEvent(World world, LivingEntity user, ItemStack stack, int remainingUseTicks, CallbackInfo ci) {
        EnchantmentUtils.stackIterator(stack, (enchant, level) -> enchant.useTick(user, stack, remainingUseTicks, level));
    }

}
