package com.anurag.cse.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.time.LocalDate;

@Entity
public class Bill {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Long userId;
    @NotNull(message = "Previous reading is required")
    @PositiveOrZero(message = "Previous reading must be zero or positive")
    private Double previousReading;
    @NotNull(message = "Current reading is required")
    @Positive(message = "Current reading must be positive")
    private Double currentReading;
    private Double unitsConsumed;
    @NotNull(message = "Bill amount is required")
    @Positive(message = "Bill amount must be positive")
    private Double billAmount;
    @NotNull(message = "Bill date is required")
    private LocalDate billDate;
    @NotBlank(message = "Bill status is required")
    @Pattern(regexp = "PAID|UNPAID|OVERDUE", message = "Status must be PAID, UNPAID or OVERDUE")
    private String status;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public Long getUserId() { return userId; }
    public void setUserId(Long userId) { this.userId = userId; }
    public Double getPreviousReading() { return previousReading; }
    public void setPreviousReading(Double v) { this.previousReading = v; }
    public Double getCurrentReading() { return currentReading; }
    public void setCurrentReading(Double v) { this.currentReading = v; }
    public Double getUnitsConsumed() { return unitsConsumed; }
    public void setUnitsConsumed(Double v) { this.unitsConsumed = v; }
    public Double getBillAmount() { return billAmount; }
    public void setBillAmount(Double v) { this.billAmount = v; }
    public LocalDate getBillDate() { return billDate; }
    public void setBillDate(LocalDate d) { this.billDate = d; }
    public String getStatus() { return status; }
    public void setStatus(String s) { this.status = s; }
}