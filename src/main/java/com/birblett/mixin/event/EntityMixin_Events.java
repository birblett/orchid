package com.birblett.mixin.event;

import com.birblett.enchantment.OrchidEnchantWrapper;
import com.birblett.interfaces.entity.EnchantmentFlags;
import com.birblett.util.EnchantmentUtils;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.minecraft.entity.Entity;
import net.minecraft.entity.projectile.ProjectileEntity;
import org.apache.commons.lang3.mutable.MutableDouble;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Entity.class)
public class EntityMixin_Events implements EnchantmentFlags {

    @Unique long lastProcessedTick = 0;

    @Override
    public boolean orchid_isTickProcessed() {
        long time = ((Entity) (Object) this).getWorld().getTime();
        long lastTime = this.lastProcessedTick;
        this.lastProcessedTick = time;
        return time == lastTime;
    }

    @Override
    public void orchid_processTick(CallbackInfo c) {
        if (!this.orchid_isTickProcessed()) {
            Entity e = (Entity) (Object) this;
            if (EnchantmentUtils.entityIterator(e, (enchant, level) -> enchant.onEntityTick(e, e.getWorld(), level))) {
                c.cancel();
            }
        }
    }

    @Inject(method = "tick", at = @At("HEAD"))
    private void tickEvents(CallbackInfo ci) {
        this.orchid_processTick(ci);
    }

    @WrapOperation(method = "applyGravity", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/Entity;getFinalGravity()D"))
    private double applyGravityMods(Entity instance, Operation<Double> original) {
        MutableDouble gravity = new MutableDouble(original.call(instance));
        if (instance instanceof ProjectileEntity p) {
            EnchantmentUtils.entityIterator(p, (enchant, level) -> {
                gravity.setValue(enchant.projectileGravityModifier(p, gravity.getValue(), level));
                return OrchidEnchantWrapper.ControlFlow.CONTINUE;
            });
        }
        return gravity.doubleValue();
    }

}
