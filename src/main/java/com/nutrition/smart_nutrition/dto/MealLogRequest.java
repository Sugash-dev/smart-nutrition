package com.nutrition.smart_nutrition.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Data;

import java.time.LocalDate;

@Data
public class MealLogRequest {

    @NotNull
    private Long userId;

    @NotNull
    private Long foodId;

    @NotNull
    private LocalDate logDate;

    @Positive
    private double servings;
}