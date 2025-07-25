package com.birblett.enchantment.impl;

import com.birblett.enchantment.OrchidEnchantWrapper;
import com.birblett.entity.Ticker;
import com.birblett.mixin.accessor.TridentEntityAccessor;
import net.minecraft.component.type.AttributeModifierSlot;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.projectile.ArrowEntity;
import net.minecraft.entity.projectile.PersistentProjectileEntity;
import net.minecraft.entity.projectile.ProjectileEntity;
import net.minecraft.entity.projectile.TridentEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.tag.TagKey;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.hit.EntityHitResult;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.Vec3d;

public class AirstrikeEnchantment extends OrchidEnchantWrapper {

    public AirstrikeEnchantment(String id, int processPriority, TagKey<Item> supportedItems, int weight, int maxLevel,
                                Enchantment.Cost minCost, Enchantment.Cost maxCost, int anvilCost, AttributeModifierSlot... slots) {
        super(id, processPriority, supportedItems, null, weight, maxLevel, minCost, maxCost, anvilCost, slots);
    }

    @Override
    public ControlFlow onProjectileFired(LivingEntity shooter, ProjectileEntity entity, ItemStack stack, ItemStack projectileStack, ServerWorld world, boolean critical, int level, Flag flag) {
        if (critical && flag != Flag.SUMMON && world instanceof ServerWorld) {
            Ticker.set(entity, AirstrikeTicker.ID, new AirstrikeTicker(stack, projectileStack));
        }
        return ControlFlow.CONTINUE;
    }

    @Override
    public ControlFlow onProjectileHit(ProjectileEntity entity, HitResult result, int level) {
        if (entity.getOwner() instanceof LivingEntity owner && owner.getWorld() instanceof ServerWorld world) {
            Ticker.apply(entity, AirstrikeTicker.ID, t -> {
                boolean critical = entity instanceof ArrowEntity p && p.isCritical() || !(entity instanceof ArrowEntity);
                if (critical && t.isDirect() && (!(entity instanceof TridentEntity) || !((TridentEntityAccessor) entity).orchid_dealtDamage())) {
                    Vec3d target = result instanceof EntityHitResult entityHitResult ?
                            entityHitResult.getEntity().getPos().add(0, entityHitResult.getEntity().getHeight() / 2, 0) :
                            entity.getPos();
                    Ticker.addAnonymous(owner, new AirstrikeTicker(owner, world, target, t.weaponStack, t.projectileStack));
                } else if (t.isSummon() && entity instanceof PersistentProjectileEntity) {
                    entity.discard();
                }
            });
        }
        return ControlFlow.CONTINUE;
    }

    @Override
    public float projectileKnockbackMultiplier(ProjectileEntity entity, Entity target, int level) {
        return 0;
    }

}
