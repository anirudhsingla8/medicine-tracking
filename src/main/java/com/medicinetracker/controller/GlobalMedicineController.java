package com.medicinetracker.controller;

import com.medicinetracker.dto.GlobalMedicineDto;
import com.medicinetracker.dto.GlobalMedicineResponseDto;
import com.medicinetracker.mapper.GlobalMedicineMapper;
import com.medicinetracker.model.GlobalMedicine;
import com.medicinetracker.service.GlobalMedicineService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/global-medicines")
@RequiredArgsConstructor
public class GlobalMedicineController {

    private final GlobalMedicineService globalMedicineService;
    private final GlobalMedicineMapper globalMedicineMapper;

    @PostMapping
    public ResponseEntity<GlobalMedicineResponseDto> createGlobalMedicine(@Valid @RequestBody GlobalMedicineDto globalMedicineDto) {
        GlobalMedicine createdMedicine = globalMedicineService.createGlobalMedicine(globalMedicineDto);
        return new ResponseEntity<>(globalMedicineMapper.toResponseDto(createdMedicine), HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<GlobalMedicineResponseDto>> getAllGlobalMedicines() {
        List<GlobalMedicine> medicines = globalMedicineService.getAllGlobalMedicines();
        List<GlobalMedicineResponseDto> responseDtos = medicines.stream()
                .map(globalMedicineMapper::toResponseDto)
                .collect(Collectors.toList());
        return ResponseEntity.ok(responseDtos);
    }

    @GetMapping("/{id}")
    public ResponseEntity<GlobalMedicineResponseDto> getGlobalMedicineById(@PathVariable UUID id) {
        GlobalMedicine medicine = globalMedicineService.getGlobalMedicineById(id);
        return ResponseEntity.ok(globalMedicineMapper.toResponseDto(medicine));
    }

    @PutMapping("/{id}")
    public ResponseEntity<GlobalMedicineResponseDto> updateGlobalMedicine(@PathVariable UUID id, @Valid @RequestBody GlobalMedicineDto globalMedicineDto) {
        GlobalMedicine updatedMedicine = globalMedicineService.updateGlobalMedicine(id, globalMedicineDto);
        return ResponseEntity.ok(globalMedicineMapper.toResponseDto(updatedMedicine));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteGlobalMedicine(@PathVariable UUID id) {
        globalMedicineService.deleteGlobalMedicine(id);
        return ResponseEntity.noContent().build();
    }
}
