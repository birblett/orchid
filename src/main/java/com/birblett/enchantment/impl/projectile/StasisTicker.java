package com.birblett.enchantment.impl.projectile;

import com.birblett.enchantment.OrchidEnchantWrapper;
import com.birblett.enchantment.OrchidEnchantments;
import com.birblett.entity.Ticker;
import com.birblett.util.EnchantmentUtils;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.projectile.PersistentProjectileEntity;
import net.minecraft.entity.projectile.ProjectileEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.Hand;
import net.minecraft.util.Pair;
import net.minecraft.util.math.Vec3d;

import java.util.HashMap;
import java.util.Map;

public class StasisTicker extends Ticker {

    public static ID<StasisTicker> ID = new ID<>("stasis");

    private final LivingEntity entity;
    private final ItemStack stack;
    private final ItemStack projectileStack;
    private final Hand hand;

    HashMap<ProjectileEntity, Pair<Vec3d, Boolean>> stasisedProjectiles = new HashMap<>();

    public StasisTicker(LivingEntity entity, ItemStack stack, ItemStack projectileStack, Hand hand) {
        super(null, Type.DIRECT);
        this.entity = entity;
        this.stack = stack;
        this.projectileStack = projectileStack;
        this.hand = hand;
    }

    public StasisTicker(Entity entity) {
        super(entity, Type.SUMMON);
        this.entity = null;
        this.stack = null;
        this.projectileStack = null;
        this.hand = null;
        super.entity = entity;
    }

    public void addStasisedProjectile(ProjectileEntity e, Vec3d v, boolean b) {
        this.stasisedProjectiles.put(e, new Pair<>(v, b));
    }

    @Override
    public boolean shouldRemove() {
        if (this.type == Type.SUMMON) {
            return false;
        } else if (this.entity == null || this.entity.getStackInHand(this.hand) != this.stack || this.stasisedProjectiles.size() > 9) {
            this.fireAll(false);
            return true;
        }
        return false;
    }

    public void fireAll(boolean remove) {
        if (this.type == Type.DIRECT && this.entity != null && this.entity.getWorld() instanceof ServerWorld world) {
            for (Map.Entry<ProjectileEntity, Pair<Vec3d, Boolean>> e : this.stasisedProjectiles.entrySet()) {
                ProjectileEntity p = e.getKey();
                if (p.isRemoved()) {
                    continue;
                }
                EnchantmentUtils.removeTracked(p, OrchidEnchantments.STASIS);
                Ticker.remove(p, StasisTicker.ID);
                boolean critical = e.getValue().getRight();
                if (p instanceof PersistentProjectileEntity pr) {
                    pr.setCritical(critical);
                }
                EnchantmentUtils.stackIterator(this.stack, (ench, level) -> ench.onProjectileFired(this.entity,
                        e.getKey(), this.stack, this.projectileStack, world, critical, level, OrchidEnchantWrapper.Flag.SECONDARY));
            }
            if (remove) {
                Ticker.remove(this.entity, ID);
            }
        }
    }

}
