package com.anurag.cse.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;

@Entity
public class Appliance {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Long userId;
    @NotBlank(message = "Appliance name is required")
    private String applianceName;
    @NotNull(message = "Wattage is required")
    @Positive(message = "Wattage must be positive")
    private Double wattage;
    @NotNull(message = "Quantity is required")
    @Positive(message = "Quantity must be positive")
    private Integer quantity;
    @NotNull(message = "Hours per day is required")
    @Positive(message = "Hours per day must be positive")
    @Max(value = 24, message = "Hours per day cannot exceed 24")
    private Double hoursPerDay;
    @NotNull(message = "Number of days is required")
    @Positive(message = "Number of days must be positive")
    @Max(value = 31, message = "Days per month cannot exceed 31")
    private Integer daysPerMonth;
    private Double monthlyUnits;
    private Double estimatedCost;

    /** Daily consumption in kWh - computed, not stored. */
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    public Double getDailyUnits() {
        if (wattage == null || quantity == null || hoursPerDay == null) return 0.0;
        return Math.round(wattage * quantity * hoursPerDay / 1000.0 * 100.0) / 100.0;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public Long getUserId() { return userId; }
    public void setUserId(Long userId) { this.userId = userId; }
    public String getApplianceName() { return applianceName; }
    public void setApplianceName(String n) { this.applianceName = n; }
    public Double getWattage() { return wattage; }
    public void setWattage(Double w) { this.wattage = w; }
    public Integer getQuantity() { return quantity; }
    public void setQuantity(Integer q) { this.quantity = q; }
    public Double getHoursPerDay() { return hoursPerDay; }
    public void setHoursPerDay(Double h) { this.hoursPerDay = h; }
    public Integer getDaysPerMonth() { return daysPerMonth; }
    public void setDaysPerMonth(Integer d) { this.daysPerMonth = d; }
    public Double getMonthlyUnits() { return monthlyUnits; }
    public void setMonthlyUnits(Double m) { this.monthlyUnits = m; }
    public Double getEstimatedCost() { return estimatedCost; }
    public void setEstimatedCost(Double c) { this.estimatedCost = c; }
}