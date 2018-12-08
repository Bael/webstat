package com.github.bael.challenge.webstat.rest.dto;

import com.github.bael.challenge.webstat.domain.SiteEntryStats;
import lombok.Data;

import java.util.Objects;

@Data
public class SiteEntryStatsDTO {
    private long totalCount;
    private long uniqueCount;
    private long persistentCount;

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
