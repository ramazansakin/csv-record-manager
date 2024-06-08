package com.rsakin.csv_manager.model.dto;

import java.time.LocalDate;

// Immutable DTO class for Record responses
public record RecordDTO(
        String code,
        String source,
        String codeListCode,
        String displayValue,
        String longDescription,
        LocalDate fromDate,
        LocalDate toDate) {
}
