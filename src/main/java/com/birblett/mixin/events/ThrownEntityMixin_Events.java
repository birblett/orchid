package com.birblett.mixin.events;

import com.birblett.util.EnchantmentUtils;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.minecraft.entity.projectile.thrown.ThrownEntity;
import net.minecraft.util.math.Vec3d;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(ThrownEntity.class)
public class ThrownEntityMixin_Events {

    @WrapOperation(method = "applyDrag", at = @At(value = "INVOKE", target = "Lnet/minecraft/util/math/Vec3d;multiply(D)Lnet/minecraft/util/math/Vec3d;"))
    private Vec3d modifyDrag(Vec3d instance, double value, Operation<Vec3d> original) {
        return original.call(instance, EnchantmentUtils.projectileDragModifier((ThrownEntity) (Object) this, value));
    }

}
