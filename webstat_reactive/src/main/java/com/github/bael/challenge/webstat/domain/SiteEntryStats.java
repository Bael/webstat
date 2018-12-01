package com.github.bael.challenge.webstat.domain;

import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

public class SiteEntryStats {
    public AtomicInteger totalCount = new AtomicInteger();
    public Map<Integer, Set<Integer>> persistentUserMap = new ConcurrentHashMap<>();
}
