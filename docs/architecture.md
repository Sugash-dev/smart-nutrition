\# System Architecture



\## 1. Project Overview



The Smart Nutrition Analysis and Personalized Meal Recommendation System is a Spring Boot application that helps users track their food intake, calculate daily nutritional consumption, and receive personalized food recommendations.



\## 2. Technology Stack



\- \*\*Programming Language:\*\* Java 17

\- \*\*Backend Framework:\*\* Spring Boot

\- \*\*Web Layer:\*\* Spring Web MVC

\- \*\*Persistence:\*\* Spring Data JPA and Hibernate

\- \*\*Database:\*\* MySQL 8

\- \*\*Validation:\*\* Jakarta Validation

\- \*\*API Documentation:\*\* Springdoc OpenAPI

\- \*\*Testing:\*\* JUnit and Mockito

\- \*\*API Testing:\*\* Postman



\## 3. Application Layers



\### Controller Layer



Handles incoming HTTP requests and returns responses to clients. It exposes endpoints for users, foods, meal logs, nutrition summaries, and food recommendations.



\### Service Layer



Contains the application's business logic.



\- \*\*UserService:\*\* Manages user operations and calculates daily calorie targets.

\- \*\*FoodService:\*\* Manages food-related operations.

\- \*\*MealLogService:\*\* Records and retrieves meal logs.

\- \*\*NutritionService:\*\* Calculates nutritional summaries and generates food recommendations.



\### Repository Layer



Uses Spring Data JPA to communicate with the database.



\- \*\*UserRepository:\*\* Performs user database operations.

\- \*\*FoodRepository:\*\* Performs food database operations and supports filtering by diet type.

\- \*\*MealLogRepository:\*\* Performs meal-log operations and retrieves logs by user and date.



\### Model Layer



Defines the application's data entities, including `User`, `Food`, and `MealLog`.



\### Exception Handling and Validation



The application uses resource-not-found exception handling and Jakarta Validation to help manage invalid or missing data.



\## 4. Request Processing Flow



1\. A client sends an HTTP request to an API endpoint.

2\. The controller receives and processes the request.

3\. The service layer executes the required business logic.

4\. The repository interacts with the MySQL database when necessary.

5\. The result returns through the service and controller layers to the client.



\## 5. Database Relationships



\- A user can have multiple meal logs.

\- A food item can be referenced by multiple meal logs.

\- Each meal log links a user, a food item, a date, and the number of servings.



\## 6. Testing Approach



Service-layer unit tests use JUnit and Mockito to test business logic independently of the database. Repository dependencies are mocked to verify successful operations and exception-handling scenarios.



\## 7. Architecture Summary



The application follows a layered architecture that separates request handling, business logic, database access, and data representation. This separation improves maintainability and makes individual service components easier to test.

