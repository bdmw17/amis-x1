package de.amis.backend.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.Instant;

/**
 * Gemeinsame Basisklasse für alle persistierten Entitäten.
 * Liefert:
 *  - Optimistic Locking via @Version (AP-01: kein Lost-Update bei parallelem Zugriff)
 *  - Audit-Felder erstellt/geändert von/am (Basis für AP-03)
 */
@Getter
@Setter
@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
public abstract class BaseEntity {

    @Version
    @Column(nullable = false)
    private Long version = 0L;

    @CreatedDate
    @Column(name = "erstellt_am", nullable = false, updatable = false)
    private Instant erstelltAm;

    @LastModifiedDate
    @Column(name = "geaendert_am", nullable = false)
    private Instant geaendertAm;

    @CreatedBy
    @Column(name = "erstellt_von", updatable = false, length = 100)
    private String erstelltVon;

    @LastModifiedBy
    @Column(name = "geaendert_von", length = 100)
    private String geaendertVon;
}
