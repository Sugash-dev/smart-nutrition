\# Testing Documentation



\## 1. Overview



The Smart Nutrition Analysis and Personalized Meal Recommendation System uses JUnit and Mockito to test service-layer business logic. The tests check successful operations, data retrieval, calculations, and exception-handling scenarios.



\## 2. Testing Tools



\- \*\*JUnit:\*\* Runs test cases and checks expected results.

\- \*\*Mockito:\*\* Creates mock dependencies and verifies interactions.

\- \*\*Maven:\*\* Compiles the project and executes the test suite.



\## 3. Unit Test Classes



\### UserServiceTest



Tests user-related operations, including creation, retrieval, updating, deletion, calorie-target calculations, and handling users that do not exist.



\### FoodServiceTest



Tests food creation, retrieval, updating, deletion, and handling missing food records.



\### MealLogServiceTest



Tests recording meals, retrieving meal logs for a user and date, deleting meal logs, and handling missing users, foods, or meal logs.



\### NutritionServiceTest



Tests daily nutritional summaries, calorie calculations, nutrition status, remaining calories, and food recommendation logic.



\## 4. Mocking Strategy



Service tests use Mockito to mock repository dependencies. This allows service methods to be tested independently without requiring database operations for those unit tests.



\## 5. Running the Tests



Open a terminal in the project root directory and run the complete test suite:



```powershell

.\\mvnw.cmd clean test

```



To run an individual service test class, use the corresponding command:



```powershell

.\\mvnw.cmd -Dtest=UserServiceTest test

.\\mvnw.cmd -Dtest=FoodServiceTest test

.\\mvnw.cmd -Dtest=MealLogServiceTest test

.\\mvnw.cmd -Dtest=NutritionServiceTest test

```



\## 6. Test Results



The complete test run reported:



\- Tests run: 29

\- Failures: 0

\- Errors: 0

\- Skipped: 0

\- Build status: SUCCESS



These results correspond to the test execution recorded during project development.



\## 7. Integration Test Note



The project also contains `SmartNutritionApplicationTests`, which checks whether the Spring application context loads. Unlike isolated service unit tests, this test may depend on the application's runtime configuration and database availability.



\## 8. Conclusion



JUnit and Mockito provide a way to verify the application's service-layer behavior and detect regressions. Running the full Maven test suite before committing changes helps confirm that the tested functionality continues to work.

