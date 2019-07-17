package com.dornu.hsd.server.domain.cache;

import com.dornu.hsd.server.domain.models.HsdProcessorAccessTokenResponse;
import com.dornu.hsd.server.domain.models.Status;
import com.dornu.hsd.server.helpers.Helper;
import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

@Service
public class AccessTokenCacheManager extends AbstractCacheManager<HsdProcessorAccessTokenResponse> {

    private static final String cacheKey = UUID.randomUUID().toString();

    private Cache<String, HsdProcessorAccessTokenResponse> cache;

    public void saveAccessTokenToCache(HsdProcessorAccessTokenResponse accessToken) {
        if (accessToken.getStatus() == Status.FAILURE) return;
        if (cache == null) initCache(Helper.getDesiredValueOrDefault(Integer.parseInt(accessToken.getExpires_in()), 120));
        saveEntry(cacheKey, accessToken);
    }

    public Optional<HsdProcessorAccessTokenResponse> getAccessTokenFromCache() {
        HsdProcessorAccessTokenResponse accessToken = tryGetEntry(cacheKey);
        if (accessToken == null) return Optional.empty();
        return Optional.of(accessToken);
    }

    public void removeAccessTokenFromCache() {
        deleteEntry(cacheKey);
    }

    @Override
    public void initCache(int cacheExpirySeconds) {
        cache = CacheBuilder.newBuilder()
                .expireAfterWrite(cacheExpirySeconds - 2, TimeUnit.SECONDS) // -2 seconds latency
                .build();
    }

    @Override
    public void saveEntry(String key, HsdProcessorAccessTokenResponse value) {
        if (cache != null) cache.put(key, value);
    }

    @Override
    public HsdProcessorAccessTokenResponse tryGetEntry(String key) {
        return (cache != null) ? cache.getIfPresent(key) : null;
    }

    @Override
    public void deleteEntry(String key) {
        if (cache != null) cache.invalidate(key);
    }
}
