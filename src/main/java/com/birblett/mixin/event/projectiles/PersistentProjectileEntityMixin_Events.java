package com.birblett.mixin.event.projectiles;

import com.birblett.enchantment.OrchidEnchantWrapper;
import com.birblett.interfaces.entity.EnchantmentFlags;
import com.birblett.interfaces.entity.ProjectileFlags;
import com.birblett.util.EnchantmentUtils;
import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.projectile.PersistentProjectileEntity;
import net.minecraft.particle.ParticleEffect;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import org.apache.commons.lang3.mutable.MutableFloat;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(PersistentProjectileEntity.class)
public abstract class PersistentProjectileEntityMixin_Events implements ProjectileFlags, EnchantmentFlags {

    @Inject(method = "tick", at = @At("HEAD"), cancellable = true)
    private void tickEvents(CallbackInfo ci) {
        this.orchid_processTick(ci);
    }

    @WrapOperation(method = "tick", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/World;addParticleClient(Lnet/minecraft/particle/ParticleEffect;DDDDDD)V"))
    private void modifyParticle(World instance, ParticleEffect parameters, double x, double y, double z, double velocityX, double velocityY, double velocityZ, Operation<Void> original) {
        PersistentProjectileEntity p = (PersistentProjectileEntity) (Object) this;
        ParticleEffect tmp = EnchantmentUtils.entityIteratorGeneric(p, (enchant, level) ->
                enchant.projectileParticleModifier(p, level));
        original.call(instance, tmp == null ? parameters : tmp, x, y, z, velocityX, velocityY, velocityZ);
    }

    @ModifyExpressionValue(method = "knockback", at = @At(value = "INVOKE", target = "Lnet/minecraft/enchantment/EnchantmentHelper;modifyKnockback(Lnet/minecraft/server/world/ServerWorld;Lnet/minecraft/item/ItemStack;Lnet/minecraft/entity/Entity;Lnet/minecraft/entity/damage/DamageSource;F)F"))
    private float applyEnchantmentKnockback(float original, @Local(argsOnly = true) LivingEntity target) {
        MutableFloat f = new MutableFloat(original);
        PersistentProjectileEntity p = (PersistentProjectileEntity) (Object) this;
        EnchantmentUtils.entityIterator(p, (enchant, level) -> {
            f.setValue(f.toFloat() * enchant.projectileKnockbackMultiplier(p, target, level));
            return f.getValue() != 0 ? OrchidEnchantWrapper.ControlFlow.CONTINUE : OrchidEnchantWrapper.ControlFlow.BREAK;
        });
        return f.getValue();
    }

    @WrapOperation(method = "applyDrag", at = @At(value = "INVOKE", target = "Lnet/minecraft/util/math/Vec3d;multiply(D)Lnet/minecraft/util/math/Vec3d;"))
    private Vec3d modifyDrag(Vec3d instance, double value, Operation<Vec3d> original) {
        return original.call(instance, EnchantmentUtils.projectileDragModifier((PersistentProjectileEntity) (Object) this, value));
    }

}
