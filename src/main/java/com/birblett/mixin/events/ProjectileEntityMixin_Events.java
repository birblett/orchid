package com.birblett.mixin.events;

import com.birblett.entity.EntityDamageFlags;
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
public class ProjectileEntityMixin_Events implements EntityDamageFlags {

    @Unique boolean ignoresIFrames = false;

    @Override
    public void orchid_setIgnoreIFrames(boolean b) {
        this.ignoresIFrames = b;
    }

    @Override
    public boolean orchid_ignoresIFrames() {
        return this.ignoresIFrames;
    }

    @WrapOperation(method = "onCollision", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/projectile/ProjectileEntity;onEntityHit(Lnet/minecraft/util/hit/EntityHitResult;)V"))
    private void entityHitHandler(ProjectileEntity instance, EntityHitResult entityHitResult, Operation<Void> original) {
        if (this.orchid_ignoresIFrames()) {
            EntityUtils.resetIframes(entityHitResult.getEntity());
        }
        ProjectileEntity p = (ProjectileEntity) (Object) this;
        if (!EnchantmentUtils.projectileIterator(p, (enchant, level) -> enchant.onProjectileEntityHit(p, entityHitResult, level))) {
            original.call(instance, entityHitResult);
        }
    }

    @WrapOperation(method = "onCollision", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/projectile/ProjectileEntity;onBlockHit(Lnet/minecraft/util/hit/BlockHitResult;)V"))
    private void blockHitHandler(ProjectileEntity instance, BlockHitResult blockHitResult, Operation<Void> original) {
        ProjectileEntity p = (ProjectileEntity) (Object) this;
        if (!EnchantmentUtils.projectileIterator(p, (enchant, level) -> enchant.onProjectileBlockHit(p, blockHitResult, level))) {
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
