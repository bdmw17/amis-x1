# Implementierungsplan: Asylverwaltungsprogramm (AMIS)

> Basis: Anforderungskatalog `a.csv`  
> Stand: Mai 2026

---

## Legende

| Symbol | Bedeutung |
|--------|-----------|
| ✅ | Anforderungen teilweise/vollständig bereits implementiert |
| 🔴 | Voraussetzung (blockierend) |
| 🟡 | Soft-Abhängigkeit (parallel möglich, aber sinnvoll sequenziell) |
| PT | Personentage (Schätzspanne aus CSV) |
| (D) | Anforderung bereits implementiert (Spalte „Bereits implementiert" in der CSV) |

---

## Übersicht Arbeitspakete

```
AP-01  Systeminfrastruktur & Basisarchitektur
AP-02  Rollen- & Rechteverwaltung
AP-03  Historisierung & Audit-Trail
AP-04  Bewohner-Stammdaten & Registrierungsprozess
AP-05  AZR-Schnittstelle
AP-06  Sonderstatus-Management
AP-07  Bewohner-Übersichten & Detailansichten
AP-08  Freitext-Dokumentation
AP-09  Terminverwaltung
AP-10  Vordrucke & Formulare
AP-11  Bewohnerausweis
AP-12  Liegenschaftsmanagement
AP-13  Belegungsplanung
AP-14  Anwesenheitserfassung & Abgängigkeit
AP-15  Verlegungsplanung
AP-16  Landesinterne Verteilung
AP-17  Bewachung & Sicherheit (Pforte/Besucher)
AP-18  Krankenstation & Medizinisches Modul
AP-19  Sachmittel- & Leistungsverwaltung
AP-20  Soziale Leistungen & Kassenführung
AP-21  Statistiken & Reporting
```

---

## AP-01 – Systeminfrastruktur & Basisarchitektur

**Anforderungen:** A-SYS-AL-03 (D), A-SYS-AL-04 (D), A-SYS-AL-05 (D), A-SYS-AL-06, A-SYS-NF-02 (D), A-SYS-NF-03 (D), A-SYS-NF-04 (D)

**Beschreibung:**  
Technische Grundlage der Anwendung: Web-Framework (React), Datenbankwahl, Concurrent-Access-Strategie, responsives Layout, Multi-Fenster-Fähigkeit.

**Akzeptanzkriterien:**
- [ ] Anwendung läuft browserbasiert (kein Desktop-Client notwendig)
- [ ] Gleichzeitiger Zugriff mehrerer Nutzer:innen auf denselben Datensatz ohne Lost-Update (optimistic locking oder vergleichbar)
- [ ] UI passt sich an Fenstergröße an (responsive); Elemente sind in der Größe anpassbar
- [ ] Mehrere Module lassen sich in verschiedenen Browser-Tabs/Fenstern gleichzeitig nutzen
- [ ] Öffnen einer Detailansicht (z. B. Anhang) lässt die übergeordnete Bewohner:innen-Ansicht offen
- [ ] Neue Module können ohne Architekturänderungen integriert werden (Plugin-/Modul-Konzept dokumentiert)

**Abhängigkeiten:** keine  
**Schätzung:** 10–20 PT (Basisinfrastruktur, nicht in CSV beziffert)

---

## AP-02 – Rollen- & Rechteverwaltung ✅

**Anforderungen:** A-SYS-RR-01, A-SYS-RR-02, A-SYS-RR-03, A-SYS-RR-05

**Beschreibung:**  
Granulares Rollen- und Berechtigungsmodell: Zugriff auf Module, Daten und Funktionen steuerbar nach Rolle und AfA-Zugehörigkeit (AfA, ADD, ZRF, MFFKI).

**Akzeptanzkriterien:**
- [x] Nutzer:innen lassen sich Rollen zuweisen; jede Rolle hat definierte Berechtigungen pro Modul
- [x] AfA-Zugehörigkeit der Nutzer:in ist im Datenmodell verankert und konfigurierbar (`benutzer.afa_id`)
- [x] Nutzer:innen können je nach Modul unterschiedliche Rechte haben (lesen/schreiben/administrieren)
- [x] Sichtbare Daten hängen ausschließlich von der Rolle ab, nicht vom aktuell geöffneten Modul (stateless, per-request)
- [x] Berechtigungsänderungen wirken sofort ohne Neuanmeldung (kein Cache in `BenutzerDetailsService`)
- [ ] **⚠️ Offen (→ AP-04):** Abfrage-Filterung nach AfA-Zugehörigkeit in Datenbankabfragen (A-SYS-RR-02) – Infrastruktur vorhanden, Enforcement folgt mit Bewohner-Endpunkten

**Abhängigkeiten:**  
🔴 AP-01  
**Schätzung:** 23–35 PT (aus A-SYS-RR-01 bis -05: 5+12+5+1 = 23 min, 35 max)

---

## AP-03 – Historisierung & Audit-Trail

**Anforderungen:** A-SYS-HT-01

**Beschreibung:**  
Vollständige, unveränderliche Protokollierung aller Änderungen und Ergänzungen an Bewohner:innen-Einträgen. Berechtigte Nutzer:innen können jede frühere Version einsehen.

**Akzeptanzkriterien:**
- [ ] Jede Änderung an einem Datensatz erzeugt einen Historieintrag (Zeitstempel, Nutzer:in, Feldwert vorher/nachher)
- [ ] Daten können nicht endgültig gelöscht oder überschrieben werden
- [ ] Nutzer:innen mit entsprechender Berechtigung können jede frühere Version eines Eintrags aufrufen
- [ ] Audit-Log ist selbst unveränderlich (keine Admin-Funktion zum Löschen)

**Abhängigkeiten:**  
🔴 AP-01, 🔴 AP-02  
**Schätzung:** 6–10 PT

---

## AP-04 – Bewohner-Stammdaten & Registrierungsprozess

**Anforderungen:** A-AU-VR-01 (D), A-AU-VR-02 (D), A-AU-VR-03 (D), A-AU-RE-01 (D), A-AU-RE-05 (D), A-AU-RE-06, A-AU-RE-11 (D), A-AU-RE-14, A-AU-RE-15, A-AU-RE-16, A-AU-RE-17, A-AU-RE-18, A-AU-RE-19 (D)

**Beschreibung:**  
Vollständiger Vorregistrierungs- und Registrierungsprozess: Erfassung aller Stammdaten, Familien-/Fluchtgemeinschaften, Sonderaufnahmegruppen, Statusübergänge.

**Akzeptanzkriterien:**
- [ ] Neuaufnahme kann mit Name, Vorname, Titel, Geburtsdatum, Familienstand, Staatsangehörigkeit, Sprachkenntnissen und Freitextfeldern vorregistriert werden → Status „Vorregistriert"
- [ ] Ausweisdokumente können der Vorregistrierung beigefügt werden
- [ ] Vorregistrierungsdaten stehen bei der Registrierung ohne manuelle Übertragung zur Verfügung
- [ ] Abweichungen/Widersprüche in Personendaten aus verschiedenen Quellen werden erkannt und angezeigt
- [ ] Originaldokumente können eingescannt und dem Bewohner:innen-Eintrag angehängt werden
- [ ] Mehrere Personen können zu einem **Familienverbund** zusammengefasst werden
- [ ] Mehrere Personen können zu einer **Fluchtgemeinschaft** zusammengefasst werden
- [ ] Sonderaufnahme-Status und -Gruppe kann während der Registrierung erfasst werden
- [ ] Berechtigte Nutzer:innen können Sonderaufnahme-Gruppen erstellen, bearbeiten und löschen (Bezeichnung + Freitext)
- [ ] Abschluss der Registrierung überführt Status von „Vorregistriert" nach „Registriert"
- [ ] Filtermöglichkeit nach Status und Sonderaufnahme-Gruppe in Übersichten vorhanden

**Abhängigkeiten:**  
🔴 AP-01, 🔴 AP-02, 🔴 AP-03  
**Schätzung:** 28–46 PT (aus den Einzel-APs summiert)

---

## AP-05 – AZR-Schnittstelle

**Anforderungen:** A-AU-RE-02 (D), A-AU-RE-03 (D), A-AU-RE-06

**Beschreibung:**  
Bidirektionale Anbindung an das Ausländerzentralregister (AZR): Datenabfrage, automatischer Import (PIK-Prozess), automatische Updates bei Datenänderungen im AZR.

**Akzeptanzkriterien:**
- [ ] Prüfung ob AZR-Daten zu einer Person vorhanden sind direkt aus der Anwendung heraus möglich
- [ ] AZR-Daten können per Klick in den Bewohner:innen-Eintrag übernommen werden (kein manueller Tipp-Aufwand)
- [ ] Personenbezogene Daten aus dem PIK-Prozess werden automatisiert per Schnittstelle übernommen
- [ ] Nachträgliche AZR-Aktualisierungen werden automatisch in die Stammdaten übertragen
- [ ] Automatische Aktualisierungen werden für Nutzer:innen sichtbar markiert (z. B. Hinweis-Banner)

**Abhängigkeiten:**  
🔴 AP-04  
**Schätzung:** 10–15 PT

---

## AP-06 – Sonderstatus-Management

**Anforderungen:** A-AU-SM-01 (D), A-AU-SM-02, A-AU-SM-03, A-AU-SM-04, A-AU-SM-05, A-AU-SM-06, A-AU-SM-08, A-AU-SM-10, A-AU-SM-11, A-AU-SM-12, A-AU-SM-15 (D), A-AU-SM-16 (D), A-AU-SM-17 (D), A-AU-SM-18 (D), A-AU-SM-19 (D), A-AU-SM-20

**Beschreibung:**  
Verwaltung aller besonderen Personenstatus: Vulnerable Personen, Schwangere, Nachgeborene, UMA, Folgeantragsteller:innen, Abgängige, Wohnpflicht.

**Akzeptanzkriterien:**
- [ ] Bewohner:innen können als **vulnerabel** markiert werden (mit Begründung); filterbar
- [ ] Für Schwangere kann ein Entbindungstermin hinterlegt werden
- [ ] Nachgeborene Kinder lassen sich erfassen und dem Familienverbund zuordnen (zwei Kategorien)
- [ ] **UMA**-Status ist setzbar; Bezugspersonen (1-n) und Kommunen können hinterlegt werden; Zuweisungsdokument (PDF) anhängbar
- [ ] Alterseinschätzung kann UMA-Status auf volljährig ändern
- [ ] **BAMF-Verfahrensstand** ist erfassbar (Antrag gestellt, Anhörung, kurzfristige Entscheidung, Abgeschlossen + Ausgang)
- [ ] **Folgeantragsteller:innen** werden mit Datum markiert und sind in Übersichten filterbar
- [ ] **Wohnpflicht**: Standard-Dauer ist konfigurierbar; verlängerbar um Abgängigkeits-/Haftzeiten
- [ ] **Abgängigkeit**: Von-Bis-Datum erfassbar; Rückkehr als Wiederaufnahme mit Datum markierbar; filterbar

**Abhängigkeiten:**  
🔴 AP-04, 🔴 AP-02  
**Schätzung:** 30–50 PT

---

## AP-07 – Bewohner-Übersichten & Detailansichten

**Anforderungen:** A-SYS-BÜ-01 (D), A-SYS-BÜ-02, A-SYS-BÜ-03 (D), A-SYS-BÜ-04 (D), A-SYS-BÜ-05 (D), A-SYS-BÜ-06 (D), A-SYS-BÜ-08, A-SYS-BÜ-11, A-SYS-BÜ-12, A-SYS-BD-01, A-SYS-BD-02

**Beschreibung:**  
Tabellarische Übersichten mit Sortierung, Filterung und Export sowie eine zweistufige Detailansicht (Zusammenfassung → Vollansicht) pro Bewohner:in.

**Akzeptanzkriterien:**
- [ ] Tabellen zeigen alle strukturierten Felder als Spalten; Spaltensatz ist pro Prozess vordefiniert
- [ ] Sortierung per Klick auf Spaltenköpfe; zweistufige Sortierung (zweite Ebene) möglich
- [ ] Filterung nach beliebigen angezeigten Spalten; gefilterte Ansicht ist weiterarbeitsfähig
- [ ] Export als `.pdf` und `.xlsx` jederzeit möglich
- [ ] Anzahl der Mitglieder eines Familienverbunds ist als Spalte anzeigbar
- [ ] Vulnerable Bewohner:innen und solche mit besonderem Schutzbedarf sind durch Icons gekennzeichnet (max. 5 definierte Icons, geliefert durch Auftraggeber)
- [ ] **Kurzansicht** (Klick auf Eintrag) zeigt: Name, Geburtsdatum, Foto, AfA, Zimmer, Herkunftsland, Familienverband, Sprachkenntnisse, Hinweise, geplante Verlegung/Verteilung; Felder ohne Zugriffsrecht sind ausgeblendet
- [ ] **Vollansicht** zeigt alle gespeicherten und berechtigten Daten der Person

**Abhängigkeiten:**  
🔴 AP-04, 🔴 AP-02  
**Schätzung:** 20–33 PT

---

## AP-08 – Freitext-Dokumentation

**Anforderungen:** A-SYS-FD-01, A-SYS-FD-02, A-SYS-FD-04, A-SYS-FD-05, A-SYS-FD-11, A-SYS-FD-12, A-SYS-FD-15, A-SYS-FD-16

**Beschreibung:**  
Unveränderliches Tagebuch pro Bewohner:in mit Kategorisierung, Hinweisfunktion und rechtebasiertem Zugriff.

**Akzeptanzkriterien:**
- [ ] Beliebig viele Einträge pro Bewohner:in anlegbar
- [ ] Einmal erstellte Einträge sind nicht mehr editierbar oder löschbar
- [ ] Jeder Eintrag enthält: Verfasser:in, Rolle/Bereich, Zeitstempel (automatisch)
- [ ] Neueste Einträge stehen oben; Folge-Einträge orientieren sich am jüngsten Folge-Eintrag
- [ ] Lese- und Schreibzugriff über Rollen- & Rechteverwaltung steuerbar
- [ ] Einträge können einer Hinweis-Kategorie zugeordnet werden (z. B. „Isolationspflicht")
- [ ] Hinweise sind für alle Nutzer:innen sichtbar
- [ ] Bestimmte Hinweis-Kategorien können in anderen Prozessen aktiv genutzt werden (z. B. Warnanzeige an Pforte)

**Abhängigkeiten:**  
🔴 AP-04, 🔴 AP-02, 🔴 AP-03  
**Schätzung:** 6–8 PT

---

## AP-09 – Terminverwaltung

**Anforderungen:** A-SYS-TV-01 (D), A-SYS-TV-02 (D), A-SYS-TV-03 (D), A-SYS-TV-04 (D), A-SYS-TV-05 (D), A-SYS-TV-06 (D), A-SYS-TV-07 (D), A-SYS-TV-09 (D), A-SYS-TV-10 (D), A-SYS-TV-11 (D), A-SYS-TV-12 (D)

**Beschreibung:**  
Vollständige Terminverwaltung pro Bewohner:in mit Übersichten, Export und automatischer Pflichttermin-Überwachung.

**Akzeptanzkriterien:**
- [ ] Beliebig viele Termine pro Bewohner:in; jeder Termin hat Titel, Freitext, Kategorie und Priorität (0–5, farblich codiert)
- [ ] Terminkategorien sind durch berechtigte Nutzer:innen anpassbar
- [ ] Terminübersichten filterbar nach Kategorie, AfA und Zeitraum; für einzelne oder alle AfAs
- [ ] Export jeder Übersicht als `.pdf` und `.xlsx`
- [ ] Termine sind jederzeit editierbar (alle Felder außer Kategorie); Kategorie nicht nachträglich änderbar
- [ ] Schnell-Verschiebung um vorkonfigurierte Zeiträume (z. B. 1 Woche, 14 Tage, 1 Monat) per Klick
- [ ] Für definierte Bewohner:innen-Gruppen und festgelegte Kategorien stellt das System sicher, dass stets mindestens ein zukünftiger Termin existiert (Pflichttermin-Wächter)

**Abhängigkeiten:**  
🔴 AP-04, 🔴 AP-02  
**Schätzung:** 16–27 PT

---

## AP-10 – Vordrucke & Formulare

**Anforderungen:** A-SYS-VD-01, A-SYS-VD-02, A-SYS-VD-03 (D), A-SYS-VD-04 (D), A-SYS-VD-05, A-SYS-VD-06 (D), A-SYS-VD-07 (D)

**Beschreibung:**  
Dynamisches Formular-System: automatisches Befüllen mit Bewohnerdaten, mehrsprachig, mit Unterschriftenpad-Support, nutzerseitig erweiterbar.

**Akzeptanzkriterien:**
- [ ] Aus einem Bewohner:innen-Eintrag heraus kann ein Vordruck ausgewählt und mit Bewohnerdaten befüllt werden; Export als `.docx` und `.pdf` oder direkter Druck
- [ ] Vordruck-Schritt kann fester Bestandteil eines Prozesses sein (prozessgesteuert, kein manuelle Auswahl nötig)
- [ ] Mehrere Sprachversionen pro Vordruck auswählbar; vorausgewählte Sprache orientiert sich an Sprachkenntnissen der Person
- [ ] Vordrucke können Unterschriftenfelder enthalten, die per Unterschriftenpad ausgefüllt werden können
- [ ] Berechtigte Nutzer:innen können neue Vordrucke und neue Übersetzungen anlegen und im System nutzen

**Abhängigkeiten:**  
🔴 AP-04  
**Schätzung:** 7–14 PT

---

## AP-11 – Bewohnerausweis

**Anforderungen:** A-SYS-BA-01, A-SYS-BA-02, A-SYS-BA-03, A-SYS-BA-05, A-SYS-BA-06

**Beschreibung:**  
Ausstellen, Invalidieren und Scannen von Bewohner:innen-Ausweisen; Nutzung zum schnellen Datenzugriff und bei der Essensausgabe.

**Akzeptanzkriterien:**
- [ ] Neuer Ausweis kann ausgestellt werden; ein vorhandener Ausweis derselben Person wird automatisch invalidiert
- [ ] Ausweis enthält: Vor-/Nachname, Foto, Staatsangehörigkeit, Geburtsdatum, Aufnahmedatum, Personen-Nr. sowie QR-Code, Barcode oder NFC-Chip
- [ ] Durch Scannen des Ausweises wird der zugehörige Bewohner:innen-Eintrag in einem Schritt geöffnet
- [ ] Essensausgabe kann per Ausweis-Scan oder manueller Auswahl erfasst und der Leistungsakte hinzugefügt werden
- [ ] Ausweis-Druck ist mit EVOLIS-Kartendruckern kompatibel

**Abhängigkeiten:**  
🔴 AP-04  
**Schätzung:** 6–11 PT

---

## AP-12 – Liegenschaftsmanagement

**Anforderungen:** A-SD-LM-01 (D), A-SD-LM-02 (D), A-SD-LM-03 (D), A-SD-LM-04 (D), A-SD-LM-05 (D), A-SD-LM-06 (D), A-SD-LM-07 (D), A-SD-LM-08 (D), A-SD-LM-09 (D), A-SD-LM-10 (D), A-SD-LM-11 (D), A-SD-LM-12 (D), A-SD-LM-13 (D), A-SD-LM-14 (D), A-SD-LM-15 (D), A-SD-LM-16 (D), A-SD-LM-18 (D), A-SD-LM-19 (D), A-SD-LM-20 (D), A-SD-LM-21 (D)

**Beschreibung:**  
Hierarchische Abbildung aller Liegenschaften von der AfA bis zum einzelnen Bett; interaktiver Lageplan mit Belegungsanzeige.

**Akzeptanzkriterien:**
- [ ] Hierarchie: AfA-Objekt → Gebäude / Halle → Zimmer → Bett; alle Ebenen können angelegt, bearbeitet und gelöscht werden
- [ ] Jedem Bett kann genau eine Bewohner:in zugeordnet sein; jede aktive Bewohner:in hat immer genau ein Bett
- [ ] Räume können kategorisiert werden (z. B. „Alleinstehende Männer", „Familien", „Isolation", „UMA")
- [ ] Zimmer können Sanitärausstattung, Barrierefreiheit, Sonderausstattung und Mängel erfassen
- [ ] Belegungsmodelle (z. B. „Sozialverträglich", „Pandemiekonform") können als Schlagwörter vergeben werden
- [ ] Interaktiver Lageplan stellt alle Ebenen bis zum Bett dar; Farben/Symbole sind konfigurierbar; Legende vorhanden
- [ ] Aus dem Lageplan heraus ist ein Doppelklick auf eine Person → direkter Wechsel zu deren Stammdaten möglich
- [ ] Lageplan ist als `.pdf` und `.xlsx` exportierbar

**Abhängigkeiten:**  
🔴 AP-01, 🔴 AP-02  
🟡 AP-04 (für Belegungsanzeige)  
**Schätzung:** 20–35 PT (nicht explizit in CSV beziffert, da bereits implementiert laut Kommentar)

---

## AP-13 – Belegungsplanung

**Anforderungen:** A-SD-BP-01 bis A-SD-BP-10

**Beschreibung:**  
Automatische und manuelle Zimmerzuweisung mit Inkompatibilitätsprüfung, Verlegungslisten und Historisierung.

**Akzeptanzkriterien:**
- [ ] System schlägt automatisch Zimmer basierend auf Eigenschaften vor (Nationalität, Geschlecht, Religion, Krankheiten, etc.)
- [ ] Vorschlag kann manuell angepasst oder verworfen und neu erstellt werden
- [ ] Inkompatibilitätskriterien (beliebige Attribute) können von berechtigten Nutzer:innen konfiguriert werden
- [ ] Warnmeldung, wenn inkompatible Personen in dasselbe Zimmer eingeplant werden (auch rückwirkend für die letzten 3 Tage)
- [ ] Isolationszimmer werden während der Isolation aus der Belegungsplanung ausgeblendet
- [ ] Zimmerwechsel per Drag-and-Drop möglich; alle Ansichten aktualisieren sich sofort
- [ ] Bisherige Zimmerzuweisungen werden historisiert
- [ ] Verlegungslisten können erstellt, versendet und nach Durchführung bestätigt werden; Export als `.pdf`
- [ ] Teilt sich ein Zimmer eine Sanitäreinrichtung mit einem anderen Zimmer, erscheint bei der Belegungsplanung ein Hinweis

**Abhängigkeiten:**  
🔴 AP-12, 🔴 AP-06  
**Schätzung:** 21–30 PT (aus CSV-Einzel-APs)

---

## AP-14 – Anwesenheitserfassung & Abgängigkeit

**Anforderungen:** A-SD-AE-01 (D), A-SD-AE-02 (D), A-SD-AE-03 (D), A-SD-AE-04 (D), A-SD-AE-05 (D), A-SD-AE-09 (D), A-SD-AE-10 (D), A-SD-AE-11 (D), A-SD-AE-12 (D), A-SD-AE-14 (D), A-SD-AE-15 (D), A-SD-AE-16 (D), A-SD-AE-17 (D), A-SD-AE-18 (D), A-SD-AE-19 (D)

**Beschreibung:**  
Anwesenheitserfassung im Rundgang (mobil oder Desktop), Abgängigkeitsverwaltung und automatische Statusübergänge.

**Akzeptanzkriterien:**
- [ ] An- und Abwesenheiten können pro Bewohner:in erfasst werden (Desktop und Tablet/iOS/Android)
- [ ] Abwesenheitsgrund ist aus vordefinierter Liste wählbar (Kirchenasyl, Jugendhilfe, Haft, etc.) + Freitextfeld
- [ ] Status „abgängig" kann manuell gesetzt werden; Automatik setzt diesen nach konfiguriertem Zeitraum (bei bestimmten Gründen)
- [ ] Mehrfache Abgängigkeitszeiten werden kumuliert angezeigt (für Sozialdienst und Soziale Leistungen sichtbar)
- [ ] Rundganglisten zeigen Bewohner:innen in Zimmer-Reihenfolge; auf Mobilgeräten bedienbar
- [ ] Notizen zum Rundgang können als Freitext-Dokumentationseintrag erfasst werden (mobil oder nachträglich)
- [ ] Tagesübersicht: neu abgängige und zurückgekehrte Bewohner:innen; Export als `.xlsx`

**Abhängigkeiten:**  
🔴 AP-11, 🔴 AP-12, 🔴 AP-08  
**Schätzung:** 10–15 PT

---

## AP-15 – Verlegungsplanung (intern)

**Anforderungen:** A-AU-VD-01, A-AU-VD-02, A-AU-VD-04, A-AU-VD-05

**Beschreibung:**  
Planung und Durchführung von Verlegungen zwischen AfAs; Listenerstellung, Sortierung, Bestätigung und automatische Datensatz-Aktualisierung.

**Akzeptanzkriterien:**
- [ ] Verlegungsliste kann exportiert werden (`.xlsx`, `.docx`, `.pdf`)
- [ ] Liste ist sortierbar nach ID-Nummer, Unterbringungsort und Nachnamen
- [ ] Nach Durchführung wird pro Person bestätigt, ob die Verlegung stattgefunden hat
- [ ] Bestätigte Personen werden automatisch dem neuen Objekt zugeordnet
- [ ] Geplante Verlegungen sind in der Bewohner-Kurzansicht (AP-07) sichtbar
- [ ] Krankenstation-Sperre (AP-18) verhindert Einplanung gesperrter Personen

**Abhängigkeiten:**  
🔴 AP-06, 🔴 AP-13, 🔴 AP-08  
🟡 AP-18 (Sperrfunktion)  
**Schätzung:** 8–10 PT

---

## AP-16 – Landesinterne Verteilung & Umverteilung

**Anforderungen:** A-AU-LP-01 bis A-AU-LP-26, A-AU-LD-01 bis A-AU-LD-06, A-AU-UD-01, A-AU-UD-02

**Beschreibung:**  
Vollständiger Verteilungsprozess: Planung mit Gemeindequoten, Familienverbund-Berücksichtigung, E-Mail-Benachrichtigung, rechtliche Dokumente und automatische Archivierung.

**Akzeptanzkriterien:**
- [ ] Verteilungsplanungen mit Ausgangspunkt, Zielkommune, Kapazität und Datum anlegen/bearbeiten/löschen
- [ ] Muss-Verteilfälle werden automatisch ergänzt; Kriterien sind konfigurierbar
- [ ] Familienverbünde werden nicht getrennt verteilt ohne Hinweis + Wahlmöglichkeit (zusammen / nur Auswahl / gar nicht) + Begründungspflicht bei Teilverteilung
- [ ] Bezugspersonen von UMAs werden auf dieselbe Kommune verteilt wie der/die UMA
- [ ] Gemeindequoten sind hinterlegbar und anpassbar; Über-/Untererfüllung wird angezeigt
- [ ] Verteilungshinweise (Freitext) und Einschränkungen (Herkunftsland, Familientyp etc.) je Kommune hinterlegbar
- [ ] Gesundheitsamts-Sperren und Krankenstations-Hinweise sind während der Planung sichtbar und sperren ggf. Personen
- [ ] Verteilungsverfügung und Zuweisungsentscheidung generierbar (aus Dokumentenvorlagen, Rechtsgrundlage wählbar)
- [ ] 10-stellige Verteilnummer wird automatisch generiert
- [ ] E-Mail-Benachrichtigungen an Kommunen (konfigurierbare Adressen) werden automatisch unter Einhaltung der 14-Tage-Frist erstellt und versendet
- [ ] Nach Bestätigung wird Bewohner:in als nicht mehr in AfA wohnhaft markiert und archiviert
- [ ] Umverteilung zwischen Kommunen (Ausgangs- und Zielkommune, Datum, Dokumente) ist erfassbar

**Abhängigkeiten:**  
🔴 AP-06, 🔴 AP-15, 🔴 AP-10  
🟡 AP-18 (Krankenstation-Sperre)  
**Schätzung:** 45–65 PT

---

## AP-17 – Bewachung & Sicherheit (Pforte & Besucher)

**Anforderungen:** A-BS-KG-01, A-BS-KG-02, A-BS-KG-03, A-BS-KG-04, A-BS-KG-05, A-BS-KG-06, A-BS-KG-09, A-BS-KG-10, A-BS-BE-02 (D), A-BS-BE-03 (D), A-BS-BE-04 (D), A-BS-BE-05 (D), A-BS-BE-06 (D), A-BS-BE-08 (D), A-BS-BE-09 (D), A-BS-BZ-01 (D), A-BS-BZ-02 (D)

**Beschreibung:**  
Erfassung von Ein-/Ausgängen der Bewohner:innen und Besucher:innen, Hausverbotsverwaltung, automatische Warnmeldungen.

**Akzeptanzkriterien:**
- [ ] Bewohner:innen-Bewegungen (Betreten/Verlassen) werden erfasst: ID-Nummer, AfA, Zeit, Richtung, Mitarbeiter:in
- [ ] Erfassung per Ausweis-Scan (AP-11) oder manueller Auswahl
- [ ] System leitet Richtung (kommt/geht) aus gespeicherter Anwesenheit ab; manuelles Überschreiben möglich
- [ ] Hinweisanzeige bei Bewohner:innen mit aktiven Kategorien „Isolation", „Gewalt-Vorfälle", „Bitte um Meldung"
- [ ] Warnmeldung, wenn Minderjährige unter definierter Altersgrenze die AfA alleine verlassen wollen
- [ ] Besucher:innen-Registrierung: Name, Vorname, Geburtsdatum, Anschrift, Ausweis-Nr., besuchte Person; alternativ Bewohnerausweis scannen
- [ ] Liste aller aktuell anwesenden Besucher:innen; Austragen mit Zeitstempel
- [ ] Hausverbot: bei Übereinstimmung mit gespeichertem Hausverbot erscheint Warnmeldung; Verstoßsversuch kann erfasst werden
- [ ] Besuchszeit-Ende konfigurierbar; Warnmeldung bei noch anwesenden Besucher:innen nach Besuchsende

**Abhängigkeiten:**  
🔴 AP-11, 🔴 AP-07, 🔴 AP-08  
**Schätzung:** 16–28 PT

---

## AP-18 – Krankenstation & Medizinisches Modul

**Anforderungen:** A-KS-AL-01 (D), A-KS-AL-02 (D), A-KS-AL-05, A-KS-SM-01 (D), A-KS-SM-03, A-KS-SM-04 (D), A-KS-SM-05 (D), A-KS-SM-06 (D), A-KS-SM-07, A-KS-SM-08, A-KS-SM-09, A-KS-SM-12 (D), A-KS-SM-13 (D), A-KS-SM-18, A-KS-RE-01 bis A-KS-RE-09, A-KS-GA-03, A-KS-GA-04, A-KS-KÜ-01 bis A-KS-KÜ-10

**Beschreibung:**  
Patientenakte mit medizinischer Dokumentation, Medikamentenverwaltung, Rezepterstellung, Kostenübernahmen und Gesundheitsamt-Dokumentation.

**Akzeptanzkriterien:**
- [ ] Jede Bewohner:in hat eine Patientenakte mit medizinischen Freitext-Einträgen (KS-Rolle) + strukturierten Feldern
- [ ] Krankenblatt-Übersicht zeigt: Diagnosen, Medikamente, Allergien, Impfungen, Schwangerschaften, Operationen, Isolationspflicht
- [ ] Gesamte Patientenakte als `.pdf` exportierbar
- [ ] Impfungen (vorhanden / notwendig / neu durchgeführt) statistisch auswertbar
- [ ] Medikamentenverwaltung: Erfassung, Ausgabeprotokoll, Übersicht sortierbar und filterbar
- [ ] Befunde können als `.pdf` abgelegt, eingescannt und in einer Übersicht eingesehen werden
- [ ] **Rezept**: Personendaten aus Stammdaten; zusätzliche Felder (IK-Nummer, Beitragsfrei, PZN, Dosierung etc.); alle gängigen Rezept-Drucker unterstützt; Rezept automatisch der Patientenakte hinzugefügt; Wiederholungsdruck möglich; Sprechstundenbedarf-Rezept erstellbar
- [ ] **Kostenübernahme**: Aus Patientenakte startbar; Bewohnerdaten automatisch befüllt; VKNR und IK-Nummer automatisch; als PDF in Patientenakte gespeichert; Abrechnung mit Betrag in Leistungsakte
- [ ] Gesundheitsamtstermine mit Datum und Befunden separat einsehbar
- [ ] Krankenstation kann **Verteilungs-/Verlegungsverbot** aus gesundheitlichen Gründen eintragen; dieses wird in Planungsprozessen AP-15/AP-16 angezeigt

**Abhängigkeiten:**  
🔴 AP-08, 🔴 AP-10, 🔴 AP-04  
🟡 AP-09 (Arzttermine)  
**Schätzung:** 35–55 PT

---

## AP-19 – Sachmittel- & Leistungsverwaltung

**Anforderungen:** A-SD-SM-01 (D), A-SD-SM-02 (D), A-SD-SM-03 (D), A-SD-SM-04 (D), A-SD-SM-06 (D), A-SD-SM-07 (D), A-SD-SM-08 (D), A-SD-SM-09, A-SD-SM-10, A-SD-EG-02 bis A-SD-EG-06, A-SD-AE (Leistungsakte), A-SL-ED-01

**Beschreibung:**  
Artikelverwaltung mit Barcodes, Ausgabe-/Rücknahme-Protokoll, Leistungsakte pro Bewohner:in, Verwahrgut-Verwaltung.

**Akzeptanzkriterien:**
- [ ] Artikel können angelegt werden (Artikelnummer, Beschreibung, Kosten, Leih-Gegenstand ja/nein)
- [ ] Artikel lassen sich mit Barcodes assoziieren; Erfassung per Scanner oder manuell; kompatibel mit gängigen Barcodescannern
- [ ] Leistungsakte pro Bewohner:in führt alle Ausgaben (inkl. Essensausgabe aus AP-11)
- [ ] Taschengeldkonto gesondert auswertbar
- [ ] Hinweis bei bevorstehender Verlegung/Verteilung, dass Leihgegenstände zurückgegeben werden müssen
- [ ] Rückgabe-Bestätigung setzt Leistungsakten-Betrag auf 0,00 €
- [ ] Eingezogenes Eigentum kann mit Beschreibung, Datum und Zimmernummer inventarisiert werden; Quittung über Vordruck-Funktion generierbar
- [ ] Bei Verlassen der AfA: automatische Meldung für Rückgabe eingezogenen Eigentums; Rückgabe-Bestätigung und -Beleg über Vordruck möglich

**Abhängigkeiten:**  
🔴 AP-04, 🔴 AP-11, 🔴 AP-10  
🟡 AP-15, AP-16 (Verlegungs-Hinweis)  
**Schätzung:** 25–40 PT

---

## AP-20 – Soziale Leistungen & Kassenführung

**Anforderungen:** A-SL-KA-01 bis A-SL-KA-17, A-SL-BK-01, A-SL-PH-01, A-SL-PH-03, A-SL-PZ-01, A-SL-ED-01, A-SL-ED-04

**Beschreibung:**  
Kassenführung mit Tages-/Monats-/Jahresabschlüssen, Pfandverwaltung, Einmalzahlungen, Quittungen und Bezahlkarten-Verwaltung.

**Akzeptanzkriterien:**
- [ ] Pro Tag können ein oder mehrere Tagesabschlüsse je Kasse sowie ein Gesamttagesabschluss erstellt werden (enthält Kassenbestand, Summen, Journal, Nutzer:in, AfA)
- [ ] IST-Bestand der Kasse ist beim Tagesabschluss erfassbar
- [ ] Buchungsarten sind mit Titeln versehen (aus Stammdatenverwaltung konfigurierbar)
- [ ] Alle Tagesabschlüsse werden historisiert und sind jederzeit abrufbar
- [ ] Monats- und Jahreskontoauszug nach Leistungsarten und Datum erstellbar
- [ ] Alle Buchungen sind nach Personen, Beträgen und Zeiträumen filterbar und auswertbar
- [ ] Export aller Abschlüsse und Kontoauszüge als `.pdf` und `.xlsx`
- [ ] **Bezahlkarte**: IBAN und Auszahlungen pro Person erfasst; Auswahl Bar/Karte; CSV-Export für Auszahlungen in einem Zeitraum
- [ ] **Pfand**: Pfanderhebung (ohne Teilbeträge), Begleichung über Einmalzahlung, Pfandrückzahlung erfassbar
- [ ] Einmalzahlungen erfassbar; Quittungsdruck über Vordruck-Funktion möglich

**Abhängigkeiten:**  
🔴 AP-04, 🔴 AP-10, 🔴 AP-19  
**Schätzung:** 30–50 PT

---

## AP-21 – Statistiken & Reporting

**Anforderungen:** A-SYS-ST-01, A-SYS-ST-02, A-SYS-ST-03

**Beschreibung:**  
Auswertung strukturierter Bewohnerdaten, freie Datenkombination, Statistikvorlagen; Power-BI-Anbindung für erweiterte Analysen.

**Akzeptanzkriterien:**
- [ ] Alle strukturierten Felder sind für Auswertungen zugänglich
- [ ] Statistikvorlagen können hinterlegt werden; aus einer Vorlage lässt sich eine Statistik für einen beliebigen Zeitraum und eine bestimmte AfA mit wenigen Klicks erstellen
- [ ] Power-BI-Anbindung (oder vergleichbare BI-Lösung) ermöglicht freie Kombinierbarkeit der Daten
- [ ] Ergebnisse exportierbar

**Abhängigkeiten:**  
🔴 AP-04, 🔴 AP-07  
🟡 Alle anderen APs (je mehr Module, desto mehr auswertbare Daten)  
**Schätzung:** 5–10 PT (Basis); BI-Integration separat (30–50 PT, liegt lt. CSV bei der ADD)

---

## Abhängigkeitsmatrix

```
AP-01  ←──────────────────── (keine)
AP-02  ← AP-01
AP-03  ← AP-01, AP-02
AP-04  ← AP-01, AP-02, AP-03
AP-05  ← AP-04
AP-06  ← AP-04, AP-02
AP-07  ← AP-04, AP-02
AP-08  ← AP-04, AP-02, AP-03
AP-09  ← AP-04, AP-02
AP-10  ← AP-04
AP-11  ← AP-04
AP-12  ← AP-01, AP-02  [+ AP-04 für Belegungsanzeige]
AP-13  ← AP-12, AP-06
AP-14  ← AP-11, AP-12, AP-08
AP-15  ← AP-06, AP-13, AP-08  [+ AP-18 für Sperre]
AP-16  ← AP-06, AP-15, AP-10  [+ AP-18 für Sperre]
AP-17  ← AP-11, AP-07, AP-08
AP-18  ← AP-08, AP-10, AP-04  [+ AP-09 optional]
AP-19  ← AP-04, AP-11, AP-10  [+ AP-15/16 für Hinweis]
AP-20  ← AP-04, AP-10, AP-19
AP-21  ← AP-04, AP-07
```

---

## Empfohlene Lieferstufen (Sprints/Releases)

| Stufe | Inhalt | Voraussetzung |
|-------|--------|---------------|
| **R1 – Fundament** | AP-01, AP-02, AP-03 | – |
| **R2 – Stammdaten** | AP-04, AP-07, AP-08 | R1 |
| **R3 – Basismodule I** | AP-05, AP-06, AP-09, AP-10, AP-11 | R2 |
| **R4 – Basismodule II** | AP-12, AP-13, AP-14 | R3 |
| **R5 – Operative Module** | AP-15, AP-17, AP-18, AP-19 | R4 |
| **R6 – Verteilung & Finanzen** | AP-16, AP-20 | R5 |
| **R7 – Statistik & BI** | AP-21 | R2+ |

---

## Gesamtschätzung

| Arbeitspaket | PT min | PT max |
|---|---|---|
| AP-01 Systeminfrastruktur | 10 | 20 |
| AP-02 Rollen & Rechte | 23 | 35 |
| AP-03 Historisierung | 6 | 10 |
| AP-04 Stammdaten & Registrierung | 28 | 46 |
| AP-05 AZR-Schnittstelle | 10 | 15 |
| AP-06 Sonderstatus | 30 | 50 |
| AP-07 Übersichten & Detailansicht | 20 | 33 |
| AP-08 Freitext-Dokumentation | 6 | 8 |
| AP-09 Terminverwaltung | 16 | 27 |
| AP-10 Vordrucke & Formulare | 7 | 14 |
| AP-11 Bewohnerausweis | 6 | 11 |
| AP-12 Liegenschaftsmanagement | 20 | 35 |
| AP-13 Belegungsplanung | 21 | 30 |
| AP-14 Anwesenheitserfassung | 10 | 15 |
| AP-15 Verlegungsplanung | 8 | 10 |
| AP-16 Landesinterne Verteilung | 45 | 65 |
| AP-17 Bewachung & Sicherheit | 16 | 28 |
| AP-18 Krankenstation | 35 | 55 |
| AP-19 Sachmittel & Leistungen | 25 | 40 |
| AP-20 Soziale Leistungen & Kasse | 30 | 50 |
| AP-21 Statistiken & Reporting | 5 | 10 |
| **Gesamt** | **377** | **607** |

> ⚠️ Bereits implementierte Anforderungen (Spalte „Bereits implementiert" = X in der CSV) reduzieren den Aufwand erheblich. Die obigen Zahlen beziehen sich auf den **Gesamtaufwand** inkl. bereits gelieferter Teile; für offene Restarbeiten sind die CSV-Spalten „Schätzung in PT min/max (2)" maßgeblich.
