# AMIS – Implementierungsprotokoll

> Projekt: Asylverwaltungsprogramm (AMIS)  
> Stack: Vue 3 + TypeScript + Vite · Spring Boot 3.4.5 (Java 21) · PostgreSQL 16 · Flyway 10  
> Stand: Mai 2026

---

## Projektstruktur

```
last/
├── frontend/                              # Vue 3 SPA
│   ├── src/
│   │   ├── api/
│   │   │   └── client.ts                 # Axios-Client (Auth, Interceptoren)
│   │   ├── components/
│   │   │   └── ToastContainer.vue        # Globale Toast-Benachrichtigungen
│   │   ├── router/
│   │   │   └── index.ts                  # Vue Router (alle Routen)
│   │   ├── stores/
│   │   │   └── toast.ts                  # Pinia-Store für Toasts
│   │   ├── views/
│   │   │   ├── AdminBenutzerView.vue     # AP-02: Benutzerverwaltung
│   │   │   ├── AdminRollenView.vue       # AP-02: Rollenverwaltung
│   │   │   ├── BewohnerListeView.vue     # Stub – AP-04, AP-07
│   │   │   ├── BewohnerDetailView.vue    # Stub – AP-04, AP-06, AP-08
│   │   │   ├── TermineView.vue           # Stub – AP-09
│   │   │   └── LiegenschaftView.vue      # Stub – AP-12, AP-13
│   │   ├── App.vue                       # App-Shell (Topbar, Sidebar, RouterView)
│   │   ├── main.ts                       # Pinia + Router + mount
│   │   └── style.css                     # Globale Styles / Design-System
│   └── .env.local                        # API-URL & Credentials (nicht einchecken)
│
├── backend/                              # Spring Boot (Maven)
│   └── src/main/
│       ├── java/de/amis/backend/
│       │   ├── config/
│       │   │   ├── SecurityConfig.java   # CORS, HTTP-Basic, Stateless, @EnableMethodSecurity
│       │   │   ├── JpaConfig.java        # JPA-Auditing (Benutzername aus SecurityContext)
│       │   │   └── DataInitializer.java  # AP-02: Admin-Initialbenutzer
│       │   ├── controller/
│       │   │   ├── ApiInfoController.java # GET /api/v1 → Version + Status
│       │   │   ├── AfAController.java    # AP-02: GET /api/v1/afa
│       │   │   ├── BenutzerController.java # AP-02: /api/v1/admin/benutzer
│       │   │   ├── MeController.java     # AP-02: GET /api/v1/me
│       │   │   └── RolleController.java  # AP-02: /api/v1/admin/rollen
│       │   ├── dto/
│       │   │   ├── AfADto.java / BenutzerRequest.java / BenutzerResponse.java  # AP-02
│       │   │   ├── ModulBerechtigungDto.java / RolleDto.java                   # AP-02
│       │   ├── exception/
│       │   │   ├── GlobalExceptionHandler.java  # RFC-9457 ProblemDetail für alle Fehler
│       │   │   └── ResourceNotFoundException.java
│       │   ├── model/
│       │   │   ├── BaseEntity.java       # @Version, @CreatedBy/Date, @LastModifiedBy/Date
│       │   │   ├── AfA.java / Benutzer.java / Rolle.java / ModulBerechtigung.java  # AP-02
│       │   │   └── ModulName.java / BerechtigungsTyp.java  # AP-02: Enums
│       │   ├── repository/
│       │   │   ├── AfARepository.java / BenutzerRepository.java / RolleRepository.java  # AP-02
│       │   └── service/
│       │       ├── BenutzerDetailsService.java  # AP-02: UserDetailsService
│       │       ├── BenutzerService.java / RolleService.java  # AP-02
│       └── resources/
│           ├── application.properties    # DB, Flyway, Security, Actuator
│           └── db/migration/
│               ├── V1__init_schema.sql   # Initiales Datenbankschema
│               ├── V2__add_audit_columns.sql  # Audit-Spalten für alle Tabellen
│               └── V3__rollen_rechteverwaltung.sql  # AP-02: AfA, Rolle, Berechtigungen
│
├── docker-compose.yml                    # PostgreSQL 16 auf Port 5432
└── docs/
    ├── a.csv                             # Anforderungskatalog (Quelle)
    ├── implementierungsplan.md           # Arbeitspakete mit (D)-Markierungen
    └── implementation-protocol.md       # dieses Dokument
```

---

## Implementierte Arbeitspakete

### AP-01 – Systeminfrastruktur & Basisarchitektur ✅

**Anforderungen:** A-SYS-AL-03 (D), A-SYS-AL-04 (D), A-SYS-AL-05 (D), A-SYS-AL-06, A-SYS-NF-02 (D), A-SYS-NF-03 (D), A-SYS-NF-04 (D)

#### Akzeptanzkriterien – Status

| Kriterium | Status | Umsetzung |
|-----------|--------|-----------|
| Browserbasiert | ✅ | Vue 3 SPA |
| Kein Lost-Update bei parallelem Zugriff | ✅ | `@Version` in `BaseEntity`, 409-Handler + Toast |
| Responsives UI | ✅ | CSS-Grid App-Shell, Sidebar klappt bei < 768 px ein |
| Mehrere Module in verschiedenen Tabs nutzbar | ✅ | Vue Router (stateless, Hash-freie History) |
| Detailansicht lässt übergeordnete Ansicht offen | ✅ | Router-Navigation (`/bewohner/:id`) |
| Neue Module ohne Architekturänderung integrierbar | ✅ | Routen-Eintrag in `router/index.ts` + neue View-Datei genügt |

#### Backend-Komponenten

**`model/BaseEntity.java`**
- `@MappedSuperclass` – gemeinsame Basisklasse für alle JPA-Entitäten
- `@Version Long version` – Optimistic Locking (JPA-Standard); bei Konflikt wirft Hibernate `OptimisticLockException`
- `@CreatedDate Instant erstelltAm` / `@LastModifiedDate Instant geaendertAm` – automatisch per JPA-Auditing
- `@CreatedBy String erstelltVon` / `@LastModifiedBy String geaendertVon` – Benutzername aus `SecurityContextHolder`

**`config/JpaConfig.java`**
- `@EnableJpaAuditing` aktiviert die oben genannten Audit-Annotationen
- `AuditorAware<String>` Bean liest `Authentication.getName()` aus dem Security-Kontext

**`config/SecurityConfig.java`**
- HTTP-Basic-Auth, Session-Policy `STATELESS`
- CORS für `http://localhost:5173` auf `/api/**`
- Öffentlich: `/actuator/health`, `/actuator/info`, `/api/v1`

**`exception/GlobalExceptionHandler.java`**  
Zentrale Fehlerbehandlung per `@RestControllerAdvice` mit RFC 9457 `ProblemDetail`:

| Exception | HTTP-Status | Bedeutung |
|-----------|-------------|-----------|
| `OptimisticLockException` / `OptimisticLockingFailureException` | 409 Conflict | Datensatz zwischenzeitlich geändert |
| `MethodArgumentNotValidException` | 422 Unprocessable Entity | `@Valid`-Fehler an Request-Body |
| `ConstraintViolationException` | 422 Unprocessable Entity | `@Validated`-Fehler an Methodenparameter |
| `ResourceNotFoundException` | 404 Not Found | Ressource nicht vorhanden |
| `AccessDeniedException` | 403 Forbidden | Fehlende Berechtigung |
| `Exception` (Catch-All) | 500 Internal Server Error | Unerwarteter Fehler |

**`controller/ApiInfoController.java`**
- `GET /api/v1` → `{ application, apiVersion, status }`
- Erlaubt dem Frontend, die Backend-Erreichbarkeit und API-Version zu prüfen

**`db/migration/V1__init_schema.sql`**  
Tabellen: `bewohner`, `unterkunft`, `zimmer`, `belegung`, `termin`, `dokument`, `benutzer`  
Alle mit `version BIGINT` für Optimistic Locking und Audit-Feldern.

**`db/migration/V2__add_audit_columns.sql`**  
Ergänzt `erstellt_am`, `geaendert_am`, `erstellt_von`, `geaendert_von`, `version` bei allen Tabellen, die in V1 noch unvollständig waren.

**`application.properties`**

```properties
spring.datasource.url=jdbc:postgresql://localhost:5432/amis
spring.datasource.username=amis
spring.datasource.password=amis
spring.jpa.hibernate.ddl-auto=validate
spring.flyway.enabled=true
spring.flyway.locations=classpath:db/migration
server.port=8080
```

---

#### Frontend-Komponenten

**`style.css`** – globales Design-System
- CSS-Grid App-Shell: `grid-template-areas: "topbar topbar" / "sidebar main"`
- Breakpoint 768 px: Sidebar wird ausgeblendet
- Utility-Klassen: `.card`, `.btn`, `.btn-primary`, `.btn-danger`, `.btn-ghost`, `.form-group`, `.toast`, `.toast-error / -warning / -success`

**`App.vue`** – App-Shell
```
<header class="topbar">   AMIS · Asylverwaltungsprogramm
<aside  class="sidebar">  Nav-Links (Bewohner / Termine / Liegenschaft)
<main   class="main-content">  <RouterView />
<ToastContainer />        globale Benachrichtigungen
```

**`router/index.ts`** – Routen

| Pfad | Komponente | Beschreibung |
|------|-----------|--------------|
| `/` | Redirect → `/bewohner` | |
| `/bewohner` | `BewohnerListeView` | Listenansicht (AP-04, AP-07) |
| `/bewohner/:id` | `BewohnerDetailView` | Detailansicht (AP-04, AP-06, AP-08) |
| `/termine` | `TermineView` | Terminverwaltung (AP-09) |
| `/liegenschaft` | `LiegenschaftView` | Liegenschaft/Belegung (AP-12, AP-13) |

**`stores/toast.ts`** – Pinia Toast-Store
- `show(message, type, durationMs)` – zeigt Toast für `durationMs` ms
- `remove(id)` – entfernt einzelnen Toast
- Typen: `'success'` | `'warning'` | `'error'`

**`api/client.ts`** – Axios-Client

| HTTP-Status | Verhalten |
|-------------|-----------|
| 409 Conflict | Warning-Toast mit `detail`-Feld aus ProblemDetail (Optimistic Lock) |
| 401 Unauthorized | Error-Toast „Sitzung abgelaufen" |
| 403 Forbidden | Error-Toast „Keine Berechtigung" |
| ≥ 500 | Error-Toast „Serverfehler" |

---

## Entwicklungsstart

```bash
# 1. Datenbank
docker compose up -d

# 2. Backend  (Flyway läuft automatisch beim Start)
cd backend
./mvnw spring-boot:run

# 3. Frontend
cd frontend
npm run dev
# → http://localhost:5173
```

---

---

### AP-02 – Rollen- & Rechteverwaltung ✅

**Anforderungen:** A-SYS-RR-01, A-SYS-RR-02, A-SYS-RR-03, A-SYS-RR-05

#### Akzeptanzkriterien – Status

| Kriterium | Status | Umsetzung |
|-----------|--------|-----------|
| Benutzerverwaltung (anlegen, bearbeiten, deaktivieren) | ✅ | `BenutzerController` + `AdminBenutzerView.vue` |
| Rollenverwaltung (CRUD + Berechtigungsmatrix) | ✅ | `RolleController` + `AdminRollenView.vue` |
| Modul-/Funktionsberechtigungen (LESEN, SCHREIBEN, ADMINISTRIEREN) | ✅ | `ModulBerechtigung`-Entität, 18 Module × 3 Typen |
| Authentifizierung aus Datenbank | ✅ | `BenutzerDetailsService` (UserDetailsService) |
| BCrypt-Passwortspeicherung | ✅ | `BCryptPasswordEncoder(12)` in `SecurityConfig` |
| Method-Level-Security per Berechtigung | ✅ | `@EnableMethodSecurity` + `@PreAuthorize("hasAuthority('MODUL:TYP')")` |
| AfA-Zugehörigkeit pro Benutzer | ✅ | `AfA`-Entität + FK `benutzer.afa_id` |
| Admin-Initialbenutzer beim ersten Start | ✅ | `DataInitializer` erzeugt `admin/changeme` mit ADMIN-Rolle |

#### Datenbankschema – `V3__rollen_rechteverwaltung.sql`

| Tabelle | Inhalt |
|---------|--------|
| `afa` | Aufnahmeeinrichtungen (6 Seed-Einträge: AfA-TRI, AfA-LAN, AfA-IDA, ADD, ZRF, MFFKI) |
| `rolle` | Rollen (5 Einträge: ADMIN, SACHBEARBEITER, SOZIALARBEITER, SICHERHEIT, STATISTIK) |
| `modul_berechtigung` | n:m Rolle ↔ Modul × Berechtigungstyp (ADMIN erhält alle 54 Kombinationen) |
| `benutzer_rolle` | n:m Benutzer ↔ Rolle |
| `benutzer` | ALTER: +`afa_id`, +Audit-Spalten (`version`, `erstellt_am`, etc.) |

#### Backend-Komponenten

**`model/AfA.java`** – Entität für Aufnahmeeinrichtungen (extends BaseEntity)

**`model/Rolle.java`** – Entität mit `@OneToMany(fetch=EAGER, cascade=ALL)` auf `ModulBerechtigung`

**`model/ModulBerechtigung.java`** – Entität `(rolle_id, modul, berechtigung)` mit UNIQUE-Constraint

**`model/Benutzer.java`** – Entität mit `@ManyToMany(fetch=EAGER)` auf `Rolle` via `benutzer_rolle`

**`model/ModulName.java`** – Enum mit 18 Modulnamen (BEWOHNER, AZR, SONDERSTATUS, …, ADMINISTRATION)

**`model/BerechtigungsTyp.java`** – Enum `LESEN | SCHREIBEN | ADMINISTRIEREN`

**`service/BenutzerDetailsService.java`** – `UserDetailsService`-Implementierung
- Lädt Benutzer aus DB per `benutzername`
- Baut `GrantedAuthority`-Liste: `ROLE_{ROLLENNAME}` + `{MODUL}:{BERECHTIGUNGSTYP}`
- Kein Caching → Berechtigungsänderungen wirken sofort

**`service/BenutzerService.java`** – CRUD inkl. Rollenzuweisung, BCrypt-Passwort-Hashing

**`service/RolleService.java`** – CRUD inkl. Berechtigungsmatrix-Update (orphanRemoval)

**`config/DataInitializer.java`** – `CommandLineRunner`, legt `admin/changeme` mit ADMIN-Rolle beim ersten Start an

**`config/SecurityConfig.java`** (Änderungen zu AP-01)
- `@EnableMethodSecurity` hinzugefügt
- `BCryptPasswordEncoder(12)` Bean
- `DaoAuthenticationProvider` Bean → `BenutzerDetailsService`
- In-Memory-User entfernt

#### REST-Endpunkte

| Methode | Pfad | Berechtigung | Beschreibung |
|---------|------|-------------|--------------|
| GET | `/api/v1/me` | authentifiziert | Eigener Benutzer + Authorities |
| GET | `/api/v1/afa` | authentifiziert | Aktive AfAs |
| GET/POST | `/api/v1/admin/rollen` | `ADMINISTRATION:ADMINISTRIEREN` | Rollenliste / Rolle anlegen |
| GET/PUT/DELETE | `/api/v1/admin/rollen/{id}` | `ADMINISTRATION:ADMINISTRIEREN` | Rolle lesen/aktualisieren/löschen |
| GET/POST | `/api/v1/admin/benutzer` | `ADMINISTRATION:ADMINISTRIEREN` | Benutzerliste / Benutzer anlegen |
| GET/PUT/DELETE | `/api/v1/admin/benutzer/{id}` | `ADMINISTRATION:ADMINISTRIEREN` | Benutzer lesen/aktualisieren/löschen |

#### Frontend-Komponenten

**`views/AdminBenutzerView.vue`** – Benutzerverwaltung
- Tabelle aller Benutzer mit Inline-Aktiv/Inaktiv-Anzeige
- Modal-Formular: Benutzername, Passwort, Vor-/Nachname, AfA-Auswahl, Rollen-Checkboxen
- Anlegen / Bearbeiten / Löschen

**`views/AdminRollenView.vue`** – Rollenverwaltung
- Checkbox-Matrix: 18 Module × 3 Berechtigungstypen
- Rolle anlegen, Berechtigungen direkt in der Tabelle setzen, Speichern

**`router/index.ts`** – neue Routen

| Pfad | Komponente |
|------|-----------|
| `/admin/benutzer` | `AdminBenutzerView` |
| `/admin/rollen` | `AdminRollenView` |

**`App.vue`** – Sidebar ergänzt um „Benutzer" und „Rollen" (mit `<hr>`-Trenner)

#### Technische Besonderheit – Flyway-Fix
Die ursprüngliche `pom.xml` enthielt keine Flyway-Dependency; `spring.flyway.enabled=true` in `application.properties` hatte daher keine Wirkung. Behoben durch Hinzufügen von:
```xml
<dependency>
    <groupId>org.flywaydb</groupId>
    <artifactId>flyway-core</artifactId>
</dependency>
<dependency>
    <groupId>org.flywaydb</groupId>
    <artifactId>flyway-database-postgresql</artifactId>
</dependency>
```
In Spring Boot 3.3+ ist `flyway-database-postgresql` für PostgreSQL-Support Pflicht.

---

## Offene Arbeitspakete

| AP | Titel | Abhängigkeit | Schätzung |
|----|-------|-------------|-----------|
| ~~**AP-02**~~ | ~~Rollen- & Rechteverwaltung~~ | ~~AP-01 ✅~~ | ~~23–35 PT~~ ✅ |
| **AP-03** | Historisierung & Audit-Trail | AP-01 ✅, AP-02 | 6–10 PT |
| **AP-04** | Bewohner-Stammdaten & Registrierung | AP-01 ✅ | — |
| **AP-05** | AZR-Schnittstelle | AP-04 | — |
| **AP-06** | Sonderstatus-Management | AP-04 | — |
| **AP-07** | Bewohner-Übersichten & Detailansichten | AP-04 | — |
| **AP-08** | Freitext-Dokumentation | AP-04 | — |
| **AP-09** | Terminverwaltung | AP-04 | — |
| **AP-10** | Vordrucke & Formulare | AP-04 | — |
| **AP-11** | Bewohnerausweis | AP-04 | — |
| **AP-12** | Liegenschaftsmanagement | AP-01 ✅ | — |
| **AP-13** | Belegungsplanung | AP-12 | — |
| **AP-14** | Anwesenheitserfassung & Abgängigkeit | AP-04 | — |
| **AP-15** | Verlegungsplanung | AP-12, AP-13 | — |
| **AP-16** | Landesinterne Verteilung | AP-15 | — |
| **AP-17** | Bewachung & Sicherheit (Pforte) | AP-01 ✅ | — |
| **AP-18** | Krankenstation & Medizin | AP-04 | — |
| **AP-19** | Sachmittel- & Leistungsverwaltung | AP-04 | — |
| **AP-20** | Soziale Leistungen & Kassenführung | AP-04 | — |
| **AP-21** | Statistiken & Reporting | AP-04, AP-12 | — |

---

## Technische Entscheidungen

| Thema | Entscheidung | Begründung |
|-------|-------------|------------|
| Optimistic Locking | JPA `@Version` + HTTP 409 | Standard-Mechanismus; kein zusätzliches Framework nötig |
| Fehlerformat | RFC 9457 ProblemDetail | Spring 6 built-in; strukturiert, interoperabel |
| Auth (initial) | HTTP Basic (stateless) | Einfachste sichere Option; JWT-Upgrade in AP-02 |
| DB-Migrationen | Flyway | Reproduzierbare Schema-Evolution; Versionskontrolle |
| State Management | Pinia | Vue-3-nativer Standard; besser typisiert als Vuex |
| CSS-Ansatz | Vanilla CSS (Custom Properties + Grid) | Kein Build-Overhead; leicht anpassbar; kein Framework-Lock-in |
