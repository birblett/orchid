package com.birblett.mixin;

import com.birblett.entity.TickedEntity;
import com.birblett.entity.Ticker;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.function.Consumer;

@Mixin(Entity.class)
public abstract class EntityMixin_Ticker implements TickedEntity {

    @Shadow public abstract EntityType<?> getType();

    @Unique private final HashMap<String, Ticker> tickers = new HashMap<>();
    @Unique private final HashMap<String, Ticker> addedTickers = new HashMap<>();
    @Unique private final ArrayList<Ticker> anonymousTickers = new ArrayList<>();
    @Unique private final ArrayList<Ticker> addedAnonymousTickers = new ArrayList<>();

    @Override
    public Ticker orchid_getTicker(String id) {
        return this.tickers.get(id);
    }

    @Override
    public boolean orchid_hasTicker(String id) {
        return this.tickers.containsKey(id);
    }

    @Override
    public void orchid_setTicker(String id, Ticker t) {
        this.addedTickers.put(id, t);
        this.tickers.put(id, t);
    }

    @Override
    public void orchid_addAnonymousTicker(Ticker t) {
        this.addedAnonymousTickers.add(t);
        this.anonymousTickers.add(t);
    }

    @Override
    public void orchid_removeTicker(String id) {
        this.tickers.remove(id);
    }

    @Override
    public <T extends Ticker> void orchid_applyIfPresent(String id, Consumer<T> consumer) {
        T t = (T) this.tickers.get(id);
        if (t != null) {
            consumer.accept(t);
        }
    }

    @Inject(method = "tick", at = @At("HEAD"))
    private void handleTickers(CallbackInfo ci) {
        if (!this.addedTickers.isEmpty()) {
            this.tickers.putAll(this.addedTickers);
            this.addedTickers.clear();
        }
        if (!this.tickers.isEmpty()) {
            this.tickers.forEach((key, t) -> t.tick());
            this.tickers.entrySet().removeIf(e -> e.getValue().shouldRemove());
        }
        if (!this.addedAnonymousTickers.isEmpty()) {
            this.anonymousTickers.addAll(this.addedAnonymousTickers);
            this.addedAnonymousTickers.clear();
        }
        if (!this.anonymousTickers.isEmpty()) {
            this.anonymousTickers.forEach(Ticker::tick);
            this.anonymousTickers.removeIf(Ticker::shouldRemove);
        }
    }

}

