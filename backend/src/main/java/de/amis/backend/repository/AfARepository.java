package de.amis.backend.repository;

import de.amis.backend.model.AfA;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AfARepository extends JpaRepository<AfA, Long> {
    List<AfA> findByAktivTrue();
}
