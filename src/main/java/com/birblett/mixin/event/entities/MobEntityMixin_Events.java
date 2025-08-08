package com.birblett.mixin.event.entities;

import com.birblett.enchantment.OrchidEnchantWrapper;
import com.birblett.util.EnchantmentUtils;
import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.entity.Entity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.server.world.ServerWorld;
import org.apache.commons.lang3.mutable.MutableFloat;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(MobEntity.class)
public class MobEntityMixin_Events {

    @ModifyVariable(method = "tryAttack", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/Entity;damage(Lnet/minecraft/server/world/ServerWorld;Lnet/minecraft/entity/damage/DamageSource;F)Z"))
    private float onAttackEvent(float value, @Local(argsOnly = true) Entity target, @Local ItemStack weaponStack, @Local DamageSource source, @Local(ordinal = 0) float damage) {
        MutableFloat dmg = new MutableFloat(damage);
        MobEntity p = (MobEntity) (Object) this;
        EnchantmentUtils.stackIterator(weaponStack, (enchant, level) -> {
            dmg.setValue(enchant.attackModifier(p, target, weaponStack, dmg.getValue(), source, level));
            return dmg.getValue() != 0 ? OrchidEnchantWrapper.ControlFlow.CONTINUE : OrchidEnchantWrapper.ControlFlow.BREAK;
        });
        return dmg.getValue();
    }

    @Inject(method = "tryAttack", at = @At(value = "INVOKE", target = "Lnet/minecraft/item/ItemStack;postHit(Lnet/minecraft/entity/LivingEntity;Lnet/minecraft/entity/LivingEntity;)Z"))
    private void postAttackEvent(ServerWorld world, Entity target, CallbackInfoReturnable<Boolean> cir, @Local(ordinal = 0) float damage, @Local ItemStack weaponStack, @Local DamageSource source) {
        MobEntity p = (MobEntity) (Object) this;
        EnchantmentUtils.stackIterator(weaponStack, (enchant, level) -> enchant.postAttack(p, target, weaponStack, damage, source, level));
    }

}
