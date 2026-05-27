package de.amis.backend.repository;

import de.amis.backend.model.Benutzer;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface BenutzerRepository extends JpaRepository<Benutzer, Long> {
    Optional<Benutzer> findByBenutzername(String benutzername);
    boolean existsByBenutzername(String benutzername);
}
