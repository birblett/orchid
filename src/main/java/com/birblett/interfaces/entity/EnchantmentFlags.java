package com.birblett.interfaces.entity;

import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

public interface EnchantmentFlags {

    boolean orchid_isTickProcessed();
    void orchid_processTick(CallbackInfo c);

}
