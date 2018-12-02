package com.github.bael.challenge.webstat.services;

import com.github.bael.challenge.webstat.data.SiteEntryRepository;
import com.github.bael.challenge.webstat.domain.SiteEntry;
import com.github.bael.challenge.webstat.domain.SiteEntryStats;
import com.github.bael.challenge.webstat.dto.SiteEntrySaveDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
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
    @Value("${persistentLimit:10}")
    private int limit;
    /**
     * Счетчик уникальных посетителей за сегодня
     */
    private Map<LocalDate, Set<Integer>> countUniqueMap = new ConcurrentHashMap<>();
    /**
     * Счетчик общего количества посетителей за сегодня
     */
    private Map<LocalDate, AtomicInteger> countTotalMap = new ConcurrentHashMap<>();

    @Autowired
    public WebStatServiceImpl(SiteEntryRepository entryRepository) {
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

        getStatFromPeriod(start, finish)
                .flatMap(siteEntryStats -> {
                    // кладем в карту уникальных пользователей за сегодня
                    this.countUniqueMap.put(now, siteEntryStats.getUserIdsSet());
                    // кладем в карту общего количества пользователей за сегодня
                    this.countTotalMap.put(now, new AtomicInteger(siteEntryStats.getTotalCount()));
                    return Mono.empty();
                })
                .subscribe(o -> logger.info("Loaded initial data"));
    }


    @Override
    public Mono<SiteEntryStats> getStatFromPeriod(LocalDateTime start, LocalDateTime finish) {
        Objects.requireNonNull(start);
        Objects.requireNonNull(finish);
        return entryRepository.findAllByCreatedOnBetween(start, finish)
                .reduceWith(() -> new SiteEntryStats(limit), SiteEntryStats::addSiteEntry);
    }

    @Override
    /**
     * Запускаем метод сохранения входа пользователя, по его исполнению увеличиваем статистику за сегодня.
     * Не дожидаясь сохранения возвращаем текущую статистику увеличенную на 1
     * (оптимистично считаем что сохранение пойдет без проблем)
     * @userId код пользователя
     * @pageId код страницы
     */
    public Mono<SiteEntrySaveDTO> saveSiteEntry(int userId, int pageId) {
        SiteEntry s = new SiteEntry(userId, pageId, LocalDateTime.now(ZoneId.of("UTC")));
        entryRepository.save(s).subscribe(siteEntry ->
                {
                    LocalDate created = siteEntry.getCreatedOn().toLocalDate();
                    // add user
                    Set<Integer> uniqueSet = countUniqueMap.getOrDefault(created, new HashSet<>());
                    uniqueSet.add(userId);
                    countUniqueMap.put(siteEntry.getCreatedOn().toLocalDate(), uniqueSet);

                    // add total
                    AtomicInteger counter = countTotalMap.getOrDefault(created, new AtomicInteger());
                    counter.incrementAndGet();
                    countTotalMap.put(created, counter);
                }

        );

        // не ждем сохранения и сразу возвращаем даные за сутки
        LocalDate l = LocalDate.now(ZoneId.of("UTC"));

        // ищем уникальные посещения за сегодня
        Set<Integer> uniqueSet = countUniqueMap.getOrDefault(l, new HashSet<>());

        // их число увеличилось с учетом текущего посещения?
        int unique = uniqueSet.contains(userId) ? uniqueSet.size() : uniqueSet.size() + 1;

        // подсчитываем общее количество
        AtomicInteger counter = countTotalMap.getOrDefault(l, new AtomicInteger());

        return Mono.just(new SiteEntrySaveDTO(counter.get() + 1, unique));

    }
}
