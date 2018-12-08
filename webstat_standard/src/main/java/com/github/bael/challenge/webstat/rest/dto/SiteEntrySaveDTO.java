package com.github.bael.challenge.webstat.rest.dto;

public class SiteEntrySaveDTO {
    public long totalCount;
    public long uniqueCount;

    public SiteEntrySaveDTO(long totalCount, long uniqueCount) {
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
