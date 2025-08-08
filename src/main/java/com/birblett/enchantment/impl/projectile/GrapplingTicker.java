package com.birblett.enchantment.impl.projectile;

import com.birblett.enchantment.OrchidEnchantments;
import com.birblett.entity.Ticker;
import com.birblett.util.EnchantmentUtils;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.projectile.ProjectileEntity;
import net.minecraft.item.ItemStack;

import java.util.ArrayList;

public class GrapplingTicker extends Ticker {

    public static ID<GrapplingTicker> ID = new ID<>("grappling");

    public final ItemStack weaponStack;
    private final ArrayList<ProjectileEntity> entities;

    public GrapplingTicker(Entity owner, ProjectileEntity entity, ItemStack weaponStack) {
        super(owner);
        this.weaponStack = weaponStack;
        this.entities = new ArrayList<>();
        this.addEntity(entity);
    }

    public void addEntity(ProjectileEntity entity) {
        this.entities.removeIf(e -> !EnchantmentUtils.trackedContains(e, OrchidEnchantments.GRAPPLING));
        if (this.entities.size() >= 3) {
            EnchantmentUtils.removeTracked(this.entities.get(0), OrchidEnchantments.GRAPPLING);
            this.entities.set(0, this.entities.get(1));
            this.entities.set(1, this.entities.get(2));
            this.entities.set(2, entity);
        } else {
            this.entities.add(entity);
        }
    }

    public void removeAll() {
        for (Entity e : this.entities) {
            EnchantmentUtils.removeTracked(e, OrchidEnchantments.GRAPPLING);
        }
        this.entities.clear();
    }

    @Override
    public boolean shouldRemove() {
        boolean b = this.entities.isEmpty();
        if (!b && this.entity instanceof LivingEntity e) {
            b = !e.isHolding(i -> i == this.weaponStack);
        }
        if (b) {
            this.removeAll();
        }
        return b;
    }

}
