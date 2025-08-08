package com.birblett.mixin.event.entities;

import com.birblett.enchantment.OrchidEnchantWrapper;
import com.birblett.interfaces.entity.EnchantmentFlags;
import com.birblett.util.EnchantmentUtils;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.projectile.ProjectileEntity;
import net.minecraft.particle.BlockStateParticleEffect;
import net.minecraft.particle.ParticleEffect;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.Vec3d;
import org.apache.commons.lang3.mutable.MutableDouble;
import org.apache.commons.lang3.mutable.MutableFloat;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(LivingEntity.class)
public abstract class LivingEntityMixin_Events implements EnchantmentFlags {

    @Override
    public void orchid_processTick(CallbackInfo c) {
        if (!this.orchid_isTickProcessed()) {
            LivingEntity e = (LivingEntity) (Object) this;
            if (EnchantmentUtils.entityIterator(e, (enchant, level) -> enchant.onEntityTick(e, e.getWorld(), level))
                    || EnchantmentUtils.equipIterator(e, (enchant, level) -> enchant.onEntityTickEquip(e, e.getWorld(), level))) {
                c.cancel();
            }
        }
    }

    @Inject(method = "tick", at = @At("HEAD"))
    private void tickEvents(CallbackInfo ci) {
        this.orchid_processTick(ci);
    }

    @WrapOperation(method = "damage", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/LivingEntity;takeKnockback(DDD)V"))
    private void applyKnockbackMods(LivingEntity instance, double strength, double x, double z, Operation<Void> original, @Local(argsOnly = true) DamageSource source) {
        MutableFloat f = new MutableFloat(1);
        if (source.getSource() instanceof ProjectileEntity p) {
            EnchantmentUtils.entityIterator(p, (enchant, level) -> {
                f.setValue(f.toFloat() * enchant.projectileKnockbackMultiplier(p, instance, level));
                return f.getValue() != 0 ? OrchidEnchantWrapper.ControlFlow.CONTINUE : OrchidEnchantWrapper.ControlFlow.BREAK;
            });
        }
        original.call(instance, f.floatValue() * strength, x, z);
    }

    @WrapOperation(method = "fall", at = @At(value = "INVOKE", target = "Lnet/minecraft/server/world/ServerWorld;spawnParticles(Lnet/minecraft/particle/ParticleEffect;DDDIDDDD)I"))
    private int replaceFallParticles(ServerWorld instance, ParticleEffect parameters, double x, double y, double z, int count, double offsetX, double offsetY, double offsetZ, double speed, Operation<Integer> original, @Local(argsOnly = true) BlockState state) {
        BlockState temp;
        if ((temp = EnchantmentUtils.getEntityBlockFeedbackState((LivingEntity) (Object) this, state)) != null) {
            parameters = new BlockStateParticleEffect(ParticleTypes.BLOCK, temp.getBlock().getDefaultState());
        }
        return original.call(instance, parameters, x, y, z, count, offsetX, offsetY, offsetZ, speed);
    }

    @WrapOperation(method = "travelMidAir", at = @At(value = "INVOKE", target = "Lnet/minecraft/block/Block;getSlipperiness()F"))
    private float modifySlipperiness(Block instance, Operation<Float> original) {
        LivingEntity e = (LivingEntity) (Object) this;
        MutableFloat f = new MutableFloat(original.call(instance));
        EnchantmentUtils.equipIterator(e, (enchant, level) -> {
            f.setValue(enchant.modifySlipperiness(e, e.getWorld(), instance, f.getValue(), level));
            return f.getValue() != 0 ? OrchidEnchantWrapper.ControlFlow.CONTINUE : OrchidEnchantWrapper.ControlFlow.BREAK;
        });
        return f.floatValue();
    }

    @WrapOperation(method = "getMovementSpeed(F)F", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/LivingEntity;getMovementSpeed()F"))
    private float modifySlipperyMovement(LivingEntity instance, Operation<Float> original, @Local(argsOnly = true) float slipperiness) {
        MutableFloat f = new MutableFloat(original.call(instance));
        EnchantmentUtils.equipIterator(instance, (enchant, level) -> {
            f.setValue(enchant.modifySlipperyMovementSpeed(instance, instance.getWorld(), f.getValue(), slipperiness, level));
            return f.getValue() != 0 ? OrchidEnchantWrapper.ControlFlow.CONTINUE : OrchidEnchantWrapper.ControlFlow.BREAK;
        });
        return f.floatValue();
    }

    @WrapOperation(method = "jump", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/LivingEntity;setVelocity(DDD)V"))
    private void modifyJumpVelocityDirect(LivingEntity instance, double x, double y, double z, Operation<Void> original) {
        MutableDouble dx = new MutableDouble(x), dy = new MutableDouble(y), dz = new MutableDouble(z);
        EnchantmentUtils.equipIterator(instance, (enchant, level) -> {
            Vec3d v = enchant.modifyJumpDirect(instance, instance.getWorld(),
                    new Vec3d(dx.doubleValue(), dy.doubleValue(), dz.doubleValue()), level);
            dx.setValue(v.x);
            dy.setValue(v.y);
            dz.setValue(v.z);
            return OrchidEnchantWrapper.ControlFlow.CONTINUE;
        });
        original.call(instance, dx.doubleValue(), dy.doubleValue(), dz.doubleValue());
    }

}
