package de.acksmi.firmapp.firmpass_backend_project.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.Set;

@Entity
@Table(name = "firmling")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Firmling {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String firstName;
    private String lastName;
    private String gender;
    private LocalDate birthDate;
    private boolean vegan;
    private boolean vegetarian;
    private String allergies;

    @ElementCollection
    private Set<String> availableTimeSlots;

    private String groupPreferences;

    @OneToOne
    @JoinColumn(name = "user_id")
    private User user;
}
