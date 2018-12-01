package com.github.bael.challenge.webstat.domain;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Document
public class SiteEntry {
    @Id
    private String id;
    private int userId;
    private int pageId;
    private LocalDateTime createdOn;

    public SiteEntry(int userId, int pageId, LocalDateTime createdOn) {
        this.userId = userId;
        this.pageId = pageId;
        this.createdOn = createdOn;
    }

    public String getId() {
        return id;
    }

    public int getUserId() {
        return userId;
    }

    public int getPageId() {
        return pageId;
    }

    public LocalDateTime getCreatedOn() {
        return createdOn;
    }
}
