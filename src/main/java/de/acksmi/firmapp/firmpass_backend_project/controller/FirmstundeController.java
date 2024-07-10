package de.acksmi.firmapp.firmpass_backend_project.controller;

import de.acksmi.firmapp.firmpass_backend_project.model.Firmstunde;
import de.acksmi.firmapp.firmpass_backend_project.model.connections.FirmlingFirmstunde;
import de.acksmi.firmapp.firmpass_backend_project.service.FirmstundeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/firmstunde")
public class FirmstundeController {

    @Autowired
    private FirmstundeService firmstundeService;

    @GetMapping
    public List<Firmstunde> getAllFirmstunden() {
        return firmstundeService.getAllFirmstunden();
    }

    @PostMapping
    public Firmstunde createFirmstunde(@RequestBody Firmstunde firmstunde) {
        return firmstundeService.createFirmstunde(firmstunde);
    }

    @PutMapping("/{firmlingId}/{firmstundeId}/complete")
    public FirmlingFirmstunde markAsCompleted(@PathVariable Long firmlingId, @PathVariable Long firmstundeId) {
        return firmstundeService.markAsCompleted(firmlingId, firmstundeId);
    }
}
