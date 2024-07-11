package de.acksmi.firmapp.firmpass_backend_project.model;

import de.acksmi.firmapp.firmpass_backend_project.model.connections.FirmlingFirmsonntag;
import de.acksmi.firmapp.firmpass_backend_project.model.connections.FirmlingFirmstunde;
import de.acksmi.firmapp.firmpass_backend_project.service.UserService;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

import java.time.LocalDate;
import java.util.List;
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
    private String nutrition;
    private String allergies;
    private Firmgruppe firmgruppe;
    private boolean ausflugDone;
    private boolean sozialeAktionDone;
    private int gottediensteDone;
    private int stundenDone;
    private int sonntageDone;

    @ElementCollection
    @CollectionTable(name = "firmling_available_time_slots", joinColumns = @JoinColumn(name = "firmling_id"))
    @Column(name = "time_slot")
    private Set<String> availableTimeSlots;

    private String groupPreferences;

    @OneToOne(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "user_id")
    private User user;

    @OneToMany(mappedBy = "firmling", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<FirmlingFirmstunde> firmlingFirmstunden;

    @OneToMany(mappedBy = "firmling", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<FirmlingFirmsonntag> firmlingFirmsonntage;
}