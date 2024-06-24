package de.acksmi.firmapp.firmpass_backend_project.repository;

import de.acksmi.firmapp.firmpass_backend_project.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
    User findByUsername(String username);
}