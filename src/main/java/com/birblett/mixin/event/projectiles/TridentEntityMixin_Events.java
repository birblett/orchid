package com.birblett.mixin.event.projectiles;

import com.birblett.Orchid;
import com.birblett.interfaces.entity.EnchantmentFlags;
import com.birblett.mixin.accessor.PersistentProjectileAccessor;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.minecraft.entity.Entity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.projectile.TridentEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.server.world.ServerWorld;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(TridentEntity.class)
public abstract class TridentEntityMixin_Events implements EnchantmentFlags {

    @Inject(method = "tick", at = @At("HEAD"), cancellable = true)
    private void tickEvents(CallbackInfo ci) {
        this.orchid_processTick(ci);
    }

    @WrapOperation(method = "onEntityHit", at = @At(value = "INVOKE", target = "Lnet/minecraft/enchantment/EnchantmentHelper;getDamage(Lnet/minecraft/server/world/ServerWorld;Lnet/minecraft/item/ItemStack;Lnet/minecraft/entity/Entity;Lnet/minecraft/entity/damage/DamageSource;F)F"))
    private float getModifiedDamage(ServerWorld world, ItemStack stack, Entity target, DamageSource damageSource, float baseDamage, Operation<Float> original) {
        return original.call(world, stack, target, damageSource, (float) ((PersistentProjectileAccessor) this).orchid_damage());
    }

}
