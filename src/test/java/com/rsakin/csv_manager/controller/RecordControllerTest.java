package com.rsakin.csv_manager.controller;

import com.rsakin.csv_manager.model.entity.Record;
import com.rsakin.csv_manager.service.RecordService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.List;
import java.util.Optional;

import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@ExtendWith(MockitoExtension.class)
class RecordControllerTest {

    private MockMvc mockMvc;

    @Mock
    private RecordService recordService;

    @InjectMocks
    private RecordController recordController;

    @BeforeEach
    void setUp() {
        this.mockMvc = MockMvcBuilders.standaloneSetup(recordController).build();
    }

    @Test
    void testUploadFile() throws Exception {
        MockMultipartFile file = new MockMultipartFile(
                "file",
                "test.csv",
                MediaType.TEXT_PLAIN_VALUE,
                "source,codeListCode,code,displayValue,longDescription,fromDate,toDate,sortingPriority\nZIB,ZIB001,123,Test Display,Test Description,01-01-2019,,1\n".getBytes()
        );

        mockMvc.perform(multipart("/api/v1/records").file(file))
                .andExpect(status().isOk())
                .andExpect(content().string("File uploaded successfully"));
    }

    @Test
    void testGetAllRecords() throws Exception {
        Record record = new Record();
        record.setCode("123");
        when(recordService.findAll()).thenReturn(List.of(record));

        mockMvc.perform(get("/api/v1/records"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].code", is("123")));
    }

    @Test
    void testGetRecordByCode() throws Exception {
        Record record = new Record();
        record.setCode("123456");
        when(recordService.findByCode("123456")).thenReturn(Optional.of(record));

        mockMvc.perform(get("/api/v1/records/123456"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code", is("123456")));
    }

    @Test
    void testDeleteAllRecords() throws Exception {
        mockMvc.perform(delete("/api/v1/records"))
                .andExpect(status().isNoContent());
    }

}