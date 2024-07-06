package de.acksmi.firmapp.firmpass_backend_project.model;

import lombok.Getter;
import lombok.Setter;

import java.util.Set;

@Getter
@Setter
public class FirmlingDTO {
    private String firstName;
    private String lastName;
    private String groupPreferences;
    private Set<String> availableTimeSlots;

    public FirmlingDTO(String firstName, String lastName, String groupPreferences, Set<String> availableTimeSlots) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.groupPreferences = groupPreferences;
        this.availableTimeSlots = availableTimeSlots;
    }
}
