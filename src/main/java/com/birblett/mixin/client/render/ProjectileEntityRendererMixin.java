package com.birblett.mixin.client.render;

import com.birblett.interfaces.client.CustomProjectileData;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.ProjectileEntityRenderer;
import net.minecraft.client.render.entity.state.ProjectileEntityRenderState;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.projectile.PersistentProjectileEntity;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import org.joml.Matrix4f;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Environment(EnvType.CLIENT)
@Mixin(ProjectileEntityRenderer.class)
public class ProjectileEntityRendererMixin {

    @Inject(method = "render(Lnet/minecraft/client/render/entity/state/ProjectileEntityRenderState;Lnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/VertexConsumerProvider;I)V", at = @At("HEAD"))
    private void tickRenderer(ProjectileEntityRenderState state, MatrixStack matrixStack, VertexConsumerProvider vertexConsumerProvider, int i, CallbackInfo ci) {
        CustomProjectileData c = (CustomProjectileData) state;
        if (c.orchid_getGrappling() instanceof Vec3d pos) {
            renderGrapple(matrixStack, vertexConsumerProvider, state.x, state.y, state.z, pos);
        }
    }

    @Inject(method = "updateRenderState(Lnet/minecraft/entity/projectile/PersistentProjectileEntity;Lnet/minecraft/client/render/entity/state/ProjectileEntityRenderState;F)V", at = @At("HEAD"))
    private void updateRenderer(PersistentProjectileEntity entity, ProjectileEntityRenderState state, float tickProgress, CallbackInfo ci) {
        ((CustomProjectileData) state).orchid_updateCustom(entity, tickProgress);
    }

    @Unique
    private static void renderGrapple(MatrixStack matrices, VertexConsumerProvider vertexConsumers, double x, double y, double z, Vec3d vec3d) {
        matrices.push();
        float j = (float)(vec3d.x - x);
        float k = (float)(vec3d.y - y);
        float l = (float)(vec3d.z - z);
        VertexConsumer vertexConsumer = vertexConsumers.getBuffer(RenderLayer.getLeash());
        Matrix4f matrix4f = matrices.peek().getPositionMatrix();
        float n = (float) (MathHelper.fastInverseSqrt(j * j + l * l) * 0.025F / 2.0F);
        float o = l * n;
        float p = j * n;
        int u;
        for(u = 0; u <= 24; ++u) {
            renderGrappleSegment(vertexConsumer, matrix4f, j, k, l, 0.025F, o, p, u);
        }
        matrices.pop();
    }

    @Unique
    private static void renderGrappleSegment(VertexConsumer vertexConsumer, Matrix4f positionMatrix, float f, float g, float h, float j, float k, float l, int pieceIndex) {
        float index = (float)pieceIndex / 24.0F;
        float u = f * index;
        float v = g > 0.0F ? g * index * index : g - g * (1.0F - index) * (1.0F - index);
        float w = h * index;
        vertexConsumer.vertex(positionMatrix, u - k, v + j, w + l).color(0.1F, 0.1F, 0.1F, 1.0F).light(1);
        vertexConsumer.vertex(positionMatrix, u + k, v + (float) 0.025 - j, w - l).color(0.1F, 0.1F, 0.1F, 1.0F).light(1);
    }

}
