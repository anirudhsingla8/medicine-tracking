package com.medicinetracker.service;

import com.medicinetracker.dto.GlobalMedicineDto;
import com.medicinetracker.exception.ResourceNotFoundException;
import com.medicinetracker.mapper.GlobalMedicineMapper;
import com.medicinetracker.model.GlobalMedicine;
import com.medicinetracker.repository.GlobalMedicineRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class GlobalMedicineService {

    private final GlobalMedicineRepository globalMedicineRepository;
    private final GlobalMedicineMapper globalMedicineMapper;

    @Transactional
    public GlobalMedicine createGlobalMedicine(GlobalMedicineDto globalMedicineDto) {
        GlobalMedicine globalMedicine = globalMedicineMapper.toEntity(globalMedicineDto);
        return globalMedicineRepository.save(globalMedicine);
    }

    @Transactional(readOnly = true)
    public List<GlobalMedicine> getAllGlobalMedicines() {
        return globalMedicineRepository.findAll();
    }

    @Transactional(readOnly = true)
    public GlobalMedicine getGlobalMedicineById(UUID id) {
        return globalMedicineRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Global medicine not found with id: " + id));
    }

    @Transactional
    public GlobalMedicine updateGlobalMedicine(UUID id, GlobalMedicineDto globalMedicineDto) {
        GlobalMedicine existingMedicine = getGlobalMedicineById(id);

        // Update fields from DTO
        existingMedicine.setName(globalMedicineDto.getName());
        existingMedicine.setBrandName(globalMedicineDto.getBrandName());
        existingMedicine.setGenericName(globalMedicineDto.getGenericName());
        existingMedicine.setDosageForm(globalMedicineDto.getDosageForm());
        existingMedicine.setStrength(globalMedicineDto.getStrength());
        existingMedicine.setManufacturer(globalMedicineDto.getManufacturer());
        existingMedicine.setDescription(globalMedicineDto.getDescription());
        existingMedicine.setIndications(globalMedicineDto.getIndications());
        existingMedicine.setContraindications(globalMedicineDto.getContraindications());
        existingMedicine.setSideEffects(globalMedicineDto.getSideEffects());
        existingMedicine.setWarnings(globalMedicineDto.getWarnings());
        existingMedicine.setInteractions(globalMedicineDto.getInteractions());
        existingMedicine.setStorageInstructions(globalMedicineDto.getStorageInstructions());
        existingMedicine.setCategory(globalMedicineDto.getCategory());
        existingMedicine.setAtcCode(globalMedicineDto.getAtcCode());
        existingMedicine.setFdaApprovalDate(globalMedicineDto.getFdaApprovalDate());

        return globalMedicineRepository.save(existingMedicine);
    }

    @Transactional
    public void deleteGlobalMedicine(UUID id) {
        GlobalMedicine existingMedicine = getGlobalMedicineById(id);
        globalMedicineRepository.delete(existingMedicine);
    }
}
