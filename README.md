# AMIS – Asylverwaltungsprogramm

## Stack

| Schicht | Technologie |
|---------|-------------|
| Frontend | Vue 3 + TypeScript + Vite + Pinia + Vue Router |
| Backend | Spring Boot 3.5 (Java 21) + Spring Security + Spring Data JPA |
| Datenbank | PostgreSQL 16 |
| Migrationen | Flyway |
| Container | Docker Compose |

---

## Projektstruktur

```
last/
├── frontend/          # Vue 3 App (Vite)
│   ├── src/
│   │   ├── api/       # Axios-Client
│   │   ├── router/    # Vue Router
│   │   ├── stores/    # Pinia Stores
│   │   └── views/     # Seitenkomponenten
│   └── .env.local     # API-URL & Credentials (lokal)
├── backend/           # Spring Boot App (Maven)
│   ├── src/main/java/de/amis/backend/
│   │   └── config/    # Security & CORS
│   └── src/main/resources/
│       ├── application.properties
│       └── db/migration/  # Flyway SQL-Skripte
├── docker-compose.yml # PostgreSQL-Dienst
└── docs/
    ├── a.csv                  # Anforderungskatalog
    └── implementierungsplan.md
```

---

## Entwicklungsstart

### 1. Datenbank starten

```bash
docker compose up -d
```

Postgres läuft dann auf `localhost:5432`, Datenbank `amis`, User `amis`, Passwort `amis`.

### 2. Backend starten

```bash
cd backend
./mvnw spring-boot:run
```

Backend läuft auf `http://localhost:8080`.  
Flyway führt beim ersten Start automatisch `V1__init_schema.sql` aus.

### 3. Frontend starten

```bash
cd frontend
npm run dev
```

Frontend läuft auf `http://localhost:5173`.

---

## Umgebungsvariablen (Frontend)

Datei: `frontend/.env.local`

| Variable | Beschreibung | Standard |
|----------|-------------|---------|
| `VITE_API_BASE_URL` | Backend-URL | `http://localhost:8080/api` |
| `VITE_API_USER` | HTTP-Basic-Benutzername | `admin` |
| `VITE_API_PASS` | HTTP-Basic-Passwort | `changeme` |

> **Hinweis:** `.env.local` ist in `.gitignore` – Passwörter nicht einchecken.

---

## Datenbank-Migrationen

Neue Migrationen als `V{n}__{beschreibung}.sql` in  
`backend/src/main/resources/db/migration/` ablegen.  
Flyway führt fehlende Migrationen automatisch beim Serverstart aus.

---

## Nächste Schritte (Arbeitspakete)

Siehe [docs/implementierungsplan.md](docs/implementierungsplan.md).  
Empfohlene Reihenfolge: AP-02 → AP-04 → AP-07 → AP-09 → AP-12 → ...
