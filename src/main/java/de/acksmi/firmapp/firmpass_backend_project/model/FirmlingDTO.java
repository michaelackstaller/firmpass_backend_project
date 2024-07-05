package de.acksmi.firmapp.firmpass_backend_project.model;

public class FirmlingDTO {
    private String firstName;
    private String lastName;
    private String groupPreferences;

    // Konstruktoren
    public FirmlingDTO() {}

    public FirmlingDTO(String firstName, String lastName, String groupPreferences) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.groupPreferences = groupPreferences;
    }

    // Getter und Setter
    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getGroupPreferences() {
        return groupPreferences;
    }

    public void setGroupPreferences(String groupPreferences) {
        this.groupPreferences = groupPreferences;
    }
}
