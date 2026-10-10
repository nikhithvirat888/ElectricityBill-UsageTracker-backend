package com.anurag.cse.controller;

import com.anurag.cse.model.Appliance;
import com.anurag.cse.repository.ApplianceRepository;
import com.anurag.cse.service.TariffService;
import jakarta.validation.Valid;
import java.util.*;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/appliances")
public class ApplianceController {
    private final ApplianceRepository repo;
    private final TariffService tariff;

    public ApplianceController(ApplianceRepository repo, TariffService tariff) { this.repo = repo; this.tariff = tariff; }

    @GetMapping
    public List<Appliance> all(@RequestAttribute("userId") Long uid) { return repo.findByUserId(uid); }

    @GetMapping("/summary")
    public Map<String, Object> summary(@RequestAttribute("userId") Long uid) {
        List<Appliance> list = repo.findByUserId(uid);
        double daily = list.stream().mapToDouble(Appliance::getDailyUnits).sum();
        double monthly = list.stream().mapToDouble(Appliance::getMonthlyUnits).sum();
        double cost = list.stream().mapToDouble(Appliance::getEstimatedCost).sum();
        Map<String, Object> m = new LinkedHashMap<>();
        m.put("totalDailyUnits", TariffService.round2(daily));
        m.put("totalMonthlyUnits", TariffService.round2(monthly));
        m.put("totalEstimatedCost", TariffService.round2(cost));
        m.put("ratePerUnit", tariff.getAverageRate());
        return m;
    }

    @PostMapping
    public ResponseEntity<Appliance> add(@RequestAttribute("userId") Long uid, @Valid @RequestBody Appliance a) {
        a.setId(null);
        a.setUserId(uid);
        double monthly = a.getWattage() * a.getQuantity() * a.getHoursPerDay() * a.getDaysPerMonth() / 1000.0;
        a.setMonthlyUnits(TariffService.round2(monthly));
        a.setEstimatedCost(TariffService.round2(monthly * tariff.getAverageRate()));
        return ResponseEntity.status(HttpStatus.CREATED).body(repo.save(a));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@RequestAttribute("userId") Long uid, @PathVariable Long id) {
        Appliance a = repo.findByIdAndUserId(id, uid).orElseThrow(() -> new NoSuchElementException("Appliance not found"));
        repo.delete(a);
        return ResponseEntity.noContent().build();
    }
}