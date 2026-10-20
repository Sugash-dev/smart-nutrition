# Setup and Installation (Windows 11 / PowerShell)

## Prerequisites
Install JDK 17, MySQL 8, and Node.js with npm. The Maven Wrapper (`mvnw.cmd`) is included, so a separate Maven installation is not normally required.

Verify tools:
```powershell
java -version
.\mvnw.cmd -version
mysql --version
node --version
npm --version
```
Run the Maven command from the project root.

## 1. Create the database
Start the MySQL service and create the schema:
```sql
CREATE DATABASE nutritiondb;
```
The application configuration expects MySQL at `localhost:3306` and schema `nutritiondb`.

## 2. Configure database credentials safely
Open `src/main/resources/application.properties`. The uploaded project has literal database credentials in this file. Do not commit real passwords. Prefer environment-variable placeholders, or a local configuration file excluded from Git. If credentials were committed to a public repository, rotate them.

Current non-secret configuration values include:
```properties
spring.datasource.url=jdbc:mysql://localhost:3306/nutritiondb
spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true
```
The username/password must be set to credentials valid on your machine.

## 3. Open and run backend
Open the repository root in IntelliJ IDEA or VS Code. From PowerShell:
```powershell
.\mvnw.cmd clean test
.\mvnw.cmd spring-boot:run
```
Wait for Spring Boot to start; the default API base URL is `http://localhost:8080`. If MySQL is unavailable or credentials are wrong, startup can fail.

## 4. Run frontend
Open a second PowerShell window:
```powershell
cd frontend
npm install
npm run dev
```
Open the localhost address printed by Vite. The backend CORS configuration currently permits ports 5173, 5174, and 5175.

## 5. Try the API
Open `http://localhost:8080/swagger-ui.html`, or import `postman_collection.json` into Postman. Create a user and use its returned ID when logging meals. Ensure a food ID exists; `DataSeeder` seeds 20 foods when the food table is empty.

## 6. Common issues
- **Database connection refused:** ensure MySQL service is running and the port is correct.
- **Access denied:** verify local database username/password.
- **Unknown database:** create `nutritiondb`.
- **Port 8080 already in use:** stop the other process or configure a different server port.
- **Validation error:** check required fields and enum values (`MALE`, `FEMALE`, `VEG`, `NON_VEG`, etc.).
- **Date parse error:** use ISO date `YYYY-MM-DD`.
- **Frontend CORS error:** check the Vite port is one of the allowed origins or update CORS intentionally.
- **Maven compile failure:** check Java version and inspect the compiler error. The uploaded ZIP has unrelated text embedded in `MealLogRepository.java`; this must be corrected before a clean build can succeed.
- **Frontend dependency issue:** remove only generated `frontend/node_modules` and reinstall with `npm install`; do not commit `node_modules`.

## 7. Before submission
Run `.\mvnw.cmd clean test`, inspect `git status`, and ensure `.git`, `.idea`, `target`, `node_modules`, build outputs, credentials, and unrelated files are not committed.
