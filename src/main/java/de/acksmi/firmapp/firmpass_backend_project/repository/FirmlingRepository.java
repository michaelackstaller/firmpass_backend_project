package de.acksmi.firmapp.firmpass_backend_project.repository;

import de.acksmi.firmapp.firmpass_backend_project.model.Firmling;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FirmlingRepository extends JpaRepository<Firmling, Long> {
}
