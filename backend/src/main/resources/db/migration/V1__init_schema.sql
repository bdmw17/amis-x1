-- AMIS – Initiales Datenbankschema
-- Basiert auf Anforderungskatalog a.csv

CREATE TABLE IF NOT EXISTS bewohner (
    id              BIGSERIAL PRIMARY KEY,
    az_nummer       VARCHAR(50) UNIQUE NOT NULL,
    vorname         VARCHAR(100) NOT NULL,
    nachname        VARCHAR(100) NOT NULL,
    geburtsdatum    DATE,
    geburtsort      VARCHAR(150),
    staatsangehoerigkeit VARCHAR(3),
    einreisedatum   DATE,
    status          VARCHAR(50) NOT NULL DEFAULT 'AKTIV',
    version         BIGINT NOT NULL DEFAULT 0,
    erstellt_am     TIMESTAMP NOT NULL DEFAULT NOW(),
    geaendert_am    TIMESTAMP NOT NULL DEFAULT NOW(),
    erstellt_von    VARCHAR(100),
    geaendert_von   VARCHAR(100)
);

CREATE TABLE IF NOT EXISTS unterkunft (
    id          BIGSERIAL PRIMARY KEY,
    name        VARCHAR(200) NOT NULL,
    ort         VARCHAR(200),
    kapazitaet  INT,
    version     BIGINT NOT NULL DEFAULT 0
);

CREATE TABLE IF NOT EXISTS zimmer (
    id              BIGSERIAL PRIMARY KEY,
    unterkunft_id   BIGINT NOT NULL REFERENCES unterkunft(id),
    bezeichnung     VARCHAR(50) NOT NULL,
    belegbar        BOOLEAN NOT NULL DEFAULT TRUE,
    kapazitaet      INT NOT NULL DEFAULT 1
);

CREATE TABLE IF NOT EXISTS belegung (
    id              BIGSERIAL PRIMARY KEY,
    bewohner_id     BIGINT NOT NULL REFERENCES bewohner(id),
    zimmer_id       BIGINT NOT NULL REFERENCES zimmer(id),
    einzug_datum    DATE NOT NULL,
    auszug_datum    DATE,
    version         BIGINT NOT NULL DEFAULT 0
);

CREATE TABLE IF NOT EXISTS termin (
    id              BIGSERIAL PRIMARY KEY,
    bewohner_id     BIGINT REFERENCES bewohner(id),
    art             VARCHAR(100) NOT NULL,
    betreff         VARCHAR(300),
    termin_datum    TIMESTAMP NOT NULL,
    ort             VARCHAR(200),
    erledigt        BOOLEAN NOT NULL DEFAULT FALSE,
    erstellt_am     TIMESTAMP NOT NULL DEFAULT NOW()
);

CREATE TABLE IF NOT EXISTS dokument (
    id              BIGSERIAL PRIMARY KEY,
    bewohner_id     BIGINT NOT NULL REFERENCES bewohner(id),
    art             VARCHAR(100) NOT NULL,
    freitext        TEXT,
    erstellt_am     TIMESTAMP NOT NULL DEFAULT NOW(),
    erstellt_von    VARCHAR(100)
);

CREATE TABLE IF NOT EXISTS benutzer (
    id          BIGSERIAL PRIMARY KEY,
    benutzername VARCHAR(100) UNIQUE NOT NULL,
    passwort_hash VARCHAR(255) NOT NULL,
    vorname     VARCHAR(100),
    nachname    VARCHAR(100),
    rolle       VARCHAR(50) NOT NULL DEFAULT 'SACHBEARBEITER',
    aktiv       BOOLEAN NOT NULL DEFAULT TRUE
);

-- Indizes
CREATE INDEX idx_bewohner_az ON bewohner(az_nummer);
CREATE INDEX idx_belegung_bewohner ON belegung(bewohner_id);
CREATE INDEX idx_belegung_zimmer ON belegung(zimmer_id);
CREATE INDEX idx_termin_datum ON termin(termin_datum);
