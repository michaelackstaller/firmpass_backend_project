package de.acksmi.firmapp.firmpass_backend_project.model.connections;

import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.util.Objects;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Embeddable
public class FirmlingFirmsonntagId implements Serializable {
    private Long firmling;
    private Long firmsonntag;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        FirmlingFirmsonntagId that = (FirmlingFirmsonntagId) o;
        return Objects.equals(firmling, that.firmling) && Objects.equals(firmsonntag, that.firmsonntag);
    }

    @Override
    public int hashCode() {
        return Objects.hash(firmling, firmsonntag);
    }
}