# DevOps Todo – Spring Boot + H2

## Starten
```bash
mvn spring-boot:run
```

Server läuft auf `http://localhost:8080`.

## API testen (PowerShell)
```powershell
# In einem zweiten Terminal
./scripts/test-api.ps1 -BaseUrl http://localhost:8080
```

## End-to-End Tests (JUnit)
```bash
mvn -DskipTests=false test
```

## Continuous Integration
- GitHub Actions Workflow [`ci.yml`](.github/workflows/ci.yml) läuft automatisch bei jedem Push (alle Branches) sowie bei Pull-Requests gegen `main` und `develop`.
- Der Backend-Job richtet Java 17 ein und führt `mvn -B -ntp clean verify jacoco:report` aus. Wenn ein `SONAR_TOKEN`-Secret hinterlegt ist, startet anschließend automatisch `mvn -B sonar:sonar` für die SonarCloud-Analyse.
- Parallel prüft der Frontend-Job mit Node 20 & Chrome Headless sowohl Angular-Unit-Tests (`npm run test -- --watch=false --browsers=ChromeHeadlessNoSandbox --code-coverage`) als auch die Cypress-E2E-Suite (`npx --yes start-server-and-test "npm start -- --port 4200" http://localhost:4200 "npm run e2e"`).
- Für Branches, die mit `feat/` oder `feature/` beginnen, gibt es zusätzlich einen Informations-Job, der die Namenskonvention validiert.

## API
- GET /api/todos
- GET /api/todos/{id}
- POST /api/todos  – Body: { "title": "...", "completed": false }
- PUT /api/todos/{id} – Body: { "title": "...", "completed": true }
- DELETE /api/todos/{id}
