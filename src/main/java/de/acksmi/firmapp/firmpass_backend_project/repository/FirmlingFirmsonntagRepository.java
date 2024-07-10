package de.acksmi.firmapp.firmpass_backend_project.repository;

import de.acksmi.firmapp.firmpass_backend_project.model.connections.FirmlingFirmsonntag;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FirmlingFirmsonntagRepository extends JpaRepository<FirmlingFirmsonntag, Long> {
}
