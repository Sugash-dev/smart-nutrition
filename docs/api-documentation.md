# REST API Documentation

Base URL for local development: `http://localhost:8080`. JSON bodies should use `Content-Type: application/json`. Enum values are case-sensitive and must match the source enums.

## User APIs

| Method | Path | Purpose | Success response |
|---|---|---|---|
| POST | `/api/users` | Create user | `201 Created`, saved `User` JSON |
| GET | `/api/users` | List users | `200 OK`, JSON array |
| GET | `/api/users/{id}` | Get user | `200 OK`, `User` JSON |
| PUT | `/api/users/{id}` | Update user | `200 OK`, updated `User` JSON |
| DELETE | `/api/users/{id}` | Delete user and dependent logs | `204 No Content` |

Example `POST /api/users`:
```json
{
  "name": "Sample User",
  "email": "sample.user@example.com",
  "age": 22,
  "gender": "MALE",
  "heightCm": 172.0,
  "weightKg": 68.0,
  "activityLevel": "MODERATE",
  "goal": "MAINTAIN",
  "dietType": "VEG"
}
```
`dailyCalorieTarget` is calculated by `UserService`; do not rely on a client-supplied value.

## Food APIs

| Method | Path | Purpose | Success response |
|---|---|---|---|
| POST | `/api/foods` | Create food | `201 Created`, saved `Food` JSON |
| GET | `/api/foods` | List foods | `200 OK`, JSON array |
| GET | `/api/foods/{id}` | Get food | `200 OK`, `Food` JSON |
| PUT | `/api/foods/{id}` | Update food | `200 OK`, updated `Food` JSON |
| DELETE | `/api/foods/{id}` | Delete food and dependent logs | `204 No Content` |

Example `POST /api/foods`:
```json
{
  "name": "Oats",
  "calories": 150,
  "proteinG": 5.0,
  "carbsG": 27.0,
  "fatG": 3.0,
  "dietType": "VEG"
}
```

## Meal-log APIs

| Method | Path | Purpose | Success response |
|---|---|---|---|
| POST | `/api/meal-logs` | Record meal | `201 Created`, saved `MealLog` JSON |
| GET | `/api/meal-logs?userId=1&date=2026-10-20` | Get logs for user and date | `200 OK`, JSON array |
| DELETE | `/api/meal-logs/{id}` | Delete a meal log | `204 No Content` |

Example `POST /api/meal-logs`:
```json
{
  "userId": 1,
  "foodId": 1,
  "logDate": "2026-10-20",
  "servings": 2.0
}
```
The `date` query parameter and `logDate` body value use ISO `YYYY-MM-DD`.

## Nutrition and recommendation APIs

| Method | Path | Purpose | Success response |
|---|---|---|---|
| GET | `/api/users/{id}/nutrition-summary` | Today's summary when date omitted | `200 OK`, summary object |
| GET | `/api/users/{id}/nutrition-summary?date=2026-10-20` | Summary for specified date | `200 OK`, summary object |
| GET | `/api/users/{id}/recommendations` | Recommend foods for today | `200 OK`, JSON array of foods |

Summary fields: `consumedCalories`, `consumedProtein`, `consumedCarbs`, `consumedFat`, `targetCalories`, `remainingCalories`, and `status`.

## Validation and error responses
`GlobalExceptionHandler` uses a JSON object with `timestamp`, `status`, and `message`. Handled cases include:
- `404 Not Found`: `ResourceNotFoundException` or unknown endpoint.
- `400 Bad Request`: bean validation failure, unreadable/missing JSON body, invalid parameter type, or missing required query parameter.
- `405 Method Not Allowed`: unsupported HTTP method.
- `409 Conflict`: database integrity violation, such as a duplicate email.
- `500 Internal Server Error`: unexpected exception.

For validation errors, `message` is an object mapping field names to validation messages. The generic 500 response includes exception message text; review this before production use because internal details can leak.

## Swagger and Postman
- Swagger UI: `http://localhost:8080/swagger-ui.html`
- OpenAPI JSON: `http://localhost:8080/v3/api-docs`
- Postman collection file: `postman_collection.json` at repository root.

Status codes above are derived from controller return statements and the configured exception handler. Actual JSON serialization can include entity fields/getters and is not a dedicated DTO contract for every endpoint.
