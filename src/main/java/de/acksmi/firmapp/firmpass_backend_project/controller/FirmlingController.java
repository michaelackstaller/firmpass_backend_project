package de.acksmi.firmapp.firmpass_backend_project.controller;

import de.acksmi.firmapp.firmpass_backend_project.model.FirmlingDTO;
import de.acksmi.firmapp.firmpass_backend_project.service.FirmlingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api")
public class FirmlingController {

    @Autowired
    private FirmlingService firmlingService;

    @GetMapping("/firmlinge")
    public ResponseEntity<List<FirmlingDTO>> getAllFirmlinge() {
        List<FirmlingDTO> firmlinge = firmlingService.findAllFirmlinge();
        return ResponseEntity.ok(firmlinge);
    }
}

