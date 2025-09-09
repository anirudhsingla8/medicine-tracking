-- Sample data for the global_medicines table

INSERT INTO global_medicines (id, name, brand_name, generic_name, dosage_form, strength, manufacturer, description, indications, contraindications, side_effects, warnings, interactions, storage_instructions, category, atc_code, fda_approval_date)
VALUES
('a1b2c3d4-e5f6-7890-1234-567890abcdef', 'Paracetamol', 'Tylenol', 'Acetaminophen', 'Tablet', '500mg', 'Johnson & Johnson', 'An analgesic and antipyretic drug.', '{"Headache", "Fever", "Pain"}', '{"Severe liver disease"}', '{"Nausea", "Stomach pain"}', '{"Do not exceed recommended dose"}', '{"Warfarin"}', 'Store at room temperature', 'Analgesic', 'N02BE01', '1955-01-01'),
('b2c3d4e5-f6a7-8901-2345-67890abcdef0', 'Ibuprofen', 'Advil', 'Ibuprofen', 'Tablet', '200mg', 'Pfizer', 'A nonsteroidal anti-inflammatory drug (NSAID).', '{"Headache", "Menstrual cramps", "Arthritis"}', '{"Aspirin allergy", "Stomach ulcers"}', '{"Constipation", "Dizziness"}', '{"Take with food or milk to prevent stomach upset"}', '{"Aspirin", "Blood thinners"}', 'Store at room temperature', 'NSAID', 'M01AE01', '1969-01-01');
