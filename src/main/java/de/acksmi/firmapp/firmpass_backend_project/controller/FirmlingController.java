package de.acksmi.firmapp.firmpass_backend_project.controller;

import de.acksmi.firmapp.firmpass_backend_project.model.Firmling;
import de.acksmi.firmapp.firmpass_backend_project.model.User;
import de.acksmi.firmapp.firmpass_backend_project.model.dtos.FirmlingDTO;
import de.acksmi.firmapp.firmpass_backend_project.service.FirmlingService;
import de.acksmi.firmapp.firmpass_backend_project.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
public class FirmlingController {

    @Autowired
    private FirmlingService firmlingService;

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
            User user = firmling.getUser();
            firmling.setUser(null);
            firmlingService.saveFirmling(firmling); // Save changes to Firmling
            if (user != null) {
                user.setFirmling(null);
            } else {
                firmlingService.deleteFirmling(id); // Just delete Firmling if user is already null
                System.out.println("DEBUG1");
            }
            System.out.println("DEBUG4");
            return ResponseEntity.noContent().build();
        }
        System.out.println("DEBUG5");
        return ResponseEntity.status(404).build();
    }

    @PutMapping("/firmling/{id}")
    public ResponseEntity<?> updateFirmling(@PathVariable Long id, @RequestBody FirmlingDTO firmlingDTO) {
        Firmling firmling = firmlingService.findById(id);
        if (firmling == null) {
            return ResponseEntity.notFound().build();
        }

        firmling.setFirstName(firmlingDTO.getFirstName());
        firmling.setLastName(firmlingDTO.getLastName());
        firmling.setGender(firmlingDTO.getGender());
        firmling.setBirthDate(firmlingDTO.getBirthDate());
        firmling.setNutrition(firmlingDTO.getNutrition());
        firmling.setAllergies(firmlingDTO.getAllergies());
        firmling.setFirmgruppe(firmlingDTO.getFirmgruppe());
        firmling.setAvailableTimeSlots(firmlingDTO.getAvailableTimeSlots());
        firmling.setGroupPreferences(firmlingDTO.getGroupPreferences());

        firmlingService.saveFirmling(firmling);
        return ResponseEntity.ok(firmling);
    }


    @PreAuthorize("hasRole('ROLE_USER')")
    @DeleteMapping("/deleteUser/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable Long id) {
        userService.deleteUser(id);
        return ResponseEntity.noContent().build();
    }
}

