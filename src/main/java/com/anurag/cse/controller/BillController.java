package com.anurag.cse.controller;

import com.anurag.cse.model.Bill;
import com.anurag.cse.repository.BillRepository;
import com.anurag.cse.service.TariffService;
import jakarta.validation.Valid;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/bills")
public class BillController {
    private final BillRepository repo;

    public BillController(BillRepository repo) { this.repo = repo; }

    /** Optional filters: month (yyyy-MM), status, from, to (yyyy-MM-dd). */
    @GetMapping
    public List<Bill> list(@RequestAttribute("userId") Long uid,
                           @RequestParam(required = false) String month,
                           @RequestParam(required = false) String status,
                           @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate from,
                           @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate to) {
        return repo.findByUserIdOrderByBillDateDesc(uid).stream()
                .filter(b -> month == null || month.isBlank() || b.getBillDate().toString().startsWith(month))
                .filter(b -> status == null || status.isBlank() || b.getStatus().equalsIgnoreCase(status))
                .filter(b -> from == null || !b.getBillDate().isBefore(from))
                .filter(b -> to == null || !b.getBillDate().isAfter(to))
                .collect(Collectors.toList());
    }

    @GetMapping("/{id}")
    public Bill get(@RequestAttribute("userId") Long uid, @PathVariable Long id) {
        return find(uid, id);
    }

    @PostMapping
    public ResponseEntity<Bill> create(@RequestAttribute("userId") Long uid, @Valid @RequestBody Bill b) {
        checkReadings(b);
        b.setId(null);
        b.setUserId(uid);
        b.setUnitsConsumed(TariffService.round2(b.getCurrentReading() - b.getPreviousReading()));
        return ResponseEntity.status(HttpStatus.CREATED).body(repo.save(b));
    }

    @PutMapping("/{id}")
    public Bill update(@RequestAttribute("userId") Long uid, @PathVariable Long id, @Valid @RequestBody Bill in) {
        Bill b = find(uid, id);
        checkReadings(in);
        b.setPreviousReading(in.getPreviousReading());
        b.setCurrentReading(in.getCurrentReading());
        b.setUnitsConsumed(TariffService.round2(in.getCurrentReading() - in.getPreviousReading()));
        b.setBillAmount(in.getBillAmount());
        b.setBillDate(in.getBillDate());
        b.setStatus(in.getStatus());
        return repo.save(b);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@RequestAttribute("userId") Long uid, @PathVariable Long id) {
        repo.delete(find(uid, id));
        return ResponseEntity.noContent().build();
    }

    private Bill find(Long uid, Long id) {
        return repo.findByIdAndUserId(id, uid).orElseThrow(() -> new NoSuchElementException("Bill not found"));
    }

    private void checkReadings(Bill b) {
        if (b.getCurrentReading() < b.getPreviousReading())
            throw new IllegalArgumentException("Current reading cannot be lower than previous reading");
    }
}