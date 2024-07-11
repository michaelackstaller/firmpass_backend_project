package de.acksmi.firmapp.firmpass_backend_project.controller;

import de.acksmi.firmapp.firmpass_backend_project.model.Firmling;
import de.acksmi.firmapp.firmpass_backend_project.model.Firmstunde;
import de.acksmi.firmapp.firmpass_backend_project.model.connections.FirmlingFirmstunde;
import de.acksmi.firmapp.firmpass_backend_project.model.dtos.FirmlingDTO;
import de.acksmi.firmapp.firmpass_backend_project.repository.FirmlingFirmstundeRepository;
import de.acksmi.firmapp.firmpass_backend_project.service.FirmlingService;
import de.acksmi.firmapp.firmpass_backend_project.service.FirmstundeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Async;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

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

    @PutMapping("/{id}")
    public ResponseEntity<Firmstunde> updateFirmstunde(@PathVariable Long id, @RequestBody Firmstunde firmstundeDetails) {
        Firmstunde firmstunde = firmstundeService.findById(id);
        if (firmstunde == null) {
            return ResponseEntity.status(404).body(null);
        }
        firmstunde.setName(firmstundeDetails.getName());
        firmstunde.setContent(firmstundeDetails.getContent());
        firmstunde.setWeek(firmstundeDetails.getWeek());
        final Firmstunde updatedFirmstunde = firmstundeService.updateFirmstunde(firmstunde);
        return ResponseEntity.ok(updatedFirmstunde);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Firmstunde> getFirmstunde(@PathVariable Long id) {
        Firmstunde firmstunde = firmstundeService.findById(id);
        if (firmstunde == null) {
            return ResponseEntity.status(404).body(null);
        }
        return ResponseEntity.ok(firmstunde);
    }

    @PutMapping("/complete/{firmlingId}/{firmstundeId}")
    public ResponseEntity<?> markAsCompleted(@PathVariable Long firmlingId, @PathVariable Long firmstundeId) {
        FirmlingFirmstunde firmlingFirmstunde = firmlingFirmstundeRepository.findByFirmlingIdAndFirmstundeId(firmlingId, firmstundeId);
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
        return ResponseEntity.ok("Firmstunde" + firmstunde.getName() + " für Firmling " + firmling.getFirstName() + " als erledigt markiert!");
    }

    @PutMapping("/uncomplete/{firmlingId}/{firmstundeId}")
    public ResponseEntity<?> markAsUnCompleted(@PathVariable Long firmlingId, @PathVariable Long firmstundeId) {
        FirmlingFirmstunde firmlingFirmstunde = firmlingFirmstundeRepository.findByFirmlingIdAndFirmstundeId(firmlingId, firmstundeId);
        if (firmlingFirmstunde != null) {
            firmlingFirmstunde.setCompleted(false);
            firmlingFirmstundeRepository.deleteByFirmlingIdAndFirmstundeId(firmlingId, firmstundeId);
            return ResponseEntity.ok().build();
        }
        return ResponseEntity.status(404).body("Eintrag existiert nicht");
    }

    @Async
    @DeleteMapping("/{id}")
    public void deleteFirmstunde(@PathVariable Long id) {
        CompletableFuture<Void> future = CompletableFuture.runAsync(() -> {
            try {
                Thread.sleep(300);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            firmstundeService.deleteFirmstunde(id);
        });

        for(FirmlingDTO firmling : firmlingService.findAllFirmlinge()) {
            firmlingFirmstundeRepository.deleteByFirmlingIdAndFirmstundeId(firmling.getId(), id);
        }
        future.join();
    }

    @GetMapping("/list/{firmlingId}")
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
            map.put("firmstundeId", firmstunde.getId());
            map.put("firmstundeName", firmstunde.getName());
            map.put("completed", completedMap.getOrDefault(firmstunde.getId(), false));
            return map;
        }).collect(Collectors.toList());
        return ResponseEntity.ok(response);
    }
}
