package com.nutrition.smart_nutrition.repository;

import com.nutrition.smart_nutrition.model.Food;
import com.nutrition.smart_nutrition.model.enums.DietType;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface FoodRepository extends JpaRepository<Food, Long> {

    List<Food> findByDietType(DietType dietType);
}