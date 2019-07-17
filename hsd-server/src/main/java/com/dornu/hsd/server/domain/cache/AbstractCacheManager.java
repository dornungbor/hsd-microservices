package com.dornu.hsd.server.domain.cache;

public abstract class AbstractCacheManager<T> {
    public abstract void initCache(int cacheExpiryMinutes);

    public abstract void saveEntry(String key, T value);

    public abstract T tryGetEntry(String key);

    public abstract void deleteEntry(String key);
}
