\# Database Design



\## 1. Overview



The Smart Nutrition Analysis and Personalized Meal Recommendation System uses MySQL 8 to store user information, food nutritional details, and meal consumption records. Spring Data JPA and Hibernate are used for persistence.



\## 2. Database



\- \*\*Database:\*\* MySQL

\- \*\*Database name:\*\* `nutritiondb`

\- \*\*Persistence framework:\*\* Spring Data JPA

\- \*\*ORM:\*\* Hibernate



\## 3. Main Entities



\### User



The User entity stores information required for nutrition analysis and personalized recommendations.



Key attributes include:



\- User ID

\- Name

\- Email

\- Age

\- Gender

\- Height in centimetres

\- Weight in kilograms

\- Activity level

\- Goal

\- Diet type

\- Daily calorie target



The daily calorie target is calculated by the application based on user information and is not intended to be supplied directly by the client.



\### Food



The Food entity stores food items and their nutritional information.



Key attributes include:



\- Food ID

\- Food name

\- Calories

\- Protein in grams

\- Carbohydrates in grams

\- Fat in grams

\- Diet type



Validation is applied to food names and nutritional values.



\### MealLog



The MealLog entity records food consumed by a user.



Key attributes include:



\- Meal log ID

\- User reference

\- Food reference

\- Log date

\- Number of servings



Each meal log references one user and one food item.



\## 4. Relationships



\- \*\*User to MealLog:\*\* One user can have multiple meal logs.

\- \*\*Food to MealLog:\*\* One food item can appear in multiple meal logs.

\- \*\*MealLog to User:\*\* Each meal log references a user.

\- \*\*MealLog to Food:\*\* Each meal log references a food item.



\## 5. Data Access Layer



The application uses the following repositories:



\- `UserRepository` for user persistence.

\- `FoodRepository` for food persistence, including filtering by diet type.

\- `MealLogRepository` for meal-log persistence and retrieving records by user ID and log date.



\## 6. Data Validation



The model uses validation constraints to help maintain data quality. Examples include:



\- Required values for specified fields.

\- Email format validation.

\- Age range validation.

\- Positive height and weight values.

\- Non-negative nutritional values.

\- Positive meal serving quantities.



\## 7. Nutrition Calculations



Meal nutrition totals are calculated from the nutritional values of each food item and the recorded serving quantity. Daily summaries compare consumed calories with the user's daily calorie target.



\## 8. Summary



The database design separates user profiles, food nutritional information, and meal consumption records into three main entities. Their relationships support daily nutrition tracking and personalized food recommendations.

