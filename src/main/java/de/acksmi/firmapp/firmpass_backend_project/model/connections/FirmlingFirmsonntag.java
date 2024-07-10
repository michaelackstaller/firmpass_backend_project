package de.acksmi.firmapp.firmpass_backend_project.model.connections;

import de.acksmi.firmapp.firmpass_backend_project.model.Firmling;
import de.acksmi.firmapp.firmpass_backend_project.model.Firmsonntag;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "firmling_firmsonntag")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class FirmlingFirmsonntag {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "firmling_id")
    private Firmling firmling;

    @ManyToOne
    @JoinColumn(name = "firmsonntag_id")
    private Firmsonntag firmsonntag;

    private boolean completed;
}
