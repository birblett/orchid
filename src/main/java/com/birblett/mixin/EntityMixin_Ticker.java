package com.birblett.mixin;

import com.birblett.entity.Ticker;
import com.birblett.interfaces.entity.TickedEntity;
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
    @Unique private final ArrayList<Ticker> anonymousTickers = new ArrayList<>();

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
        this.tickers.put(id, t);
    }

    @Override
    public void orchid_addAnonymousTicker(Ticker t) {
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

    @SuppressWarnings("unchecked")
    @Inject(method = "tick", at = @At("HEAD"))
    private void handleTickers(CallbackInfo ci) {
        if (!this.tickers.isEmpty()) {
            ((HashMap<String, Ticker>) this.tickers.clone()).values().forEach(Ticker::tick);
            this.tickers.entrySet().removeIf(e -> e.getValue().shouldRemove());
        }
        if (!this.anonymousTickers.isEmpty()) {
            ((ArrayList<Ticker>) this.anonymousTickers.clone()).forEach(Ticker::tick);
            this.anonymousTickers.removeIf(Ticker::shouldRemove);
        }
    }

}

