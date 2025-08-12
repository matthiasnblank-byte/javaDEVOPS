# arc42 – Architektur-Dokumentation

## 1. Einleitung und Ziele
- Zweck: Kleine Referenzanwendung (Todo-API) zur Demonstration von Spring Boot, Schichten-Architektur und H2 In-Memory-DB.
- Qualitätsziele: Einfachheit, Lesbarkeit, schnelle Tests, geringer Betriebsoverhead.
- Stakeholder: Entwickler:innen, DevOps, Tester:innen.

## 2. Randbedingungen
- Laufzeitumgebung: Java 17+, Spring Boot 3.3.x, H2 In-Memory Datenbank.
- Build: Maven.

## 3. Kontextabgrenzung
- System stellt eine REST-API bereit: CRUD auf `Todo`-Einträgen.
- Keine externen Integrationen, Datenhaltung In-Memory (H2) zur Entwicklungs- und Testzwecken.

## 4. Lösungsstrategie
- Schichten: Controller → Service → Repository → H2/DB.
- Nutzung von Spring Data JPA für Persistenz, Bean Validation für Request-Validierung.
- End-to-End-Tests gegen eingebetteten Server mit zufälligem Port.

## 5. Bausteinsicht
- `TodoController`: REST-Endpunkte unter `/api/todos`.
- `TodoService`: Geschäftslogik (CRUD, Validierung delegiert an Bean Validation).
- `TodoRepository`: `JpaRepository<Todo, Long>`.
- `Todo` Entity: `id`, `title`, `completed`.

## 6. Laufzeitsicht (Beispielablauf Create)
1. Client POST `/api/todos` mit JSON `{ "title": "X", "completed": false }`.
2. Controller validiert (`@Valid`) und ruft Service `create`.
3. Service speichert via Repository in H2, gibt Entity mit `id` zurück.
4. Controller antwortet `201 Created` mit Location-Header und Body.

## 7. Verteilungssicht
- Single-Prozess-Anwendung. Tests starten eingebetteten Webserver.

## 8. Qualitätsszenarien
- Funktional: Vollständiges CRUD über REST.
- Testbarkeit: Unit-Tests (Service, Mock Repo), E2E-Tests (HTTP gegen Random-Port).
- Wartbarkeit: Klare Trennung der Schichten, sprechende Namen.

## 9. Risiken & Technische Schulden
- Keine Authentifizierung/Autorisierung.
- In-Memory DB nicht für Produktion geeignet.

## 10. Entscheidungen
- Spring Boot + Spring Data JPA + H2.
- Validierung via Bean Validation (`@NotBlank` auf `title`).

## 11. Glossar
- CRUD: Create, Read, Update, Delete.
- E2E-Test: Ende-zu-Ende Test über HTTP.
