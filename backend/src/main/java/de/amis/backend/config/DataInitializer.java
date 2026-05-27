package de.amis.backend.config;

import de.amis.backend.model.Benutzer;
import de.amis.backend.model.Rolle;
import de.amis.backend.repository.BenutzerRepository;
import de.amis.backend.repository.RolleRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

/**
 * Erstellt beim ersten Start einen Admin-Benutzer mit der ADMIN-Rolle,
 * sofern noch keiner vorhanden ist.
 */
@Component
@RequiredArgsConstructor
@Slf4j
public class DataInitializer implements CommandLineRunner {

    private final BenutzerRepository benutzerRepository;
    private final RolleRepository rolleRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    @Transactional
    public void run(String... args) {
        if (benutzerRepository.existsByBenutzername("admin")) {
            return;
        }

        Rolle adminRolle = rolleRepository.findByName("ADMIN")
                .orElseThrow(() -> new IllegalStateException(
                        "ADMIN-Rolle fehlt in der Datenbank – Flyway-Migration V3 nicht ausgeführt?"));

        Benutzer admin = new Benutzer();
        admin.setBenutzername("admin");
        admin.setPasswortHash(passwordEncoder.encode("changeme"));
        admin.setVorname("System");
        admin.setNachname("Admin");
        admin.setAktiv(true);
        admin.getRollen().add(adminRolle);

        benutzerRepository.save(admin);
        log.info("Admin-Benutzer angelegt (Benutzername: admin, Passwort: changeme – bitte sofort ändern!)");
    }
}
