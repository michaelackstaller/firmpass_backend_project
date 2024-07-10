package de.acksmi.firmapp.firmpass_backend_project.controller;

import de.acksmi.firmapp.firmpass_backend_project.model.Firmling;
import de.acksmi.firmapp.firmpass_backend_project.model.Firmsonntag;
import de.acksmi.firmapp.firmpass_backend_project.model.Firmstunde;
import de.acksmi.firmapp.firmpass_backend_project.model.connections.FirmlingFirmsonntag;
import de.acksmi.firmapp.firmpass_backend_project.model.connections.FirmlingFirmstunde;
import de.acksmi.firmapp.firmpass_backend_project.model.connections.FirmlingFirmstundeId;
import de.acksmi.firmapp.firmpass_backend_project.repository.FirmlingFirmsonntagRepository;
import de.acksmi.firmapp.firmpass_backend_project.repository.FirmlingFirmstundeRepository;
import de.acksmi.firmapp.firmpass_backend_project.service.FirmlingService;
import de.acksmi.firmapp.firmpass_backend_project.service.FirmsonntagService;
import de.acksmi.firmapp.firmpass_backend_project.service.FirmstundeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/firmstunde")
public class FirmstundeController {

    @Autowired
    private FirmstundeService firmstundeService;
    @Autowired
    private FirmlingService firmlingService;
    @Autowired
    private FirmlingFirmstundeRepository firmlingFirmstundeRepository;

    @GetMapping
    public List<Firmstunde> getAllFirmstunden() {
        return firmstundeService.getAllFirmstunden();
    }

    @PostMapping
    public Firmstunde createFirmstunde(@RequestBody Firmstunde firmstunde) {
        return firmstundeService.createFirmstunde(firmstunde);
    }

    @PutMapping("/{firmlingId}/{firmstundeId}/complete")
    public ResponseEntity<?> markAsCompleted(@PathVariable Long firmlingId, @PathVariable Long firmstundeId) {
        FirmlingFirmstunde firmlingFirmstunde = firmlingFirmstundeRepository.findByFirmlingIdAndFirmsonntagId(firmlingId, firmstundeId);
        if (firmlingFirmstunde != null) {
            firmlingFirmstunde.setCompleted(true);
            firmlingFirmstundeRepository.save(firmlingFirmstunde);
            return ResponseEntity.ok("Eintrag existierte bereits, wurde erledigt markiert.");
        }
        Firmling firmling = firmlingService.findById(firmlingId);
        Firmstunde firmstunde = firmstundeService.findById(firmstundeId);
        if (firmling == null) {
            return ResponseEntity.status(404).body("Firmling nicht gefunden");
        }
        if (firmstunde == null) {
            return ResponseEntity.status(404).body("Firmstunde nicht gefunden");
        }
        firmstundeService.markAsCompleted(firmling, firmstunde);
        return ResponseEntity.ok("Firmsonntag" + firmstunde.getName() + " für Firmling " + firmling.getFirstName() + " als erledigt markiert!");
    }

    @PutMapping("/{firmlingId}/{firmsonntagId}/uncomplete")
    public ResponseEntity<?> markAsUnCompleted(@PathVariable Long firmlingId, @PathVariable Long firmsonntagId) {
        FirmlingFirmstunde firmlingFirmstunde = firmlingFirmstundeRepository.findByFirmlingIdAndFirmsonntagId(firmlingId, firmsonntagId);
        if (firmlingFirmstunde != null) {
            firmlingFirmstunde.setCompleted(false);
            firmlingFirmstundeRepository.save(firmlingFirmstunde);
            return ResponseEntity.ok("Eintrag existierte bereits, wurde unerledigt markiert.");
        }
        return ResponseEntity.status(404).body("Eintrag existiert nicht");
    }

    @DeleteMapping("/{id}")
    public void deleteFirmstunde(@PathVariable Long id) {
        firmstundeService.deleteFirmstunde(id);
    }
}
