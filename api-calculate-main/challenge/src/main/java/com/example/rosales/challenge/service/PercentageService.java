package com.example.rosales.challenge.service;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.concurrent.locks.ReentrantReadWriteLock;

@Service
public class PercentageService {
    private final ExternalPercentageClient client;

    private volatile Double cachedPercentage = null;
    private volatile Instant cachedAt = null;
    private final ReentrantReadWriteLock lock = new ReentrantReadWriteLock();

    private static final long TTL_MILLIS = 30 * 60 * 1000L; // 30 minutes

    public PercentageService(ExternalPercentageClient client) {
        this.client = client;
        fetchAndCache();
    }

    public double getPercentage() {
        if (isCacheValid()) {
            return cachedPercentage;
        }
        try {
            fetchAndCache();
            if (cachedPercentage != null) return cachedPercentage;
        } catch (Exception e) {
        }
        if (cachedPercentage != null) return cachedPercentage;
        throw new IllegalStateException("Percentage not available and external service failed.");
    }

    private boolean isCacheValid() {
        Instant at = cachedAt;
        if (cachedPercentage == null || at == null) return false;
        long age = Instant.now().toEpochMilli() - at.toEpochMilli();
        return age <= TTL_MILLIS;
    }

    private void fetchAndCache() {
        Double val = client.fetchPercentage().orElse(null);
        if (val != null) {
            lock.writeLock().lock();
            try {
                cachedPercentage = val;
                cachedAt = Instant.now();
            } finally {
                lock.writeLock().unlock();
            }
        }
    }

    @Scheduled(fixedRate = 10 * 60 * 1000)
    public void scheduledRefresh() {
        try { fetchAndCache(); } catch (Exception ignored) {}
    }


}
