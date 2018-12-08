package com.github.bael.challenge.webstat.services;

import com.github.bael.challenge.webstat.data.SiteEntryRepository;
import com.github.bael.challenge.webstat.domain.SiteEntry;
import com.github.bael.challenge.webstat.domain.SiteEntryStats;
import com.github.bael.challenge.webstat.domain.TodayStatInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import java.util.concurrent.Future;
import java.util.concurrent.atomic.AtomicInteger;

@Service
public class WebStatServiceImpl implements WebStatService {

    private final Logger logger = LoggerFactory.getLogger(WebStatService.class);
    private final SiteEntryRepository entryRepository;
    private final SiteEntrySaver entrySaver;

    @Value("${persistentLimit:10}")
    private int limit;

    @Autowired
    public WebStatServiceImpl(SiteEntryRepository entryRepository, SiteEntrySaver entrySaver) {
        this.entryRepository = entryRepository;
        this.entrySaver = entrySaver;
    }

    @Override
    public SiteEntryStats getStatFromPeriod(LocalDateTime start, LocalDateTime finish) {
        Objects.requireNonNull(start);
        Objects.requireNonNull(finish);
        long totalCount = entryRepository.findAllByPeriodCount(start, finish).orElse(0L);
        long uniqueCount = entryRepository.findUniqueByPeriodCount(start, finish).orElse(0L);
        long persistentCount = entryRepository.findPersistentByPeriodCount(start, finish, limit).orElse(0L);
        return new SiteEntryStats(totalCount, uniqueCount, persistentCount);
    }

    @Override
    /**
     * сохраняем и пересчитываем счетчики
     * @userId код пользователя
     * @pageId код страницы
     */
    public TodayStatInfo saveSiteEntry(int userId, int pageId) {
        SiteEntry siteEntry = new SiteEntry(userId, pageId, LocalDateTime.now(ZoneId.of("UTC")));
        // асинхронно сохраняем
        entrySaver.asyncSaveEntry(siteEntry);
        // не ждем сохранения и сразу возвращаем даные за сутки
        return entrySaver.getTodayStat(siteEntry);
    }

}
