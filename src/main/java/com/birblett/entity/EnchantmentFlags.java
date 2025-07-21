package com.birblett.entity;

import com.birblett.util.EnchantmentUtils;
import net.minecraft.entity.projectile.ProjectileEntity;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

public interface EnchantmentFlags {

    boolean orchid_isTickProcessed();

    default void processTick(CallbackInfo c) {
        if (!this.orchid_isTickProcessed()) {
            ProjectileEntity p = (ProjectileEntity) this;
            if (EnchantmentUtils.projectileIterator(p, (enchant, level) -> enchant.onProjectileTick(p, p.getWorld(), level))) {
                c.cancel();
            }
        }
    }

}
