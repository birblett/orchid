package com.birblett.mixin.client.render;

import com.birblett.enchantment.OrchidEnchantments;
import com.birblett.util.EnchantmentUtils;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.render.Frustum;
import net.minecraft.client.render.WorldRenderer;
import net.minecraft.client.render.entity.EntityRenderDispatcher;
import net.minecraft.entity.Entity;
import net.minecraft.entity.projectile.ProjectileEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Environment(EnvType.CLIENT)
@Mixin(value = WorldRenderer.class, priority = -1)
public class WorldRendererMixin {

    @WrapOperation(method = "getEntitiesToRender", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/render/entity/EntityRenderDispatcher;shouldRender(Lnet/minecraft/entity/Entity;Lnet/minecraft/client/render/Frustum;DDD)Z"))
    private boolean renderOverrides(EntityRenderDispatcher instance, Entity entity, Frustum frustum, double x, double y, double z, Operation<Boolean> original) {
        if (entity instanceof ProjectileEntity p && EnchantmentUtils.trackedContains(p, OrchidEnchantments.GRAPPLING)) {
            return true;
        }
        return original.call(instance, entity, frustum, x, y, z);
    }

}
