package com.rsakin.csv_manager.model.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

import java.time.LocalDate;

@Entity
@Table(name = "records")
@Data
public class Record {

    @Id
    private String code;
    private String source;
    private String codeListCode;
    private String displayValue;
    private String longDescription;
    private LocalDate fromDate;
    private LocalDate toDate;
    private String sortingPriority;

}
