package com.github.bael.challenge.webstat.data;

import com.github.bael.challenge.webstat.domain.SiteEntry;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import reactor.core.publisher.Flux;

import java.time.LocalDateTime;

public interface SiteEntryRepository extends ReactiveMongoRepository<SiteEntry, String> {
    Flux<SiteEntry> findAllByCreatedOnBetween(LocalDateTime before, LocalDateTime after);
}
