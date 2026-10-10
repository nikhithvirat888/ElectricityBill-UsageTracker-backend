package com.anurag.cse.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.time.LocalDate;

@Entity
public class MeterReading {
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
    @NotNull(message = "Reading date is required")
    @PastOrPresent(message = "Reading date cannot be in the future")
    private LocalDate readingDate;

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
    public LocalDate getReadingDate() { return readingDate; }
    public void setReadingDate(LocalDate d) { this.readingDate = d; }
}