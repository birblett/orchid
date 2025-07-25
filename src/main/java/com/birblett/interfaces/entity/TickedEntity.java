package com.birblett.interfaces.entity;

import com.birblett.entity.Ticker;

import java.util.function.Consumer;

public interface TickedEntity {

    Ticker orchid_getTicker(String id);
    boolean orchid_hasTicker(String id);
    void orchid_setTicker(String id, Ticker t);
    void orchid_addAnonymousTicker(Ticker t);
    void orchid_removeTicker(String id);
    <T extends Ticker> void orchid_applyIfPresent(String id, Consumer<T> consumer);

}
