package de.acksmi.firmapp.firmpass_backend_project.model.connections;

import lombok.Getter;
import lombok.Setter;
import java.io.Serializable;
import java.util.Objects;

@Getter
@Setter
public class FirmlingFirmstundeId implements Serializable {
    private Long firmling;
    private Long firmstunde;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        FirmlingFirmstundeId that = (FirmlingFirmstundeId) o;
        return Objects.equals(firmling, that.firmling) && Objects.equals(firmstunde, that.firmstunde);
    }

    @Override
    public int hashCode() {
        return Objects.hash(firmling, firmstunde);
    }
}