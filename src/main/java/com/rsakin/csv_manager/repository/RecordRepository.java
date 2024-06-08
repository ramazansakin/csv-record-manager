package com.rsakin.csv_manager.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.rsakin.csv_manager.model.entity.Record;

public interface RecordRepository extends JpaRepository<Record, String> {
}
