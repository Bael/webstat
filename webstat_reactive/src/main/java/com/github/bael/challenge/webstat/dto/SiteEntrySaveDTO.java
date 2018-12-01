package com.github.bael.challenge.webstat.dto;

public class SiteEntrySaveDTO {
    public int totalCount;
    public int uniqueCount;

    public SiteEntrySaveDTO(int totalCount, int uniqueCount) {
        this.totalCount = totalCount;
        this.uniqueCount = uniqueCount;
    }

    @Override
    public String toString() {
        return "SiteEntrySaveDTO{" +
                "totalCount=" + totalCount +
                ", uniqueCount=" + uniqueCount +
                '}';
    }
}
