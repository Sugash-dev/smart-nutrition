package com.nutrition.smart_nutrition.model;

import com.nutrition.smart_nutrition.model.enums.DietType;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "foods")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Food {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Food name is required")
    @Column(nullable = false)
    private String name;

    @PositiveOrZero(message = "Calories cannot be negative")
    private double calories;

    @PositiveOrZero(message = "Protein cannot be negative")
    private double proteinG;

    @PositiveOrZero(message = "Carbohydrates cannot be negative")
    private double carbsG;

    @PositiveOrZero(message = "Fat cannot be negative")
    private double fatG;

    @NotNull(message = "Diet type is required")
    @Enumerated(EnumType.STRING)
    private DietType dietType;
}
