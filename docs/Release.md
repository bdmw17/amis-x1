# AMIS – Releaseplan

> Basis: `implementierungsplan.md` und Anforderungskatalog `a.csv`  
> Stand: Mai 2026

---

## Legende

| Symbol | Bedeutung |
|--------|-----------|
| ✅ | Prozess in diesem Release vollständig abgeschlossen |
| ⚠️ | Abgeschlossen mit dokumentierten offenen Punkten (`implementation-protocol.md`) |
| 🔶 | Prozess beginnt in diesem Release, Abschluss folgt in späterem Release |
| ➕ | Prozess wird in diesem Release abgeschlossen (begann in früherem Release) |
| G-… | Generische (querschnittliche) Anforderungsgruppe (gilt systemweit) |
| P-… | Prozessspezifische Anforderungsgruppe |

---

## R1 – Fundament

**Arbeitspakete:** AP-01, AP-02, AP-03  
**Schätzung:** 39–65 PT  
**Voraussetzung:** –  
**Liefert:** Technische Basis (Vue 3, Spring Boot, PostgreSQL), vollständiges Rollen- und Rechtemodell, Historisierungs-Infrastruktur

### Enthaltene Arbeitspakete

| AP | Titel | Abgedeckte Anforderungen (Prozess → Nummern) |
|----|-------|----------------------------------------------|
| AP-01 | Systeminfrastruktur & Basisarchitektur | G-SYS-AL → A-SYS-AL-03, A-SYS-AL-04, A-SYS-AL-05, A-SYS-AL-06 · G-SYS-NF → A-SYS-NF-02, A-SYS-NF-03, A-SYS-NF-04 |
| AP-02 | Rollen- & Rechteverwaltung | G-SYS-RR → A-SYS-RR-01, A-SYS-RR-02, A-SYS-RR-03, A-SYS-RR-05 |
| AP-03 | Historisierung & Audit-Trail | G-SYS-HT → A-SYS-HT-01 |

### Prozessbeteiligung

| Prozess | Bezeichnung | Status in R1 |
|---------|-------------|--------------|
| G-SYS-AL | Allgemeine Systemarchitektur & Technologiebasis | ✅ Abgeschlossen |
| G-SYS-NF | Nichtfunktionale Anforderungen / UI | ⚠️ Basis abgeschlossen; NF-04 (übergeordnete Ansicht offen beim Öffnen von Anhängen) → folgt in R2 (AP-08) |
| G-SYS-RR | Rollen- und Rechteverwaltung | ⚠️ Basis abgeschlossen; RR-02 (AfA-Filterung in Abfragen) → folgt in R2 (AP-04) |
| G-SYS-HT | Historisierung & Audit-Trail | ✅ Abgeschlossen |

**PT-Schätzung:** min 39 PT · max 65 PT

---

## R2 – Stammdaten

**Arbeitspakete:** AP-04, AP-07, AP-08  
**Schätzung:** 54–87 PT  
**Voraussetzung:** R1  
**Liefert:** Vollständiger Aufnahmeprozess (Vorregistrierung & Registrierung), Bewohner-Übersichten und Detailansichten, Freitext-Dokumentation mit Anhängen

### Enthaltene Arbeitspakete

| AP | Titel | Abgedeckte Anforderungen (Prozess → Nummern) |
|----|-------|----------------------------------------------|
| AP-04 | Bewohner-Stammdaten & Registrierungsprozess | P-AU-VR → A-AU-VR-01, A-AU-VR-02, A-AU-VR-03 · P-AU-RE → A-AU-RE-01, A-AU-RE-05, A-AU-RE-06, A-AU-RE-11, A-AU-RE-14, A-AU-RE-15, A-AU-RE-16, A-AU-RE-17, A-AU-RE-18, A-AU-RE-19 |
| AP-07 | Bewohner-Übersichten & Detailansichten | G-SYS-BÜ → A-SYS-BÜ-01, A-SYS-BÜ-02, A-SYS-BÜ-03, A-SYS-BÜ-04, A-SYS-BÜ-05, A-SYS-BÜ-06, A-SYS-BÜ-08, A-SYS-BÜ-11, A-SYS-BÜ-12 · G-SYS-BD → A-SYS-BD-01, A-SYS-BD-02 |
| AP-08 | Freitext-Dokumentation | G-SYS-FD → A-SYS-FD-01, A-SYS-FD-02, A-SYS-FD-04, A-SYS-FD-05, A-SYS-FD-11, A-SYS-FD-12, A-SYS-FD-15, A-SYS-FD-16 |

### Prozessbeteiligung

| Prozess | Bezeichnung | Status in R2 |
|---------|-------------|--------------|
| P-AU-VR | Vorregistrierung | ✅ Abgeschlossen |
| P-AU-RE | Registrierung | 🔶 Basis abgeschlossen; AZR-Teile (A-AU-RE-02, -03) → folgen in R3 (AP-05) |
| G-SYS-BÜ | Bewohner:innen-Übersichten | ✅ Abgeschlossen |
| G-SYS-BD | Bewohner:innen-Detailansicht | 🔶 Basis abgeschlossen; Verlege-/Verteilungsanzeige in Kurzansicht (A-SYS-BD-01) erst vollständig nach R5/R6 |
| G-SYS-FD | Freitext-Dokumentation | ✅ Abgeschlossen |
| G-SYS-NF | Nichtfunktionale Anforderungen / UI | ➕ NF-04 abgeschlossen (Modal/Drawer-Pattern für Anhänge in AP-08) |
| G-SYS-RR | Rollen- und Rechteverwaltung | ➕ RR-02 abgeschlossen (AfA-Filterung in Bewohner-Endpunkten von AP-04) |

**PT-Schätzung:** min 54 PT · max 87 PT

---

## R3 – Basismodule I

**Arbeitspakete:** AP-05, AP-06, AP-09, AP-10, AP-11  
**Schätzung:** 69–117 PT  
**Voraussetzung:** R2  
**Liefert:** AZR-Schnittstelle, Sonderstatus-Management (vulnerabel, UMA, BAMF-Verfahren, Wohnpflicht, Abgängigkeit), Terminverwaltung, Vordrucke & Formulare, Bewohnerausweis

### Enthaltene Arbeitspakete

| AP | Titel | Abgedeckte Anforderungen (Prozess → Nummern) |
|----|-------|----------------------------------------------|
| AP-05 | AZR-Schnittstelle | P-AU-RE → A-AU-RE-02, A-AU-RE-03, A-AU-RE-06 |
| AP-06 | Sonderstatus-Management | G-AU-SM → A-AU-SM-01, A-AU-SM-02, A-AU-SM-03, A-AU-SM-04, A-AU-SM-05, A-AU-SM-06, A-AU-SM-08, A-AU-SM-10, A-AU-SM-11, A-AU-SM-12, A-AU-SM-15, A-AU-SM-16, A-AU-SM-17, A-AU-SM-18, A-AU-SM-19, A-AU-SM-20 |
| AP-09 | Terminverwaltung | G-SYS-TV → A-SYS-TV-01, A-SYS-TV-02, A-SYS-TV-03, A-SYS-TV-04, A-SYS-TV-05, A-SYS-TV-06, A-SYS-TV-07, A-SYS-TV-09, A-SYS-TV-10, A-SYS-TV-11, A-SYS-TV-12 |
| AP-10 | Vordrucke & Formulare | G-SYS-VD → A-SYS-VD-01, A-SYS-VD-02, A-SYS-VD-03, A-SYS-VD-04, A-SYS-VD-05, A-SYS-VD-06, A-SYS-VD-07 |
| AP-11 | Bewohnerausweis | G-SYS-BA → A-SYS-BA-01, A-SYS-BA-02, A-SYS-BA-03, A-SYS-BA-05, A-SYS-BA-06 |

### Prozessbeteiligung

| Prozess | Bezeichnung | Status in R3 |
|---------|-------------|--------------|
| P-AU-RE | Registrierung (AZR-Anteil) | ➕ Abgeschlossen (AZR-Anbindung via AP-05) |
| P-AU-EF | Ersterfassung (VR + AZR-Check) | ➕ Abgeschlossen |
| G-AU-SM | Sonderstatus-Management | ✅ Abgeschlossen |
| G-SYS-TV | Terminverwaltung | ✅ Abgeschlossen |
| G-SYS-VD | Vordrucke & Dokumente | ✅ Abgeschlossen |
| G-SYS-BA | Bewohner:innen-Ausweis | ✅ Abgeschlossen |

**PT-Schätzung:** min 69 PT · max 117 PT

---

## R4 – Basismodule II

**Arbeitspakete:** AP-12, AP-13, AP-14  
**Schätzung:** 51–80 PT  
**Voraussetzung:** R3  
**Liefert:** Hierarchische Liegenschaftsverwaltung mit interaktivem Lageplan, automatische und manuelle Belegungsplanung mit Inkompatibilitätsprüfung, Anwesenheitserfassung & Rundgang (Desktop und mobil)

### Enthaltene Arbeitspakete

| AP | Titel | Abgedeckte Anforderungen (Prozess → Nummern) |
|----|-------|----------------------------------------------|
| AP-12 | Liegenschaftsmanagement | P-SD-LM → A-SD-LM-01, A-SD-LM-02, A-SD-LM-03, A-SD-LM-04, A-SD-LM-05, A-SD-LM-06, A-SD-LM-07, A-SD-LM-08, A-SD-LM-09, A-SD-LM-10, A-SD-LM-11, A-SD-LM-12, A-SD-LM-13, A-SD-LM-14, A-SD-LM-15, A-SD-LM-16, A-SD-LM-18, A-SD-LM-19, A-SD-LM-20, A-SD-LM-21 |
| AP-13 | Belegungsplanung | P-SD-BP → A-SD-BP-01, A-SD-BP-02, A-SD-BP-03, A-SD-BP-04, A-SD-BP-05, A-SD-BP-06, A-SD-BP-07, A-SD-BP-08, A-SD-BP-09, A-SD-BP-10 |
| AP-14 | Anwesenheitserfassung & Abgängigkeit | P-SD-AE → A-SD-AE-01, A-SD-AE-02, A-SD-AE-03, A-SD-AE-04, A-SD-AE-05, A-SD-AE-09, A-SD-AE-10, A-SD-AE-11, A-SD-AE-12, A-SD-AE-14, A-SD-AE-15, A-SD-AE-16, A-SD-AE-17, A-SD-AE-18, A-SD-AE-19 |

### Prozessbeteiligung

| Prozess | Bezeichnung | Status in R4 |
|---------|-------------|--------------|
| P-SD-LM | Liegenschaftsmanagement | ✅ Abgeschlossen |
| P-SD-BP | Belegungsplanung | ✅ Abgeschlossen |
| P-SD-AE | Anwesenheitserfassung & Abgängigkeit | ✅ Abgeschlossen |

**PT-Schätzung:** min 51 PT · max 80 PT

---

## R5 – Operative Module

**Arbeitspakete:** AP-15, AP-17, AP-18, AP-19  
**Schätzung:** 84–133 PT  
**Voraussetzung:** R4  
**Liefert:** Interne Verlegungsplanung und -durchführung, Pforte & Besuchermanagement, vollständiges medizinisches Modul (Krankenstation, Rezept, Kostenübernahme), Sachmittel- und Leistungsverwaltung

### Enthaltene Arbeitspakete

| AP | Titel | Abgedeckte Anforderungen (Prozess → Nummern) |
|----|-------|----------------------------------------------|
| AP-15 | Verlegungsplanung (intern) | P-AU-VD → A-AU-VD-01, A-AU-VD-02, A-AU-VD-04, A-AU-VD-05 |
| AP-17 | Bewachung & Sicherheit (Pforte & Besucher) | P-BS-KG → A-BS-KG-01, A-BS-KG-02, A-BS-KG-03, A-BS-KG-04, A-BS-KG-05, A-BS-KG-06, A-BS-KG-09, A-BS-KG-10 · P-BS-BE → A-BS-BE-02, A-BS-BE-03, A-BS-BE-04, A-BS-BE-05, A-BS-BE-06, A-BS-BE-08, A-BS-BE-09 · P-BS-BZ → A-BS-BZ-01, A-BS-BZ-02 |
| AP-18 | Krankenstation & Medizinisches Modul | G-KS-AL → A-KS-AL-01, A-KS-AL-02, A-KS-AL-05 · G-KS-SM → A-KS-SM-01, A-KS-SM-03, A-KS-SM-04, A-KS-SM-05, A-KS-SM-06, A-KS-SM-07, A-KS-SM-08, A-KS-SM-09, A-KS-SM-12, A-KS-SM-13, A-KS-SM-18 · P-KS-RE → A-KS-RE-01, A-KS-RE-02, A-KS-RE-03, A-KS-RE-04, A-KS-RE-05, A-KS-RE-06, A-KS-RE-07, A-KS-RE-08, A-KS-RE-09 · P-KS-GA → A-KS-GA-03, A-KS-GA-04 · P-KS-KÜ → A-KS-KÜ-01, A-KS-KÜ-02, A-KS-KÜ-03, A-KS-KÜ-07, A-KS-KÜ-08, A-KS-KÜ-10 |
| AP-19 | Sachmittel- & Leistungsverwaltung | P-SD-SM → A-SD-SM-01, A-SD-SM-02, A-SD-SM-03, A-SD-SM-04, A-SD-SM-06, A-SD-SM-07, A-SD-SM-08, A-SD-SM-09, A-SD-SM-10 · P-SD-EG → A-SD-EG-02, A-SD-EG-03, A-SD-EG-04, A-SD-EG-05, A-SD-EG-06 · P-SL-ED → A-SL-ED-01 |

### Prozessbeteiligung

| Prozess | Bezeichnung | Status in R5 |
|---------|-------------|--------------|
| P-AU-VD | Verlegung durchführen | ✅ Abgeschlossen |
| P-BS-KG | Bewachung & Geländekontrolle (Pforte) | ✅ Abgeschlossen |
| P-BS-BE | Besuchermanagement | ✅ Abgeschlossen |
| P-BS-BZ | Besuchszeit-Verwaltung | ✅ Abgeschlossen |
| G-KS-AL | Krankenstation Allgemein | ✅ Abgeschlossen |
| G-KS-SM | Krankenstation Standardmedizin | ✅ Abgeschlossen |
| P-KS-RE | Rezepterstellung | ✅ Abgeschlossen |
| P-KS-GA | Gesundheitsamt-Termine & Befunde | ✅ Abgeschlossen |
| P-KS-KÜ | Kostenübernahme | ✅ Abgeschlossen |
| P-SD-SM | Sachmittelverwaltung | ✅ Abgeschlossen |
| P-SD-EG | Essensausgabe (Leistungsakte) | ✅ Abgeschlossen |
| P-SL-ED | Eingezogenes Eigentum | 🔶 Begonnen (A-SL-ED-01); Abschluss in R6 (AP-20: A-SL-ED-04) |
| G-SYS-BD | Bewohner:innen-Detailansicht | ➕ Verlegungsplanung in Kurzansicht ergänzt (A-SYS-BD-01 Teilaspekt) |

**PT-Schätzung:** min 84 PT · max 133 PT

---

## R6 – Verteilung & Finanzen

**Arbeitspakete:** AP-16, AP-20  
**Schätzung:** 75–115 PT  
**Voraussetzung:** R5  
**Liefert:** Vollständiger landesinterner Verteilungsprozess (Planung, Durchführung, E-Mail-Benachrichtigung, Verteilungsverfügung), Umverteilung, Kassenführung mit Tages-/Monatsabschlüssen, Pfand, Bezahlkarte, Einmalzahlungen

### Enthaltene Arbeitspakete

| AP | Titel | Abgedeckte Anforderungen (Prozess → Nummern) |
|----|-------|----------------------------------------------|
| AP-16 | Landesinterne Verteilung & Umverteilung | P-AU-LP → A-AU-LP-01, A-AU-LP-03, A-AU-LP-04, A-AU-LP-05, A-AU-LP-06, A-AU-LP-07, A-AU-LP-08, A-AU-LP-09, A-AU-LP-10, A-AU-LP-12, A-AU-LP-13, A-AU-LP-14, A-AU-LP-15, A-AU-LP-16, A-AU-LP-17, A-AU-LP-18, A-AU-LP-19, A-AU-LP-20, A-AU-LP-21, A-AU-LP-22, A-AU-LP-23, A-AU-LP-24, A-AU-LP-25, A-AU-LP-26 · P-AU-LD → A-AU-LD-01, A-AU-LD-02, A-AU-LD-03, A-AU-LD-04, A-AU-LD-06 · P-AU-UD → A-AU-UD-01, A-AU-UD-02 |
| AP-20 | Soziale Leistungen & Kassenführung | P-SL-KA → A-SL-KA-01, A-SL-KA-02, A-SL-KA-03, A-SL-KA-11, A-SL-KA-12, A-SL-KA-13, A-SL-KA-14, A-SL-KA-15, A-SL-KA-16, A-SL-KA-17 · G-SL-BK → A-SL-BK-01 · P-SL-PH → A-SL-PH-01, A-SL-PH-03 · P-SL-PZ → A-SL-PZ-01 · P-SL-ED → A-SL-ED-01, A-SL-ED-04 |

### Prozessbeteiligung

| Prozess | Bezeichnung | Status in R6 |
|---------|-------------|--------------|
| P-AU-LP | Landesinterne Verteilung planen | ✅ Abgeschlossen |
| P-AU-LD | Landesinterne Verteilung durchführen | ✅ Abgeschlossen |
| P-AU-UD | Umverteilung (inter-kommunal) | ✅ Abgeschlossen |
| P-SL-KA | Kassenführung | ✅ Abgeschlossen |
| P-SL-BK | Bezahlkarte | ✅ Abgeschlossen |
| P-SL-PH | Pfandverwaltung | ✅ Abgeschlossen |
| P-SL-PZ | Einmalzahlungen | ✅ Abgeschlossen |
| P-SL-ED | Eingezogenes Eigentum | ➕ Abgeschlossen (A-SL-ED-04, begann in R5) |
| G-SYS-BD | Bewohner:innen-Detailansicht | ➕ Vollständig abgeschlossen (Verteilungsplanung in Kurzansicht A-SYS-BD-01 vollständig) |

**PT-Schätzung:** min 75 PT · max 115 PT

---

## R7 – Statistik & BI

**Arbeitspakete:** AP-21  
**Schätzung:** 5–10 PT (Basis-Modul); BI-Integration ca. 30–50 PT (liegt lt. a.csv bei der ADD)  
**Voraussetzung:** R2+ (parallel zu anderen Releases möglich; je mehr Module, desto mehr auswertbare Daten)  
**Liefert:** Statistische Auswertungen strukturierter Bewohnerdaten, Statistikvorlagen, Power-BI-Anbindung

### Enthaltene Arbeitspakete

| AP | Titel | Abgedeckte Anforderungen (Prozess → Nummern) |
|----|-------|----------------------------------------------|
| AP-21 | Statistiken & Reporting | G-SYS-ST → A-SYS-ST-01, A-SYS-ST-02, A-SYS-ST-03 |

### Prozessbeteiligung

| Prozess | Bezeichnung | Status in R7 |
|---------|-------------|--------------|
| G-SYS-ST | Statistiken & Reporting | ✅ Abgeschlossen |

**PT-Schätzung:** min 5 PT · max 10 PT (BI-Integration separat, Verantwortung ADD)

---

## Prozessübersicht: Betroffen & Abgeschlossen je Release

| Prozess | Bezeichnung | Betroffen ab | Abgeschlossen in |
|---------|-------------|:------------:|:----------------:|
| G-SYS-AL | Allgemeine Systemarchitektur | R1 | R1 |
| G-SYS-HT | Historisierung & Audit-Trail | R1 | R1 |
| G-SYS-NF | Nichtfunktionale Anforderungen / UI | R1 | R2 |
| G-SYS-RR | Rollen- und Rechteverwaltung | R1 | R2 |
| P-AU-VR | Vorregistrierung | R2 | R2 |
| G-SYS-BÜ | Bewohner:innen-Übersichten | R2 | R2 |
| G-SYS-FD | Freitext-Dokumentation | R2 | R2 |
| P-AU-RE | Registrierung | R2 | R3 |
| G-SYS-BD | Bewohner:innen-Detailansicht | R2 | R6 |
| P-AU-EF | Ersterfassung (VR + AZR) | R2 | R3 |
| G-AU-SM | Sonderstatus-Management | R3 | R3 |
| G-SYS-TV | Terminverwaltung | R3 | R3 |
| G-SYS-VD | Vordrucke & Dokumente | R3 | R3 |
| G-SYS-BA | Bewohner:innen-Ausweis | R3 | R3 |
| P-SD-LM | Liegenschaftsmanagement | R4 | R4 |
| P-SD-BP | Belegungsplanung | R4 | R4 |
| P-SD-AE | Anwesenheitserfassung & Abgängigkeit | R4 | R4 |
| P-AU-VD | Verlegung durchführen | R5 | R5 |
| P-BS-KG | Bewachung & Geländekontrolle (Pforte) | R5 | R5 |
| P-BS-BE | Besuchermanagement | R5 | R5 |
| P-BS-BZ | Besuchszeit-Verwaltung | R5 | R5 |
| G-KS-AL | Krankenstation Allgemein | R5 | R5 |
| G-KS-SM | Krankenstation Standardmedizin | R5 | R5 |
| P-KS-RE | Rezepterstellung | R5 | R5 |
| P-KS-GA | Gesundheitsamt-Termine & Befunde | R5 | R5 |
| P-KS-KÜ | Kostenübernahme | R5 | R5 |
| P-SD-SM | Sachmittelverwaltung | R5 | R5 |
| P-SD-EG | Essensausgabe (Leistungsakte) | R5 | R5 |
| P-SL-ED | Eingezogenes Eigentum | R5 | R6 |
| P-AU-LP | Landesinterne Verteilung planen | R6 | R6 |
| P-AU-LD | Landesinterne Verteilung durchführen | R6 | R6 |
| P-AU-UD | Umverteilung (inter-kommunal) | R6 | R6 |
| P-SL-KA | Kassenführung | R6 | R6 |
| P-SL-BK | Bezahlkarte | R6 | R6 |
| P-SL-PH | Pfandverwaltung | R6 | R6 |
| P-SL-PZ | Einmalzahlungen | R6 | R6 |
| G-SYS-ST | Statistiken & Reporting | R7 | R7 |

---

## Gesamtschätzung

| Release | Arbeitspakete | PT min | PT max |
|---------|---------------|-------:|-------:|
| R1 – Fundament | AP-01, AP-02, AP-03 | 39 | 65 |
| R2 – Stammdaten | AP-04, AP-07, AP-08 | 54 | 87 |
| R3 – Basismodule I | AP-05, AP-06, AP-09, AP-10, AP-11 | 69 | 117 |
| R4 – Basismodule II | AP-12, AP-13, AP-14 | 51 | 80 |
| R5 – Operative Module | AP-15, AP-17, AP-18, AP-19 | 84 | 133 |
| R6 – Verteilung & Finanzen | AP-16, AP-20 | 75 | 115 |
| R7 – Statistik & BI | AP-21 | 5 | 10 |
| **Gesamt** | **AP-01 bis AP-21** | **377** | **607** |

> ⚠️ Hinweis zu BI (R7): A-SYS-ST-02 (freie Datenkombination über Power BI) liegt lt. a.csv in der Verantwortung der ADD und ist im Aufwand von 5–10 PT nicht enthalten. Der AMIS-seitige Anteil (Datenbereitstellung, Schnittstelle) ist im Basismodul enthalten.
