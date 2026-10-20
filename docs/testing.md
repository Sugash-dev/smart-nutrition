# Testing Documentation

## Strategy
The project includes service-layer unit tests using JUnit 5 and Mockito. Repository dependencies are mocked so that service behavior can be tested without a live MySQL database for these unit tests.

## Test classes found
| Test class | Main scenarios represented |
|---|---|
| `FoodServiceTest` | Create/list/get/update/delete food; missing food; dependent log deletion |
| `UserServiceTest` | User operations, calorie-target calculation, missing user handling |
| `MealLogServiceTest` | Log creation, retrieval by user/date, delete, missing user/food/log |
| `NutritionServiceTest` | Daily nutrient totals, remaining calories, status, recommendation filtering/sorting, missing user |
| `SmartNutritionApplicationTests` | Spring application context loading |

## Tools
- JUnit Jupiter for test methods and assertions.
- Mockito for mocks, stubbing, and interaction verification.
- Maven for compilation and test execution.
- Spring Boot test dependencies are declared in `pom.xml`.

## Run tests (PowerShell)
From repository root:
```powershell
.\mvnw.cmd clean test
```
Run a specific class:
```powershell
.\mvnw.cmd -Dtest=UserServiceTest test
.\mvnw.cmd -Dtest=FoodServiceTest test
.\mvnw.cmd -Dtest=MealLogServiceTest test
.\mvnw.cmd -Dtest=NutritionServiceTest test
```

## Scenarios represented
- Successful persistence calls are verified with mocked repositories.
- Missing resources should throw `ResourceNotFoundException`.
- Nutrition totals multiply food nutrient values by meal servings.
- Remaining calories are target minus consumed calories.
- Status is checked for under-target, on-track, and over-target cases.
- Recommendations are filtered by diet type and remaining calories and sorted by protein.
- Delete operations check existence and remove dependent meal logs where required.

## Test result reporting
A previous project documentation file records 29 tests, 0 failures, 0 errors, 0 skipped, and `BUILD SUCCESS`. That is a historical claim in the uploaded documentation, not a result independently reproduced during this pass. The current ZIP also contains unrelated prose embedded in `MealLogRepository.java`; therefore, repair/check that file and rerun the test command before using any test result in a final submission.

## Additional recommended tests
These are recommendations, not claims that tests currently exist:
- Validation tests for invalid user and food request bodies.
- Controller tests for response status codes and JSON error shape.
- Repository integration tests against a disposable MySQL/Testcontainers database.
- Recommendation tests for zero/negative remaining calorie edge cases.
- Tests for duplicate email and data-integrity handling.
- Tests for omitted summary date defaulting to today.

## Reading Maven output
- `Tests run`: number of executed test cases.
- `Failures`: assertions that did not match expectations.
- `Errors`: unexpected exceptions during test execution.
- `Skipped`: tests intentionally not executed.
- `BUILD SUCCESS`: Maven completed successfully; still inspect the test count and reports under `target/surefire-reports`.
