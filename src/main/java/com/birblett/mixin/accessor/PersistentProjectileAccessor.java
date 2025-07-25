package com.birblett.mixin.accessor;

import net.minecraft.entity.projectile.PersistentProjectileEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(PersistentProjectileEntity.class)
public interface PersistentProjectileAccessor {

    @Accessor("damage")
    double orchid_damage();

    @Invoker("isInGround")
    boolean orchid_inGround();

    @Invoker("setInGround")
    void orchid_setInGround(boolean inGround);

}
