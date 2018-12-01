package com.github.bael.challenge.webstat.controllers;

import com.github.bael.challenge.webstat.dto.SiteEntrySaveDTO;
import com.github.bael.challenge.webstat.dto.SiteEntryStatsDTO;
import com.github.bael.challenge.webstat.services.WebStatService;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;
import java.util.Map;

@RestController
@RequestMapping("/entries")
public class SiteEntryController {


    private final WebStatService webStatService;

    public SiteEntryController(WebStatService webStatService) {
        this.webStatService = webStatService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Mono<SiteEntrySaveDTO> saveEntry(@RequestBody Map<String, Integer> siteEntry) {
        System.out.println();
        int userId = siteEntry.get("userId");
        int pageId = siteEntry.get("pageId");
        return webStatService.saveSiteEntry(userId, pageId);
    }

    @GetMapping(value = "/", params = {"start", "finish"})
    public Mono<SiteEntryStatsDTO> getStat(
            @RequestParam("start") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime start,
            @RequestParam("finish") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime finish

    ) {
        return webStatService.getStatFromPeriod(start, finish)
                .doOnError(throwable -> System.out.println(throwable.getStackTrace()))
                .map(SiteEntryStatsDTO::fillFrom);
    }


}
