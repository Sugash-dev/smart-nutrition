package com.nutrition.smart_nutrition.repository;

import com.nutrition.smart_nutrition.model.MealLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;

public interface MealLogRepository extends JpaRepository<MealLog, Long> {

    List<MealLog> findByUserIdAndLogDate(Long userId, LocalDate date);

    @Modifying
    @Query("DELETE FROM MealLog m WHERE m.user.id = :userId")
    int deleteByUserId(@Param("userId") Long userId);

    @Modifying
    @Query("DELETE FROM MealLog m WHERE m.food.id = :foodId")
    int deleteByFoodId(@Param("foodId") Long foodId);
}
