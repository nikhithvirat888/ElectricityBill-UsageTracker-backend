package com.anurag.cse.repository;

import com.anurag.cse.model.Appliance;
import java.util.*;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ApplianceRepository extends JpaRepository<Appliance, Long> {
    List<Appliance> findByUserId(Long userId);
    Optional<Appliance> findByIdAndUserId(Long id, Long userId);
}