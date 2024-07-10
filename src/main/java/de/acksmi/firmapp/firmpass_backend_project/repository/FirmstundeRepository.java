package de.acksmi.firmapp.firmpass_backend_project.repository;

import de.acksmi.firmapp.firmpass_backend_project.model.Firmstunde;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FirmstundeRepository extends JpaRepository<Firmstunde, Long> {
}
