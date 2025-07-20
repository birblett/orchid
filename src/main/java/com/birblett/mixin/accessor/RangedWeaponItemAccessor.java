package com.birblett.mixin.accessor;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.projectile.ProjectileEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.RangedWeaponItem;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.Hand;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

import java.util.List;

@Mixin(RangedWeaponItem.class)
public interface RangedWeaponItemAccessor {

    @Invoker("createArrowEntity")
    ProjectileEntity createProjectile(World world, LivingEntity shooter, ItemStack weaponStack, ItemStack projectileStack, boolean critical);

    @Invoker("load")
    static List<ItemStack> load(ItemStack stack, ItemStack projectileStack, LivingEntity shooter) {
        throw new UnsupportedOperationException();
    };

    @Invoker("shootAll")
    void shootAllProjectiles(ServerWorld world, LivingEntity shooter, Hand hand, ItemStack stack, List<ItemStack> projectiles, float speed, float divergence, boolean critical, @Nullable LivingEntity target);

}
