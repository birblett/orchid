package com.birblett.entity;

import com.mojang.serialization.Codec;
import net.minecraft.entity.Entity;

import java.util.function.Consumer;

public class Ticker {

    protected final Type type;
    protected Entity entity;

    public Ticker(Entity entity) {
        this(entity, null);
    }

    public Ticker(Type type) {
        this(null, type);
    }

    public Ticker(Entity entity, Type type) {
        this.entity = entity;
        this.type = type;
    }

    public static boolean contains(Entity e, ID<?> t) {
        if (e == null) {
            return false;
        }
        return ((TickedEntity) e).orchid_hasTicker(t.value());
    }

    @SuppressWarnings("unchecked")
    public static <T extends Ticker> T get(Entity e, ID<T> t) {
        if (e == null) {
            return null;
        }
        return (T) ((TickedEntity) e).orchid_getTicker(t.value());
    }

    public static <T extends Ticker> void set(Entity e, ID<T> t, T ticker) {
        if (e == null) {
            return;
        }
        ((TickedEntity) e).orchid_setTicker(t.value(), ticker);
    }

    public static void remove(Entity e, ID<?> t) {
        if (e == null) {
            return;
        }
        ((TickedEntity) e).orchid_removeTicker(t.value());
    }

    public static void addAnonymous(Entity e, Ticker t) {
        if (e == null) {
            return;
        }
        ((TickedEntity) e).orchid_addAnonymousTicker(t);
    }

    public static <T extends Ticker> void apply(Entity e, ID<T> t, Consumer<T> consumer) {
        if (e == null) {
            return;
        }
        ((TickedEntity) e).orchid_applyIfPresent(t.value(), consumer);
    }

    public boolean isSource() {
        return this.type == Type.SOURCE;
    }

    public boolean isDirect() {
        return this.type == Type.DIRECT;
    }

    public boolean isSummon() {
        return this.type == Type.SUMMON;
    }

    public void tick() {}

    public boolean shouldRemove() {
        return false;
    }

    public Codec<Ticker> getCodec() {
        return null;
    }

    public enum Type {
        SOURCE,
        DIRECT,
        SUMMON
    }

    public record ID<T extends Ticker>(String value) {}

}
