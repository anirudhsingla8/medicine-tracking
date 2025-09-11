package com.medicinetracker.model;

import java.io.Serializable;

public class MedicineComponent implements Serializable {
    private String name;
    private Double strength_value;
    private String strength_unit;

    public MedicineComponent() {
    }

    public MedicineComponent(String name, Double strength_value, String strength_unit) {
        this.name = name;
        this.strength_value = strength_value;
        this.strength_unit = strength_unit;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Double getStrength_value() {
        return strength_value;
    }

    public void setStrength_value(Double strength_value) {
        this.strength_value = strength_value;
    }

    public String getStrength_unit() {
        return strength_unit;
    }

    public void setStrength_unit(String strength_unit) {
        this.strength_unit = strength_unit;
    }
}
