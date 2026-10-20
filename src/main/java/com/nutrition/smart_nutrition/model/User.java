package com.nutrition.smart_nutrition.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.nutrition.smart_nutrition.model.enums.ActivityLevel;
import com.nutrition.smart_nutrition.model.enums.DietType;
import com.nutrition.smart_nutrition.model.enums.Gender;
import com.nutrition.smart_nutrition.model.enums.Goal;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(
        name = "users",
        uniqueConstraints = {
                @UniqueConstraint(columnNames = "email")
        }
)
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Name is required")
    private String name;

    @NotBlank(message = "Email is required")
    @Email(message = "Invalid email format")
    @Column(nullable = false, unique = true)
    private String email;

    @NotNull(message = "Age is required")
    @Min(value = 1, message = "Age must be greater than 0")
    private Integer age;

    @NotNull(message = "Gender is required")
    @Enumerated(EnumType.STRING)
    private Gender gender;

    @NotNull(message = "Height is required")
    @Positive(message = "Height must be positive")
    private Double heightCm;

    @NotNull(message = "Weight is required")
    @Positive(message = "Weight must be positive")
    private Double weightKg;

    @NotNull(message = "Activity level is required")
    @Enumerated(EnumType.STRING)
    private ActivityLevel activityLevel;

    @NotNull(message = "Goal is required")
    @Enumerated(EnumType.STRING)
    private Goal goal;

    @NotNull(message = "Diet type is required")
    @Enumerated(EnumType.STRING)
    private DietType dietType;

    private Double dailyCalorieTarget;

    /*
     * Meal logs belong to a user.
     * UserService explicitly removes these rows before deleting
     * the user, which also makes deletion safe with the existing
     * MySQL foreign-key constraint.
     *
     * JsonIgnore prevents circular JSON serialization.
     */
    @JsonIgnore
    @OneToMany(
            mappedBy = "user",
            cascade = CascadeType.ALL,
            orphanRemoval = true
    )
    private List<MealLog> mealLogs = new ArrayList<>();

    public User() {
    }

    public User(
            String name,
            String email,
            Integer age,
            Gender gender,
            Double heightCm,
            Double weightKg,
            ActivityLevel activityLevel,
            Goal goal,
            DietType dietType
    ) {
        this.name = name;
        this.email = email;
        this.age = age;
        this.gender = gender;
        this.heightCm = heightCm;
        this.weightKg = weightKg;
        this.activityLevel = activityLevel;
        this.goal = goal;
        this.dietType = dietType;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    public Gender getGender() {
        return gender;
    }

    public void setGender(Gender gender) {
        this.gender = gender;
    }

    public Double getHeightCm() {
        return heightCm;
    }

    public void setHeightCm(Double heightCm) {
        this.heightCm = heightCm;
    }

    public Double getWeightKg() {
        return weightKg;
    }

    public void setWeightKg(Double weightKg) {
        this.weightKg = weightKg;
    }

    public ActivityLevel getActivityLevel() {
        return activityLevel;
    }

    public void setActivityLevel(ActivityLevel activityLevel) {
        this.activityLevel = activityLevel;
    }

    public Goal getGoal() {
        return goal;
    }

    public void setGoal(Goal goal) {
        this.goal = goal;
    }

    public DietType getDietType() {
        return dietType;
    }

    public void setDietType(DietType dietType) {
        this.dietType = dietType;
    }

    public Double getDailyCalorieTarget() {
        return dailyCalorieTarget;
    }

    public void setDailyCalorieTarget(Double dailyCalorieTarget) {
        this.dailyCalorieTarget = dailyCalorieTarget;
    }

    public List<MealLog> getMealLogs() {
        return mealLogs;
    }

    public void setMealLogs(List<MealLog> mealLogs) {
        this.mealLogs = mealLogs;
    }

    public void addMealLog(MealLog mealLog) {
        mealLogs.add(mealLog);
        mealLog.setUser(this);
    }

    public void removeMealLog(MealLog mealLog) {
        mealLogs.remove(mealLog);
        mealLog.setUser(null);
    }
}
