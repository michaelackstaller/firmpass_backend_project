package de.acksmi.firmapp.firmpass_backend_project.service;

import de.acksmi.firmapp.firmpass_backend_project.model.Firmling;
import de.acksmi.firmapp.firmpass_backend_project.model.connections.FirmlingFirmsonntag;
import de.acksmi.firmapp.firmpass_backend_project.model.connections.FirmlingFirmstunde;
import de.acksmi.firmapp.firmpass_backend_project.model.dtos.FirmlingDTO;
import de.acksmi.firmapp.firmpass_backend_project.model.dtos.FirmsonntagDTO;
import de.acksmi.firmapp.firmpass_backend_project.model.dtos.FirmstundeDTO;
import de.acksmi.firmapp.firmpass_backend_project.repository.FirmlingRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
        return firmlingRepository.findAll().stream().map(this::convertToDTO).collect(Collectors.toList());
    }

    public FirmlingDTO convertToDTO(Firmling firmling) {
        List<FirmstundeDTO> completedFirmstunden = firmling.getFirmlingFirmstunden().stream()
                .filter(FirmlingFirmstunde::isCompleted)
                .map(f -> new FirmstundeDTO(f.getFirmstunde().getId(), f.getFirmstunde().getName(), f.getFirmstunde().getContent(), f.isCompleted()))
                .collect(Collectors.toList());

        List<FirmsonntagDTO> completedFirmsonntage = firmling.getFirmlingFirmsonntage().stream()
                .filter(FirmlingFirmsonntag::isCompleted)
                .map(f -> new FirmsonntagDTO(f.getFirmsonntag().getId(), f.getFirmsonntag().getName(), f.getFirmsonntag().getContent(), f.isCompleted()))
                .collect(Collectors.toList());

        return new FirmlingDTO(
                firmling.getId(),
                firmling.getFirstName(),
                firmling.getLastName(),
                firmling.getGender(),
                firmling.getBirthDate(),
                firmling.getNutrition(),
                firmling.getAllergies(),
                firmling.getFirmgruppe(),
                firmling.getAvailableTimeSlots(),
                firmling.getGroupPreferences(),
                completedFirmstunden,
                completedFirmsonntage
        );
    }

    public Firmling findById(Long id) {
        if(firmlingRepository.findById(id).isPresent()) {
            return firmlingRepository.findById(id).get();
        }
        return null;
    }

    @Transactional
    public void deleteFirmling(Long id) {
        if (firmlingRepository.existsById(id)) {
            System.out.println("INFORMATION");
            System.out.println("Deleting Firmling with id: {}" + id);
            firmlingRepository.deleteFirmlingById(id);
        } else {
            throw new IllegalArgumentException("Firmling with id " + id + " does not exist.");
        }
    }
}

