# arc42 – Architektur-Dokumentation

## 1. Einleitung und Ziele
- Zweck: Referenzanwendung (Todo-API und -Frontend) zur Demonstration eines einfachen Full-Stack-Setups mit Spring Boot (Backend) und Angular (Frontend).
- Qualitätsziele: Einfachheit, Lesbarkeit, schnelle Tests (Unit/E2E), geringer Betriebsoverhead, lokale Developer Experience (DX).
- Stakeholder: Entwickler:innen, DevOps, Tester:innen.

## 2. Randbedingungen
- Laufzeitumgebung Backend: Java 17+, Spring Boot 3.3.x, H2 In-Memory-Datenbank.
- Laufzeitumgebung Frontend: Node.js 18+ (lokal), Angular 18.
- Build/Tooling: Maven (Backend), npm (Frontend), Angular CLI, Tailwind CSS, Cypress.

## 3. Kontextabgrenzung
- System stellt eine REST-API bereit: CRUD auf `Todo`-Einträgen unter `/api/todos`.
- Frontend ist eine Single-Page Application (SPA) und konsumiert die REST-API.
- Entwicklungsmodus: Proxy leitet Aufrufe von `http://localhost:4200/api/*` an `http://localhost:8080` weiter (siehe `frontend/proxy.conf.json`).
- Datenhaltung: In-Memory (H2) für Entwicklungs- und Testzwecke; keine externen Integrationen.

## 4. Lösungsstrategie
- Backend-Schichten: Controller → Service → Repository → H2/DB. Persistenz via Spring Data JPA, Validierung via Bean Validation.
- Frontend: Angular 18 (Standalone Components, Router, HttpClient), Tailwind CSS für Styles. Todos-Seite als zentrale UI mit CRUD-Funktionalität.
- Kommunikation: JSON/HTTP. Im Dev-Betrieb via Angular-Proxy, in Produktion über direkte API-URL (siehe Risiken/CORS).
- Teststrategie:
  - Backend: Unit-/Integrations-/E2E-Tests über Spring Boot (eingebetteter Server).
  - Frontend: Unit-Tests (Karma/Jasmine) und E2E-Tests (Cypress, Netzwerk-Stubs; optional gegen laufendes Backend).

## 5. Bausteinsicht
- Backend
  - `TodoController`: REST-Endpunkte unter `/api/todos` (GET, POST, GET/{id}, PUT/{id}, DELETE/{id}).
  - `TodoService`: Geschäftslogik (CRUD), Fehlerbehandlung.
  - `TodoRepository`: `JpaRepository<Todo, Long>`.
  - `Todo` Entity: `id`, `title`, `completed`.
- Frontend (Angular)
  - `TodosPageComponent` (`frontend/src/app/pages/todos-page.component.ts`): Listet Todos, Hinzufügen, Toggle `completed`, Löschen.
  - `TodoService` (`frontend/src/app/todo.service.ts`): Kapselt HTTP-Aufrufe auf `/api/todos`.
  - Routing (`frontend/src/app/app.routes.ts`): Root-Route lädt `TodosPageComponent` lazy.
  - App-Shell (`frontend/src/app/app.component.html`): Minimaler Rahmen (Header, `<router-outlet>`, Footer) mit Tailwind.

## 6. Laufzeitsicht
- Beispielablauf "Create" (Backend):
  1. Client POST `/api/todos` mit `{ "title": "X", "completed": false }`.
  2. Controller validiert (`@Valid`), ruft Service `create`.
  3. Service speichert via Repository in H2, gibt Entity mit `id` zurück.
  4. Controller antwortet `201 Created` inkl. Location-Header.
- Beispielablauf "Create" (Frontend):
  1. Nutzer gibt Titel ein und klickt auf "Hinzufügen".
  2. `TodosPageComponent` ruft `TodoService.create(...)` auf.
  3. `TodoService` POST `/api/todos` (im Dev über Proxy an Backend).
  4. Bei Erfolg wird Liste via `TodoService.list()` neu geladen und angezeigt.

## 7. Verteilungssicht
- Entwicklung:
  - Frontend-Dev-Server auf `http://localhost:4200` (Angular CLI).
  - Backend auf `http://localhost:8080` (Spring Boot).
  - Proxy: `frontend/proxy.conf.json` leitet `/api` an 8080 weiter.
- Produktion (vereinfachte Annahme):
  - Frontend als statisches Build-Artefakt (ng build) auf einem Webserver oder CDN.
  - Backend als eigenständiger Spring-Boot-Dienst.

## 8. Qualitätsszenarien
- Funktional: Vollständiges CRUD über REST mit UI-Bedienung.
- Testbarkeit: 
  - Frontend: Unit-Tests (Komponenten/Services) und E2E-Tests (Cypress, Network-Stubs, optional gegen Live-Backend).
  - Backend: Unit-/Integrations-/E2E-Tests (eingebetteter Server).
- Wartbarkeit: Klare Trennung (Backend/Frontend), sprechende Namen, Standalone Components.
- DX: Sofort lauffähig lokal mit zwei Prozessen + Proxy.

## 9. Risiken & Technische Schulden
- Keine Authentifizierung/Autorisierung (offene API/UI).
- In-Memory-DB (H2) nicht produktionsgeeignet.
- CORS in Produktion: Ohne Proxy muss CORS sauber konfiguriert oder ein API-Gateway/Reverse Proxy genutzt werden.
- Kein SSR/Prerendering (reines SPA); SEO unkritisch für Todo-App.

## 10. Entscheidungen
- Backend: Spring Boot + Spring Data JPA + H2; Validierung via Bean Validation (`@NotBlank` auf `title`).
- Frontend: Angular 18 (Standalone), Tailwind CSS, HttpClient mit Fetch-Backend.
- E2E: Cypress für UI-Tests; Network-Stubbing als Default, optional Live-Backend.
- Dev-Proxy: Angular `proxy.conf.json` für `/api → http://localhost:8080`.

## 11. Umsetzung & Betrieb (kurz)
- Start Backend: `mvn spring-boot:run`.
- Start Frontend: `cd frontend && npm start` (läuft auf Port 4200, Proxy aktiv).
- Tests Frontend: `npm test` (Unit), `npm run e2e` (Cypress headless) im Ordner `frontend`.

## 12. Glossar
- CRUD: Create, Read, Update, Delete.
- E2E-Test: Ende-zu-Ende Test über UI/HTTP.
- SPA: Single-Page Application.
- Proxy (Dev): Weiterleitung von Frontend-Requests an das Backend ohne CORS-Probleme.
