package com.rsakin.csv_manager.service;

import com.rsakin.csv_manager.model.entity.Record;
import com.rsakin.csv_manager.repository.RecordRepository;
import lombok.RequiredArgsConstructor;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class RecordService {

    private final RecordRepository recordRepository;

    public void saveAll(List<Record> records) {
        recordRepository.saveAll(records);
    }

    public void uploadCSV(MultipartFile file) throws IOException {
        try (BufferedReader fileReader = new BufferedReader(new InputStreamReader(file.getInputStream()));
             CSVParser csvParser = new CSVParser(fileReader, CSVFormat.DEFAULT.withFirstRecordAsHeader().withIgnoreHeaderCase().withTrim())) {

            List<Record> records = new ArrayList<>();
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");

            for (CSVRecord csvRecord : csvParser.getRecords()) {
                Record record = new Record();
                record.setSource(csvRecord.get("source"));
                record.setCodeListCode(csvRecord.get("codeListCode"));
                record.setCode(csvRecord.get("code"));
                record.setDisplayValue(csvRecord.get("displayValue"));
                record.setLongDescription(csvRecord.get("longDescription"));

                String fromDateStr = csvRecord.get("fromDate");
                if (fromDateStr != null && !fromDateStr.isEmpty()) {
                    record.setFromDate(LocalDate.parse(fromDateStr, formatter));
                }

                String toDateStr = csvRecord.get("toDate");
                if (toDateStr != null && !toDateStr.isEmpty()) {
                    record.setToDate(LocalDate.parse(toDateStr, formatter));
                }

                record.setSortingPriority(csvRecord.get("sortingPriority"));
                records.add(record);
            }
            saveAll(records);
        }
    }

    public List<Record> findAll() {
        return recordRepository.findAll();
    }

    public Optional<Record> findByCode(String code) {
        return recordRepository.findById(code);
    }

    public void deleteAll() {
        recordRepository.deleteAll();
    }

}