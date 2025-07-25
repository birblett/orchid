package com.birblett.mixin.events.projectiles;

import com.birblett.entity.EnchantmentFlags;
import com.birblett.util.EnchantmentUtils;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.minecraft.entity.projectile.FishingBobberEntity;
import net.minecraft.util.math.Vec3d;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(FishingBobberEntity.class)
public abstract class FishingBobberEntityMixin_Events implements EnchantmentFlags {

    @Inject(method = "tick", at = @At("HEAD"), cancellable = true)
    private void tickEvents(CallbackInfo ci) {
        this.orchid_processTick(ci);
    }

    @WrapOperation(method = "tick", at = @At(value = "INVOKE", target = "Lnet/minecraft/util/math/Vec3d;multiply(D)Lnet/minecraft/util/math/Vec3d;", ordinal = 0))
    private Vec3d modifyDrag(Vec3d instance, double value, Operation<Vec3d> original) {
        return original.call(instance, EnchantmentUtils.projectileDragModifier((FishingBobberEntity) (Object) this, value));
    }

}
