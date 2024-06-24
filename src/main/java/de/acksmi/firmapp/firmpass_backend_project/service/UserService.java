package de.acksmi.firmapp.firmpass_backend_project.service;

import de.acksmi.firmapp.firmpass_backend_project.model.Role;
import de.acksmi.firmapp.firmpass_backend_project.model.User;
import de.acksmi.firmapp.firmpass_backend_project.repository.RoleRepository;
import de.acksmi.firmapp.firmpass_backend_project.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public User saveUser(User user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));

        Role role = roleRepository.findByName("ROLE_ADMIN");
        if(role == null) {
            role = checkRoleExist();
        }
        user.setRoles(List.of(role));
        user.setIsLocked(false);
        return userRepository.save(user);
    }

    public User findByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    private Role checkRoleExist() {
        Role role = new Role();
        role.setName("ROLE_USER");
        return roleRepository.save(role);
    }
}
