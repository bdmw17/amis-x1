package de.amis.backend.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.util.List;

@Data
public class BenutzerRequest {

    @NotBlank
    private String benutzername;

    @Size(min = 8, message = "Passwort muss mindestens 8 Zeichen haben")
    private String passwort;

    private String vorname;
    private String nachname;
    private Long afaId;
    private List<Long> rollenIds = List.of();
    private boolean aktiv = true;
}
