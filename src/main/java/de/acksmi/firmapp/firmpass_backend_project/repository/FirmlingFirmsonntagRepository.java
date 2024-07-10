package de.acksmi.firmapp.firmpass_backend_project.repository;

import de.acksmi.firmapp.firmpass_backend_project.model.connections.FirmlingFirmsonntag;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface FirmlingFirmsonntagRepository extends JpaRepository<FirmlingFirmsonntag, Long> {

    @Query("SELECT f FROM FirmlingFirmsonntag f WHERE f.firmling.id = :firmlingId AND f.firmsonntag.id = :firmsonntagId")
    FirmlingFirmsonntag findByFirmlingIdAndFirmsonntagId(@Param("firmlingId") Long firmlingId, @Param("firmsonntagId") Long firmsonntagId);

}
