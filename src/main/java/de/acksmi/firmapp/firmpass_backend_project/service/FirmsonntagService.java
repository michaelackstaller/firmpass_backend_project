package de.acksmi.firmapp.firmpass_backend_project.service;

import de.acksmi.firmapp.firmpass_backend_project.model.Firmling;
import de.acksmi.firmapp.firmpass_backend_project.model.Firmsonntag;
import de.acksmi.firmapp.firmpass_backend_project.model.connections.FirmlingFirmsonntag;
import de.acksmi.firmapp.firmpass_backend_project.model.connections.FirmlingFirmstunde;
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

    public Firmsonntag findById(Long id) {
        if(firmsonntagRepository.findById(id).isPresent()) {
            return firmsonntagRepository.findById(id).get();
        }
        return null;
    }

    public Firmsonntag createFirmsonntag(Firmsonntag firmsonntag) {
        return firmsonntagRepository.save(firmsonntag);
    }

    public FirmlingFirmsonntag markAsCompleted(Firmling firmling, Firmsonntag firmsonntag) {

        FirmlingFirmsonntag firmlingFirmsonntag = firmlingFirmsonntagRepository.findByFirmlingIdAndFirmsonntagId(firmling.getId(), firmsonntag.getId());

        if(firmlingFirmsonntag == null) {
            firmlingFirmsonntag = new FirmlingFirmsonntag(firmling, firmsonntag, true);
        }
        return firmlingFirmsonntagRepository.save(firmlingFirmsonntag);
    }

    public void deleteFirmsonntag(Long id) {
        firmsonntagRepository.deleteById(id);
    }
}
