package com.github.bael.challenge.webstat.domain;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;


@Entity
@Getter
@Setter
public class SiteEntry {
    @Id
    @GeneratedValue
    private long id;
    private int userId;
    private int pageId;
    private LocalDateTime createdOn;

    public SiteEntry(int userId, int pageId, LocalDateTime createdOn) {
        this.userId = userId;
        this.pageId = pageId;
        this.createdOn = createdOn;
    }


}
