# Module Documentation

## 1. User management
**Classes:** `UserController`, `UserService`, `UserRepository`, `User`.

- `UserController`: `create`, `getAll`, `getById`, `update`, `delete`.
- `UserService`: CRUD behavior and private `calculateCalorieTarget`.
- `UserRepository`: standard CRUD through `JpaRepository<User, Long>`.
- Business rule: create and update calculate `dailyCalorieTarget` from profile fields; the controller accepts a `User` request body but the service recalculates the target.

The calorie estimate uses a Mifflin–St Jeor-style equation:
- Male BMR = `10 × weightKg + 6.25 × heightCm − 5 × age + 5`
- Female BMR = `10 × weightKg + 6.25 × heightCm − 5 × age − 161`
- Activity multipliers: SEDENTARY 1.2, LIGHT 1.375, MODERATE 1.55, ACTIVE 1.725.
- Goal adjustment: LOSE subtracts 500; MAINTAIN adds 0; GAIN adds 300.

## 2. Food management
**Classes:** `FoodController`, `FoodService`, `FoodRepository`, `Food`.

- CRUD operations are exposed under `/api/foods`.
- `FoodRepository.findByDietType(DietType)` filters food by enum value.
- `FoodService.update` copies name, calories, protein, carbohydrates, fat, and diet type to the existing entity.
- `FoodService.delete` checks existence, deletes dependent meal logs, then deletes the food in a transaction.
- `DataSeeder` inserts 20 sample food rows if the table is empty.

## 3. Meal logging
**Classes:** `MealLogController`, `MealLogService`, `MealLogRepository`, `MealLogRequest`, `MealLog`.

- Request fields: `userId`, `foodId`, `logDate`, `servings`.
- `MealLogService.logMeal` loads the referenced user and food, creates a log, and saves it.
- `getLogs` checks that the user exists, then returns logs for that date.
- `deleteLog` loads and deletes a log or throws `ResourceNotFoundException`.
- Repository query `findByUserIdAndLogDate` retrieves logs for a user and date.
- `MealLog` links to `User` and `Food` using `ManyToOne` mappings.

## 4. Nutrition analysis
**Classes:** `NutritionController`, `NutritionService`.

`getDailySummary(userId, date)` loads the user and meal logs for that date. For each log, it multiplies each nutrient value by `servings` and accumulates calories, protein, carbohydrates, and fat. It returns `consumedCalories`, `consumedProtein`, `consumedCarbs`, `consumedFat`, `targetCalories`, `remainingCalories`, and `status`.

Status logic:
- `over target` when consumed calories exceed the target.
- `on track` when consumption is not over target and the absolute difference from target is at most 10% of target.
- Otherwise `under target`.

The summary endpoint defaults to today's date when `date` is omitted.

## 5. Food recommendations
`NutritionService.recommendFoods(userId)`:
1. Uses today's date to obtain the daily summary.
2. Reads the remaining calorie allowance.
3. Loads the user's diet type.
4. Gets foods matching that diet type.
5. Keeps foods whose per-item `calories` value is less than or equal to remaining calories.
6. Sorts eligible foods by `proteinG` descending.
7. Returns at most five foods.

Note: the eligibility comparison uses the food's stored calorie value for one food item and does not multiply it by a serving quantity in this method.

## 6. Validation and error handling
- `User`: name and email required; email format; age at least 1; required gender/activity/goal/diet type; positive height and weight.
- `Food`: nonblank name; non-negative calories/protein/carbohydrates/fat; required diet type.
- `MealLogRequest`: user ID and food ID not null, log date not null, servings positive.
- `GlobalExceptionHandler`: maps resource-not-found and request/data errors to HTTP responses with `timestamp`, `status`, and `message`.

## 7. Dependencies between modules
Controllers depend on services. Services depend on repositories. `NutritionService` depends on user, meal-log, and food repositories. `MealLogService` depends on user, food, and meal-log repositories.
