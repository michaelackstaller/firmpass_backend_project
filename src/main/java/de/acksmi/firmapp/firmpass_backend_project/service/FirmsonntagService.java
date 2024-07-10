package de.acksmi.firmapp.firmpass_backend_project.service;

import de.acksmi.firmapp.firmpass_backend_project.model.Firmsonntag;
import de.acksmi.firmapp.firmpass_backend_project.model.connections.FirmlingFirmsonntag;
import de.acksmi.firmapp.firmpass_backend_project.repository.FirmsonntagRepository;
import de.acksmi.firmapp.firmpass_backend_project.repository.FirmlingFirmsonntagRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class FirmsonntagService {

    @Autowired
    private FirmsonntagRepository firmsonntagRepository;

    @Autowired
    private FirmlingFirmsonntagRepository firmlingFirmsonntagRepository;

    public List<Firmsonntag> getAllFirmsonntage() {
        return firmsonntagRepository.findAll();
    }

    public Firmsonntag createFirmsonntag(Firmsonntag firmsonntag) {
        return firmsonntagRepository.save(firmsonntag);
    }

    public FirmlingFirmsonntag markAsCompleted(Long firmlingId, Long firmsonntagId) {
        FirmlingFirmsonntag firmlingFirmsonntag = firmlingFirmsonntagRepository.findById(firmsonntagId)
                .orElseThrow(() -> new RuntimeException("Firmsonntag not found"));
        firmlingFirmsonntag.setCompleted(true);
        return firmlingFirmsonntagRepository.save(firmlingFirmsonntag);
    }
}
