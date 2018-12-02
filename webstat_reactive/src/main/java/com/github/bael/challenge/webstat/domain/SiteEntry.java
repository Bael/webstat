package com.github.bael.challenge.webstat.domain;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Document
/**
 * Вход пользователя на страницу
 */
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

    int getUserId() {
        return userId;
    }

    int getPageId() {
        return pageId;
    }

    public LocalDateTime getCreatedOn() {
        return createdOn;
    }
}
