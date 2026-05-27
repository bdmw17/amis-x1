package de.amis.backend.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "benutzer")
@Getter
@Setter
@NoArgsConstructor
public class Benutzer extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false, length = 100)
    private String benutzername;

    @Column(name = "passwort_hash", nullable = false, length = 255)
    private String passwortHash;

    @Column(length = 100)
    private String vorname;

    @Column(length = 100)
    private String nachname;

    @ManyToOne
    @JoinColumn(name = "afa_id")
    private AfA afa;

    @Column(nullable = false)
    private boolean aktiv = true;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "benutzer_rolle",
            joinColumns = @JoinColumn(name = "benutzer_id"),
            inverseJoinColumns = @JoinColumn(name = "rolle_id")
    )
    private Set<Rolle> rollen = new HashSet<>();
}
