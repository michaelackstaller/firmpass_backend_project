package de.acksmi.firmapp.firmpass_backend_project.controller;

import de.acksmi.firmapp.firmpass_backend_project.model.Firmling;
import de.acksmi.firmapp.firmpass_backend_project.model.Firmsonntag;
import de.acksmi.firmapp.firmpass_backend_project.model.Firmstunde;
import de.acksmi.firmapp.firmpass_backend_project.model.connections.FirmlingFirmsonntag;
import de.acksmi.firmapp.firmpass_backend_project.model.dtos.FirmlingDTO;
import de.acksmi.firmapp.firmpass_backend_project.repository.FirmlingFirmsonntagRepository;
import de.acksmi.firmapp.firmpass_backend_project.service.FirmlingService;
import de.acksmi.firmapp.firmpass_backend_project.service.FirmsonntagService;
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
@RequestMapping("/api/firmsonntag")
public class FirmsonntagController {

    @Autowired
    private FirmsonntagService firmsonntagService;
    @Autowired
    private FirmlingService firmlingService;
    @Autowired
    private FirmlingFirmsonntagRepository firmlingFirmsonntagRepository;

    @GetMapping
    public List<Firmsonntag> getAllFirmsonntage() {
        return firmsonntagService.getAllFirmsonntage();
    }

    @PostMapping
    public Firmsonntag createFirmsonntag(@RequestBody Firmsonntag firmsonntag) {
        return firmsonntagService.createFirmsonntag(firmsonntag);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Firmsonntag> updateFirmsonntag(@PathVariable Long id, @RequestBody Firmsonntag firmsonntagDetails) {
        Firmsonntag firmsonntag = firmsonntagService.findById(id);
        if (firmsonntag == null) {
            return ResponseEntity.status(404).body(null);
        }
        firmsonntag.setName(firmsonntagDetails.getName());
        firmsonntag.setContent(firmsonntagDetails.getContent());
        firmsonntag.setDate(firmsonntagDetails.getDate());
        final Firmsonntag updatedFirmsonntag = firmsonntagService.updateFirmsonntag(firmsonntag);
        return ResponseEntity.ok(updatedFirmsonntag);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Firmsonntag> getFirmsonntag(@PathVariable Long id) {
        Firmsonntag firmsonntag = firmsonntagService.findById(id);
        if (firmsonntag == null) {
            return ResponseEntity.status(404).body(null);
        }
        return ResponseEntity.ok(firmsonntag);
    }


    @PutMapping("/complete/{firmlingId}/{firmsonntagId}")
    public ResponseEntity<?> markAsCompleted(@PathVariable Long firmlingId, @PathVariable Long firmsonntagId) {
        FirmlingFirmsonntag firmlingFirmsonntag = firmlingFirmsonntagRepository.findByFirmlingIdAndFirmsonntagId(firmlingId, firmsonntagId);
        if (firmlingFirmsonntag != null) {
            firmlingFirmsonntag.setCompleted(true);
            firmlingFirmsonntagRepository.save(firmlingFirmsonntag);
            return ResponseEntity.ok("Eintrag existierte bereits, wurde erledigt markiert.");
        }
        Firmling firmling = firmlingService.findById(firmlingId);
        Firmsonntag firmsonntag = firmsonntagService.findById(firmsonntagId);
        if (firmling == null) {
            return ResponseEntity.status(404).body("Firmling nicht gefunden");
        }
        if (firmsonntag == null) {
            return ResponseEntity.status(404).body("Firmsonntag nicht gefunden");
        }
        firmsonntagService.markAsCompleted(firmling, firmsonntag);
        return ResponseEntity.ok("Firmsonntag" + firmsonntag.getName() + " für Firmling " + firmling.getFirstName() + " als erledigt markiert!");
    }

    @PutMapping("/uncomplete/{firmlingId}/{firmsonntagId}")
    public ResponseEntity<?> markAsUnCompleted(@PathVariable Long firmlingId, @PathVariable Long firmsonntagId) {
        FirmlingFirmsonntag firmlingFirmsonntag = firmlingFirmsonntagRepository.findByFirmlingIdAndFirmsonntagId(firmlingId, firmsonntagId);
        if (firmlingFirmsonntag != null) {
            firmlingFirmsonntag.setCompleted(false);
            firmlingFirmsonntagRepository.deleteByFirmlingIdAndFirmsonntagId(firmlingId, firmsonntagId);
            return ResponseEntity.ok().build();
        }
        return ResponseEntity.status(404).body("Eintrag existiert nicht");
    }

    @Async
    @DeleteMapping("/{id}")
    public void deleteFirmsonntag(@PathVariable Long id) {

        CompletableFuture<Void> future = CompletableFuture.runAsync(() -> {
            try {
                Thread.sleep(300);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            firmsonntagService.deleteFirmsonntag(id);
        });

        for(FirmlingDTO firmling : firmlingService.findAllFirmlinge()) {
            firmlingFirmsonntagRepository.deleteByFirmlingIdAndFirmsonntagId(firmling.getId(), id);
        }
        future.join();
    }

    @GetMapping("/list/{firmlingId}")
    public ResponseEntity<?> getFirmsonntageFromFirmling(@PathVariable Long firmlingId) {
        Firmling firmling = firmlingService.findById(firmlingId);
        if (firmling == null) {
            return ResponseEntity.status(404).body("Firmling nicht gefunden");
        }
        List<Firmsonntag> allFirmsonntage = firmsonntagService.getAllFirmsonntage();
        List<FirmlingFirmsonntag> completedFirmsonntage = firmling.getFirmlingFirmsonntage();
        Map<Long, Boolean> completedMap = completedFirmsonntage.stream()
                .collect(Collectors.toMap(f -> f.getFirmsonntag().getId(), FirmlingFirmsonntag::isCompleted));
        List<Map<String, Object>> response = allFirmsonntage.stream().map(firmsonntag -> {
            Map<String, Object> map = new HashMap<>();
            map.put("firmsonntagId", firmsonntag.getId());
            map.put("firmsonntagName", firmsonntag.getName());
            map.put("completed", completedMap.getOrDefault(firmsonntag.getId(), false));
            return map;
        }).collect(Collectors.toList());
        return ResponseEntity.ok(response);
    }
}