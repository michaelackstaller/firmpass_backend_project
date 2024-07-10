package de.acksmi.firmapp.firmpass_backend_project.controller;

import de.acksmi.firmapp.firmpass_backend_project.model.Firmling;
import de.acksmi.firmapp.firmpass_backend_project.model.Firmstunde;
import de.acksmi.firmapp.firmpass_backend_project.model.User;
import de.acksmi.firmapp.firmpass_backend_project.model.connections.FirmlingFirmsonntag;
import de.acksmi.firmapp.firmpass_backend_project.model.connections.FirmlingFirmstunde;
import de.acksmi.firmapp.firmpass_backend_project.model.dtos.FirmlingDTO;
import de.acksmi.firmapp.firmpass_backend_project.repository.FirmlingFirmsonntagRepository;
import de.acksmi.firmapp.firmpass_backend_project.repository.FirmlingFirmstundeRepository;
import de.acksmi.firmapp.firmpass_backend_project.service.FirmlingService;
import de.acksmi.firmapp.firmpass_backend_project.service.FirmstundeService;
import de.acksmi.firmapp.firmpass_backend_project.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api")
public class FirmlingController {

    @Autowired
    private FirmlingService firmlingService;
    @Autowired
    private FirmstundeService firmstundeService;
    @Autowired
    private FirmlingFirmstundeRepository firmlingFirmstundeRepository;
    @Autowired
    private FirmlingFirmsonntagRepository firmlingFirmsonntagRepository;

    @Autowired
    private UserService userService;

    @PreAuthorize("hasRole('ROLE_USER')")
    @GetMapping("/firmlinge")
    public ResponseEntity<List<FirmlingDTO>> getAllFirmlinge() {
        List<FirmlingDTO> firmlinge = firmlingService.findAllFirmlinge();
        return ResponseEntity.ok(firmlinge);
    }

    @PreAuthorize("hasRole('ROLE_USER')")
    @GetMapping("/firmlinge/{id}")
    public ResponseEntity<FirmlingDTO> getFirmling(@PathVariable Long id) {
        FirmlingDTO firmlinge = firmlingService.convertToDTO(firmlingService.findById(id));
        return ResponseEntity.ok(firmlinge);
    }

    @PreAuthorize("hasRole('ROLE_USER')")
    @DeleteMapping("/deleteFirmling/{id}")
    public ResponseEntity<Void> deleteFirmling(@PathVariable Long id) {
        Firmling firmling = firmlingService.findById(id);
        if (firmling != null) {
            // Löschen der abhängigen FirmlingFirmstunde Einträge
            List<FirmlingFirmstunde> firmlingFirmstunden = firmling.getFirmlingFirmstunden();
            for (FirmlingFirmstunde firmlingFirmstunde : firmlingFirmstunden) {
                firmlingFirmstunde.setFirmling(null);
                firmlingFirmstundeRepository.delete(firmlingFirmstunde);

            }
            // Löschen der abhängigen FirmlingFirmsonntag Einträge
            List<FirmlingFirmsonntag> firmlingFirmsonntage = firmling.getFirmlingFirmsonntage();
            for (FirmlingFirmsonntag firmlingFirmsonntag : firmlingFirmsonntage) {
                firmlingFirmsonntag.setFirmling(null);
                //firmlingFirmsonntagRepository.delete(firmlingFirmsonntag);
            }
            User user = firmling.getUser();
            firmling.setUser(null);
            //firmlingService.saveFirmling(firmling); // Save changes to Firmling

            if (user != null) {
                user.setFirmling(null);
                userService.saveUser(user); // Save changes to User
            }

           // firmlingService.deleteFirmling(id); // Lösche den Firmling

            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.status(404).build();
    }

    @PreAuthorize("hasRole('ROLE_USER')")
    @DeleteMapping("/deleteUser/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable Long id) {
        userService.deleteUser(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{firmlingId}/firmstunden")
    public ResponseEntity<?> getFirmstundenForFirmling(@PathVariable Long firmlingId) {
        Firmling firmling = firmlingService.findById(firmlingId);
        if (firmling == null) {
            return ResponseEntity.status(404).body("Firmling nicht gefunden");
        }
        List<Firmstunde> allFirmstunden = firmstundeService.getAllFirmstunden();
        List<FirmlingFirmstunde> completedFirmstunden = firmling.getFirmlingFirmstunden();
        Map<Long, Boolean> completedMap = completedFirmstunden.stream()
                .collect(Collectors.toMap(f -> f.getFirmstunde().getId(), FirmlingFirmstunde::isCompleted));
        List<Map<String, Object>> response = allFirmstunden.stream().map(firmstunde -> {
            Map<String, Object> map = new HashMap<>();
            map.put("firmstundeName", firmstunde.getName());
            map.put("completed", completedMap.getOrDefault(firmstunde.getId(), false));
            return map;
        }).collect(Collectors.toList());
        return ResponseEntity.ok(response);
    }
}