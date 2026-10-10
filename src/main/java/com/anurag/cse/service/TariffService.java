package com.anurag.cse.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class TariffService {

    // Edit these slabs to match your electricity board's tariff
    private static final double[] SLAB_SIZE = {100, 100, 100};          // first 100, next 100, next 100 units
    private static final double[] SLAB_RATE = {3.0, 4.5, 6.0, 7.5};     // last value = above 300 units
    private static final double FIXED_CHARGE = 50.0;

    @Value("${tariff.average-rate:6.0}")
    private double averageRate;

    public double getAverageRate() { return averageRate; }

    public double calculate(double units) {
        if (units <= 0) return 0.0;
        double remaining = units, bill = 0;
        for (int i = 0; i < SLAB_SIZE.length && remaining > 0; i++) {
            double used = Math.min(remaining, SLAB_SIZE[i]);
            bill += used * SLAB_RATE[i];
            remaining -= used;
        }
        if (remaining > 0) bill += remaining * SLAB_RATE[SLAB_RATE.length - 1];
        return round2(bill + FIXED_CHARGE);
    }

    public static double round2(double v) { return Math.round(v * 100.0) / 100.0; }
}