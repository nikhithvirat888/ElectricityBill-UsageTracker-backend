package com.anurag.cse.controller;

import com.anurag.cse.model.MeterReading;
import com.anurag.cse.repository.MeterReadingRepository;
import com.anurag.cse.service.TariffService;
import jakarta.validation.Valid;
import java.util.*;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/meter")
public class MeterController {
    private final MeterReadingRepository repo;

    public MeterController(MeterReadingRepository repo) { this.repo = repo; }

    @GetMapping
    public List<MeterReading> all(@RequestAttribute("userId") Long uid) {
        return repo.findByUserIdOrderByReadingDateDesc(uid);
    }

    /** Previous reading to pre-fill the form = last saved current reading. */
    @GetMapping("/latest")
    public Map<String, Object> latest(@RequestAttribute("userId") Long uid) {
        double prev = repo.findTopByUserIdOrderByReadingDateDescIdDesc(uid)
                .map(MeterReading::getCurrentReading).orElse(0.0);
        return Map.of("previousReading", prev);
    }

    @PostMapping
    public ResponseEntity<MeterReading> save(@RequestAttribute("userId") Long uid, @Valid @RequestBody MeterReading r) {
        // previous reading is always taken from the last saved reading (if any)
        repo.findTopByUserIdOrderByReadingDateDescIdDesc(uid)
            .ifPresent(last -> r.setPreviousReading(last.getCurrentReading()));
        if (r.getCurrentReading() < r.getPreviousReading())
            throw new IllegalArgumentException("Current reading cannot be lower than previous reading");
        r.setId(null);
        r.setUserId(uid);
        r.setUnitsConsumed(TariffService.round2(r.getCurrentReading() - r.getPreviousReading()));
        return ResponseEntity.status(HttpStatus.CREATED).body(repo.save(r));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@RequestAttribute("userId") Long uid, @PathVariable Long id) {
        MeterReading r = repo.findByIdAndUserId(id, uid).orElseThrow(() -> new NoSuchElementException("Reading not found"));
        repo.delete(r);
        return ResponseEntity.noContent().build();
    }
}