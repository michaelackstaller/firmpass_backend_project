package de.acksmi.firmapp.firmpass_backend_project.service;

import de.acksmi.firmapp.firmpass_backend_project.model.Firmstunde;
import de.acksmi.firmapp.firmpass_backend_project.model.connections.FirmlingFirmstunde;
import de.acksmi.firmapp.firmpass_backend_project.repository.FirmstundeRepository;
import de.acksmi.firmapp.firmpass_backend_project.repository.FirmlingFirmstundeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class FirmstundeService {

    @Autowired
    private FirmstundeRepository firmstundeRepository;

    @Autowired
    private FirmlingFirmstundeRepository firmlingFirmstundeRepository;

    public List<Firmstunde> getAllFirmstunden() {
        return firmstundeRepository.findAll();
    }

    public Firmstunde createFirmstunde(Firmstunde firmstunde) {
        return firmstundeRepository.save(firmstunde);
    }

    public FirmlingFirmstunde markAsCompleted(Long firmlingId, Long firmstundeId) {
        FirmlingFirmstunde firmlingFirmstunde = firmlingFirmstundeRepository.findById(firmstundeId)
                .orElseThrow(() -> new RuntimeException("Firmstunde not found"));
        firmlingFirmstunde.setCompleted(true);
        return firmlingFirmstundeRepository.save(firmlingFirmstunde);
    }
}
