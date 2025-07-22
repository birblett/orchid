package com.birblett.util;

import net.minecraft.entity.LivingEntity;
import net.minecraft.sound.SoundEvent;
import net.minecraft.world.World;

public class WorldUtils {

    public static void playSound(LivingEntity origin, World world, LivingEntity entity, SoundEvent sound, float volume, float pitch) {
        world.playSound(origin, entity.getX(), entity.getY(), entity.getZ(), sound, entity.getSoundCategory(), volume, pitch);
    }

}
