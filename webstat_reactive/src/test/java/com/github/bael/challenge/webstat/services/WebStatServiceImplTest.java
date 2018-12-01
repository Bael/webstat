package com.github.bael.challenge.webstat.services;

import com.github.bael.challenge.webstat.data.SiteEntryRepository;
import com.github.bael.challenge.webstat.domain.SiteEntry;
import com.github.bael.challenge.webstat.dto.SiteEntryStatsDTO;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest
public class WebStatServiceImplTest {

    @Autowired
    private SiteEntryRepository repository;
    @Autowired
    private WebStatService webStatService;

    private List<SiteEntry> entries = new ArrayList<>();


    @Before
    public void setUp() throws Exception {

        LocalDateTime l = LocalDateTime.now(ZoneId.of("UTC"));
        entries.clear();
        Arrays.asList(1, 2).forEach(
                userId ->
                        Arrays.asList(1, 2, 3, 4, 5, 6, 7, 8, 9)
                                .forEach(pageId -> entries.add(new SiteEntry(userId, pageId, l)))
        );
        entries.add(new SiteEntry(1, 10, l));
        entries.add(new SiteEntry(2, 10, l.minusDays(1)));

        repository.deleteAll()
                .thenMany(Flux.fromIterable(entries))
                .flatMap(repository::save)
                .then()
                .block();
    }

    @Test
    public void save() {

        StepVerifier.create(webStatService.saveSiteEntry(3, 1))
                .expectNextMatches(siteEntrySaveDTO ->
                        siteEntrySaveDTO.totalCount == 1 && siteEntrySaveDTO.uniqueCount == 1
                )
                .verifyComplete();

        StepVerifier.create(webStatService.saveSiteEntry(1, 1))
                .expectNextMatches(siteEntrySaveDTO -> siteEntrySaveDTO.totalCount == 2
                        && siteEntrySaveDTO.uniqueCount == 2
                )
                .verifyComplete();

        StepVerifier.create(webStatService.saveSiteEntry(1, 1))
                .expectNextMatches(siteEntrySaveDTO ->
                        siteEntrySaveDTO.totalCount == 3 && siteEntrySaveDTO.uniqueCount == 2
                )
                .verifyComplete();
    }


    @Test
    public void getStatFromPeriod() {

        LocalDate now = LocalDate.now(ZoneId.of("UTC"));
        LocalDateTime start = LocalDateTime.of(now, LocalTime.MIN);
        LocalDateTime finish = LocalDateTime.of(now, LocalTime.MAX);

        StepVerifier.create(webStatService.getStatFromPeriod(start, finish)
                .map(SiteEntryStatsDTO::fillFrom))
                .expectNextMatches(siteEntryStatsDTO ->
                        siteEntryStatsDTO.totalCount == 19 &&
                                siteEntryStatsDTO.uniqueCount == 2
                                && siteEntryStatsDTO.persistentCount == 1)
                .verifyComplete();
    }

}