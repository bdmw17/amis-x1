-- V3: Rollen- & Rechteverwaltung (AP-02)
-- Anforderungen: A-SYS-RR-01, A-SYS-RR-02, A-SYS-RR-03, A-SYS-RR-05

-- AfA (Aufnahmeeinrichtungen für Asylbegehren)
CREATE TABLE afa (
    id              BIGSERIAL PRIMARY KEY,
    kuerzel         VARCHAR(20)  UNIQUE NOT NULL,
    name            VARCHAR(200) NOT NULL,
    ort             VARCHAR(200),
    aktiv           BOOLEAN      NOT NULL DEFAULT TRUE,
    version         BIGINT       NOT NULL DEFAULT 0,
    erstellt_am     TIMESTAMP    NOT NULL DEFAULT NOW(),
    geaendert_am    TIMESTAMP    NOT NULL DEFAULT NOW(),
    erstellt_von    VARCHAR(100),
    geaendert_von   VARCHAR(100)
);

-- Rollen
CREATE TABLE rolle (
    id              BIGSERIAL PRIMARY KEY,
    name            VARCHAR(100) UNIQUE NOT NULL,
    beschreibung    TEXT,
    version         BIGINT    NOT NULL DEFAULT 0,
    erstellt_am     TIMESTAMP NOT NULL DEFAULT NOW(),
    geaendert_am    TIMESTAMP NOT NULL DEFAULT NOW(),
    erstellt_von    VARCHAR(100),
    geaendert_von   VARCHAR(100)
);

-- Modul-Berechtigungen pro Rolle  (LESEN | SCHREIBEN | ADMINISTRIEREN)
CREATE TABLE modul_berechtigung (
    id              BIGSERIAL PRIMARY KEY,
    rolle_id        BIGINT      NOT NULL REFERENCES rolle(id) ON DELETE CASCADE,
    modul           VARCHAR(50) NOT NULL,
    berechtigung    VARCHAR(20) NOT NULL,
    UNIQUE (rolle_id, modul, berechtigung)
);

-- Benutzer ↔ Rollen  (n:m)
CREATE TABLE benutzer_rolle (
    benutzer_id BIGINT NOT NULL REFERENCES benutzer(id) ON DELETE CASCADE,
    rolle_id    BIGINT NOT NULL REFERENCES rolle(id)    ON DELETE CASCADE,
    PRIMARY KEY (benutzer_id, rolle_id)
);

-- AfA-Zugehörigkeit zum Benutzer
ALTER TABLE benutzer
    ADD COLUMN IF NOT EXISTS afa_id       BIGINT REFERENCES afa(id),
    ADD COLUMN IF NOT EXISTS version      BIGINT    NOT NULL DEFAULT 0,
    ADD COLUMN IF NOT EXISTS erstellt_am  TIMESTAMP NOT NULL DEFAULT NOW(),
    ADD COLUMN IF NOT EXISTS geaendert_am TIMESTAMP NOT NULL DEFAULT NOW(),
    ADD COLUMN IF NOT EXISTS erstellt_von  VARCHAR(100),
    ADD COLUMN IF NOT EXISTS geaendert_von VARCHAR(100);

-- ─────────────────────────── Seed-Daten ───────────────────────────

-- AfAs
INSERT INTO afa (kuerzel, name, ort) VALUES
    ('AfA-TRI', 'Aufnahmeeinrichtung Trier',              'Trier'),
    ('AfA-LAN', 'Aufnahmeeinrichtung Landau',             'Landau'),
    ('AfA-IDA', 'Aufnahmeeinrichtung Idar-Oberstein',     'Idar-Oberstein'),
    ('ADD',     'Aufsichts- und Dienstleistungsdirektion','Trier'),
    ('ZRF',     'Zentrale Rückführungsstelle',             'Trier'),
    ('MFFKI',   'Ministerium für Familie, Frauen, Kultur und Integration', 'Mainz');

-- Rollen
INSERT INTO rolle (name, beschreibung) VALUES
    ('ADMIN',          'Systemadministrator – hat alle Rechte'),
    ('SACHBEARBEITER', 'Standardzugang für Sachbearbeitung in einer AfA'),
    ('SOZIALARBEITER', 'Lesend + eingeschränkt schreibend (soziale Module)'),
    ('SICHERHEIT',     'Zugang für Bewachung und Pfortenbereich'),
    ('STATISTIK',      'Nur lesender Zugriff auf Statistik und Reporting');

-- ADMIN: alle Module × alle Berechtigungen
INSERT INTO modul_berechtigung (rolle_id, modul, berechtigung)
SELECT r.id, m.modul, b.berechtigung
FROM   rolle r,
       (VALUES ('BEWOHNER'),('AZR'),('SONDERSTATUS'),('FREITEXT'),('TERMINE'),
               ('FORMULARE'),('AUSWEIS'),('LIEGENSCHAFTEN'),('BELEGUNG'),
               ('ANWESENHEIT'),('VERLEGUNG'),('VERTEILUNG'),('BEWACHUNG'),
               ('KRANKENSTATION'),('SACHMITTEL'),('SOZIALLEISTUNGEN'),
               ('STATISTIKEN'),('ADMINISTRATION')) AS m(modul),
       (VALUES ('LESEN'),('SCHREIBEN'),('ADMINISTRIEREN'))              AS b(berechtigung)
WHERE  r.name = 'ADMIN';

-- SACHBEARBEITER
INSERT INTO modul_berechtigung (rolle_id, modul, berechtigung)
SELECT r.id, m.modul, b.berechtigung
FROM   rolle r,
       (VALUES ('BEWOHNER'),('TERMINE'),('FREITEXT'),('FORMULARE'),
               ('AUSWEIS'),('BELEGUNG'),('ANWESENHEIT')) AS m(modul),
       (VALUES ('LESEN'),('SCHREIBEN'))                  AS b(berechtigung)
WHERE  r.name = 'SACHBEARBEITER'
UNION ALL
SELECT r.id, 'SONDERSTATUS', 'LESEN'   FROM rolle r WHERE r.name = 'SACHBEARBEITER'
UNION ALL
SELECT r.id, 'STATISTIKEN',  'LESEN'   FROM rolle r WHERE r.name = 'SACHBEARBEITER';

-- SOZIALARBEITER
INSERT INTO modul_berechtigung (rolle_id, modul, berechtigung)
SELECT r.id, m.modul, b.berechtigung
FROM   rolle r,
       (VALUES ('FREITEXT'),('TERMINE')) AS m(modul),
       (VALUES ('LESEN'),('SCHREIBEN'))  AS b(berechtigung)
WHERE  r.name = 'SOZIALARBEITER'
UNION ALL
SELECT r.id, 'BEWOHNER',    'LESEN' FROM rolle r WHERE r.name = 'SOZIALARBEITER'
UNION ALL
SELECT r.id, 'SONDERSTATUS','LESEN' FROM rolle r WHERE r.name = 'SOZIALARBEITER';

-- SICHERHEIT
INSERT INTO modul_berechtigung (rolle_id, modul, berechtigung)
SELECT r.id, m.modul, b.berechtigung
FROM   rolle r,
       (VALUES ('BEWACHUNG'),('ANWESENHEIT')) AS m(modul),
       (VALUES ('LESEN'),('SCHREIBEN'))       AS b(berechtigung)
WHERE  r.name = 'SICHERHEIT'
UNION ALL
SELECT r.id, 'BEWOHNER', 'LESEN' FROM rolle r WHERE r.name = 'SICHERHEIT';

-- STATISTIK
INSERT INTO modul_berechtigung (rolle_id, modul, berechtigung)
SELECT r.id, 'STATISTIKEN', 'LESEN' FROM rolle r WHERE r.name = 'STATISTIK';
