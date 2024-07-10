package de.acksmi.firmapp.firmpass_backend_project.repository;

import de.acksmi.firmapp.firmpass_backend_project.model.connections.FirmlingFirmstunde;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface FirmlingFirmstundeRepository extends JpaRepository<FirmlingFirmstunde, Long> {

    @Query("SELECT f FROM FirmlingFirmstunde f WHERE f.firmling.id = :firmlingId AND f.firmstunde.id = :firmstundeId")
    FirmlingFirmstunde findByFirmlingIdAndFirmstundeId(@Param("firmlingId") Long firmlingId, @Param("firmstundeId") Long firmstundeId);

}
