# Project Overview

## Introduction and abstract
Smart Nutrition Analysis and Personalized Meal Recommendation System is a Java/Spring Boot REST backend with a React/Vite frontend. It stores user profile information and food nutrient data, records meals by date and servings, calculates daily nutrient totals, and returns a short list of foods compatible with the user's diet type and remaining calorie allowance.

## Problem statement
Manual nutrition tracking can be inconsistent and make it hard to compare daily intake against an individual calorie target. The application centralizes food information and meal logs and exposes endpoints for calculating daily totals.

## Existing system and proposed system
**General problem context:** tracking may be performed manually in notes or spreadsheets, requiring users to calculate totals themselves.

**Implemented system:** a REST API and frontend project with user/food management, meal logging, daily summaries, and rule-based recommendations. No claim is made that the frontend has been built or run in this documentation pass.

## Objectives
1. Store nutrition-related user profile fields.
2. Estimate daily calorie target from age, gender, height, weight, activity level, and goal.
3. Maintain food items and nutrient values.
4. Link meal records to a user and food item.
5. Calculate daily calories, protein, carbohydrates, fat, and remaining calories.
6. Suggest up to five eligible foods ordered by protein content.

## Scope and target users
The project is intended as an educational nutrition-tracking application and API demonstration. Target users are people who want to record food intake and developers/testers demonstrating REST, persistence, validation, and testing concepts.

## Functional requirements
- Create, read, update, and delete user profiles.
- Create, read, update, and delete food items.
- Create meal logs with user ID, food ID, date, and servings.
- Retrieve meal logs for a user and date.
- Delete an existing meal log.
- Retrieve daily nutrition summary for a supplied date or today.
- Retrieve food recommendations for today.
- Return structured JSON errors for handled failures.

## Non-functional requirements
- Layered separation between web, business, and persistence code.
- Validation of selected fields and positive serving quantity.
- Persistent storage using MySQL/JPA.
- Testable service methods through mocked repositories.
- Local browser access restricted by configured CORS origins.

## Advantages
- Reuses stored food nutrition values.
- Calculates totals from serving quantities.
- Calculates calorie target in the service rather than trusting a client-provided target.
- Provides separate layers and service unit tests.

## Limitations
- No authentication or authorization was identified in the inspected backend.
- Recommendations are deterministic rules based on diet type, calories, and protein; they do not consider allergies or all medical/dietary needs.
- A daily calorie target is an estimate, not clinical guidance.
- The code's current configuration contains database credentials in `application.properties`; secure this before publishing.

## Future enhancements (not implemented claims)
- Authentication and authorization.
- Food search and additional filters.
- Allergy and dietary restriction support.
- API response DTOs and consistent response schemas.
- Integration tests against a disposable database.
- Configurable calorie formula and recommendation rules.
