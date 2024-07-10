package de.acksmi.firmapp.firmpass_backend_project.model.dtos;

import de.acksmi.firmapp.firmpass_backend_project.model.Firmgruppe;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;
import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class FirmlingDTO {

    private Long id;
    private String firstName;
    private String lastName;
    private String gender;
    private LocalDate birthDate;
    private String nutrition;
    private String allergies;
    private Firmgruppe firmgruppe;
    private Set<String> availableTimeSlots;
    private String groupPreferences;
    private List<FirmstundeDTO> completedFirmstunden;
    private List<FirmsonntagDTO> completedFirmsonntage;
}
