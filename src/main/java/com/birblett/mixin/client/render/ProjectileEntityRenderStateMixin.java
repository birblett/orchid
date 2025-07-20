package com.birblett.mixin.client.render;

import com.birblett.enchantment.OrchidEnchantments;
import com.birblett.interfaces.client.CustomProjectileData;
import com.birblett.util.EnchantmentUtils;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.render.entity.state.ProjectileEntityRenderState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.projectile.ProjectileEntity;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;

@Environment(EnvType.CLIENT)
@Mixin(ProjectileEntityRenderState.class)
public class ProjectileEntityRenderStateMixin implements CustomProjectileData {

    @Unique private Vec3d grapplePos;

    @Override
    public void orchid_updateCustom(ProjectileEntity e, float tickProgress) {
        Entity owner = EnchantmentUtils.trackedContains(e, OrchidEnchantments.GRAPPLING) ? e.getOwner() : null;
        if (owner != null) {
            this.grapplePos = MathHelper.lerp(tickProgress, new Vec3d(owner.lastX, owner.lastY, owner.lastZ), owner.getPos()).add(0, owner.getHeight() / 2, 0);
        } else {
            this.grapplePos = null;
        }
    }

    @Override
    public Vec3d orchid_getGrappling() {
        return this.grapplePos;
    }

}
