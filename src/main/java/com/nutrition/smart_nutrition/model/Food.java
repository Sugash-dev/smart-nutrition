package com.nutrition.smart_nutrition.model;

import com.nutrition.smart_nutrition.model.enums.DietType;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Food {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    private String name;

    @PositiveOrZero
    private double calories;

    @PositiveOrZero
    private double proteinG;

    @PositiveOrZero
    private double carbsG;

    @PositiveOrZero
    private double fatG;

    @Enumerated(EnumType.STRING)
    private DietType dietType;
}