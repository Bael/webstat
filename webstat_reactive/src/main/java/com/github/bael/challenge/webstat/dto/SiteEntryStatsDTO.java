package com.github.bael.challenge.webstat.dto;

import com.github.bael.challenge.webstat.domain.SiteEntryStats;

import java.util.Objects;

public class SiteEntryStatsDTO {
    public long totalCount;
    public int uniqueCount;
    public long persistentCount;

    public static SiteEntryStatsDTO fillFrom(SiteEntryStats siteEntryStats) {
        SiteEntryStatsDTO dto = new SiteEntryStatsDTO();
        Objects.requireNonNull(siteEntryStats);

        dto.persistentCount = siteEntryStats.getPersistentCount();
        dto.uniqueCount = siteEntryStats.getUniqueCount();
        dto.totalCount = siteEntryStats.getTotalCount();
        return dto;
    }

    @Override
    public String toString() {
        return "SiteEntryStatsDTO{" +
                "totalCount=" + totalCount +
                ", uniqueCount=" + uniqueCount +
                ", persistentCount=" + persistentCount +
                '}';
    }
}
