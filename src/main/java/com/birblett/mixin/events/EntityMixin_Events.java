package com.birblett.mixin.events;

import com.birblett.enchantment.OrchidEnchantWrapper;
import com.birblett.util.EnchantmentUtils;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.minecraft.entity.Entity;
import net.minecraft.entity.projectile.ProjectileEntity;
import org.apache.commons.lang3.mutable.MutableDouble;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(Entity.class)
public class EntityMixin_Events {

    @WrapOperation(method = "applyGravity", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/Entity;getFinalGravity()D"))
    private double applyGravityMods(Entity instance, Operation<Double> original) {
        MutableDouble gravity = new MutableDouble(original.call(instance));
        if (instance instanceof ProjectileEntity p) {
            EnchantmentUtils.projectileIterator(p, (enchant, level) -> {
                gravity.setValue(enchant.projectileGravityModifier(p, gravity.getValue(), level));
                return OrchidEnchantWrapper.Flow.CONTINUE;
            });
        }
        return gravity.doubleValue();
    }

}
