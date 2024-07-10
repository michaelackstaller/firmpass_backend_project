package de.acksmi.firmapp.firmpass_backend_project.service;

import de.acksmi.firmapp.firmpass_backend_project.model.Firmling;
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

    public FirmlingFirmstunde markAsCompleted(Firmling firmling, Firmstunde firmstunde) {

        FirmlingFirmstunde firmlingFirmstunde = firmlingFirmstundeRepository.findByFirmlingIdAndFirmstundeId(firmling.getId(), firmstunde.getId());

        if(firmlingFirmstunde == null) {
            firmlingFirmstunde = new FirmlingFirmstunde(firmling, firmstunde, true);
        }
        return firmlingFirmstundeRepository.save(firmlingFirmstunde);
    }
    public void deleteFirmstunde(Long id) {
        firmstundeRepository.deleteById(id);
    }

    public Firmstunde findById(Long id) {
        if(firmstundeRepository.findById(id).isPresent()) {
            return firmstundeRepository.findById(id).get();
        }
        return null;
    }
}