\# API Documentation



\## 1. Overview



The Smart Nutrition Analysis and Personalized Meal Recommendation System provides REST API endpoints for managing users, food items, meal logs, nutrition summaries, and personalized food recommendations.



Base URL:



`http://localhost:8080`



\## 2. User APIs



| Method | Endpoint | Description |

|---|---|---|

| GET | `/api/users` | Retrieve all users |

| GET | `/api/users/{id}` | Retrieve a user by ID |

| POST | `/api/users` | Create a new user |

| PUT | `/api/users/{id}` | Update an existing user |

| DELETE | `/api/users/{id}` | Delete a user |



\## 3. Food APIs



| Method | Endpoint | Description |

|---|---|---|

| GET | `/api/foods` | Retrieve all food items |

| GET | `/api/foods/{id}` | Retrieve a food item by ID |

| POST | `/api/foods` | Create a food item |

| PUT | `/api/foods/{id}` | Update a food item |

| DELETE | `/api/foods/{id}` | Delete a food item |



\## 4. Meal Log APIs



| Method | Endpoint | Description |

|---|---|---|

| POST | `/api/meal-logs` | Record a meal |

| GET | `/api/meal-logs?userId={id}\&date={date}` | Retrieve meal logs for a user on a date |

| DELETE | `/api/meal-logs/{id}` | Delete a meal log |



The date parameter should use the ISO format `YYYY-MM-DD`.



\## 5. Nutrition Summary API



\*\*Endpoint:\*\* `GET /api/users/{id}/nutrition-summary?date={date}`



Retrieves the user's daily nutrition summary for the specified date.



The summary includes:



\- Total calories consumed

\- Total protein consumed

\- Total carbohydrates consumed

\- Total fat consumed

\- Daily calorie target

\- Remaining calories

\- Nutrition status



\## 6. Food Recommendation API



\*\*Endpoint:\*\* `GET /api/users/{id}/recommendations`



Returns food recommendations based on the user's diet type and remaining daily calorie allowance.



\## 7. API Documentation Interface



When the application is running, the Swagger UI may be accessed at:



`http://localhost:8080/swagger-ui.html`



The OpenAPI specification is available at:



`http://localhost:8080/v3/api-docs`



\## 8. API Testing



The endpoints can be tested using Postman. Use the appropriate HTTP method, URL, request body, and query parameters for each operation.



For POST and PUT requests, provide JSON request bodies that match the application's actual model fields and validation requirements.



\## 9. Error Handling



Requests involving nonexistent resources may return a resource-not-found error. Invalid input may trigger validation errors. Actual HTTP status codes and response formats depend on the application's configured exception handling.



\## 10. Notes



\- Start the Spring Boot application before making requests.

\- Ensure the MySQL database is configured and running.

\- Use valid IDs and dates when testing endpoints.

\- Refer to the existing Postman collection for available request examples.

