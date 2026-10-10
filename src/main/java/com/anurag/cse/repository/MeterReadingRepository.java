package com.anurag.cse.repository;

import com.anurag.cse.model.MeterReading;
import java.util.*;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MeterReadingRepository extends JpaRepository<MeterReading, Long> {
    List<MeterReading> findByUserIdOrderByReadingDateDesc(Long userId);
    Optional<MeterReading> findByIdAndUserId(Long id, Long userId);
    Optional<MeterReading> findTopByUserIdOrderByReadingDateDescIdDesc(Long userId);
}