package com.birblett.mixin.events;

import com.birblett.enchantment.OrchidEnchantWrapper;
import com.birblett.util.EnchantmentUtils;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.projectile.ProjectileEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.RangedWeaponItem;
import net.minecraft.server.world.ServerWorld;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;

import java.util.function.Consumer;

@Mixin(RangedWeaponItem.class)
public class RangedWeaponItemMixin_Events {

    @Unique private static final Consumer<ProjectileEntity> NO_OP = p -> {};

    @WrapOperation(method = "shootAll", at = @At(value = "INVOKE", target = "net/minecraft/entity/projectile/ProjectileEntity.spawn(Lnet/minecraft/entity/projectile/ProjectileEntity;Lnet/minecraft/server/world/ServerWorld;Lnet/minecraft/item/ItemStack;Ljava/util/function/Consumer;)Lnet/minecraft/entity/projectile/ProjectileEntity;"))
    private ProjectileEntity projectileEnchantmentEffects(ProjectileEntity entity, ServerWorld world, ItemStack projectileStack, Consumer<ProjectileEntity> beforeSpawn, Operation<ProjectileEntity> original, @Local(argsOnly = true, ordinal = 0) float speed, @Local(argsOnly = true, ordinal = 0) LivingEntity shooter, @Local(argsOnly = true) ItemStack stack, @Local(argsOnly = true) boolean critical) {
        beforeSpawn.accept(entity);
        EnchantmentUtils.onProjectileFired(stack, projectileStack, entity, shooter, world, critical, OrchidEnchantWrapper.Flag.DIRECT);
        if (!entity.isRemoved()) {
            original.call(entity, world, projectileStack, NO_OP);
        }
        return entity;
    }

}
