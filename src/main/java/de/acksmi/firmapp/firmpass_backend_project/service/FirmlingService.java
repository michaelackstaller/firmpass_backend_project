package de.acksmi.firmapp.firmpass_backend_project.service;

import de.acksmi.firmapp.firmpass_backend_project.model.Firmling;
import de.acksmi.firmapp.firmpass_backend_project.model.FirmlingDTO;
import de.acksmi.firmapp.firmpass_backend_project.model.TimeSlot;
import de.acksmi.firmapp.firmpass_backend_project.repository.FirmlingRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

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

    public List<FirmlingDTO> findAllFirmlinge() {
        List<Firmling> firmlinge = firmlingRepository.findAll();
        return firmlinge.stream()
                .map(f -> new FirmlingDTO(f.getFirstName(), f.getLastName(), f.getGroupPreferences()))
                .collect(Collectors.toList());
    }
}

