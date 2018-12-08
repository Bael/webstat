package com.github.bael.challenge.webstat.domain;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * Модель для хранения статистики за день
 */
@Data
@AllArgsConstructor
public class TodayStatInfo {
    private long totalCount;
    private long uniqueCount;
}
