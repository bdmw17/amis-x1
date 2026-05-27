package de.amis.backend.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "modul_berechtigung",
        uniqueConstraints = @UniqueConstraint(columnNames = {"rolle_id", "modul", "berechtigung"}))
@Getter
@Setter
@NoArgsConstructor
public class ModulBerechtigung {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false)
    @JoinColumn(name = "rolle_id", nullable = false)
    private Rolle rolle;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 50)
    private ModulName modul;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private BerechtigungsTyp berechtigung;

    public ModulBerechtigung(Rolle rolle, ModulName modul, BerechtigungsTyp berechtigung) {
        this.rolle = rolle;
        this.modul = modul;
        this.berechtigung = berechtigung;
    }
}
