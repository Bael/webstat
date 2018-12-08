package com.github.bael.challenge.webstat.rest.controllers;

import com.github.bael.challenge.webstat.domain.TodayStatInfo;
import com.github.bael.challenge.webstat.rest.dto.SiteEntrySaveDTO;
import com.github.bael.challenge.webstat.rest.dto.SiteEntryStatsDTO;
import com.github.bael.challenge.webstat.services.WebStatService;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

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
    public SiteEntrySaveDTO saveEntry(@RequestBody Map<String, Integer> siteEntry) {

        int userId = siteEntry.get("userId");
        int pageId = siteEntry.get("pageId");
        TodayStatInfo info = webStatService.saveSiteEntry(userId, pageId);
        return new SiteEntrySaveDTO(info.getTotalCount(), info.getUniqueCount());
    }

    @GetMapping(value = "/", params = {"start", "finish"})
    public SiteEntryStatsDTO getStat(
            @RequestParam("start") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime start,
            @RequestParam("finish") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime finish

    ) {
        return SiteEntryStatsDTO.fillFrom(webStatService.getStatFromPeriod(start, finish));
    }


}
