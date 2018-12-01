package com.github.bael.challenge.webstat.services;

import com.github.bael.challenge.webstat.data.SiteEntryRepository;
import com.github.bael.challenge.webstat.domain.SiteEntry;
import com.github.bael.challenge.webstat.domain.SiteEntryStats;
import com.github.bael.challenge.webstat.dto.SiteEntrySaveDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.util.HashSet;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

@Service
public class WebStatServiceImpl implements WebStatService {

    private final Logger logger = LoggerFactory.getLogger(WebStatService.class);
    private final SiteEntryRepository entryRepository;
    /**
     * Счетчик уникальных посетителей за сегодня
     */
    private Map<LocalDate, Set<Integer>> countUniqMap = new ConcurrentHashMap<>();
    /**
     * Счетчик общего количества посетителей за сегодня
     */
    private Map<LocalDate, AtomicInteger> countTotalMap = new ConcurrentHashMap<>();

    @Autowired
    public WebStatServiceImpl(SiteEntryRepository entryRepository) {
        this.entryRepository = entryRepository;

        init();
    }


    // загружаем из базы данные за сегодня
    private void init() {

        LocalDate now = LocalDate.now(ZoneId.of("UTC"));
        LocalDateTime start = LocalDateTime.of(now, LocalTime.MIN);
        LocalDateTime finish = LocalDateTime.of(now, LocalTime.MAX);

        getStatFromPeriod(start, finish)
                .flatMap(siteEntryStats -> {
                    // кладем в карту уникальных пользователей за сегодня
                    this.countUniqMap.put(now, new HashSet<>(siteEntryStats.persistentUserMap.keySet()));

                    // кладем в карту общее количество пользователей за сегодня
                    this.countTotalMap.put(now, new AtomicInteger(siteEntryStats.totalCount.get()));

                    return Mono.empty();
                })
                .subscribe(o -> logger.info("Loaded initial data"));

    }


    @Override
    public Mono<SiteEntryStats> getStatFromPeriod(LocalDateTime start, LocalDateTime finish) {
        Objects.requireNonNull(start);
        Objects.requireNonNull(finish);

        return entryRepository.findAllByCreatedOnBetween(start, finish)
                .reduceWith(SiteEntryStats::new,
                        (siteEntryStats, siteEntry) -> {
                            siteEntryStats.totalCount.getAndIncrement();
                            Set<Integer> pageIds = siteEntryStats.persistentUserMap
                                    .getOrDefault(siteEntry.getUserId(), new HashSet<>());
                            pageIds.add(siteEntry.getPageId());
                            siteEntryStats.persistentUserMap.put(siteEntry.getUserId(), pageIds);
                            return siteEntryStats;
                        });
    }

    @Override
    public Mono<SiteEntrySaveDTO> saveSiteEntry(int userId, int pageId) {
        SiteEntry s = new SiteEntry(userId, pageId, LocalDateTime.now(ZoneId.of("UTC")));
        entryRepository.save(s).subscribe(siteEntry ->
                {
                    LocalDate created = siteEntry.getCreatedOn().toLocalDate();
                    // add user
                    Set<Integer> uniqueSet = countUniqMap.getOrDefault(created, new HashSet<>());
                    uniqueSet.add(userId);
                    countUniqMap.put(siteEntry.getCreatedOn().toLocalDate(), uniqueSet);

                    // add total
                    AtomicInteger counter = countTotalMap.getOrDefault(created, new AtomicInteger());
                    counter.incrementAndGet();
                    countTotalMap.put(created, counter);
//                            return Mono.just(siteEntry);
                }

        );
//        .subscribe(o -> logger.debug("Saved entry"));

        // не ждем сохранения
        LocalDate l = LocalDate.now(ZoneId.of("UTC"));

        // add user
        Set<Integer> uniqueSet = countUniqMap.getOrDefault(l, new HashSet<>());
        int unique = uniqueSet.contains(userId) ? uniqueSet.size() : uniqueSet.size() + 1;
        // add total
        AtomicInteger counter = countTotalMap.getOrDefault(l, new AtomicInteger());

        return Mono.just(new SiteEntrySaveDTO(counter.get() + 1, unique));

    }
}
