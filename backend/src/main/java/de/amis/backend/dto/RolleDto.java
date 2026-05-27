package de.amis.backend.dto;

import de.amis.backend.model.ModulBerechtigung;
import de.amis.backend.model.Rolle;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

import java.util.List;
import java.util.stream.Collectors;

@Data
public class RolleDto {

    private Long id;

    @NotBlank
    private String name;

    private String beschreibung;

    @Valid
    private List<ModulBerechtigungDto> berechtigungen = List.of();

    public static RolleDto from(Rolle rolle) {
        RolleDto dto = new RolleDto();
        dto.setId(rolle.getId());
        dto.setName(rolle.getName());
        dto.setBeschreibung(rolle.getBeschreibung());
        dto.setBerechtigungen(
                rolle.getBerechtigungen().stream()
                        .map(mb -> {
                            ModulBerechtigungDto mbDto = new ModulBerechtigungDto();
                            mbDto.setModul(mb.getModul());
                            mbDto.setBerechtigung(mb.getBerechtigung());
                            return mbDto;
                        })
                        .collect(Collectors.toList())
        );
        return dto;
    }
}
