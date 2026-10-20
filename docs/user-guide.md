# User Guide

This guide describes API usage. The application includes frontend source code, but this guide uses the documented backend endpoints so each step can also be performed with Swagger or Postman.

## Recommended workflow
1. Start MySQL and the backend.
2. Retrieve available foods using `GET /api/foods`.
3. Create a user using `POST /api/users`.
4. Record a meal using `POST /api/meal-logs` with the returned user ID and a food ID.
5. Retrieve that day's logs using `GET /api/meal-logs?userId={id}&date=YYYY-MM-DD`.
6. Retrieve the nutrition summary using `GET /api/users/{id}/nutrition-summary?date=YYYY-MM-DD`.
7. Retrieve recommendations using `GET /api/users/{id}/recommendations` (recommendations use today's date).
8. Delete a log using `DELETE /api/meal-logs/{id}` when needed.

## Create a user
`POST /api/users`
```json
{
  "name": "Sample User",
  "email": "sample.user@example.com",
  "age": 22,
  "gender": "MALE",
  "heightCm": 172,
  "weightKg": 68,
  "activityLevel": "MODERATE",
  "goal": "MAINTAIN",
  "dietType": "VEG"
}
```
Use a unique email. The server calculates `dailyCalorieTarget`.

## View foods
- `GET /api/foods`: list all foods.
- `GET /api/foods/{id}`: retrieve one food.
- `POST /api/foods`: create a food with `name`, `calories`, `proteinG`, `carbsG`, `fatG`, and `dietType`.

## Log a meal
`POST /api/meal-logs`
```json
{
  "userId": 1,
  "foodId": 1,
  "logDate": "2026-10-20",
  "servings": 2.0
}
```
The user and food IDs must refer to existing records and servings must be positive.

## Read the daily summary
`GET /api/users/1/nutrition-summary?date=2026-10-20`

The response includes consumed calories and macronutrients, target calories, remaining calories, and status. If the date is omitted, the endpoint uses the current date.

## Get recommendations
`GET /api/users/1/recommendations`

The service uses today's summary, filters foods by the user's `dietType` and the remaining calorie allowance, sorts by protein grams descending, and returns at most five foods. The comparison is based on each food's stored calories, not a custom serving count in the recommendation response.

## Delete records
- `DELETE /api/meal-logs/{id}`
- `DELETE /api/users/{id}` removes the user's meal logs first.
- `DELETE /api/foods/{id}` removes meal logs referencing that food first.

Successful delete endpoints return `204 No Content`.
