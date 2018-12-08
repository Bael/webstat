package com.github.bael.challenge.webstat.domain;

import lombok.Data;

/**
 * Модель для хранения статистики за период
 */
@Data
public class SiteEntryStats {

    private final long persistentCount;
    private final long uniqueCount;
    private final long totalCount;
    public SiteEntryStats(long totalCount, long uniqueCount, long persistentCount) {
        this.persistentCount = persistentCount;
        this.uniqueCount = uniqueCount;
        this.totalCount = totalCount;
    }


}
