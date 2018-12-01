package com.github.bael.challenge.webstat.services;

import com.github.bael.challenge.webstat.domain.SiteEntryStats;
import com.github.bael.challenge.webstat.dto.SiteEntrySaveDTO;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;

public interface WebStatService {
    /**
     * Получение статистики посещений за заданный период
     *
     * @param start  дата время начала периода (UTC)
     * @param finish дата время конца периода (UTC)
     * @return статистику
     */
    Mono<SiteEntryStats> getStatFromPeriod(LocalDateTime start, LocalDateTime finish);

    Mono<SiteEntrySaveDTO> saveSiteEntry(int userId, int pageId);
}
