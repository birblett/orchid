package com.birblett.entity;

import net.minecraft.entity.projectile.ProjectileEntity;

public interface ProjectileFlags {

    void orchid_setIgnoreIFrames(boolean b);
    boolean orchid_ignoresIFrames();
    void orchid_setLifeTime(int i);

    static void setIgnoreIFrames(ProjectileEntity e, boolean b) {
        ((ProjectileFlags) e).orchid_setIgnoreIFrames(b);
    }

    static boolean ignoresIFrames(ProjectileEntity e) {
        return ((ProjectileFlags) e).orchid_ignoresIFrames();
    }

    static void setLifetime(ProjectileEntity e, int i) {
        ((ProjectileFlags) e).orchid_setLifeTime(i);
    }

}
