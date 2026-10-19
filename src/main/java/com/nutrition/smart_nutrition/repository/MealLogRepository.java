package com.nutrition.smart_nutrition.repository;

import com.nutrition.smart_nutrition.model.MealLog;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;

public interface MealLogRepository extends JpaRepository<MealLog, Long> {

    List<MealLog> findByUserIdAndLogDate(Long userId, LocalDate date);
}