package com.birblett.mixin.accessor;

import com.birblett.interfaces.RangedWeapon;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.projectile.ProjectileEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.RangedWeaponItem;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(RangedWeaponItem.class)
public abstract class RangedWeaponItemAccess implements RangedWeapon {

    @Shadow protected abstract ProjectileEntity createArrowEntity(World world, LivingEntity shooter, ItemStack weaponStack, ItemStack projectileStack, boolean critical);

    @Override
    public ProjectileEntity createProjectile(World world, LivingEntity shooter, ItemStack weaponStack, ItemStack projectileStack, boolean critical) {
        return this.createArrowEntity(world, shooter, weaponStack, projectileStack, true);
    }

}
