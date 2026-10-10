package com.anurag.cse.controller;

import com.anurag.cse.model.Bill;
import com.anurag.cse.model.MeterReading;
import com.anurag.cse.repository.BillRepository;
import com.anurag.cse.repository.MeterReadingRepository;
import com.anurag.cse.service.TariffService;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/dashboard")
public class DashboardController {
    private final MeterReadingRepository readings;
    private final BillRepository bills;
    private final TariffService tariff;

    public DashboardController(MeterReadingRepository readings, BillRepository bills, TariffService tariff) {
        this.readings = readings; this.bills = bills; this.tariff = tariff;
    }

    @GetMapping
    public Map<String, Object> dashboard(@RequestAttribute("userId") Long uid) {
        List<MeterReading> rs = readings.findByUserIdOrderByReadingDateDesc(uid);
        List<Bill> bs = bills.findByUserIdOrderByBillDateDesc(uid);

        // units per month: meter readings first, bills fill any month without readings
        Map<YearMonth, Double> units = rs.stream().collect(Collectors.groupingBy(
                r -> YearMonth.from(r.getReadingDate()), Collectors.summingDouble(MeterReading::getUnitsConsumed)));
        bs.stream().collect(Collectors.groupingBy(
                b -> YearMonth.from(b.getBillDate()), Collectors.summingDouble(Bill::getUnitsConsumed)))
          .forEach(units::putIfAbsent);

        Map<YearMonth, Double> billAmounts = bs.stream().collect(Collectors.groupingBy(
                b -> YearMonth.from(b.getBillDate()), Collectors.summingDouble(Bill::getBillAmount)));

        YearMonth now = YearMonth.now(), prev = now.minusMonths(1);
        double curUnits = units.getOrDefault(now, 0.0);
        double prevUnits = units.getOrDefault(prev, 0.0);
        double prevBill = billAmounts.containsKey(prev) ? billAmounts.get(prev) : tariff.calculate(prevUnits);
        double avg = units.values().stream().mapToDouble(Double::doubleValue).average().orElse(0.0);
        double change = prevUnits > 0 ? (curUnits - prevUnits) / prevUnits * 100.0 : 0.0;

        List<String> labels = new ArrayList<>();
        List<Double> usageSeries = new ArrayList<>(), billSeries = new ArrayList<>();
        DateTimeFormatter fmt = DateTimeFormatter.ofPattern("MMM yyyy", Locale.ENGLISH);
        for (int i = 5; i >= 0; i--) {
            YearMonth m = now.minusMonths(i);
            labels.add(m.format(fmt));
            usageSeries.add(TariffService.round2(units.getOrDefault(m, 0.0)));
            billSeries.add(TariffService.round2(billAmounts.getOrDefault(m, 0.0)));
        }

        Map<String, Object> out = new LinkedHashMap<>();
        out.put("currentMonthUnits", TariffService.round2(curUnits));
        out.put("estimatedBill", tariff.calculate(curUnits));
        out.put("previousMonthUnits", TariffService.round2(prevUnits));
        out.put("previousMonthBill", TariffService.round2(prevBill));
        out.put("averageMonthlyUsage", TariffService.round2(avg));
        out.put("changePercent", TariffService.round2(change));
        out.put("trend", curUnits > prevUnits ? "UP" : curUnits < prevUnits ? "DOWN" : "SAME");
        out.put("labels", labels);
        out.put("usageSeries", usageSeries);
        out.put("billSeries", billSeries);
        return out;
    }
}