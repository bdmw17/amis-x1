package de.amis.backend.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "afa")
@Getter
@Setter
@NoArgsConstructor
public class AfA extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false, length = 20)
    private String kuerzel;

    @Column(nullable = false, length = 200)
    private String name;

    @Column(length = 200)
    private String ort;

    @Column(nullable = false)
    private boolean aktiv = true;
}
