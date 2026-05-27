package de.amis.backend.dto;

import de.amis.backend.model.Benutzer;
import lombok.Data;

import java.util.List;
import java.util.stream.Collectors;

@Data
public class BenutzerResponse {

    private Long id;
    private String benutzername;
    private String vorname;
    private String nachname;
    private AfADto afa;
    private List<RolleDto> rollen;
    private boolean aktiv;

    public static BenutzerResponse from(Benutzer b) {
        BenutzerResponse dto = new BenutzerResponse();
        dto.setId(b.getId());
        dto.setBenutzername(b.getBenutzername());
        dto.setVorname(b.getVorname());
        dto.setNachname(b.getNachname());
        dto.setAktiv(b.isAktiv());
        if (b.getAfa() != null) {
            dto.setAfa(AfADto.from(b.getAfa()));
        }
        dto.setRollen(b.getRollen().stream()
                .map(RolleDto::from)
                .collect(Collectors.toList()));
        return dto;
    }
}
