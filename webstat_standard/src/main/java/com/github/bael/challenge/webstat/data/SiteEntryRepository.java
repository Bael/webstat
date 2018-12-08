package com.github.bael.challenge.webstat.data;

import com.github.bael.challenge.webstat.domain.SiteEntry;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface SiteEntryRepository extends CrudRepository<SiteEntry, Long> {

    List<SiteEntry> findAllByCreatedOnBetween(LocalDateTime before, LocalDateTime after);

    @Query("select count(entry) from SiteEntry as entry where createdOn between :start and :finish")
    Optional<Long> findAllByPeriodCount(@Param("start") LocalDateTime start, @Param("finish") LocalDateTime finish);


    @Query("select count(distinct entry.userId) from SiteEntry as entry where entry.createdOn between :start and :finish " +
            "group by entry.userId " +
            "having count (distinct entry.pageId) > :pagelimit")
    Optional<Long> findPersistentByPeriodCount(@Param("start") LocalDateTime start, @Param("finish") LocalDateTime finish,
                                               @Param("pagelimit") long pagelimit);

    @Query("select count(distinct entry.userId) from SiteEntry as entry where createdOn between :start and :finish ")
    Optional<Long> findUniqueByPeriodCount(@Param("start") LocalDateTime start, @Param("finish") LocalDateTime finish);

}
