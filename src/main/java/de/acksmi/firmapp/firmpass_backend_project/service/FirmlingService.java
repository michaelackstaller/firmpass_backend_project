package de.acksmi.firmapp.firmpass_backend_project.service;

import de.acksmi.firmapp.firmpass_backend_project.model.Firmling;
import de.acksmi.firmapp.firmpass_backend_project.repository.FirmlingRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class FirmlingService {

    private final FirmlingRepository firmlingRepository;

    @Autowired
    public FirmlingService(FirmlingRepository firmlingRepository) {
        this.firmlingRepository = firmlingRepository;
    }

    public Firmling saveFirmling(Firmling firmling) {
        return firmlingRepository.save(firmling);
    }
}
