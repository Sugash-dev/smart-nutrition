# Viva Preparation Guide

Use this guide to understand the implementation rather than memorizing answers. Verify the source-integrity issue in `MealLogRepository.java` before demonstrating a build.

## 1. Two-minute introduction
“Good morning. Our project is Smart Nutrition Analysis and Personalized Meal Recommendation System. It helps maintain user nutrition profiles, store food nutrient information, record meals, calculate daily nutrient intake, and suggest foods. The backend is built with Java 17 and Spring Boot and exposes REST APIs. Spring Data JPA and Hibernate connect the application to MySQL. The application separates controllers, services, repositories, and entities. The user service estimates a daily calorie target using age, gender, height, weight, activity level, and goal. The nutrition service calculates daily calories and macronutrients from meal logs. The recommendation logic filters foods by diet type and remaining calories and sorts them by protein. We use JUnit and Mockito to test service behavior. A React/Vite frontend is included to interact with the backend APIs.”

## 2. Five-minute explanation
1. **Problem:** manual food tracking makes daily totals and target comparisons difficult.
2. **Architecture:** React or API client → controller → service → repository → JPA/Hibernate → MySQL.
3. **User:** `User` stores age, gender, height, weight, activity, goal, diet type, and calculated calorie target. `UserService` recalculates the target on create/update.
4. **Food:** `Food` stores calories, protein, carbohydrates, fat, and diet type.
5. **Meal log:** `MealLog` connects one user and one food item with a date and serving quantity.
6. **Summary:** `NutritionService.getDailySummary` multiplies food nutrient values by serving quantity and totals them for a date.
7. **Recommendations:** `recommendFoods` uses today's summary, diet type, remaining calories, and protein sorting; it returns up to five foods.
8. **Validation/errors:** Jakarta validation checks request fields; `GlobalExceptionHandler` returns a consistent envelope with timestamp/status/message for handled errors.
9. **Testing:** JUnit tests service logic with Mockito mocks. Report only test results reproduced on the final code.
10. **Limitations:** rule-based recommendations, no backend authentication/authorization identified, and calorie target is an estimate.

## 3. Important files
| File | What to explain |
|---|---|
| `SmartNutritionApplication.java` | Spring Boot entry point |
| `DataSeeder.java` | Inserts 20 foods if the food table is empty |
| `controller/UserController.java` | User HTTP endpoints |
| `controller/FoodController.java` | Food HTTP endpoints |
| `controller/MealLogController.java` | Meal log endpoints and `MealLogRequest` input |
| `controller/NutritionController.java` | Summary and recommendation endpoints |
| `service/UserService.java` | User CRUD and calorie target calculation |
| `service/FoodService.java` | Food CRUD and safe dependent-log deletion |
| `service/MealLogService.java` | Create/read/delete meal logs |
| `service/NutritionService.java` | Daily totals, status, recommendations |
| `repository/UserRepository.java` | User CRUD via `JpaRepository` |
| `repository/FoodRepository.java` | Food CRUD and `findByDietType` |
| `repository/MealLogRepository.java` | Meal-log lookup and delete queries; verify source file is clean |
| `model/User.java` | User entity and one-to-many relationship |
| `model/Food.java` | Food entity and validation |
| `model/MealLog.java` | Many-to-one links to user and food |
| `dto/MealLogRequest.java` | Meal-log request fields |
| `exception/GlobalExceptionHandler.java` | Central error-to-response mapping |
| `config/WebConfig.java` | Local CORS policy |
| `src/test/...` | Service unit tests |

## 4. Thirty-five likely viva questions and answers

1. **What is the project about?** It records meals, calculates daily nutrition totals, and recommends foods using implemented rules.
2. **Why Spring Boot?** It simplifies setup and running of a Java web application.
3. **What is a REST API?** An HTTP interface that exposes resources and operations to clients.
4. **What does a controller do?** Maps HTTP requests and responses.
5. **What does a service do?** Executes business rules such as calorie calculations and recommendations.
6. **What does a repository do?** Provides persistence operations for entities.
7. **Why separate these layers?** It separates request handling, business logic, and data access, improving maintainability and testability.
8. **What is dependency injection?** Spring supplies required objects to classes, for example repositories into services.
9. **What is an entity?** A Java class mapped to a database table.
10. **What is JPA?** A Java persistence specification for object-relational mapping.
11. **What is Hibernate?** The ORM provider used to implement JPA operations.
12. **What does `JpaRepository` provide?** Common operations such as save, find, and delete without writing each query.
13. **What is a DTO?** An object for transferring request/response data; `MealLogRequest` carries meal-log fields.
14. **Why use a meal-log DTO?** The client sends user and food IDs rather than constructing nested entity objects.
15. **What is validation?** Checking incoming values against constraints before business logic proceeds.
16. **What does `@Valid` do here?** Triggers validation for the annotated request body.
17. **What is `@RestControllerAdvice`?** Centralized handling of exceptions thrown during controller processing.
18. **What is `ResourceNotFoundException` used for?** Signaling that a requested user, food, or meal log does not exist.
19. **What does HTTP 201 mean?** A resource was created.
20. **What does HTTP 204 mean?** The request succeeded and the response has no body, as used by delete endpoints.
21. **What is a primary key?** A unique identifier for a database row.
22. **What is a foreign key?** A reference from one table to another table's row.
23. **Why does `MealLog` reference `User` and `Food`?** Each logged meal belongs to a user and identifies the food consumed.
24. **Why delete meal logs before deleting a user?** Existing rows may reference the user through a foreign key; dependent rows are removed first.
25. **What is CORS?** Browser policy controlling which origins may call an API. Here localhost ports 5173–5175 are allowed.
26. **What does Maven do?** Manages dependencies and build/test lifecycle.
27. **What is Swagger UI?** A browser interface to inspect and try API endpoints.
28. **What is Postman?** A client for sending HTTP requests and examining responses.
29. **What is JUnit?** A framework for Java tests.
30. **What is Mockito?** A library for mocking dependencies and verifying calls in tests.
31. **How is the calorie target calculated?** Estimate BMR from weight, height, age, and gender, multiply by activity factor, then apply the selected goal adjustment.
32. **What is the activity multiplier for MODERATE?** 1.55 in `UserService`.
33. **How are nutrition totals calculated?** Each food's stored nutrient values are multiplied by logged servings and accumulated.
34. **How are recommendations ranked?** Filter by user's diet type and remaining calories, sort by protein descending, then take at most five.
35. **Is recommendation AI/ML-based?** No. The inspected implementation is rule-based.
36. **What happens if a user ID is missing?** The service throws `ResourceNotFoundException`, which the global handler maps to 404.
37. **Why is `@JsonIgnore` used on `User.mealLogs` and `MealLog.user`?** To avoid recursive/circular serialization of linked entities.
38. **What does `DataSeeder` do?** Inserts 20 foods only if the foods table is empty.
39. **Does the summary date default to today?** Yes, in `NutritionController`; recommendations also use today's date.
40. **Can we claim all tests pass now?** Only after rerunning tests on the final corrected source and checking Maven output.

## 5. Scenario-based questions
1. **A user deletion fails due to a foreign key.** Check dependent `MealLog` rows; `UserService.delete` deletes logs by user ID before deleting the user, inside a transaction.
2. **A food deletion fails due to references.** `FoodService.delete` removes meal logs by food ID before deleting the food.
3. **A meal request uses a nonexistent food ID.** `MealLogService` throws `ResourceNotFoundException`; handler returns 404.
4. **A user email is duplicated.** The unique constraint may trigger `DataIntegrityViolationException`, mapped to 409.
5. **A client sends an invalid enum.** JSON binding fails and is handled as a 400 bad request.
6. **The date is `20-10-2026`.** Use ISO `2026-10-20`; invalid date binding is a bad request.
7. **A serving quantity is zero.** `@Positive` validation should reject it on the request DTO.
8. **Summary shows negative remaining calories.** Consumed calories exceed target; status becomes `over target`.
9. **No food matches diet type or calorie allowance.** The recommendation list may be empty.
10. **Frontend reports CORS.** Confirm its origin uses an allowed port and the backend is running.
11. **Tests fail to compile at `MealLogRepository.java`.** Inspect the file for the unrelated prose present in the uploaded ZIP, restore valid Java declarations, then rerun tests.
12. **A faculty member asks to add a field to Food.** Update the entity and validation, review API examples, then add/update tests; with JPA update mode the schema may update at startup, but verify it.
13. **A faculty member asks for a new API endpoint.** Add controller mapping, service behavior if needed, repository query if needed, test, then verify through Swagger/Postman.
14. **Need a new repository filter.** Add a derived query method or explicit JPQL query, use it in a service, and test the service behavior.
15. **Need age 18–100.** Change `@Min` and add `@Max` on `User.age`, update documentation and tests. Do not make this change unless requested.

## 6. Safe on-the-spot change workflow
Entity/validation → repository (if needed) → service logic → controller endpoint → unit test → Swagger/Postman check → database check → review `git diff`. Avoid unrelated refactoring.

## 7. Important caveat before viva
The uploaded ZIP's `MealLogRepository.java` contains pasted natural-language project notes inside the interface body. This appears to be a source corruption issue and should be corrected separately before claiming a fresh successful build. Do not silently treat the current ZIP as a verified build.
