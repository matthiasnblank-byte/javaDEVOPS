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

## API
- GET /api/todos
- GET /api/todos/{id}
- POST /api/todos  – Body: { "title": "...", "completed": false }
- PUT /api/todos/{id} – Body: { "title": "...", "completed": true }
- DELETE /api/todos/{id}
