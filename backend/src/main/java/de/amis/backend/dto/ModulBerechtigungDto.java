package de.amis.backend.dto;

import de.amis.backend.model.BerechtigungsTyp;
import de.amis.backend.model.ModulName;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class ModulBerechtigungDto {

    @NotNull
    private ModulName modul;

    @NotNull
    private BerechtigungsTyp berechtigung;
}
