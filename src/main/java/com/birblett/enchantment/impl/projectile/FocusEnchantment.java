package com.birblett.enchantment.impl.projectile;

import com.birblett.enchantment.OrchidEnchantWrapper;
import com.birblett.enchantment.OrchidEnchantments;
import com.birblett.mixin.accessor.PersistentProjectileAccessor;
import com.birblett.util.EnchantmentUtils;
import com.birblett.util.WorldUtils;
import net.minecraft.component.type.AttributeModifierSlot;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.projectile.PersistentProjectileEntity;
import net.minecraft.entity.projectile.ProjectileEntity;
import net.minecraft.entity.projectile.TridentEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.registry.tag.TagKey;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

public class FocusEnchantment extends OrchidEnchantWrapper {

    public FocusEnchantment(String id, int processPriority, TagKey<Item> supportedItems, int weight, int maxLevel,
                            Enchantment.Cost minCost, Enchantment.Cost maxCost, int anvilCost, AttributeModifierSlot... slots) {
        super(id, processPriority, supportedItems, null, weight, maxLevel, minCost, maxCost, anvilCost, slots);
    }

    @Override
    public ControlFlow onUseTick(LivingEntity user, ItemStack stack, int remainingUseTicks, int level) {
        if (remainingUseTicks == 71981) {
            WorldUtils.playSound(null, user.getWorld(), user, SoundEvents.ENTITY_ARROW_HIT_PLAYER, 1.0f, 2.0f);
        }
        return ControlFlow.CONTINUE;
    }

    @Override
    public ControlFlow onProjectileFired(LivingEntity shooter, ProjectileEntity entity, ItemStack stack, ItemStack projectileStack, ServerWorld world, boolean critical, int level, Flag flag) {
        int useTime = stack.getItem().getMaxUseTime(stack, shooter) - shooter.getItemUseTimeLeft();
        if (useTime < 23 && useTime >= 20 && entity instanceof PersistentProjectileEntity p) {
            p.setDamage(((PersistentProjectileAccessor) p).orchid_damage() + (entity instanceof TridentEntity ? 6 : 1.5));
            p.setGlowing(true);
        } else {
            EnchantmentUtils.removeTracked(entity, OrchidEnchantments.FOCUS);
        }
        return ControlFlow.CONTINUE;
    }

    @Override
    public ControlFlow onProjectileTick(ProjectileEntity entity, World world, int level) {
        if (world.isClient && entity instanceof PersistentProjectileEntity p && p.isCritical() &&
                !((PersistentProjectileAccessor) p).orchid_inGround()) {
            Vec3d pos = entity.getPos();
            Vec3d v = entity.getVelocity().multiply(-0.333333);
            world.addImportantParticleClient(ParticleTypes.END_ROD, true, pos.x, pos.y, pos.z, v.x, v.y, v.z);
            world.addImportantParticleClient(ParticleTypes.END_ROD, true, pos.x + v.x, pos.y + v.y, pos.z + v.z, v.x, v.y, v.z);
            world.addImportantParticleClient(ParticleTypes.END_ROD, true, pos.x + 2 * v.x, pos.y + 2 * v.y, pos.z + 2 * v.z, v.x, v.y, v.z);
        }
        return ControlFlow.CONTINUE;
    }

}
