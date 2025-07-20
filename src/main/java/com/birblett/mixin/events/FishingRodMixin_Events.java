package com.birblett.mixin.events;

import com.birblett.enchantment.OrchidEnchantWrapper;
import com.birblett.util.EnchantmentUtils;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.FishingBobberEntity;
import net.minecraft.entity.projectile.ProjectileEntity;
import net.minecraft.item.FishingRodItem;
import net.minecraft.item.ItemStack;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.Hand;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(FishingRodItem.class)
public class FishingRodMixin_Events {

    @WrapOperation(method = "use", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/projectile/FishingBobberEntity;use(Lnet/minecraft/item/ItemStack;)I"))
    private int serverReelEvent(FishingBobberEntity instance, ItemStack usedItem, Operation<Integer> original, @Local(argsOnly = true) World world, @Local(argsOnly = true) PlayerEntity user, @Local(argsOnly = true) Hand hand) {
        if (world instanceof ServerWorld w && !EnchantmentUtils.onProjectileFired(usedItem, usedItem, instance, user, w, false, OrchidEnchantWrapper.Flag.DIRECT)) {
            return original.call(instance, usedItem);
        }
        return 0;
    }

    @WrapOperation(method = "use", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/projectile/ProjectileEntity;spawn(Lnet/minecraft/entity/projectile/ProjectileEntity;Lnet/minecraft/server/world/ServerWorld;Lnet/minecraft/item/ItemStack;)Lnet/minecraft/entity/projectile/ProjectileEntity;"))
    private ProjectileEntity serverCastEvent(ProjectileEntity projectile, ServerWorld world, ItemStack stack, Operation<ProjectileEntity> original, @Local(argsOnly = true) PlayerEntity user, @Local(argsOnly = true) Hand hand) {
        if (!EnchantmentUtils.onProjectileFired(stack, stack, projectile, user, world, true, OrchidEnchantWrapper.Flag.DIRECT)) {
            return original.call(projectile, world, stack);
        }
        return null;
    }

}
