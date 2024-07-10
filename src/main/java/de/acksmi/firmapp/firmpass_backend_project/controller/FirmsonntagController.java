package de.acksmi.firmapp.firmpass_backend_project.controller;

import de.acksmi.firmapp.firmpass_backend_project.model.Firmsonntag;
import de.acksmi.firmapp.firmpass_backend_project.model.connections.FirmlingFirmsonntag;
import de.acksmi.firmapp.firmpass_backend_project.service.FirmsonntagService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/firmsonntag")
public class FirmsonntagController {

    @Autowired
    private FirmsonntagService firmsonntagService;

    @GetMapping
    public List<Firmsonntag> getAllFirmsonntage() {
        return firmsonntagService.getAllFirmsonntage();
    }

    @PostMapping
    public Firmsonntag createFirmsonntag(@RequestBody Firmsonntag firmsonntag) {
        return firmsonntagService.createFirmsonntag(firmsonntag);
    }

    @PutMapping("/{firmlingId}/{firmsonntagId}/complete")
    public FirmlingFirmsonntag markAsCompleted(@PathVariable Long firmlingId, @PathVariable Long firmsonntagId) {
        return firmsonntagService.markAsCompleted(firmlingId, firmsonntagId);
    }
}
