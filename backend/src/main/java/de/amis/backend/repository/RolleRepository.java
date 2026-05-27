package de.amis.backend.repository;

import de.amis.backend.model.Rolle;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RolleRepository extends JpaRepository<Rolle, Long> {
    Optional<Rolle> findByName(String name);
    boolean existsByName(String name);
}
