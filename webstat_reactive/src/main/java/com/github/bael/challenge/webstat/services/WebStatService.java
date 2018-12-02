package com.github.bael.challenge.webstat.services;

import com.github.bael.challenge.webstat.domain.SiteEntryStats;
import com.github.bael.challenge.webstat.dto.SiteEntrySaveDTO;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;

public interface WebStatService {
    /**
     * Получение статистики посещений за заданный период
     *
     * @start дата время начала периода (UTC)
     * @finish дата время конца периода (UTC)
     * @return статистику посещений за период
     */
    Mono<SiteEntryStats> getStatFromPeriod(LocalDateTime start, LocalDateTime finish);

    /**
     * Сохраняем посещение пользователем страницы
     *
     * @return количество уникальных и всех посетителей за сегодня
     * @userId
     * @pageId
     */
    Mono<SiteEntrySaveDTO> saveSiteEntry(int userId, int pageId);
}
