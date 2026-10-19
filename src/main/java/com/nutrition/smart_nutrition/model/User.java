package com.nutrition.smart_nutrition.model;

import com.nutrition.smart_nutrition.model.enums.ActivityLevel;
import com.nutrition.smart_nutrition.model.enums.DietType;
import com.nutrition.smart_nutrition.model.enums.Gender;
import com.nutrition.smart_nutrition.model.enums.Goal;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "users")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    private String name;

    @NotBlank
    @Email
    @Column(unique = true)
    private String email;

    @Min(10)
    @Max(100)
    private int age;

    @Enumerated(EnumType.STRING)
    private Gender gender;

    @Positive
    private double heightCm;

    @Positive
    private double weightKg;

    @Enumerated(EnumType.STRING)
    private ActivityLevel activityLevel;

    @Enumerated(EnumType.STRING)
    private Goal goal;

    @Enumerated(EnumType.STRING)
    private DietType dietType;

    private double dailyCalorieTarget;
}