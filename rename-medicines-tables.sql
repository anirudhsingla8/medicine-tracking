-- Rename the existing medicines table to user_medicines
ALTER TABLE medicines RENAME TO user_medicines;

-- Add the composition column to user_medicines table
ALTER TABLE user_medicines ADD COLUMN IF NOT EXISTS composition JSONB DEFAULT '[]';

-- Add the form column to user_medicines table
ALTER TABLE user_medicines ADD COLUMN IF NOT EXISTS form VARCHAR(20);

-- Create the global_medicines table for storing global medicine details
CREATE TABLE global_medicines (
    id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    name VARCHAR(255) NOT NULL,
    brand_name VARCHAR(255),
    generic_name VARCHAR(255),
    dosage_form VARCHAR(100), -- e.g., tablet, capsule, liquid, injection
    strength VARCHAR(100), -- e.g., 500mg, 10ml
    manufacturer VARCHAR(255),
    description TEXT,
    indications TEXT[], -- Array of conditions the medicine treats
    contraindications TEXT[], -- Array of conditions where medicine should not be used
    side_effects TEXT[], -- Array of common side effects
    warnings TEXT[], -- Array of warnings
    interactions TEXT[], -- Array of drug interactions
    storage_instructions TEXT,
    category VARCHAR(100), -- e.g., antibiotics, pain relievers, etc.
    atc_code VARCHAR(10), -- Anatomical Therapeutic Chemical Classification System code
    fda_approval_date DATE,
    created_at TIMESTAMPTZ DEFAULT NOW(),
    updated_at TIMESTAMPTZ DEFAULT NOW()
);

-- Create indexes for faster lookups
CREATE INDEX idx_global_medicines_name ON global_medicines(name);
CREATE INDEX idx_global_medicines_brand_name ON global_medicines(brand_name);
CREATE INDEX idx_global_medicines_generic_name ON global_medicines(generic_name);
CREATE INDEX idx_global_medicines_category ON global_medicines(category);
CREATE INDEX idx_global_medicines_atc_code ON global_medicines(atc_code);

-- Update the foreign key constraint in user_medicines to reference the new table name
-- First drop the existing constraint
ALTER TABLE user_medicines DROP CONSTRAINT IF EXISTS fk_medicines_profile_id;

-- Then add the new constraint referencing the profiles table
ALTER TABLE user_medicines ADD CONSTRAINT fk_user_medicines_profile_id
FOREIGN KEY (profile_id) REFERENCES profiles(id) ON DELETE CASCADE;

-- Update the schedules table to reference the new user_medicines table
-- First drop the existing constraint
ALTER TABLE schedules DROP CONSTRAINT IF EXISTS schedules_medicine_id_fkey;

-- Then add the new constraint referencing the user_medicines table
ALTER TABLE schedules ADD CONSTRAINT fk_schedules_medicine_id
FOREIGN KEY (medicine_id) REFERENCES user_medicines(id) ON DELETE CASCADE;