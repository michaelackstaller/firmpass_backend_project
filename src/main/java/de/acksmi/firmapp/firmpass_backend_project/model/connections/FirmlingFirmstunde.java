package de.acksmi.firmapp.firmpass_backend_project.model.connections;

import de.acksmi.firmapp.firmpass_backend_project.model.Firmling;
import de.acksmi.firmapp.firmpass_backend_project.model.Firmsonntag;
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
@IdClass(FirmlingFirmstundeId.class)
public class FirmlingFirmstunde {

    @Id
    @ManyToOne
    @JoinColumn(name = "firmling_id")
    private Firmling firmling;

    @Id
    @ManyToOne
    @JoinColumn(name = "firmstunde_id")
    private Firmstunde firmstunde;

    private boolean completed;
}
