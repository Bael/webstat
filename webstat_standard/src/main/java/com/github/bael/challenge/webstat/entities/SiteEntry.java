package com.github.bael.challenge.webstat.entities;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import java.time.LocalDateTime;

@Entity
@Data
@NoArgsConstructor
public class SiteEntry {

    private long id;
    private int userId;
    private int pageId;
    private LocalDateTime createdOn;

}
