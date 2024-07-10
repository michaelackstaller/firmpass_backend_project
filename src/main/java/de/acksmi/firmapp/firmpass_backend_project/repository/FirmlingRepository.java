package de.acksmi.firmapp.firmpass_backend_project.repository;

import de.acksmi.firmapp.firmpass_backend_project.model.Firmling;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface FirmlingRepository extends JpaRepository<Firmling, Long> {

    @Modifying
    @Query("delete from Firmling f where f.id = :id")
    void deleteFirmlingById(@Param("id") Long id);

    Optional<Firmling> findById(Long id);
}
