package com.rsakin.csv_manager.service;

import com.rsakin.csv_manager.model.entity.Record;
import com.rsakin.csv_manager.repository.RecordRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class RecordServiceTest {

    @Mock
    private RecordRepository recordRepository;

    @InjectMocks
    private RecordService recordService;

    @Test
    void testSaveAll() {
        Record record = new Record();
        record.setCode("123");
        recordService.saveAll(List.of(record));

        ArgumentCaptor<List<Record>> captor = ArgumentCaptor.forClass(List.class);
        verify(recordRepository).saveAll(captor.capture());

        List<Record> capturedRecords = captor.getValue();
        assertEquals(1, capturedRecords.size());
        assertEquals("123", capturedRecords.get(0).getCode());
    }

    @Test
    void testFindAll() {
        Record record = new Record();
        record.setCode("123");
        when(recordRepository.findAll()).thenReturn(List.of(record));

        List<Record> records = recordService.findAll();
        assertEquals(1, records.size());
        assertEquals("123", records.get(0).getCode());
    }

    @Test
    void testFindByCode() {
        Record record = new Record();
        record.setCode("123");
        when(recordRepository.findById("123")).thenReturn(Optional.of(record));

        Optional<Record> result = recordService.findByCode("123");
        assertTrue(result.isPresent());
        assertEquals("123", result.get().getCode());
    }

    @Test
    void testDeleteAll() {
        recordService.deleteAll();
        verify(recordRepository).deleteAll();
    }

    @Test
    void testUploadCSV() throws IOException {
        String csvData = "source,codeListCode,code,displayValue,longDescription,fromDate,toDate,sortingPriority\n" +
                "ZIB,ZIB001,123,Test Display,Test Description,01-01-2019,,1\n";
        MultipartFile file = mock(MultipartFile.class);
        when(file.getInputStream()).thenReturn(new ByteArrayInputStream(csvData.getBytes()));

        recordService.uploadCSV(file);

        ArgumentCaptor<List<Record>> captor = ArgumentCaptor.forClass(List.class);
        verify(recordRepository).saveAll(captor.capture());

        List<Record> capturedRecords = captor.getValue();
        assertEquals(1, capturedRecords.size());
        Record record = capturedRecords.get(0);
        assertEquals("123", record.getCode());
        assertEquals("Test Display", record.getDisplayValue());
        assertEquals("Test Description", record.getLongDescription());
        assertEquals(LocalDate.of(2019, 1, 1), record.getFromDate());
        assertNull(record.getToDate());
        assertEquals("1", record.getSortingPriority());
    }

}