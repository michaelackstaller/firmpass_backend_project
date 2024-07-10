package de.acksmi.firmapp.firmpass_backend_project.model.connections;

import de.acksmi.firmapp.firmpass_backend_project.model.Firmling;
import de.acksmi.firmapp.firmpass_backend_project.model.Firmstunde;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "firmling_firmstunde")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class FirmlingFirmstunde {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "firmling_id")
    private Firmling firmling;

    @ManyToOne
    @JoinColumn(name = "firmstunde_id")
    private Firmstunde firmstunde;

    private boolean completed;
}