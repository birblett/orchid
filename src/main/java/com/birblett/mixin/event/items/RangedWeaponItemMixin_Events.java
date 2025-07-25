package com.birblett.mixin.event.items;

import com.birblett.enchantment.OrchidEnchantWrapper;
import com.birblett.util.EnchantmentUtils;
import com.birblett.util.EntityUtils;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.projectile.ProjectileEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.RangedWeaponItem;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.function.Consumer;
import java.util.function.Predicate;

@Mixin(RangedWeaponItem.class)
public class RangedWeaponItemMixin_Events {

    @Inject(method = "createArrowEntity", at = @At("HEAD"), cancellable = true)
    private void projectileEnchantmentOverride(World world, LivingEntity shooter, ItemStack weaponStack, ItemStack projectileStack, boolean critical, CallbackInfoReturnable<ProjectileEntity> cir) {
        ProjectileEntity p = EnchantmentUtils.stackIteratorGeneric(weaponStack, (enchant, level) ->
                enchant.getProjectileFromItem(shooter, weaponStack, projectileStack));
        if (p != null) {
            cir.setReturnValue(p);
        }
    }

    @WrapOperation(method = "shootAll", at = @At(value = "INVOKE", target = "net/minecraft/entity/projectile/ProjectileEntity.spawn(Lnet/minecraft/entity/projectile/ProjectileEntity;Lnet/minecraft/server/world/ServerWorld;Lnet/minecraft/item/ItemStack;Ljava/util/function/Consumer;)Lnet/minecraft/entity/projectile/ProjectileEntity;"))
    private ProjectileEntity projectileEnchantmentEffects(ProjectileEntity entity, ServerWorld world, ItemStack projectileStack, Consumer<ProjectileEntity> beforeSpawn, Operation<ProjectileEntity> original, @Local(argsOnly = true, ordinal = 0) float speed, @Local(argsOnly = true, ordinal = 0) LivingEntity shooter, @Local(argsOnly = true) ItemStack stack, @Local(argsOnly = true) boolean critical) {
        beforeSpawn.accept(entity);
        EnchantmentUtils.onProjectileFired(stack, projectileStack, entity, shooter, world, critical, OrchidEnchantWrapper.Flag.DIRECT);
        if (!entity.isRemoved()) {
            original.call(entity, world, projectileStack, EntityUtils.PROJECTILE_NO_OP);
        }
        return entity;
    }

    @Inject(method = "getHeldProjectile", at = @At("HEAD"), cancellable = true)
    private static void getHeldProjectileOverride(LivingEntity entity, Predicate<ItemStack> predicate, CallbackInfoReturnable<ItemStack> cir) {
        if (EnchantmentUtils.stackIterator(entity.getMainHandStack(), (enchant, level) ->
                enchant.allowProjectileType(entity, entity.getMainHandStack(), entity.getOffHandStack()) ?
                        OrchidEnchantWrapper.ControlFlow.CANCEL_BREAK : OrchidEnchantWrapper.ControlFlow.CONTINUE)) {
            cir.setReturnValue(entity.getOffHandStack());
        } else if (EnchantmentUtils.stackIterator(entity.getOffHandStack(), (enchant, level) ->
                enchant.allowProjectileType(entity, entity.getOffHandStack(), entity.getMainHandStack()) ?
                        OrchidEnchantWrapper.ControlFlow.CANCEL_BREAK : OrchidEnchantWrapper.ControlFlow.CONTINUE)) {
            cir.setReturnValue(entity.getMainHandStack());
        }
    }

}
