package com.birblett.enchantment.impl;

import com.birblett.entity.Ticker;
import net.minecraft.entity.Entity;

public class MarkedTicker extends Ticker {

    public static ID<MarkedTicker> ID = new ID<>("marked");

    private Entity target;
    private int ticksLeft = 200;

    public MarkedTicker(Entity entity, Entity target) {
        super(entity);
        this.target = target;
    }

    @Override
    public void tick() {
        --this.ticksLeft;
    }

    @Override
    public boolean shouldRemove() {
        return this.ticksLeft <= 0;
    }

    public void setTarget(Entity entity) {
        this.target = entity;
        this.ticksLeft = 200;
    }

    public Entity getTarget() {
        return this.target != null && this.target.isAlive() ? this.target : null;
    }

}
