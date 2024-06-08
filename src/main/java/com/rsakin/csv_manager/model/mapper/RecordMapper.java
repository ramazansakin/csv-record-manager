package com.rsakin.csv_manager.model.mapper;

import com.rsakin.csv_manager.model.dto.RecordDTO;
import com.rsakin.csv_manager.model.entity.Record;
import org.mapstruct.Mapper;

@Mapper
public interface RecordMapper {

    RecordDTO toDTO(Record entity);

    Record toEntity(RecordDTO dto);
}