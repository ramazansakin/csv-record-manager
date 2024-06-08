package com.rsakin.csv_manager.controller;


import com.rsakin.csv_manager.model.dto.RecordDTO;
import com.rsakin.csv_manager.model.entity.Record;
import com.rsakin.csv_manager.model.mapper.RecordMapper;
import com.rsakin.csv_manager.service.RecordService;
import lombok.RequiredArgsConstructor;
import org.mapstruct.factory.Mappers;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.DateTimeException;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

// URL versioning to set version on path and increase on any breaking-change to support all the clients
// without breaking their apps, it can also be defined on header
@RestController
@RequestMapping("/api/v1/records")
@RequiredArgsConstructor
public class RecordController {

    private final RecordService recordService;

    private static final RecordMapper RECORD_MAPPER = Mappers.getMapper(RecordMapper.class);

    @PostMapping
    public ResponseEntity<String> uploadFile(@RequestParam("file") MultipartFile file) {
        if (file.isEmpty()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("File is empty");
        }

        try {
            recordService.uploadCSV(file);
            return ResponseEntity.status(HttpStatus.OK).body("File uploaded successfully");
        } catch (DateTimeException dateTimeException) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid date format on records!");
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error while parsing CSV file");
        }
    }

    @GetMapping
    public ResponseEntity<List<RecordDTO>> getAllRecords() {
        List<Record> allRecords = recordService.findAll();
        List<RecordDTO> recordDTOS = allRecords.stream().map(RECORD_MAPPER::toDTO).collect(Collectors.toList());
        return ResponseEntity.ok().body(recordDTOS);
    }

    @GetMapping("/{code}")
    public ResponseEntity<RecordDTO> getRecordByCode(@PathVariable String code) {
        Optional<Record> record = recordService.findByCode(code);
        return record.map(rec -> ResponseEntity.ok(RECORD_MAPPER.toDTO(rec)))
                .orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND).build());
    }

    @DeleteMapping
    public ResponseEntity<Void> deleteAllRecords() {
        recordService.deleteAll();
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}
