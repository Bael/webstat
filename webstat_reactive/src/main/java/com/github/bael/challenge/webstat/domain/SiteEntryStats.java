package com.github.bael.challenge.webstat.domain;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;


public class SiteEntryStats {

    private final int persistentLimit;
    private AtomicInteger totalCount = new AtomicInteger();
    private Map<Integer, Set<Integer>> persistentUserMap = new ConcurrentHashMap<>();

    public SiteEntryStats(int persistentLimit) {
        this.persistentLimit = persistentLimit;
    }

    public long getPersistentCount() {
        return persistentUserMap.values().stream()
                .filter(integers -> integers.size() >= persistentLimit).count();
    }

    public int getUniqueCount() {
        return persistentUserMap.size();

    }

    public int getTotalCount() {
        return totalCount.get();
    }

    public HashSet<Integer> getUserIdsSet() {
        return new HashSet<>(persistentUserMap.keySet());
    }

    public SiteEntryStats addSiteEntry(SiteEntry siteEntry) {
        totalCount.getAndIncrement();
        Set<Integer> pageIds = persistentUserMap.getOrDefault(siteEntry.getUserId(), new HashSet<>());
        pageIds.add(siteEntry.getPageId());
        persistentUserMap.put(siteEntry.getUserId(), pageIds);
        return this;

    }
}
