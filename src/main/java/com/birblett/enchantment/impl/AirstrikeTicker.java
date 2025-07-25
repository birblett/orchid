package com.birblett.enchantment.impl;

import com.birblett.enchantment.OrchidEnchantWrapper;
import com.birblett.interfaces.entity.ProjectileFlags;
import com.birblett.entity.Ticker;
import com.birblett.mixin.accessor.RangedWeaponItemAccessor;
import com.birblett.util.EnchantmentUtils;
import com.birblett.util.VectorUtils;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.projectile.ArrowEntity;
import net.minecraft.entity.projectile.PersistentProjectileEntity;
import net.minecraft.entity.projectile.ProjectileEntity;
import net.minecraft.entity.projectile.TridentEntity;
import net.minecraft.item.FireworkRocketItem;
import net.minecraft.item.ItemStack;
import net.minecraft.item.RangedWeaponItem;
import net.minecraft.item.TridentItem;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.Vec3d;

public class AirstrikeTicker extends Ticker {

    public static ID<AirstrikeTicker> ID = new ID<>("arrow_rain");

    private final LivingEntity owner;
    private final Vec3d target;
    private final ServerWorld world;
    public final ItemStack weaponStack;
    public final ItemStack projectileStack;
    private int ticks;
    private final int ticksDelayed;

    private AirstrikeTicker(LivingEntity entity, ServerWorld world, Vec3d target, ItemStack weaponStack, ItemStack projectileStack, Type type) {
        super(type);
        this.owner = entity;
        this.world = world;
        this.target = target;
        this.weaponStack = weaponStack;
        this.projectileStack = projectileStack;
        boolean b = projectileStack != null && this.projectileStack.getItem() instanceof TridentItem;
        boolean b2 = !b && projectileStack != null && projectileStack.getItem() instanceof FireworkRocketItem;
        this.ticks = b ? 19 : (b2 ? 15 : 11);
        this.ticksDelayed = b ? 5 : (b2 ? 4 : 2);
    }

    public AirstrikeTicker(ItemStack weaponStack, ItemStack projectileStack) {
        this(null, null, null, weaponStack, projectileStack, Type.DIRECT);
    }

    public AirstrikeTicker(ProjectileEntity entity, Vec3d target) {
        this(null, null, target, null, null, Type.SUMMON);
        this.entity = entity;
    }

    public AirstrikeTicker(LivingEntity entity, ServerWorld world, Vec3d target, ItemStack weaponStack, ItemStack projectileStack) {
        this(entity, world, target, weaponStack, projectileStack, Type.SOURCE);
    }

    @Override
    public void tick() {
        if (this.isSource()) {
            if (this.ticks % this.ticksDelayed == 0) {
                Vec3d spawnPos = VectorUtils.applyDivergence(new Vec3d(0, 1, 0), 0.3).normalize()
                        .multiply(10 + this.owner.getRandom().nextBetween(0, 200) / 100.0).add(this.target);
                Vec3d velocity = VectorUtils.applyDivergence(this.target.subtract(spawnPos).normalize(), 0.03).normalize()
                        .multiply(3);
                ProjectileEntity projectileEntity = null;
                if (this.weaponStack.getItem() instanceof RangedWeaponItem w) {
                    projectileEntity = ((RangedWeaponItemAccessor) w).orchid_createArrowEntity(this.world, this.owner, this.weaponStack,
                            this.projectileStack, true);
                } else if (this.weaponStack.getItem() instanceof TridentItem) {
                    projectileEntity = new TridentEntity(this.world, this.owner, this.projectileStack);
                }
                if (projectileEntity != null) {
                    if (projectileEntity instanceof PersistentProjectileEntity p) {
                        if (p instanceof ArrowEntity) {
                            p.setCritical(true);
                        }
                        p.pickupType = PersistentProjectileEntity.PickupPermission.CREATIVE_ONLY;
                    }
                    projectileEntity.noClip = false;
                    EnchantmentHelper.onProjectileSpawned(this.world, this.projectileStack, projectileEntity, (item) -> {
                    });
                    projectileEntity.setVelocity(velocity);
                    projectileEntity.setPosition(spawnPos);
                    Vec3d p = spawnPos.subtract(velocity);
                    VectorUtils.rotateEntity(projectileEntity, velocity.normalize());
                    this.world.spawnParticles(ParticleTypes.CLOUD, p.x, p.y, p.z, 10, 0, 0, 0, 0.03);
                    Ticker.set(projectileEntity, ID, new AirstrikeTicker(projectileEntity, this.target.add(0, 5, 0)));
                    ProjectileFlags.setIgnoreIFrames(projectileEntity, true);
                    if (EnchantmentUtils.hasEnchant(this.weaponStack, Enchantments.FLAME, this.world)) {
                        projectileEntity.setOnFireFor(1000);
                    }
                    EnchantmentUtils.onProjectileFired(this.weaponStack, this.projectileStack, projectileEntity, this.owner, this.world, true, OrchidEnchantWrapper.Flag.SUMMON);
                    if (!projectileEntity.isRemoved()) {
                        this.world.spawnEntity(projectileEntity);
                    }
                }
            }
            --this.ticks;
        } else if (this.isSummon()) {
            this.entity.noClip = false;
        }
    }

    @Override
    public boolean shouldRemove() {
        return this.owner != null && this.ticks <= 0;
    }

}
