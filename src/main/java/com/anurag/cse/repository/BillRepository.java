package com.anurag.cse.repository;

import com.anurag.cse.model.Bill;
import java.util.*;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BillRepository extends JpaRepository<Bill, Long> {
    List<Bill> findByUserIdOrderByBillDateDesc(Long userId);
    Optional<Bill> findByIdAndUserId(Long id, Long userId);
}