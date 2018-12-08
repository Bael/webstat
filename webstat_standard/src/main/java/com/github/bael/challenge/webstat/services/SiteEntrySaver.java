package com.github.bael.challenge.webstat.services;

import com.github.bael.challenge.webstat.data.SiteEntryRepository;
import com.github.bael.challenge.webstat.domain.SiteEntry;
import com.github.bael.challenge.webstat.domain.TodayStatInfo;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

/***
 * Сохранение в базу с подсчетом статистики
 */
@Component
public class SiteEntrySaver {

    private final SiteEntryRepository entryRepository;

    /**
     * Счетчик уникальных посетителей за сегодня
     */
    private Map<LocalDate, Set<Integer>> countUniqueMap = new ConcurrentHashMap<>();
    /**
     * Счетчик общего количества посетителей за сегодня
     */
    private Map<LocalDate, AtomicInteger> countTotalMap = new ConcurrentHashMap<>();

    public SiteEntrySaver(SiteEntryRepository entryRepository) {
        this.entryRepository = entryRepository;
        loadTodayStat();
    }

    /**
     * загружаем в память статистику за сегодня
     */
    private void loadTodayStat() {

        LocalDate now = LocalDate.now(ZoneId.of("UTC"));
        LocalDateTime start = LocalDateTime.of(now, LocalTime.MIN);
        LocalDateTime finish = LocalDateTime.of(now, LocalTime.MAX);

        this.countUniqueMap.put(now, new HashSet<>());
        this.countTotalMap.put(now, new AtomicInteger());

        entryRepository.findAllByCreatedOnBetween(start, finish).stream().forEach(siteEntry ->
        {
            this.countUniqueMap.get(now).add(siteEntry.getUserId());
            AtomicInteger counter = countTotalMap.get(now);
            counter.incrementAndGet();
            this.countTotalMap.put(now, counter);
        });

    }


    public TodayStatInfo getTodayStat(SiteEntry entry) {

        LocalDate l = LocalDate.now(ZoneId.of("UTC"));

        // ищем уникальные посещения за сегодня
        Set<Integer> uniqueSet = countUniqueMap.getOrDefault(l, new HashSet<>());

        // их число увеличилось с учетом текущего посещения?
        int unique = uniqueSet.contains(entry.getUserId()) ? uniqueSet.size() : uniqueSet.size() + 1;

        // подсчитываем общее количество
        AtomicInteger counter = countTotalMap.getOrDefault(l, new AtomicInteger());

        return new TodayStatInfo(counter.get(), unique);

    }

    @Async
    public void asyncSaveEntry(SiteEntry siteEntry) {

        entryRepository.save(siteEntry);

        LocalDate created = siteEntry.getCreatedOn().toLocalDate();
        // add user
        synchronized (this) {
            Set<Integer> uniqueSet = countUniqueMap.getOrDefault(created, new HashSet<>());
            uniqueSet.add(siteEntry.getUserId());
            countUniqueMap.put(siteEntry.getCreatedOn().toLocalDate(), uniqueSet);
        }
        // add total
        AtomicInteger counter = countTotalMap.getOrDefault(created, new AtomicInteger());
        counter.incrementAndGet();
        countTotalMap.put(created, counter);

    }
}
