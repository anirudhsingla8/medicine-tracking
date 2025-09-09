package com.medicinetracker.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MedicineComponent implements Serializable {
    private String name;
    private Double strength_value;
    private String strength_unit;
}
