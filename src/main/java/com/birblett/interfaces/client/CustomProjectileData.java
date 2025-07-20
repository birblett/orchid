package com.birblett.interfaces.client;

import net.minecraft.entity.projectile.ProjectileEntity;
import net.minecraft.util.math.Vec3d;

public interface CustomProjectileData {

    void orchid_updateCustom(ProjectileEntity e, float tickProgress);
    Vec3d orchid_getGrappling();

}
