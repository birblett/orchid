package com.birblett.mixin.accessor;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.projectile.ProjectileEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.RangedWeaponItem;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(RangedWeaponItem.class)
public interface RangedWeaponItemAccessor {

    @Invoker("createArrowEntity")
    ProjectileEntity createProjectile(World world, LivingEntity shooter, ItemStack weaponStack, ItemStack projectileStack, boolean critical);

}
