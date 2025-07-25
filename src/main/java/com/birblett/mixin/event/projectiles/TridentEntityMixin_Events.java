package com.birblett.mixin.event.projectiles;

import com.birblett.interfaces.entity.EnchantmentFlags;
import com.birblett.mixin.accessor.PersistentProjectileAccessor;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.projectile.PersistentProjectileEntity;
import net.minecraft.entity.projectile.TridentEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(TridentEntity.class)
public abstract class TridentEntityMixin_Events implements EnchantmentFlags {

    @Inject(method = "<init>(Lnet/minecraft/world/World;Lnet/minecraft/entity/LivingEntity;Lnet/minecraft/item/ItemStack;)V", at = @At("TAIL"))
    private void setDamage1(World world, LivingEntity owner, ItemStack stack, CallbackInfo ci) {
        ((PersistentProjectileEntity) (Object) this).setDamage(8);
    }

    @Inject(method = "<init>(Lnet/minecraft/world/World;DDDLnet/minecraft/item/ItemStack;)V", at = @At("TAIL"))
    private void setDamage2(World world, double x, double y, double z, ItemStack stack, CallbackInfo ci) {
        ((PersistentProjectileEntity) (Object) this).setDamage(8);
    }

    @Inject(method = "<init>(Lnet/minecraft/entity/EntityType;Lnet/minecraft/world/World;)V", at = @At("TAIL"))
    private void setDamage3(EntityType<? extends TridentEntity> entityType, World world, CallbackInfo ci) {
        ((PersistentProjectileEntity) (Object) this).setDamage(8);
    }

    @Inject(method = "tick", at = @At("HEAD"), cancellable = true)
    private void tickEvents(CallbackInfo ci) {
        this.orchid_processTick(ci);
    }

    @WrapOperation(method = "onEntityHit", at = @At(value = "INVOKE", target = "Lnet/minecraft/enchantment/EnchantmentHelper;getDamage(Lnet/minecraft/server/world/ServerWorld;Lnet/minecraft/item/ItemStack;Lnet/minecraft/entity/Entity;Lnet/minecraft/entity/damage/DamageSource;F)F"))
    private float getModifiedDamage(ServerWorld world, ItemStack stack, Entity target, DamageSource damageSource, float baseDamage, Operation<Float> original) {
        return original.call(world, stack, target, damageSource, (float) ((PersistentProjectileAccessor) this).orchid_damage());
    }

}
