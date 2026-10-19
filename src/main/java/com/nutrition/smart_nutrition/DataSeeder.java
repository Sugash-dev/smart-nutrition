package com.nutrition.smart_nutrition;

import com.nutrition.smart_nutrition.model.Food;
import com.nutrition.smart_nutrition.model.enums.DietType;
import com.nutrition.smart_nutrition.repository.FoodRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class DataSeeder implements CommandLineRunner {

    private final FoodRepository foodRepository;

    @Override
    public void run(String... args) {

        // Insert foods only when the table is empty
        if (foodRepository.count() == 0) {

            List<Food> foods = List.of(

                    new Food(null, "Idli", 58, 2.0, 12.0, 0.2, DietType.VEG),
                    new Food(null, "Dosa", 168, 3.9, 28.0, 4.0, DietType.VEG),
                    new Food(null, "Chapati", 120, 3.5, 18.0, 3.0, DietType.VEG),
                    new Food(null, "Steamed Rice", 206, 4.3, 45.0, 0.4, DietType.VEG),
                    new Food(null, "Dal", 180, 9.0, 25.0, 4.0, DietType.VEG),
                    new Food(null, "Paneer", 265, 18.0, 6.0, 20.0, DietType.VEG),
                    new Food(null, "Vegetable Biryani", 250, 6.0, 40.0, 7.0, DietType.VEG),
                    new Food(null, "Sambar", 120, 5.0, 18.0, 3.0, DietType.VEG),
                    new Food(null, "Curd", 98, 5.0, 7.0, 5.0, DietType.VEG),
                    new Food(null, "Poha", 180, 4.0, 30.0, 5.0, DietType.VEG),
                    new Food(null, "Upma", 190, 5.0, 30.0, 5.0, DietType.VEG),
                    new Food(null, "Aloo Paratha", 220, 5.0, 35.0, 7.0, DietType.VEG),
                    new Food(null, "Banana", 105, 1.3, 27.0, 0.3, DietType.VEG),
                    new Food(null, "Apple", 95, 0.5, 25.0, 0.3, DietType.VEG),
                    new Food(null, "Chickpeas", 269, 14.5, 45.0, 4.2, DietType.VEG),
                    new Food(null, "Chicken Curry", 250, 25.0, 8.0, 13.0, DietType.NON_VEG),
                    new Food(null, "Boiled Egg", 78, 6.3, 0.6, 5.3, DietType.NON_VEG),
                    new Food(null, "Fish Curry", 220, 22.0, 6.0, 12.0, DietType.NON_VEG),
                    new Food(null, "Chicken Biryani", 350, 25.0, 45.0, 10.0, DietType.NON_VEG),
                    new Food(null, "Omelette", 154, 11.0, 1.0, 12.0, DietType.NON_VEG)
            );

            // Save all 20 foods
            foodRepository.saveAll(foods);

            System.out.println("20 foods inserted successfully.");
        }
    }
}