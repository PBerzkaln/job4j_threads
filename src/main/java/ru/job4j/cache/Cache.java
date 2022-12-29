package ru.job4j.cache;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class Cache {
    private final Map<Integer, Base> memory = new ConcurrentHashMap<>();

    public boolean add(Base model) {
        return memory.putIfAbsent(model.getId(), model) == null;
    }

    public boolean update(Base model) {
        return memory.computeIfPresent(model.getId(), (key, value) -> {
            Base stored = memory.get(model.getId());
            if (stored.getVersion() != model.getVersion()) {
                throw new OptimisticException("Versions are not equal");
            }
            Base newBase = new Base(model.getId(), model.getVersion() + 1);
            memory.replace(model.getId(), newBase);
            return newBase;
        }) != null;
    }

    public void delete(Base model) {
        memory.remove(model.getId(), model);
    }
}