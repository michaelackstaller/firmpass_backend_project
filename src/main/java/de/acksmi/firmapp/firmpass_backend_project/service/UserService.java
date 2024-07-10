package de.acksmi.firmapp.firmpass_backend_project.service;

import de.acksmi.firmapp.firmpass_backend_project.model.Role;
import de.acksmi.firmapp.firmpass_backend_project.model.User;
import de.acksmi.firmapp.firmpass_backend_project.repository.RoleRepository;
import de.acksmi.firmapp.firmpass_backend_project.repository.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public User saveNewUser(User user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));

        Role role = roleRepository.findByName("ROLE_USER");
        if(role == null) {
            role = checkRoleExist();
        }
        user.setRoles(List.of(role));
        user.setIsLocked(false);
        return userRepository.save(user);
    }

    public User saveUser(User user) {
        return userRepository.save(user);
    }

    @Transactional
    public User updateUser(Long id, User updatedUser) {
        Optional<User> optionalUser = userRepository.findById(id);
        if (optionalUser.isPresent()) {
            User existingUser = optionalUser.get();
            existingUser.setUsername(updatedUser.getUsername());

            if (updatedUser.getPassword() != null && !updatedUser.getPassword().isEmpty()) {
                existingUser.setPassword(passwordEncoder.encode(updatedUser.getPassword()));
            }

            existingUser.setIsLocked(updatedUser.getIsLocked());

            if (updatedUser.getRoles() != null && !updatedUser.getRoles().isEmpty()) {
                existingUser.setRoles(updatedUser.getRoles());
            }

            return userRepository.save(existingUser);
        } else {
            throw new IllegalArgumentException("User with id " + id + " does not exist.");
        }
    }

    public User findByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    private Role checkRoleExist() {
        Role role = new Role();
        role.setName("ROLE_USER");
        return roleRepository.save(role);
    }

    @Transactional
    public void deleteUser(Long id) {
        if (userRepository.existsById(id)) {
            System.out.println("INFORMATION");
            System.out.println("Deleting Firmling with id: {}" + id);
            userRepository.deleteById(id);
        } else {
            throw new IllegalArgumentException("User with id " + id + " does not exist.");
        }
    }
}
