package de.acksmi.firmapp.firmpass_backend_project.repository;

import de.acksmi.firmapp.firmpass_backend_project.model.Role;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoleRepository extends JpaRepository<Role, Long> {
    Role findByName(String name);
}
