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
        System.out.println(siteEntryStats.persistentUserMap.values());
        dto.persistentCount = siteEntryStats.persistentUserMap.values().stream()
                .filter(integers -> integers.size() > 9).count();

        dto.uniqueCount = siteEntryStats.persistentUserMap.size();
        dto.totalCount = siteEntryStats.totalCount.get();

        System.out.println(dto);
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
