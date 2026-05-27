package de.amis.backend.dto;

import de.amis.backend.model.AfA;
import lombok.Data;

@Data
public class AfADto {
    private Long id;
    private String kuerzel;
    private String name;
    private String ort;
    private boolean aktiv;

    public static AfADto from(AfA afa) {
        AfADto dto = new AfADto();
        dto.setId(afa.getId());
        dto.setKuerzel(afa.getKuerzel());
        dto.setName(afa.getName());
        dto.setOrt(afa.getOrt());
        dto.setAktiv(afa.isAktiv());
        return dto;
    }
}
