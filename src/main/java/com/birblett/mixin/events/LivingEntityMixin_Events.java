package com.birblett.mixin.events;

import com.birblett.enchantment.OrchidEnchantWrapper;
import com.birblett.util.EnchantmentUtils;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.projectile.ProjectileEntity;
import org.apache.commons.lang3.mutable.MutableFloat;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(LivingEntity.class)
public class LivingEntityMixin_Events {

    @WrapOperation(method = "damage", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/LivingEntity;takeKnockback(DDD)V"))
    private void applyKnockbackMods(LivingEntity instance, double strength, double x, double z, Operation<Void> original, @Local(argsOnly = true) DamageSource source) {
        MutableFloat f = new MutableFloat(1);
        if (source.getSource() instanceof ProjectileEntity p) {
            EnchantmentUtils.projectileIterator(p, (enchant, level) -> {
                f.setValue(f.toFloat() * enchant.projectileKnockbackMultiplier(p, instance, level));
                return f.getValue() != 0 ? OrchidEnchantWrapper.Flow.CONTINUE : OrchidEnchantWrapper.Flow.BREAK;
            });
        }
        original.call(instance, f.floatValue() * strength, x, z);
    }

}
