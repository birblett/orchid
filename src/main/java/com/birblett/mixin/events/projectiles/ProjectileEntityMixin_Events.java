package com.birblett.mixin.events.projectiles;

import com.birblett.interfaces.entity.EnchantmentFlags;
import com.birblett.interfaces.entity.ProjectileFlags;
import com.birblett.util.EnchantmentUtils;
import com.birblett.util.EntityUtils;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.mojang.serialization.Codec;
import net.minecraft.entity.projectile.ProjectileEntity;
import net.minecraft.storage.ReadView;
import net.minecraft.storage.WriteView;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.hit.EntityHitResult;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ProjectileEntity.class)
public abstract class ProjectileEntityMixin_Events implements ProjectileFlags, EnchantmentFlags {

    @Unique boolean ignoresIFrames = false;
    @Unique int lifeLeft = -1;

    @Override
    public void orchid_setIgnoreIFrames(boolean b) {
        this.ignoresIFrames = b;
    }

    @Override
    public boolean orchid_ignoresIFrames() {
        return this.ignoresIFrames;
    }

    @Override
    public void orchid_setLifeTime(int i) {
        this.lifeLeft = i;
    }

    @Override
    public void orchid_processTick(CallbackInfo c) {
        if (!this.orchid_isTickProcessed()) {
            ProjectileEntity p = (ProjectileEntity) (Object) this;
            if (EnchantmentUtils.entityIterator(p, (enchant, level) -> enchant.onProjectileTick(p, p.getWorld(), level))) {
                c.cancel();
            }
        }
    }

    @Inject(method = "tick", at = @At("HEAD"), cancellable = true)
    private void tickEvents(CallbackInfo ci) {
        this.orchid_processTick(ci);
    }

    @Inject(method = "tick", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/Entity;tick()V"))
    private void tickLife(CallbackInfo ci) {
        if (this.lifeLeft >= 0) {
            --this.lifeLeft;
            if (this.lifeLeft <= 0) {
                ((ProjectileEntity) (Object) this).discard();
            }
        }
    }

    @WrapOperation(method = "onCollision", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/projectile/ProjectileEntity;onEntityHit(Lnet/minecraft/util/hit/EntityHitResult;)V"))
    private void entityHitHandler(ProjectileEntity instance, EntityHitResult entityHitResult, Operation<Void> original) {
        if (this.orchid_ignoresIFrames()) {
            EntityUtils.resetIframes(entityHitResult.getEntity());
        }
        ProjectileEntity p = (ProjectileEntity) (Object) this;
        if (!EnchantmentUtils.entityIterator(p, (enchant, level) -> enchant.onProjectileEntityHit(p, entityHitResult, level))) {
            original.call(instance, entityHitResult);
        }
    }

    @WrapOperation(method = "onCollision", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/projectile/ProjectileEntity;onBlockHit(Lnet/minecraft/util/hit/BlockHitResult;)V"))
    private void blockHitHandler(ProjectileEntity instance, BlockHitResult blockHitResult, Operation<Void> original) {
        ProjectileEntity p = (ProjectileEntity) (Object) this;
        if (!EnchantmentUtils.entityIterator(p, (enchant, level) -> enchant.onProjectileBlockHit(p, blockHitResult, level))) {
            original.call(instance, blockHitResult);
        }
    }

    @Inject(method = "writeCustomData", at = @At("HEAD"))
    private void writeEnchantments(WriteView view, CallbackInfo ci) {
        view.put("IgnoresIFrames", Codec.BOOL, this.ignoresIFrames);
    }

    @Inject(method = "readCustomData", at = @At("HEAD"))
    private void readEnchantments(ReadView view, CallbackInfo ci) {
        this.ignoresIFrames = view.read("IgnoresIFrames", Codec.BOOL).orElse(false);
    }

}
