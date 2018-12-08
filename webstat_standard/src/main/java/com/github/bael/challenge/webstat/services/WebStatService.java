package com.github.bael.challenge.webstat.services;

import com.github.bael.challenge.webstat.domain.SiteEntryStats;
import com.github.bael.challenge.webstat.domain.TodayStatInfo;

import java.time.LocalDateTime;

public interface WebStatService {
    /**
     * Получение статистики посещений за заданный период
     *
     * @return статистику посещений за период
     * @start дата время начала периода (UTC)
     * @finish дата время конца периода (UTC)
     */
    SiteEntryStats getStatFromPeriod(LocalDateTime start, LocalDateTime finish);

    /**
     * Сохраняем посещение пользователем страницы
     *
     * @return количество уникальных и всех посетителей за сегодня
     * @userId
     * @pageId
     */
    TodayStatInfo saveSiteEntry(int userId, int pageId);
}
