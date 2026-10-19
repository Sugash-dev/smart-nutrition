\# Smart Nutrition Analysis and Personalized Meal Recommendation System



\## 1. Overview



Smart Nutrition is a Spring Boot REST API that helps users track their daily food intake, calculate nutrition totals, and receive personalized food recommendations based on their diet type and remaining calorie allowance.



\## 2. Technology Stack



\- Java 17

\- Spring Boot

\- Spring Web MVC

\- Spring Data JPA and Hibernate

\- MySQL 8

\- Jakarta Validation

\- Lombok

\- Springdoc OpenAPI / Swagger UI

\- JUnit 5 and Mockito

\- Postman



\## 3. Main Features



\- Create, retrieve, update, and delete user profiles.

\- Manage food nutritional information.

\- Record food consumption by user, date, and serving quantity.

\- Calculate daily calories, protein, carbohydrates, and fat.

\- Compare calorie intake against a daily calorie target.

\- Recommend foods according to diet type and remaining calories.

\- Validate input and handle resource-not-found errors.



\## 4. API Endpoints



\### Users



| Method | Endpoint | Purpose |

|---|---|---|

| GET | `/api/users` | Retrieve all users |

| GET | `/api/users/{id}` | Retrieve a user |

| POST | `/api/users` | Create a user |

| PUT | `/api/users/{id}` | Update a user |

| DELETE | `/api/users/{id}` | Delete a user |



\### Foods



| Method | Endpoint | Purpose |

|---|---|---|

| GET | `/api/foods` | Retrieve all foods |

| GET | `/api/foods/{id}` | Retrieve a food |

| POST | `/api/foods` | Create a food |

| PUT | `/api/foods/{id}` | Update a food |

| DELETE | `/api/foods/{id}` | Delete a food |



\### Meal Logs and Nutrition



| Method | Endpoint | Purpose |

|---|---|---|

| POST | `/api/meal-logs` | Record a meal |

| GET | `/api/meal-logs?userId={id}\&date={date}` | Retrieve meal logs |

| DELETE | `/api/meal-logs/{id}` | Delete a meal log |

| GET | `/api/users/{id}/nutrition-summary?date={date}` | Get daily nutrition summary |

| GET | `/api/users/{id}/recommendations` | Get food recommendations |



Use the actual request DTOs and enum values when preparing API requests.



\## 5. Configuration



1\. Install Java 17 and MySQL 8.

2\. Create the MySQL database named `nutritiondb`.

3\. Configure the database connection using the project's existing application configuration.

4\. Start the application using the Maven wrapper.



```powershell

.\\mvnw.cmd spring-boot:run

```



The application requires a working database connection. Use your team's configured database credentials; do not commit passwords or other secrets.



\## 6. API Documentation



When the application is running, open:



\- Swagger UI: `http://localhost:8080/swagger-ui.html`

\- OpenAPI specification: `http://localhost:8080/v3/api-docs`



\## 7. Testing



Run the complete test suite:



```powershell

.\\mvnw.cmd clean test

```



The service unit tests use JUnit 5 and Mockito to test user operations, food operations, meal logging, nutrition summaries, and food recommendations.



\## 8. Project Structure



```text

src/

├── main/

│   └── java/com/nutrition/smart\_nutrition/

│       ├── controller/

│       ├── dto/

│       ├── exception/

│       ├── model/

│       ├── repository/

│       ├── service/

│       └── SmartNutritionApplication.java

└── test/

&#x20;   └── java/com/nutrition/smart\_nutrition/

&#x20;       └── service/

```



\## 9. Team Contribution



This project includes REST API development, persistence, nutrition calculations, personalized recommendations, API documentation, and automated testing.



The testing and documentation contribution covers service unit tests, project setup instructions, endpoint documentation, database design documentation, and code quality review.



